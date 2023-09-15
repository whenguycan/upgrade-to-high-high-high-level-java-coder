package com.czdx.parkingcharge.domain.dynamic;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DynamicConditionMap {

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 条件Map
     */
    private List<Map<String, Object>> dynamicConditionMap;

    public DynamicConditionMap(String ruleName, List<Map<String, Object>> dynamicConditionMap) {
        this.ruleName = ruleName;
        this.dynamicConditionMap = dynamicConditionMap;
    }
}
