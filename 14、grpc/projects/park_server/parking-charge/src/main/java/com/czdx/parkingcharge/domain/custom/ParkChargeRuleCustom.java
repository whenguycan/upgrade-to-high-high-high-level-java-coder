package com.czdx.parkingcharge.domain.custom;

import com.czdx.parkingcharge.domain.ParkChargeRule;
import lombok.Data;

import java.util.List;

/**
 *
 * description: 计费规则扩展类
 * @author mingchenxu
 * @date 2023/3/14 09:50
 */
@Data
public class ParkChargeRuleCustom extends ParkChargeRule {

    /**
     * 计费区间
     */
    private List<ParkChargeDurationCustom> chargeDurations;

}
