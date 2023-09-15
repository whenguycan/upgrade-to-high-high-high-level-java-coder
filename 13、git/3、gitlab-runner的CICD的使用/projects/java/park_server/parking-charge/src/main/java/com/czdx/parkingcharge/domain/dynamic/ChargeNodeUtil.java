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
import java.util.Objects;

/**
 * description: 计费节点工具类
 *
 * @author mingchenxu
 * @date 2023/3/20 15:43
 */
@Slf4j
public class ChargeNodeUtil {

    private ChargeNodeUtil() {
    }

    /**
     * description: 组装通用节点
     * 包含 - 最高限价分割、24小时分割、0点分割、期间分割、隐藏分割、免费时长不计分扣减、免费节点
     *
     * @param parkingRule
     * @param ceilingPrice
     * @return java.util.List<com.czdx.parkingcharge.domain.pr.RuleNodeFact>
     * @author mingchenxu
     * @date 2023/3/20 18:05
     */
    public static List<RuleNodeFact> assembleDurationCommonNodes(ParkChargeRuleCustom parkingRule, BigDecimal ceilingPrice) {
        List<RuleNodeFact> ruleNodeFacts = new ArrayList<>();
        // 存在最高限价，且最高限制价格大于0元，增加最高限价分割
        if (ceilingPrice.compareTo(BigDecimal.ZERO) > 0) {
            // 组装最高限价节点
            RuleNodeFact cpNode = buildCeilingPriceNode(parkingRule);
            ruleNodeFacts.add(cpNode);
        }
        // 组装强制分割节点
        RuleNodeFact divisionNode = buildDivisionNode(parkingRule);
        if (divisionNode != null) {
            ruleNodeFacts.add(divisionNode);
        }
        return ruleNodeFacts;
    }

    /**
     *
     * description: 免费时长，构建免费节点
     * @author mingchenxu
     * @date 2023/3/28 16:03
     * @param chargeCFT
     * @param ceilingPrice
     * @param nodeId
     * @param ruleId
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     */
    public static RuleNodeFact buildFMNode(String chargeCFT, Integer freeMinutes, BigDecimal ceilingPrice,
                                           Integer ruleId, String sRuleId, int nodeId) {
        // 规则ID和期间ID
        if (YesOrNo.NO.getValue().equals(chargeCFT)) {
            // 如果算费不包含免费时长，则需要增加一个减去免费时长的节点，不需要再生成免费时间节点
            return buildSubstractFMNode(ruleId, sRuleId, freeMinutes, ceilingPrice);
        } else {
            // 生成免费时间节点
            return buildFMNode(ruleId, sRuleId, nodeId, freeMinutes);
        }
    }

    /**
     * description: 组装时刻通用节点
     *
     * @param parkingRule
     * @param ceilingPrice
     * @return java.util.List<com.czdx.parkingcharge.domain.pr.RuleNodeFact>
     * @author mingchenxu
     * @date 2023/3/28 13:18
     */
    public static List<RuleNodeFact> assembleTimeCommonNodes(ParkChargeRuleCustom parkingRule, BigDecimal ceilingPrice) {
        List<RuleNodeFact> ruleNodeFacts = new ArrayList<>();
        // 存在最高限价，且最高限制价格大于0元，增加最高限价分割
        if (ceilingPrice.compareTo(BigDecimal.ZERO) > 0) {
            // 组装最高限价节点
            RuleNodeFact cpNode = buildCeilingPriceNode(parkingRule);
            ruleNodeFacts.add(cpNode);
        }
        // 组装时刻强制分割节点，包含一个隐藏的期间多时刻分割
        List<RuleNodeFact> divisionNodes = buildTimeDivisionNode(parkingRule);
        ruleNodeFacts.addAll(divisionNodes);
        return ruleNodeFacts;
    }

    /**
     * description: 组装最高限价节点
     *
     * @param parkingRule 计费规则
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     * @author mingchenxu
     * @date 2023/3/14 13:56
     */
    public static RuleNodeFact buildCeilingPriceNode(ParkChargeRuleCustom parkingRule) {
        long ceilingPriceMinutes = parkingRule.getCeilingPriceMinute();
        BigDecimal ceilingPrice = parkingRule.getCeilingPrice();
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(parkingRule.getId());
        rn.setSRuleId("CP");
        // 设置节点ID与优先级
        rn.setNodeId(ParkingRuleEnums.SpecialNode.CEILING_PRICE_DIVISION.getId());
        rn.setSalience(ParkingRuleEnums.SpecialNode.CEILING_PRICE_DIVISION.getSalience());
        // LHS
        String lhsCondition = MessageFormat.format("$duration: parkingDuration >= {0,number,#}", ceilingPriceMinutes);
        // RHS
        String rhsOne = MessageFormat.format(
                "    BigDecimal fee = ParkingFeeUtil.divideCP2ChargeFee($pRecord, {0,number,#}, BigDecimal.valueOf({1,number,#.##}), \"{2}\", \"{3}\");\n",
                ceilingPriceMinutes, ceilingPrice, parkingRule.getFreeMinuteNumber(), parkingRule.getFirstDurationChargeWay());
        String rhsTwo = "    $pRecord.setParkingFee(fee);";
        rn.setRuleConditionStr(lhsCondition);
        rn.setRuleResultStr(rhsOne + rhsTwo);
        return rn;
    }

    /**
     * description: 组装分割节点
     * 分割节点的限制价格为最高限价，并非取期间限价与最高限价中的较小者
     * 因为分割后的时间可能还包含多个期间，可以超过期间限价，但肯定不能超过最高限价
     *
     * @param pCRule 停车记录
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     * @author mingchenxu
     * @date 2023/3/14 14:19
     */
    public static RuleNodeFact buildDivisionNode(ParkChargeRuleCustom pCRule) {
        ParkingRuleEnums.TimeDivisionWay divisionWay = ParkingRuleEnums.TimeDivisionWay.getByValue(pCRule.getTimeDivisionWay());
        RuleNodeFact rnf = null;
        switch (divisionWay) {
            case H24_DIVISION -> rnf = build24HDivisionNode(pCRule);
            case ZERO_DIVISION -> rnf = buildZeroDivisionNode(pCRule);
            case DURATION_DIVISION -> rnf = buildDurationDivisionNode(pCRule);
            default -> log.info("计费规则ID：{}，无需分割", pCRule.getId());
        }
        return rnf;
    }

    /**
     * description: 组装时刻分割节点
     * 分割节点的限制价格为最高限价，并非取期间限价与最高限价中的较小者
     * 因为分割后的时间可能还包含多个期间，可以超过期间限价，但肯定不能超过最高限价
     *
     * @param pCRule 停车记录
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     * @author mingchenxu
     * @date 2023/3/14 14:19
     */
    public static List<RuleNodeFact> buildTimeDivisionNode(ParkChargeRuleCustom pCRule) {
        List<RuleNodeFact> rnfs = new ArrayList<>();
        ParkingRuleEnums.TimeDivisionWay divisionWay = ParkingRuleEnums.TimeDivisionWay.getByValue(pCRule.getTimeDivisionWay());
        RuleNodeFact rnf = null;
        switch (divisionWay) {
            case H24_DIVISION -> rnf = build24HDivisionNode(pCRule);
            case ZERO_DIVISION -> rnf = buildZeroDivisionNode(pCRule);
            case DURATION_DIVISION -> rnf = assembleMultipleTimeDivisionNode(pCRule, false);
            default -> log.info("计费规则：{}({})，无需分割", pCRule.getRuleName(), pCRule.getId());
        }
        if (rnf != null) {
            rnfs.add(rnf);
        }
        // 如果不是期间强制分割，需要组装一个隐藏的多时刻分割节点
        if (divisionWay != ParkingRuleEnums.TimeDivisionWay.DURATION_DIVISION) {
            log.info("计费规则：{}({})，增加隐藏的多时刻分割节点", pCRule.getRuleName(), pCRule.getId());
            rnfs.add(assembleMultipleTimeDivisionNode(pCRule, true));
        }
        return rnfs;
    }

    /**
     * description: 组装期间单时段隐藏分割节点
     *
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     * @author mingchenxu
     * @date 2023/3/14 17:50
     */
    public static RuleNodeFact buildSinglePeriodHideDivisionNode(int ruleId, String srPrefix, BigDecimal cp, ParkChargeDurationCustom pcdc) {
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(ruleId);
        rn.setSRuleId(srPrefix + "HD");
        // 设置节点ID与优先级
        rn.setNodeId(ParkingRuleEnums.SpecialNode.HIDE_DURATION_DIVISION.getId());
        rn.setSalience(ParkingRuleEnums.SpecialNode.HIDE_DURATION_DIVISION.getSalience());
        // 只有一个时段的情况
        // LHS
        Integer lengthOfTime = pcdc.getDurationLengthOfTime();
        String lhsCondition = MessageFormat.format("$duration: parkingDuration > {0,number,#}", lengthOfTime);
        // RHS
        String rhsOne = MessageFormat.format(
                "BigDecimal fee = ParkingFeeUtil.divideHideDuration2ChargeFee($pRecord, {0,number,#}, BigDecimal.valueOf({1,number,#.##}));\n",
                lengthOfTime, cp);
        String rhsTwo = "        $pRecord.setParkingFee(fee);";
        rn.setRuleConditionStr(lhsCondition);
        rn.setRuleResultStr(rhsOne + rhsTwo);
        return rn;
    }

    /**
     *
     * description: 构建单期间单时段节点
     * @author mingchenxu
     * @date 2023/3/28 16:27
     * @param ruleId
     * @param rdId
     * @param nodeId
     * @param limitPrice
     * @param timeRoundWay
     * @param durationPeriod
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     */
    public static RuleNodeFact buildSinglePeriodNode(int ruleId, String sId,
                                                     int nodeId, BigDecimal limitPrice,
                                                     String timeRoundWay, ParkChargeDurationPeriod durationPeriod) {
        long minUnitPeriod = durationPeriod.getMinLenghtOfTime();
        BigDecimal rate = durationPeriod.getRate();
        // 构建期间-时段节点
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(ruleId);
        rn.setSRuleId(sId);
        rn.setNodeId(nodeId);
        // LHS
        String lhsCondition = "$duration: parkingDuration > 0";
        // RHS
        String resultOne =
                MessageFormat.format("BigDecimal fee = ParkingFeeUtil.chargeParkingFee($duration, {0,number,#}, BigDecimal.valueOf({1,number,#.##}), \"{2}\", BigDecimal.valueOf({3,number,#.##}));\n",
                        minUnitPeriod, rate, timeRoundWay, limitPrice);
        String resultTwo =  "        $pRecord.setParkingFee(fee);";
        rn.setRuleConditionStr(lhsCondition);
        rn.setRuleResultStr(resultOne + resultTwo);
        return rn;
    }

    /**
     *
     * description: 构建单期间多时段节点
     * @author mingchenxu
     * @date 2023/3/28 16:46
     * @param ruleId
     * @param rdId
     * @param nodeId
     * @param limitPrice
     * @param timeRoundWay
     * @param durationPeriods
     * @return java.util.List<com.czdx.parkingcharge.domain.pr.RuleNodeFact>
     */
    public static List<RuleNodeFact> buildMultiplePeriodNodes(int ruleId, String rdId,
                                                            int nodeId, BigDecimal limitPrice,
                                                            String timeRoundWay, List<ParkChargeDurationPeriod> durationPeriods) {
        List<RuleNodeFact> ruleNodeFacts = new ArrayList<>();
        // 已算费用和已算时长，区分能否使用首时段
        BigDecimal existFPFee = BigDecimal.ZERO;
        BigDecimal existFee = BigDecimal.ZERO;
        long existFPLot = 0L;
        long existLot = 0L;
        for (int i = 0; i < durationPeriods.size(); i++) {
            ParkChargeDurationPeriod period = durationPeriods.get(i);
            long lot = period.getLengthOfTime();
            long mlot = period.getMinLenghtOfTime();
            BigDecimal rate = period.getRate();
            // 时段总价格
            BigDecimal pFee = ParkingFeeUtil.chargeParkingFee(lot, mlot, rate, timeRoundWay, limitPrice);
            // 首时段
            if (i == 0) {
                RuleNodeFact firstPeriodNode = ChargeNodeUtil.getPeriodNode(
                        ruleId, rdId, nodeId, existFPLot, lot, mlot, rate, timeRoundWay, limitPrice, existFee, true);
                nodeId++;
                existFPLot += lot;
                existFPFee = existFee.add(pFee);
                ruleNodeFacts.add(firstPeriodNode);
            } else if (i == (durationPeriods.size() - 1)) {
                // 最后一个时段
                // 使用首时段的时段计费节点
                RuleNodeFact lastUFPPeriodNode = ChargeNodeUtil.getPeriodNode(
                        ruleId, rdId, nodeId, existFPLot, null, mlot, rate, timeRoundWay, limitPrice, existFPFee, true);
                nodeId ++;
                // 不使用首时段的时段计费节点
                RuleNodeFact lastPeriodNode = ChargeNodeUtil.getPeriodNode(
                        ruleId, rdId, nodeId, existLot, null, mlot, rate, timeRoundWay, limitPrice, existFee, false);
                ruleNodeFacts.add(lastUFPPeriodNode);
                ruleNodeFacts.add(lastPeriodNode);
            } else {
                // 其他时段
                // 使用首时段的时段计费节点
                RuleNodeFact uFPPeriodNode = ChargeNodeUtil.getPeriodNode(
                        ruleId, rdId, nodeId, existFPLot, lot, mlot, rate, timeRoundWay, limitPrice, existFPFee, true);
                ruleNodeFacts.add(uFPPeriodNode);
                nodeId++;
                existFPLot += lot;
                existFPFee = existFPFee.add(pFee);

                // 不使用首时段的时段计费节点
                RuleNodeFact periodNode = ChargeNodeUtil.getPeriodNode(
                        ruleId, rdId, nodeId, existLot, lot, mlot, rate, timeRoundWay, limitPrice, existFee, false);
                ruleNodeFacts.add(periodNode);
                nodeId++;
                existLot += lot;
                existFee = existFee.add(pFee);
            }
        }
        return ruleNodeFacts;
    }

    /**
     *
     * description: 组装期间多时段隐藏分割节点
     * @author mingchenxu
     * @date 2023/3/28 16:33
     * @param ruleId 规则ID
     * @param cp 最高限价
     * @param pcdc 计费期间
     * @return java.util.List<com.czdx.parkingcharge.domain.pr.RuleNodeFact>
     */
    public static List<RuleNodeFact> buildMultiplePeriodHideDurationDivisionNode(int ruleId, String srPrefix, BigDecimal cp, ParkChargeDurationCustom pcdc) {
        List<RuleNodeFact> rns = new ArrayList<>();
        // 只有一个期间的情况
        Integer lengthOfTime = pcdc.getDurationLengthOfTime();

        // 能使用首时段的隐藏分割
        RuleNodeFact rnUFP = new RuleNodeFact();
        rnUFP.setRuleId(ruleId);
        rnUFP.setSRuleId(srPrefix + "HD");
        // 设置节点ID与优先级
        rnUFP.setNodeId(ParkingRuleEnums.SpecialNode.HIDE_DURATION_DIVISION.getId());
        rnUFP.setSalience(ParkingRuleEnums.SpecialNode.HIDE_DURATION_DIVISION.getSalience());
        // LHS
        String lhsCondition = MessageFormat.format("$duration: parkingDuration > {0,number,#}, useFirstPeriod", lengthOfTime);
        // RHS
        String rhsOne = MessageFormat.format(
                "BigDecimal fee = ParkingFeeUtil.divideHideDuration2ChargeFee($pRecord, {0,number,#}, BigDecimal.valueOf({1,number,#.##}));\n",
                lengthOfTime, cp);
        String rhsTwo = "        $pRecord.setUseFirstPeriod(false);\n        $pRecord.setParkingFee(fee);";
        rnUFP.setRuleConditionStr(lhsCondition);
        rnUFP.setRuleResultStr(rhsOne + rhsTwo);
        rns.add(rnUFP);

        // 不能使用首时段的隐藏分割
        // 首时段长度，该次分割长度等于期间时长减去首时段
        Integer fpLot = pcdc.getDurationPeriods().get(0).getLengthOfTime();
        Integer nufpLot = lengthOfTime - fpLot;
        RuleNodeFact rnNUFP = new RuleNodeFact();
        rnNUFP.setRuleId(ruleId);
        rnNUFP.setSRuleId(srPrefix + "HD");
        // 设置节点ID与优先级
        rnNUFP.setNodeId(ParkingRuleEnums.SpecialNode.HIDE_DURATION_DIVISION_MULTIPLE_PERIOD.getId());
        rnNUFP.setSalience(ParkingRuleEnums.SpecialNode.HIDE_DURATION_DIVISION_MULTIPLE_PERIOD.getSalience());
        // LHS
        String rnNUFPLhs = MessageFormat.format("$duration: parkingDuration > {0,number,#}, !useFirstPeriod", nufpLot);
        // RHS
        String rnNUFPRhsOne = MessageFormat.format(
                "BigDecimal fee = ParkingFeeUtil.divideHideDuration2ChargeFee($pRecord, {0,number,#}, BigDecimal.valueOf({1,number,#.##}));\n",
                nufpLot, cp);
        String rnNUFPRhsTwo = "        $pRecord.setParkingFee(fee);";
        rnNUFP.setRuleConditionStr(rnNUFPLhs);
        rnNUFP.setRuleResultStr(rnNUFPRhsOne + rnNUFPRhsTwo);
        rns.add(rnNUFP);
        return rns;
    }

    /**
     * description: 组装24小时强制分割节点
     *
     * @param pCRule 计费规则
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     * @author mingchenxu
     * @date 2023/3/14 15:30
     */
    public static RuleNodeFact build24HDivisionNode(ParkChargeRuleCustom pCRule) {
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(pCRule.getId());
        rn.setSRuleId("24D");
        // 设置节点ID与优先级
        rn.setNodeId(ParkingRuleEnums.SpecialNode.H24_DIVISION.getId());
        rn.setSalience(ParkingRuleEnums.SpecialNode.H24_DIVISION.getSalience());
        // LHS
        String lhsCondition = "$duration: parkingDuration > 1440";
        // RHS
        // 最高限价，0元不生效
        BigDecimal ceilingPrice = BigDecimal.ZERO;
        if (YesOrNo.YES.getValue().equals(pCRule.getCeilingPriceFlag())) {
            ceilingPrice = Objects.requireNonNullElse(pCRule.getCeilingPrice(), BigDecimal.ZERO);
        }
        String rhsOne = MessageFormat.format(
                "    BigDecimal fee = ParkingFeeUtil.divide24Hours2ChargeFee($pRecord, \"{0}\", \"{1}\", BigDecimal.valueOf({2,number,#.##}));\n",
                pCRule.getFreeMinuteNumber(), pCRule.getFirstDurationChargeWay(), ceilingPrice);
        String rhsTwo = "    $pRecord.setParkingFee(fee);";
        rn.setRuleConditionStr(lhsCondition);
        rn.setRuleResultStr(rhsOne + rhsTwo);
        return rn;
    }

    /**
     * description: 组装0点强制分割节点
     *
     * @param pCRule 计费规则
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     * @author mingchenxu
     * @date 2023/3/14 15:30
     */
    public static RuleNodeFact buildZeroDivisionNode(ParkChargeRuleCustom pCRule) {
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(pCRule.getId());
        rn.setSRuleId("ZD");
        // 设置节点ID与优先级
        rn.setNodeId(ParkingRuleEnums.SpecialNode.ZERO_DIVISION.getId());
        rn.setSalience(ParkingRuleEnums.SpecialNode.ZERO_DIVISION.getSalience());
        // LHS
        String lhsCondition = "hasInterDay";
        // RHS
        // 最高限价，0元不生效
        BigDecimal ceilingPrice = BigDecimal.ZERO;
        if (YesOrNo.YES.getValue().equals(pCRule.getCeilingPriceFlag())) {
            ceilingPrice = Objects.requireNonNullElse(pCRule.getCeilingPrice(), BigDecimal.ZERO);
        }
        String rhsOne = MessageFormat.format(
                "    BigDecimal fee = ParkingFeeUtil.divideZero2ChargeFee($pRecord, \"{0}\", \"{1}\", BigDecimal.valueOf({2,number,#.##}));\n",
                pCRule.getFreeMinuteNumber(), pCRule.getFirstDurationChargeWay(), ceilingPrice);
        String rhsTwo = "    $pRecord.setParkingFee(fee);";
        rn.setRuleConditionStr(lhsCondition);
        rn.setRuleResultStr(rhsOne + rhsTwo);
        return rn;
    }

    /**
     * description: 组装期间强制分割节点
     *
     * @param pCRule 计费规则
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     * @author mingchenxu
     * @date 2023/3/14 15:30
     */
    public static RuleNodeFact buildDurationDivisionNode(ParkChargeRuleCustom pCRule) {
        // 只有一个时段的情况
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(pCRule.getId());
        rn.setSRuleId("DD");
        // 设置节点ID与优先级
        rn.setNodeId(ParkingRuleEnums.SpecialNode.DURATION_DIVISION.getId());
        rn.setSalience(ParkingRuleEnums.SpecialNode.DURATION_DIVISION.getSalience());
        // LHS
        Integer lengthOfTime = pCRule.getChargeDurations().get(0).getDurationLengthOfTime();
        String lhsCondition = MessageFormat.format("$duration: parkingDuration > {0,number,#}", lengthOfTime);
        // RHS
        // 最高限价，0元不生效
        BigDecimal ceilingPrice = BigDecimal.ZERO;
        if (YesOrNo.YES.getValue().equals(pCRule.getCeilingPriceFlag())) {
            ceilingPrice = Objects.requireNonNullElse(pCRule.getCeilingPrice(), BigDecimal.ZERO);
        }
        String rhsOne = MessageFormat.format(
                "    BigDecimal fee = ParkingFeeUtil.divideDuration2ChargeFee($pRecord, {0,number,#}, \"{1}\", \"{2}\", BigDecimal.valueOf({3,number,#.##}));\n",
                lengthOfTime, pCRule.getFreeMinuteNumber(), pCRule.getFirstDurationChargeWay(), ceilingPrice);
        String rhsTwo = "    $pRecord.setParkingFee(fee);";
        rn.setRuleConditionStr(lhsCondition);
        rn.setRuleResultStr(rhsOne + rhsTwo);
        return rn;
    }

    /**
     * description: 组装多时刻强制分割节点
     *
     * @param pCRule 计费规则
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     * @author mingchenxu
     * @date 2023/3/14 15:30
     */
    public static RuleNodeFact assembleMultipleTimeDivisionNode(ParkChargeRuleCustom pCRule, boolean isHide) {
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(pCRule.getId());
        rn.setSRuleId("TD");
        // 设置节点ID与优先级
        rn.setNodeId(ParkingRuleEnums.SpecialNode.DURATION_DIVISION.getId());
        rn.setSalience(ParkingRuleEnums.SpecialNode.DURATION_DIVISION.getSalience());
        // LHS -------------------
        String lhsCondition = "needMTDivide";
        // RHS -------------------
        // 最高限价，0元不生效
        BigDecimal ceilingPrice = BigDecimal.ZERO;
        if (YesOrNo.YES.getValue().equals(pCRule.getCeilingPriceFlag())) {
            ceilingPrice = Objects.requireNonNullElse(pCRule.getCeilingPrice(), BigDecimal.ZERO);
        }
        // 免费与首时段配置
        String fmn = ParkingRuleEnums.FreeMinuteNumber.ONE_CHARGE_ONE_TIME.getValue();
        String fdcw = ParkingRuleEnums.FirstDurationChargeWay.ONE_CHARGE_ONE_TIME.getValue();
        if (!isHide) {
            fmn = pCRule.getFreeMinuteNumber();
            fdcw = pCRule.getFirstDurationChargeWay();
        }
        // 根据时刻遍历出分割时间段
        StringBuilder rhsSb = new StringBuilder("BigDecimal fee = ParkingFeeUtil.divideTime2ChargeFee($pRecord, ")
                .append("BigDecimal.valueOf(").append(ceilingPrice).append("), ")
                .append("\"").append(fmn).append("\", ")
                .append("\"").append(fdcw).append("\", ");
        for (ParkChargeDurationCustom durationCustom : pCRule.getChargeDurations()) {
            rhsSb.append("\"").append(durationCustom.getStartTime()).append("\", ");
        }
        // 删除最后2个字符『, 』
        rhsSb.delete(rhsSb.length() - 2, rhsSb.length());
        rhsSb.append(");\n");
        String rhsTwo = "        $pRecord.setParkingFee(fee);";
        rn.setRuleConditionStr(lhsCondition);
        rn.setRuleResultStr(rhsSb + rhsTwo);
        return rn;
    }

    /**
     * description: 获取扣除免费时间节点
     *
     * @param ruleId      规则ID
     * @param sRuleId     子规则ID
     * @param freeMinutes 免费分钟
     * @param cp          限价
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     * @author mingchenxu
     * @date 2023/3/20 14:35
     */
    public static RuleNodeFact buildSubstractFMNode(int ruleId, String sRuleId, long freeMinutes, BigDecimal cp) {
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(ruleId);
        rn.setSRuleId(sRuleId);
        // 设置节点ID与优先级
        rn.setNodeId(ParkingRuleEnums.SpecialNode.NO_CONTAIN_FREE_MINUTE.getId());
        rn.setSalience(ParkingRuleEnums.SpecialNode.NO_CONTAIN_FREE_MINUTE.getSalience());
        // LHS
        rn.setRuleConditionStr("useFreeTime");
        // RHS
        String rhsOne = MessageFormat.format("BigDecimal fee = ParkingFeeUtil.substractFreeTime($pRecord, {0,number,#}, BigDecimal.valueOf({1,number,#.##}));\n",
                freeMinutes, cp);
        String rhsTwo = "        $pRecord.setParkingFee(fee);";
        rn.setRuleResultStr(rhsOne + rhsTwo);
        return rn;
    }

    private static RuleNodeFact buildFMNode(int ruleId, String sRuleId, int nodeId, long freeMinutes) {
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(ruleId);
        rn.setSRuleId(sRuleId);
        rn.setNodeId(nodeId);
        rn.setRuleConditionStr(MessageFormat.format("    $duration: parkingDuration <= {0,number,#}, useFreeTime", freeMinutes));
        rn.setRuleResultStr("    $pRecord.setParkingFee(BigDecimal.ZERO);");
        return rn;
    }

    /**
     * description: 获取时段节点
     *
     * @param ruleId       规则ID
     * @param sRuleId      子规则ID
     * @param nodeId       节点ID
     * @param dStart       开始时间段
     * @param duration     时段长度
     * @param minUnit      最新计费单位
     * @param rate         费率
     * @param timeRoundWay 舍入方式
     * @param lp           最高限价
     * @param existFee     已算费用
     * @param useFP        使用首时段
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     * @author mingchenxu
     * @date 2023/3/20 15:53
     */
    public static RuleNodeFact getPeriodNode(int ruleId, String sRuleId, int nodeId,
                                             long dStart, Long duration, long minUnit,
                                             BigDecimal rate, String timeRoundWay, BigDecimal lp,
                                             BigDecimal existFee, boolean useFP) {
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(ruleId);
        rn.setSRuleId(sRuleId);
        rn.setNodeId(nodeId);
        // LHS
        String rhsTwo = "        $pRecord.setParkingFee(fee);";
        if (useFP) {
            // 没有时长，说明是最后一个时段
            if (duration == null) {
                rn.setRuleConditionStr(MessageFormat.format("$duration: parkingDuration > {0,number,#}, useFirstPeriod", dStart));
            } else {
                rn.setRuleConditionStr(MessageFormat.format("$duration: parkingDuration > {0,number,#}, parkingDuration <= {1,number,#}, useFirstPeriod", dStart, dStart + duration));
            }
            // 能够使用首时段，则需要设置已使用
            rhsTwo = "        $pRecord.setUseFirstPeriod(false);\n        $pRecord.setParkingFee(fee);";
        } else {
            // 没有时长，说明是最后一个时段
            if (duration == null) {
                rn.setRuleConditionStr(MessageFormat.format("$duration: parkingDuration > {0,number,#}", dStart));
            } else {
                rn.setRuleConditionStr(MessageFormat.format("$duration: parkingDuration > {0,number,#}, parkingDuration <= {1,number,#}", dStart, dStart + duration));
            }
        }
        // RHS
        String rhsOne = MessageFormat.format(
                "BigDecimal fee = ParkingFeeUtil.chargeParkingFee($duration - {0,number,#}, " +
                        "{1,number,#}, " +
                        "BigDecimal.valueOf({2,number,#.##}), " +
                        "\"{3}\", " +
                        "BigDecimal.valueOf({4,number,#.##}), " +
                        "BigDecimal.valueOf({5,number,#.##}));\n",
                dStart, minUnit, rate, timeRoundWay, lp, existFee);
        rn.setRuleResultStr(rhsOne + rhsTwo);
        return rn;
    }

}
