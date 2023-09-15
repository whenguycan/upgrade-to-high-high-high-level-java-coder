package com.czdx.parkingcharge.domain.dynamic;

import com.czdx.parkingcharge.common.enums.YesOrNo;
import com.czdx.parkingcharge.domain.ParkChargeDurationPeriod;
import com.czdx.parkingcharge.domain.custom.ParkChargeDurationCustom;
import com.czdx.parkingcharge.domain.custom.ParkChargeRuleCustom;
import com.czdx.parkingcharge.domain.pr.ParkingRuleEnums;
import com.czdx.parkingcharge.domain.pr.RuleNodeFact;
import com.czdx.parkingcharge.utils.ParkingFeeUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * description: 单期间单时段规则文件
 * @author mingchenxu
 * @date 2023/3/15 08:43
 */
@Slf4j
public class AloneDurationAlonePeriodDrl implements DynamicDrl{

    private ParkChargeRuleCustom chargeRule;

    public AloneDurationAlonePeriodDrl(ParkChargeRuleCustom chargeRule) {
        this.chargeRule = chargeRule;
    }

    /**
     *
     * description: 获取动态条件Map
     * @author mingchenxu
     * @date 2023/3/15 08:45
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @Override
    public DynamicConditionMap getDynamicConditionMap() {
        log.info("准备组装车场[{}]的计费规则[{}]条件Map", chargeRule.getParkNo(), chargeRule.getRuleName());
        // 组装时长停车规则节点
        List<RuleNodeFact> ruleNodeFacts = assembleDurationParkingRuleNode(chargeRule);
        // 规则节点转填充为模板Map对象
        List<Map<String, Object>> maps = fillConditionMap(chargeRule.getParkNo(), ruleNodeFacts);
        // 规则名称 {车场编号}-{规则ID}
        String ruleName = chargeRule.getRuleName() + "-" + chargeRule.getId();
        return new DynamicConditionMap(ruleName, maps);
    }

    /**
     *
     * description: 组装停车计费规则
     * @author mingchenxu
     * @date 2023/3/6 18:09
     * @param parkingRule 停车计费规则
     * @return java.util.List<com.iwither.droolsspringboot.entity.RuleNodeFact>
     */
    private List<RuleNodeFact> assembleDurationParkingRuleNode(ParkChargeRuleCustom parkingRule) {
        BigDecimal limitPrice = Objects.requireNonNullElse(parkingRule.getCeilingPrice(), BigDecimal.ZERO);
        List<RuleNodeFact> ruleNodeFacts = new ArrayList<>();
        // 存在最高限价，且最高限制价格大于0元，增加最高限价分割
        if (YesOrNo.YES.getValue().equals(parkingRule.getCeilingPriceFlag())
                && limitPrice.compareTo(BigDecimal.ZERO) > 0) {
            // 组装最高限价节点
            RuleNodeFact cpNode = assembleCeilingPriceNode(parkingRule);
            ruleNodeFacts.add(cpNode);
        }
        // 组装强制分割节点
        RuleNodeFact divisionNode = assembleDivisionNode(parkingRule);
        if (divisionNode != null) {
            ruleNodeFacts.add(divisionNode);
        }
        // 组装隐藏的分割节点
        RuleNodeFact hideDivisionNode = assembleHideDurationDivisionNode(parkingRule);
        ruleNodeFacts.add(hideDivisionNode);
        // 只有一个期间
        ParkChargeDurationCustom ruleDuration = parkingRule.getChargeDurations().get(0);
        // 节点ID
        int nodeId = 0;
        // 免费时长
        long freeDuration = ruleDuration.getFreeMinute();
        // 构建免费节点
        RuleNodeFact freeNode = getFreeNode(parkingRule.getId(), ruleDuration.getId(), nodeId, freeDuration);
        ruleNodeFacts.add(freeNode);
        nodeId += 1;

        // 只有一个时段
        ParkChargeDurationPeriod durationPeriod = ruleDuration.getDurationPeriods().get(0);
        long minUnitPeriod = durationPeriod.getMinLenghtOfTime();
        BigDecimal rate = durationPeriod.getRate();
        String timeRoundWay = parkingRule.getTimeRoundWay();
        // 获取限制价格
        limitPrice = getLimitPrice(ruleDuration.getMaximumCharge(), limitPrice);

        // 构建期间-时段节点
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(parkingRule.getId());
        rn.setSRuleId(ruleDuration.getId());
        rn.setNodeId(nodeId);
        // LHS
        String lhsCondition = "$duration: parkingDuration > 0";
        // RHS
        String resultOne =
                MessageFormat.format("BigDecimal fee = ParkingFeeUtil.chargeParkingFee($duration, {0,number,#}, BigDecimal.valueOf({1,number,#}), \"{2}\", BigDecimal.valueOf({3,number,#}));\n",
                        minUnitPeriod, rate, timeRoundWay, limitPrice);
        String resultTwo =  "        $pRecord.setParkingFee(fee);";
        rn.setRuleConditionStr(lhsCondition);
        rn.setRuleResultStr(resultOne + resultTwo);
        ruleNodeFacts.add(rn);
        return ruleNodeFacts;
    }

    /**
     *
     * description: 组装最高限价节点
     * @author mingchenxu
     * @date 2023/3/14 13:56
     * @param parkingRule 计费规则
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     */
    private static RuleNodeFact assembleCeilingPriceNode(ParkChargeRuleCustom parkingRule) {
        long ceilingPriceMinutes = parkingRule.getCeilingPriceMinute();
        BigDecimal ceilingPrice = parkingRule.getCeilingPrice();
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(parkingRule.getId());
        rn.setSRuleId(0);
        // 设置节点ID与优先级
        rn.setNodeId(ParkingRuleEnums.DivisionNode.CEILING_PRICE_DIVISION.getId());
        rn.setSalience(ParkingRuleEnums.DivisionNode.CEILING_PRICE_DIVISION.getSalience());
        // LHS
        String lhsCondition = MessageFormat.format("$duration: parkingDuration >= {0,number,#}", ceilingPriceMinutes);
        // RHS
        String rhsOne = MessageFormat.format(
                "    BigDecimal fee = ParkingFeeUtil.divideCP2ChargeFee($pRecord, {0,number,#}, BigDecimal.valueOf({1,number,#}), \"{2}\", \"{3}\");\n",
                ceilingPriceMinutes, ceilingPrice, parkingRule.getFreeMinuteNumber(), parkingRule.getFirstDurationChargeWay());
        String rhsTwo = "    $pRecord.setParkingFee(fee);";
        rn.setRuleConditionStr(lhsCondition);
        rn.setRuleResultStr(rhsOne + rhsTwo);
        return rn;
    }

    /**
     *
     * description: 组装分割节点
     * @author mingchenxu
     * @date 2023/3/14 14:19
     * @param pCRule 停车记录
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     */
    public static RuleNodeFact assembleDivisionNode(ParkChargeRuleCustom pCRule) {
        ParkingRuleEnums.TimeDivisionWay divisionWay = ParkingRuleEnums.TimeDivisionWay.getByValue(pCRule.getTimeDivisionWay());
        RuleNodeFact rnf = null;
        switch (divisionWay) {
            case H24_DIVISION -> rnf = assemble24HDivisionNode(pCRule);
            case ZERO_DIVISION -> rnf = assembleZeroDivisionNode(pCRule);
            case DURATION_DIVISION -> rnf = assembleDurationDivisionNode(pCRule);
            default -> log.info("计费规则ID：{}，无需分割", pCRule.getId());
        }
        return rnf;
    }

    /**
     *
     * description: 组装期间隐藏分割节点
     * @author mingchenxu
     * @date 2023/3/14 17:50
     * @param pCRule 计费规则
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     */
    private static RuleNodeFact assembleHideDurationDivisionNode(ParkChargeRuleCustom pCRule) {
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(pCRule.getId());
        rn.setSRuleId(0);
        // 设置节点ID与优先级
        rn.setNodeId(ParkingRuleEnums.DivisionNode.HIDE_DURATION_DIVISION.getId());
        rn.setSalience(ParkingRuleEnums.DivisionNode.HIDE_DURATION_DIVISION.getSalience());
        // 只有一个时段的情况
        // LHS
        Integer lengthOfTime = pCRule.getChargeDurations().get(0).getLengthOfTime();
        String lhsCondition = MessageFormat.format("$duration: parkingDuration > {0,number,#}", lengthOfTime);
        // RHS
        // 最高限价，0元不生效
        BigDecimal ceilingPrice = BigDecimal.ZERO;
        if (YesOrNo.YES.getValue().equals(pCRule.getCeilingPriceFlag())) {
            ceilingPrice = Objects.requireNonNullElse(pCRule.getCeilingPrice(), BigDecimal.ZERO);
        }
        String rhsOne = MessageFormat.format(
                "    BigDecimal fee = ParkingFeeUtil.divideHideDuration2ChargeFee($pRecord, {0,number,#}, \"{1}\", BigDecimal.valueOf({2,number,#}));\n",
                lengthOfTime, pCRule.getChargeContainFreeTime(), ceilingPrice);
        String rhsTwo = "    $pRecord.setParkingFee(fee);";
        rn.setRuleConditionStr(lhsCondition);
        rn.setRuleResultStr(rhsOne + rhsTwo);
        return rn;
    }

    /**
     *
     * description: 组装24小时强制分割节点
     * @author mingchenxu
     * @date 2023/3/14 15:30
     * @param pCRule 计费规则
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     */
    private static RuleNodeFact assemble24HDivisionNode(ParkChargeRuleCustom pCRule) {
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(pCRule.getId());
        rn.setSRuleId(0);
        // 设置节点ID与优先级
        rn.setNodeId(ParkingRuleEnums.DivisionNode.H24_DIVISION.getId());
        rn.setSalience(ParkingRuleEnums.DivisionNode.H24_DIVISION.getSalience());
        // LHS
        String lhsCondition = "$duration: parkingDuration > 1440";
        // RHS
        // 最高限价，0元不生效
        BigDecimal ceilingPrice = BigDecimal.ZERO;
        if (YesOrNo.YES.getValue().equals(pCRule.getCeilingPriceFlag())) {
            ceilingPrice = Objects.requireNonNullElse(pCRule.getCeilingPrice(), BigDecimal.ZERO);
        }
        String rhsOne = MessageFormat.format(
                "    BigDecimal fee = ParkingFeeUtil.divide24Hours2ChargeFee($pRecord, {0}, {1}, BigDecimal.valueOf({2,number,#}));\n",
                pCRule.getFreeMinuteNumber(), pCRule.getFirstDurationChargeWay(), ceilingPrice);
        String rhsTwo = "    $pRecord.setParkingFee(fee);";
        rn.setRuleConditionStr(lhsCondition);
        rn.setRuleResultStr(rhsOne + rhsTwo);
        return rn;
    }

    /**
     *
     * description: 组装0点强制分割节点
     * @author mingchenxu
     * @date 2023/3/14 15:30
     * @param pCRule 计费规则
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     */
    private static RuleNodeFact assembleZeroDivisionNode(ParkChargeRuleCustom pCRule) {
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(pCRule.getId());
        rn.setSRuleId(0);
        // 设置节点ID与优先级
        rn.setNodeId(ParkingRuleEnums.DivisionNode.ZERO_DIVISION.getId());
        rn.setSalience(ParkingRuleEnums.DivisionNode.ZERO_DIVISION.getSalience());
        // LHS
        String lhsCondition = "hasInterDay";
        // RHS
        // 最高限价，0元不生效
        BigDecimal ceilingPrice = BigDecimal.ZERO;
        if (YesOrNo.YES.getValue().equals(pCRule.getCeilingPriceFlag())) {
            ceilingPrice = Objects.requireNonNullElse(pCRule.getCeilingPrice(), BigDecimal.ZERO);
        }
        String rhsOne = MessageFormat.format(
                "    BigDecimal fee = ParkingFeeUtil.divideZero2ChargeFee($pRecord, {0}, {1}, BigDecimal.valueOf({2,number,#}));\n",
                pCRule.getFreeMinuteNumber(), pCRule.getFirstDurationChargeWay(), ceilingPrice);
        String rhsTwo = "    $pRecord.setParkingFee(fee);";
        rn.setRuleConditionStr(lhsCondition);
        rn.setRuleResultStr(rhsOne + rhsTwo);
        return rn;
    }

    /**
     *
     * description: 组装期间强制分割节点
     * @author mingchenxu
     * @date 2023/3/14 15:30
     * @param pCRule 计费规则
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     */
    private static RuleNodeFact assembleDurationDivisionNode(ParkChargeRuleCustom pCRule) {
        // 只有一个时段的情况
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(pCRule.getId());
        rn.setSRuleId(0);
        // 设置节点ID与优先级
        rn.setNodeId(ParkingRuleEnums.DivisionNode.DURATION_DIVISION.getId());
        rn.setSalience(ParkingRuleEnums.DivisionNode.DURATION_DIVISION.getSalience());
        // LHS
        Integer lengthOfTime = pCRule.getChargeDurations().get(0).getLengthOfTime();
        String lhsCondition = MessageFormat.format("$duration: parkingDuration > {0,number,#}", lengthOfTime);
        // RHS
        // 最高限价，0元不生效
        BigDecimal ceilingPrice = BigDecimal.ZERO;
        if (YesOrNo.YES.getValue().equals(pCRule.getCeilingPriceFlag())) {
            ceilingPrice = Objects.requireNonNullElse(pCRule.getCeilingPrice(), BigDecimal.ZERO);
        }
        String rhsOne = MessageFormat.format(
                "    BigDecimal fee = ParkingFeeUtil.divideDuration2ChargeFee($pRecord, {0,number,#}, \"{1}\", \"{2}\", BigDecimal.valueOf({3,number,#}));\n",
                lengthOfTime, pCRule.getFreeMinuteNumber(), pCRule.getFirstDurationChargeWay(), ceilingPrice);
        String rhsTwo = "    $pRecord.setParkingFee(fee);";
        rn.setRuleConditionStr(lhsCondition);
        rn.setRuleResultStr(rhsOne + rhsTwo);
        return rn;
    }

    private static RuleNodeFact getFreeNode(int ruleId, int sRuleId, int nodeId, long freeDuration) {
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(ruleId);
        rn.setSRuleId(sRuleId);
        rn.setNodeId(nodeId);
        rn.setRuleConditionStr(MessageFormat.format("    $duration: parkingDuration <= {0,number,#}, useFreeTime", freeDuration));
        rn.setRuleResultStr("    $pRecord.setParkingFee(BigDecimal.ZERO);");
        return rn;
    }


}
