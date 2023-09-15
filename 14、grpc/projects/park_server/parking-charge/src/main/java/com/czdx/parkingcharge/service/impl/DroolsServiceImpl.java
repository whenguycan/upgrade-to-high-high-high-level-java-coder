package com.czdx.parkingcharge.service.impl;

import com.czdx.parkingcharge.common.enums.YesOrNo;
import com.czdx.parkingcharge.domain.ParkChargeDurationPeriod;
import com.czdx.parkingcharge.domain.ParkChargeRule;
import com.czdx.parkingcharge.domain.custom.ParkChargeDurationCustom;
import com.czdx.parkingcharge.domain.custom.ParkChargeRuleCustom;
import com.czdx.parkingcharge.domain.dynamic.DynamicConditionMap;
import com.czdx.parkingcharge.domain.dynamic.DynamicDrl;
import com.czdx.parkingcharge.domain.dynamic.DynamicDrlFactory;
import com.czdx.parkingcharge.domain.pr.ParkingRecord;
import com.czdx.parkingcharge.domain.pr.ParkingRuleEnums;
import com.czdx.parkingcharge.domain.pr.RuleNodeFact;
import com.czdx.parkingcharge.service.DroolsService;
import com.czdx.parkingcharge.system.config.drools.KieBaseTemplate;
import com.czdx.parkingcharge.system.config.drools.KieUtil;
import com.czdx.parkingcharge.utils.ParkingFeeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("droolsService")
public class DroolsServiceImpl implements DroolsService {

    /**
     * description: 计算停车费
     *
     * @param parkingRecord 停车记录
     * @author mingchenxu
     * @date 2023/3/10 11:17
     */
    @Override
    public BigDecimal chargeParkingFee(ParkingRecord parkingRecord) {
        // 根据车场配置，对预计费的停车记录进行处理
        handlePreChargeFeeRecord(parkingRecord);
        if (parkingRecord.getParkingDuration() > 0) {
            KieSession kieSession = KieUtil.getKieBase().newKieSession();
            kieSession.insert(parkingRecord);
            kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter(parkingRecord.getRulePrefix()));
            kieSession.dispose();
            // 走完了规则变成了垃圾对象，也无法被垃圾回收器回收，需要手动清除
//            kieSession.delete(factHandle);
        } else {
            parkingRecord.setParkingFee(BigDecimal.ZERO);
        }
        return parkingRecord.getParkingFee();
    }

    /**
     * description: 编译计费规则
     *
     * @param parkingRule 计费规则
     * @author mingchenxu
     * @date 2023/3/14 13:31
     */
    @Override
    public void compileChargeRule(ParkChargeRuleCustom parkingRule) {
        try {
            DynamicDrl dynamicDrl = DynamicDrlFactory.newDynamicDrl(parkingRule);
            DynamicConditionMap dcm = dynamicDrl.getDynamicConditionMap();
            // 编译停车规则
            compileParkingFeeRule(dcm.getRuleName(), dcm.getDynamicConditionMap());
        } catch (Exception e) {
            log.error("编译车场[{}]计费规则[{}]异常，异常信息：{}", parkingRule.getParkNo(), parkingRule.getRuleName(), e.getMessage(), e);
        }
    }

    /**
     * description: 根据车场配置，对预计费停车记录进行处理
     *
     * @param pr 停车记录
     * @author mingchenxu
     * @date 2023/3/9 15:20
     */
    private void handlePreChargeFeeRecord(ParkingRecord pr) {
        // 处理秒设置，不为空才需要计算
        // 停车开始时间默认进位
        LocalDateTime enTime = ParkingFeeUtil.handleSecondCarry(pr.getEntryTime(), ParkingRuleEnums.SecondCarry.UP.getValue());
        // todo 结束时间根据车场配置进位or截秒
        LocalDateTime exTime = ParkingFeeUtil.handleSecondCarry(pr.getExitTime(), ParkingRuleEnums.SecondCarry.IGNORE.getValue());
        pr.setEntryTime(enTime);
        pr.setExitTime(exTime);
        // 计算停车时长
        long pd = Duration.between(enTime, exTime).toMinutes();
        pr.setParkingDuration(pd);
        // 是否跨天判断
        pr.setHasInterDay(!enTime.toLocalDate().isEqual(exTime.toLocalDate()));
    }

    /**
     * description: 组装停车计费规则
     *
     * @param parkingRule 停车计费规则
     * @return java.util.List<com.iwither.droolsspringboot.entity.RuleNodeFact>
     * @author mingchenxu
     * @date 2023/3/6 18:09
     */
    private List<RuleNodeFact> assembleDurationParkingRuleNode(ParkChargeRuleCustom parkingRule) {
        List<RuleNodeFact> ruleNodeFacts = new ArrayList<>();
        // 存在最高限价，增加最高限价分割
        if (YesOrNo.YES.getValue().equals(parkingRule.getCeilingPriceFlag())) {
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
        RuleNodeFact hideDivisionNode = assembleDurationHideDivisionNode(parkingRule);
        if (hideDivisionNode != null) {
            ruleNodeFacts.add(hideDivisionNode);
        }
        // 遍历规则期间
        List<ParkChargeDurationCustom> ruleDurations = parkingRule.getChargeDurations();
        for (int i = 0; i < ruleDurations.size(); i++) {
            // 节点ID
            int nodeId = 0;
            // 已计算时长
            long existDuration = 0L;
            // 已计算费用
            BigDecimal existFee = BigDecimal.ZERO;
            ParkChargeDurationCustom ruleDuration = ruleDurations.get(i);
            // 免费时长
            long freeDuration = ruleDuration.getFreeMinute();
            // 构建免费节点
            RuleNodeFact freeNode = getFreeNode(parkingRule.getId(), ruleDuration.getId(), nodeId, freeDuration);
            ruleNodeFacts.add(freeNode);
            nodeId += 1;
            // 算费是否包含免费时间，不包含需要加上免费时段，否则不需要加上免费时段
            if (YesOrNo.NO.getValue().equals(parkingRule.getChargeContainFreeTime())) {
//                existDuration += freeDuration;
            }
            // 遍历规则期间-时段
            List<ParkChargeDurationPeriod> durationPeriods = ruleDuration.getDurationPeriods();
            if (CollectionUtils.isNotEmpty(durationPeriods)) {
                for (int j = 0; j < durationPeriods.size(); j++) {
                    ParkChargeDurationPeriod durationPeriod = durationPeriods.get(j);
                    long period = durationPeriod.getLengthOfTime();
                    long minUnitPeriod = durationPeriod.getMinLenghtOfTime();
                    BigDecimal rate = durationPeriod.getRate();
                    String timeRoundWay = parkingRule.getTimeRoundWay();

                    // 构建期间-时段节点
                    RuleNodeFact rn = new RuleNodeFact();
                    rn.setRuleId(parkingRule.getId());
                    rn.setSRuleId(ruleDuration.getId());
                    rn.setNodeId(nodeId);
                    // LHS
                    String lhsCondition = "";
                    // 时段没有限制
                    if (period == 0L) {
                        lhsCondition = MessageFormat.format("$duration: parkingDuration > {0,number,#}", existDuration);
                    } else {
                        long durationOne = existDuration == 0 ? freeDuration : existDuration;
                        long durationTwo = existDuration + period;
                        lhsCondition = MessageFormat.format("$duration: parkingDuration > {0,number,#}, $duration <= {1,number,#}", durationOne, durationTwo);
                    }
                    // RHS
                    String resultOne = MessageFormat.format("long chargeMins = $duration - {0,number,#};\n", existDuration);
                    String resultTwo = MessageFormat.format("    BigDecimal fee = ParkingFeeUtil.calculateParkingFee(chargeMins, {0,number,#}, BigDecimal.valueOf({1,number,#}), String.valueOf({2}));\n", minUnitPeriod, rate, timeRoundWay);
                    String resultThree = MessageFormat.format("    BigDecimal totalFee = fee.add(BigDecimal.valueOf({0,number,#})).add($pRecord.getParkingFee());\n", existFee);
                    String resultFour = "    $pRecord.setParkingFee(totalFee);";
                    rn.setRuleConditionStr(lhsCondition);
                    rn.setRuleResultStr(resultOne + resultTwo + resultThree + resultFour);
                    ruleNodeFacts.add(rn);
                    nodeId += 1L;
                    // 计算本期间总费用
                    BigDecimal periodTotalFee = ParkingFeeUtil.chargeParkingFee(period, minUnitPeriod, rate, timeRoundWay);
                    // 计算已存在的总费用
                    existFee = existFee.add(periodTotalFee);
                    // 计算已存在的总时段
                    existDuration += period;
                }
            }

        }

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
                "    BigDecimal fee = ParkingFeeUtil.divideCP2ChargeFee($pRecord, {0,number,#}, BigDecimal.valueOf({1,number,#}), {2}, {3});\n",
                ceilingPriceMinutes, ceilingPrice, parkingRule.getFreeMinuteNumber(), parkingRule.getFirstDurationChargeWay());
        String rhsTwo = "    $pRecord.setParkingFee(fee);";
        rn.setRuleConditionStr(lhsCondition);
        rn.setRuleResultStr(rhsOne + rhsTwo);
        return rn;
    }

    /**
     * description: 组装分割节点
     *
     * @param pCRule 停车记录
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     * @author mingchenxu
     * @date 2023/3/14 14:19
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
     * description: 组装期间隐藏分割节点
     *
     * @param pCRule 计费规则
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     * @author mingchenxu
     * @date 2023/3/14 17:50
     */
    private static RuleNodeFact assembleDurationHideDivisionNode(ParkChargeRuleCustom pCRule) {
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(pCRule.getId());
        rn.setSRuleId(0);
        // 设置节点ID与优先级
        rn.setNodeId(ParkingRuleEnums.DivisionNode.HIDE_DURATION_DIVISION.getId());
        rn.setSalience(ParkingRuleEnums.DivisionNode.HIDE_DURATION_DIVISION.getSalience());
        // 只有一个时段的情况
        List<ParkChargeDurationPeriod> durationPeriods = pCRule.getChargeDurations().get(0).getDurationPeriods();
        if (durationPeriods.size() == 1) {
            // LHS
            Integer lengthOfTime = pCRule.getChargeDurations().get(0).getLengthOfTime();
            String lhsCondition = MessageFormat.format("$duration: parkingDuration > {0,number,#}", lengthOfTime);
            // RHS
            String rhsOne = "";
            // 需要区分是否存在最高限价
            if (YesOrNo.YES.getValue().equals(pCRule.getCeilingPriceFlag())) {
                rhsOne = MessageFormat.format(
                        "    BigDecimal fee = ParkingFeeUtil.divideDuration2ChargeFee($pRecord, {0,number,#}, \"1\", \"1\", BigDecimal.valueOf({1,number,#}));\n",
                        lengthOfTime, pCRule.getCeilingPrice());
            } else {
                rhsOne = MessageFormat.format(
                        "    BigDecimal fee = ParkingFeeUtil.divideDuration2ChargeFee($pRecord, {0,number,#}, \"1\", \"1\", );\n",
                        lengthOfTime);
            }
            String rhsTwo = "    $pRecord.setParkingFee(fee);";
            rn.setRuleConditionStr(lhsCondition);
            rn.setRuleResultStr(rhsOne + rhsTwo);
        } else {
            // todo 多时段的处理
        }
        return rn;
    }

    /**
     * description: 组装24小时强制分割节点
     *
     * @param pCRule 计费规则
     * @return com.czdx.parkingcharge.domain.pr.RuleNodeFact
     * @author mingchenxu
     * @date 2023/3/14 15:30
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
        String rhsOne = "";
        // 需要区分是否存在最高限价
        if (YesOrNo.YES.getValue().equals(pCRule.getCeilingPriceFlag())) {
            rhsOne = MessageFormat.format(
                    "    BigDecimal fee = ParkingFeeUtil.divide24Hours2ChargeFee($pRecord, {0}, {1}, BigDecimal.valueOf({2,number,#}));\n",
                    pCRule.getFreeMinuteNumber(), pCRule.getFirstDurationChargeWay(), pCRule.getCeilingPrice());
        } else {
            rhsOne = MessageFormat.format(
                    "    BigDecimal fee = ParkingFeeUtil.divide24Hours2ChargeFee($pRecord, {0}, {1});\n",
                    pCRule.getFreeMinuteNumber(), pCRule.getFirstDurationChargeWay());
        }
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
        String rhsOne = "";
        // 需要区分是否存在最高限价
        if (YesOrNo.YES.getValue().equals(pCRule.getCeilingPriceFlag())) {
            rhsOne = MessageFormat.format(
                    "    BigDecimal fee = ParkingFeeUtil.divideZero2ChargeFee($pRecord, {0}, {1}, BigDecimal.valueOf({2,number,#}));\n",
                    pCRule.getFreeMinuteNumber(), pCRule.getFirstDurationChargeWay(), pCRule.getCeilingPrice());
        } else {
            rhsOne = MessageFormat.format(
                    "    BigDecimal fee = ParkingFeeUtil.divide24Hours2ChargeFee($pRecord, {0}, {1});\n",
                    pCRule.getFreeMinuteNumber(), pCRule.getFirstDurationChargeWay());
        }
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
    private static RuleNodeFact assembleDurationDivisionNode(ParkChargeRuleCustom pCRule) {
        // 期间强制分割，只对一个期间的有效
        if (pCRule.getDurationNum() > 1) {
            return null;
        }
        RuleNodeFact rn = null;
        // 只有一个时段的情况
        List<ParkChargeDurationPeriod> durationPeriods = pCRule.getChargeDurations().get(0).getDurationPeriods();
        if (durationPeriods.size() == 1) {
            rn = new RuleNodeFact();
            rn.setRuleId(pCRule.getId());
            rn.setSRuleId(0);
            // 设置节点ID与优先级
            rn.setNodeId(ParkingRuleEnums.DivisionNode.DURATION_DIVISION.getId());
            rn.setSalience(ParkingRuleEnums.DivisionNode.DURATION_DIVISION.getSalience());
            // LHS
            Integer lengthOfTime = pCRule.getChargeDurations().get(0).getLengthOfTime();
            String lhsCondition = MessageFormat.format("$duration: parkingDuration > {0,number,#}", lengthOfTime);
            // RHS
            String rhsOne = "";
            // 需要区分是否存在最高限价
            if (YesOrNo.YES.getValue().equals(pCRule.getCeilingPriceFlag())) {
                rhsOne = MessageFormat.format(
                        "    BigDecimal fee = ParkingFeeUtil.divideDuration2ChargeFee($pRecord, {0,number,#}, {1}, {2}, BigDecimal.valueOf({3,number,#}));\n",
                        lengthOfTime, pCRule.getFreeMinuteNumber(), pCRule.getFirstDurationChargeWay(), pCRule.getCeilingPrice());
            } else {
                rhsOne = MessageFormat.format(
                        "    BigDecimal fee = ParkingFeeUtil.divideDuration2ChargeFee($pRecord, {0,number,#}, {1}, {2});\n",
                        lengthOfTime, pCRule.getFreeMinuteNumber(), pCRule.getFirstDurationChargeWay());
            }
            String rhsTwo = "    $pRecord.setParkingFee(fee);";
            rn.setRuleConditionStr(lhsCondition);
            rn.setRuleResultStr(rhsOne + rhsTwo);
        } else {
            // todo 多时段的处理
        }
        return rn;
    }

    private static RuleNodeFact getFreeNode(int ruleId, int sRuleId, int nodeId, long freeDuration) {
        RuleNodeFact rn = new RuleNodeFact();
        rn.setRuleId(ruleId);
        rn.setSRuleId(sRuleId);
        rn.setNodeId(nodeId);
        rn.setRuleConditionStr(MessageFormat.format("$duration: parkingDuration <= {0,number,#}, useFreeTime", freeDuration));
        rn.setRuleResultStr("$pRecord.setParkingFee(BigDecimal.ZERO);");
        return rn;
    }

    /**
     * description: 填充条件Map
     *
     * @param nodeFacts 规则节点对象
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author mingchenxu
     * @date 2023/3/6 18:08
     */
    private List<Map<String, Object>> fillConditionMap(String parkNo, List<RuleNodeFact> nodeFacts) {
        List<Map<String, Object>> conditionMapList = new ArrayList<>();
        for (RuleNodeFact n : nodeFacts) {
            Map<String, Object> conditionMap = new HashMap<>();
            conditionMap.put("parkNo", parkNo);
            conditionMap.put("ruleId", n.getRuleId());
            conditionMap.put("nodeId", n.getNodeId());
            conditionMap.put("salience", n.getSalience());
            conditionMap.put("ruleCondition", n.getRuleConditionStr());
            conditionMap.put("ruleResult", n.getRuleResultStr());
            conditionMapList.add(conditionMap);
        }
        return conditionMapList;
    }

    /**
     * description: 编译停车计费规则
     *
     * @param conditionMapList 条件Map
     * @author mingchenxu
     * @date 2023/3/6 18:09
     */
    private void compileParkingFeeRule(String ruleName, List<Map<String, Object>> conditionMapList) {
        ObjectDataCompiler converter = new ObjectDataCompiler();
        String drlContent = "";
        try (InputStream dis = ResourceFactory.
                newClassPathResource("rules/park_charge_rule_template.drt", "UTF-8")
                .getInputStream()) {
            // 填充模板内容
            drlContent = converter.compile(conditionMapList, dis);
            log.info("生成的规则内容:{}", drlContent);
        } catch (IOException e) {
            log.error("获取规则模板文件出错:{}", e.getMessage());
        }
        // 检查错误，无错误再进行编译
        if (checkDrlError(drlContent)) {
            // 增加规则
            KieUtil.addRule(drlContent, ruleName);
            // 删除原来的规则
            KieUtil.delete(ruleName, ResourceType.DRL);
            // 更新KieBase
            KieUtil.updateKieBase();
        }
    }

    /**
     * 检查DRL文件异常
     * @param drl 规则文件字符串
     * @return
     */
    public static boolean checkDrlError(String drl) {
        KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kb.add(ResourceFactory.newByteArrayResource(drl.getBytes(StandardCharsets.UTF_8)), ResourceType.DRL);
        if (kb.hasErrors()) {
            String errorMessage = kb.getErrors().toString();
            log.error("规则语法异常---{}\n", errorMessage);
            return false;
        }
        return true;
    }
}
