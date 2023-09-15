package com.czdx.parkingcharge.domain.dynamic;

import com.czdx.parkingcharge.domain.custom.ParkChargeRuleCustom;
import com.czdx.parkingcharge.domain.pr.RuleNodeFact;

import java.math.BigDecimal;
import java.util.*;

/**
 *
 * description: 动态规则文件接口
 * @author mingchenxu
 * @date 2023/3/15 08:42
 */
public interface DynamicDrl {


    /**
     *
     * description: 填充条件Map
     * @author mingchenxu
     * @date 2023/3/6 18:08
     * @param nodeFacts 规则节点对象
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    default List<Map<String, Object>> fillConditionMap(String parkNo, List<RuleNodeFact> nodeFacts) {
        List<Map<String, Object>> conditionMapList = new ArrayList<>();
        for (RuleNodeFact n : nodeFacts) {
            Map<String, Object> conditionMap = new HashMap<>();
            conditionMap.put("parkNo", parkNo);
            conditionMap.put("ruleId", n.getRuleId());
            conditionMap.put("sRuleId", n.getSRuleId());
            conditionMap.put("nodeId", n.getNodeId());
            conditionMap.put("ruleSalience", n.getSalience());
            conditionMap.put("ruleCondition", n.getRuleConditionStr());
            conditionMap.put("ruleResult", n.getRuleResultStr());
            conditionMapList.add(conditionMap);
        }
        return conditionMapList;
    }

    /**
     *
     * description: 获取限制价格
     * @author mingchenxu
     * @date 2023/3/17 10:09
     * @param durationPrice 期间限价
     * @param ceilingPrice 最高限价
     * @return java.math.BigDecimal
     */
    default BigDecimal getLimitPrice(BigDecimal durationPrice, BigDecimal ceilingPrice) {
        durationPrice = Objects.requireNonNullElse(durationPrice, BigDecimal.ZERO);
        ceilingPrice = Objects.requireNonNullElse(ceilingPrice, BigDecimal.ZERO);
        return durationPrice.compareTo(ceilingPrice) > 0 ? ceilingPrice : durationPrice;
    }

    /**
     *
     * description: 获取动态条件Map
     * @author mingchenxu
     * @date 2023/3/15 08:45
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    DynamicConditionMap getDynamicConditionMap();

}
