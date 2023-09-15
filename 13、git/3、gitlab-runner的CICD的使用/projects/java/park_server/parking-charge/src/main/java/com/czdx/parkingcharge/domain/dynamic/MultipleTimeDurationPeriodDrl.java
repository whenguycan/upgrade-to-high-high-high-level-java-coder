package com.czdx.parkingcharge.domain.dynamic;

import com.czdx.parkingcharge.common.constants.BUStr;
import com.czdx.parkingcharge.common.utils.RomeUtil;
import com.czdx.parkingcharge.domain.ParkChargeDurationPeriod;
import com.czdx.parkingcharge.domain.custom.ParkChargeDurationCustom;
import com.czdx.parkingcharge.domain.custom.ParkChargeRuleCustom;
import com.czdx.parkingcharge.domain.pr.RuleNodeFact;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * description: 多时刻期间时段DRL
 * @author mingchenxu
 * @date 2023/3/27 17:29
 */
@Slf4j
public class MultipleTimeDurationPeriodDrl implements DynamicDrl {

    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern(BUStr.CHARGE_TIME_FORMATTER_STR);

    private ParkChargeRuleCustom chargeRule;

    public MultipleTimeDurationPeriodDrl(ParkChargeRuleCustom chargeRule) {
        this.chargeRule = chargeRule;
    }

    @Override
    public DynamicConditionMap getDynamicConditionMap() {
        log.info("准备组装车场[{}]的计费规则条件Map，规则类型<时刻-期间-时段>，规则ID：[{}]\n", chargeRule.getParkNo(), chargeRule.getRuleName());
        // 组装时长停车规则节点
        List<RuleNodeFact> ruleNodeFacts = assembleDurationParkingRuleNode(chargeRule);
        // 规则节点转填充为模板Map对象
        List<Map<String, Object>> maps = fillConditionMap(chargeRule.getParkNo(), ruleNodeFacts);
        // 规则名称 车场编号-规则ID
        String ruleName = chargeRule.getParkNo() + "_" + chargeRule.getId();
        return new DynamicConditionMap(ruleName, maps);
    }

    /**
     *
     * description: 组装停车计费规则
     * @author mingchenxu
     * @date 2023/3/27 17:38
     * @param pr 计费规则
     * @return java.util.List<com.czdx.parkingcharge.domain.pr.RuleNodeFact>
     */
    private List<RuleNodeFact> assembleDurationParkingRuleNode(ParkChargeRuleCustom pr) {
        BigDecimal ceilingPrice = getCeilingPrice(pr);
        // 顶层-组装通用节点，包括最高限价分割、24小时分割、0点分割，（区分隐藏与配置，本质上是一样的，配置的需要考虑免费时长次数与首时段计费方式）期间分割、隐藏分割
        // 免费时长不计费扣减、免费节点跟着独立的期间走
        List<RuleNodeFact> ruleNodeFacts = ChargeNodeUtil.assembleTimeCommonNodes(pr, ceilingPrice);

        // 每个期间走单期间-单/多时刻基础节点生成流程
        int ruleId = pr.getId();
        String ccft = pr.getChargeContainFreeTime();
        String timeRoundWay = pr.getTimeRoundWay();
        // 遍历期间，设置期间时长
        List<ParkChargeDurationCustom> chargeDurations = pr.getChargeDurations();
        for (int i = 0; i < chargeDurations.size(); i++) {
            List<RuleNodeFact> ruleNodes = null;
            // 期间代码采用罗马数字
            String sRuleId = RomeUtil.intToRoman(i+1);
            ParkChargeDurationCustom chargeDuration = chargeDurations.get(i);
            int endIndex = (i + 1) % chargeDurations.size();
            LocalTime startLT = LocalTime.parse(chargeDuration.getStartTime(), DEFAULT_TIME_FORMATTER);
            LocalTime endLT = LocalTime.parse(chargeDurations.get(endIndex).getStartTime(), DEFAULT_TIME_FORMATTER);
            // 设置期间时长，需要判断是否跨天，跨天需要跟00:00比较
            int duration = 0;
            if (endLT.isAfter(startLT)) {
                duration = (int) Duration.between(startLT, endLT).toMinutes();
            } else {
                duration = (86400 - startLT.toSecondOfDay() + endLT.toSecondOfDay()) / 60;
            }
            chargeDuration.setDurationLengthOfTime(duration);
            // 根据时段类型（单/多）生成普通节点
            if (chargeDuration.getDurationPeriods().size() == 1) {
                // 单时段
                ruleNodes = assembleSinglePeriodRuleNode(ruleId, sRuleId, ccft, timeRoundWay, ceilingPrice, chargeDuration);
            } else {
                // 多时段
                ruleNodes = assembleMultiplePeriodRuleNode(ruleId, sRuleId, ccft, timeRoundWay, ceilingPrice, chargeDuration);
            }
            ruleNodeFacts.addAll(ruleNodes);
        }
        return ruleNodeFacts;
    }

    /**
     *
     * description: 组装单时段规则节点 - 与期间单时段一致
     * @author mingchenxu
     * @date 2023/3/28 15:34
     * @return java.util.List<com.czdx.parkingcharge.domain.pr.RuleNodeFact>
     */
    private List<RuleNodeFact> assembleSinglePeriodRuleNode(int ruleId, String sRuleId, String ccft, String timeRoundWay, BigDecimal cp, ParkChargeDurationCustom ruleDuration) {
        List<RuleNodeFact> ruleNodeFacts = new ArrayList<>();

        int nodeId = 0;

        // 构建隐藏分割节点
        RuleNodeFact hideDivisionNode = ChargeNodeUtil.buildSinglePeriodHideDivisionNode(ruleId, sRuleId, cp, ruleDuration);
        ruleNodeFacts.add(hideDivisionNode);

        // 构建免费时长不计分扣减、免费节点
        if (ruleDuration.getFreeMinute() > 0) {
            RuleNodeFact fmNode = ChargeNodeUtil.buildFMNode(ccft, ruleDuration.getFreeMinute(), cp, ruleId, sRuleId, nodeId);
            ruleNodeFacts.add(fmNode);
            nodeId ++;
        }

        // 获取限制价格，期间内的限制价格，取期间限价与最高限价的较小者
        BigDecimal limitPrice = getLimitPrice(ruleDuration.getMaximumCharge(), cp);

        // 构建时段节点，只有一个时段
        ParkChargeDurationPeriod durationPeriod = ruleDuration.getDurationPeriods().get(0);
        RuleNodeFact periodNode = ChargeNodeUtil.buildSinglePeriodNode(ruleId, sRuleId, nodeId, limitPrice, timeRoundWay, durationPeriod);
        ruleNodeFacts.add(periodNode);
        return ruleNodeFacts;
    }

    /**
     *
     * description: 组装多时段规则节点 - 与期间多时段一致
     * @author mingchenxu
     * @date 2023/3/28 15:34
     * @return java.util.List<com.czdx.parkingcharge.domain.pr.RuleNodeFact>
     */
    private List<RuleNodeFact> assembleMultiplePeriodRuleNode(int ruleId, String sRuleId, String ccft, String timeRoundWay, BigDecimal cp, ParkChargeDurationCustom ruleDuration) {
        int nodeId = 0;

        // 构建隐藏分割节点
        List<RuleNodeFact> hideDivisionNodes = ChargeNodeUtil.buildMultiplePeriodHideDurationDivisionNode(ruleId, sRuleId, cp, ruleDuration);
        List<RuleNodeFact> ruleNodeFacts = new ArrayList<>(hideDivisionNodes);

        // 构建免费时长不计分扣减、免费节点
        if (ruleDuration.getFreeMinute() > 0) {
            RuleNodeFact fmNode = ChargeNodeUtil.buildFMNode(ccft, ruleDuration.getFreeMinute(), cp, ruleId, sRuleId, nodeId);
            ruleNodeFacts.add(fmNode);
            nodeId ++;
        }

        // 获取限制价格，期间内的限制价格，取期间限价与最高限价的较小者
        BigDecimal limitPrice = getLimitPrice(ruleDuration.getMaximumCharge(), cp);

        // 多个时段
        List<ParkChargeDurationPeriod> durationPeriods = ruleDuration.getDurationPeriods();
        List<RuleNodeFact> periodNodes = ChargeNodeUtil.buildMultiplePeriodNodes(ruleId, sRuleId, nodeId, limitPrice, timeRoundWay, durationPeriods);
        ruleNodeFacts.addAll(periodNodes);
        return ruleNodeFacts;
    }
}
