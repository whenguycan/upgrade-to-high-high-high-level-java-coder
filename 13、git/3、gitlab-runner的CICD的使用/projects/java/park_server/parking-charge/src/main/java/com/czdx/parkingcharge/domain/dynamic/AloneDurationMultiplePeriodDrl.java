package com.czdx.parkingcharge.domain.dynamic;

import com.czdx.parkingcharge.domain.ParkChargeDurationPeriod;
import com.czdx.parkingcharge.domain.custom.ParkChargeDurationCustom;
import com.czdx.parkingcharge.domain.custom.ParkChargeRuleCustom;
import com.czdx.parkingcharge.domain.pr.RuleNodeFact;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * description: 单期间多时段规则文件
 * @author mingchenxu
 * @date 2023/3/20 15:14
 */
@Slf4j
public class AloneDurationMultiplePeriodDrl implements DynamicDrl{

    private ParkChargeRuleCustom chargeRule;

    public AloneDurationMultiplePeriodDrl(ParkChargeRuleCustom chargeRule) {
        this.chargeRule = chargeRule;
    }

    @Override
    public DynamicConditionMap getDynamicConditionMap() {
        log.info("准备组装车场[{}]的计费规则条件Map，规则类型<时长-单期间-多时段>，规则ID：[{}]\n", chargeRule.getParkNo(), chargeRule.getRuleName());
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
     * @date 2023/3/6 18:09
     * @param pr 停车计费规则
     * @return java.util.List<com.iwither.droolsspringboot.entity.RuleNodeFact>
     */
    private List<RuleNodeFact> assembleDurationParkingRuleNode(ParkChargeRuleCustom pr) {
        BigDecimal ceilingPrice = getCeilingPrice(pr);
        // 组装通用节点，包括最高限价分割、24小时分割、0点分割、期间分割、隐藏分割、免费时长不计分扣减、免费节点
        List<RuleNodeFact> ruleNodeFacts = ChargeNodeUtil.assembleDurationCommonNodes(pr, ceilingPrice);

        // 该模板只有一个期间
        ParkChargeDurationCustom ruleDuration = pr.getChargeDurations().get(0);
        // 规则ID和期间ID
        int ruleId = pr.getId();
        String sRuleId = String.valueOf(ruleDuration.getId());
        String ccft = pr.getChargeContainFreeTime();
        int nodeId = 0;

        // 构建隐藏分割节点
        List<RuleNodeFact> hideDivisionNodes = ChargeNodeUtil.buildMultiplePeriodHideDurationDivisionNode(ruleId, "", ceilingPrice, ruleDuration);
        ruleNodeFacts.addAll(hideDivisionNodes);

        // 构建免费时长不计分扣减、免费节点
        if (ruleDuration.getFreeMinute() > 0) {
            RuleNodeFact fmNode = ChargeNodeUtil.buildFMNode(ccft, ruleDuration.getFreeMinute(), ceilingPrice, ruleId, sRuleId, nodeId);
            ruleNodeFacts.add(fmNode);
            nodeId ++;
        }

        // 获取限制价格，期间内的限制价格，取期间限价与最高限价的较小者
        BigDecimal limitPrice = getLimitPrice(ruleDuration.getMaximumCharge(), ceilingPrice);
        String timeRoundWay = pr.getTimeRoundWay();

        // 多个时段
        List<ParkChargeDurationPeriod> durationPeriods = ruleDuration.getDurationPeriods();
        List<RuleNodeFact> periodNodes = ChargeNodeUtil.buildMultiplePeriodNodes(ruleId, sRuleId, nodeId, limitPrice, timeRoundWay, durationPeriods);
        ruleNodeFacts.addAll(periodNodes);
        return ruleNodeFacts;
    }
}
