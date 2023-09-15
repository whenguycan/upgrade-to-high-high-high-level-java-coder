package com.czdx.parkingcharge.domain.pr;

import lombok.Data;

/**
 *
 * description: 规则节点对象
 * @author mingchenxu
 * @date 2023/3/6 18:03
 */
@Data
public class RuleNodeFact {

    /**
     * 主键
     */
    private Integer nodeId;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 优先级
     */
    private int salience;

    /**
     * 节点规则条件
     */
    private String ruleConditionStr;

    /**
     * 节点规则结果
     */
    private String ruleResultStr;

    /**
     * 规则id
     */
    private Integer ruleId;

    /**
     * 子规则id
     */
    private String sRuleId;

}
