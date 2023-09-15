package com.czdx.parkingcharge.domain.dynamic;

import com.czdx.parkingcharge.domain.custom.ParkChargeDurationCustom;
import com.czdx.parkingcharge.domain.custom.ParkChargeRuleCustom;
import com.czdx.parkingcharge.system.exception.UnsupportedChargeRuleException;

import java.time.temporal.UnsupportedTemporalTypeException;


/**
 *
 * description: 动态规则文件工厂类
 * @author mingchenxu
 * @date 2023/3/15 13:23
 */
public class DynamicDrlFactory {

    private DynamicDrlFactory() {}

    public static DynamicDrl newDynamicDrl(ParkChargeRuleCustom chargeRule) {
        if (chargeRule.getChargeDurations().size() == 1) {
            ParkChargeDurationCustom chargeDuration = chargeRule.getChargeDurations().get(0);
            if (isLengthOfTimeType(chargeDuration) && chargeDuration.getDurationPeriods().size() == 1) {
                return new AloneDurationAlonePeriodDrl(chargeRule);
            }
        }
        throw new UnsupportedChargeRuleException("该计费规则暂不支持");
    }

    /**
     * 是否为时长类型
     * @param chargeDuration 计费期间
     * @return
     */
    public static boolean isLengthOfTimeType(ParkChargeDurationCustom chargeDuration) {
        return chargeDuration.getLengthOfTime() != null;
    }

}
