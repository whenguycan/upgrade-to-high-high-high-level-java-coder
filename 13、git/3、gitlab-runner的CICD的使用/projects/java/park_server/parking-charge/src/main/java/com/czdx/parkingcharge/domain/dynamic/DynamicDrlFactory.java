package com.czdx.parkingcharge.domain.dynamic;

import com.czdx.parkingcharge.domain.ParkChargeDurationPeriod;
import com.czdx.parkingcharge.domain.ParkChargeRelationVehicle;
import com.czdx.parkingcharge.domain.custom.ParkChargeDurationCustom;
import com.czdx.parkingcharge.domain.custom.ParkChargeRuleCustom;
import com.czdx.parkingcharge.domain.custom.ParkChargeVehicleRelationCustom;
import com.czdx.parkingcharge.system.exception.UnsupportedChargeRuleException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


/**
 *
 * description: 动态规则文件工厂类
 * @author mingchenxu
 * @date 2023/3/15 13:23
 */
public class DynamicDrlFactory {

    private DynamicDrlFactory() {}

    public static DynamicDrl newDynamicDrl(ParkChargeRuleCustom chargeRule) {
        ParkChargeDurationCustom chargeDuration = chargeRule.getChargeDurations().get(0);
        // 时长类型
        if (isLengthOfTimeType(chargeDuration)) {
            // 时长类型只有一个期间
            if (chargeRule.getChargeDurations().size() == 1) {
                List<ParkChargeDurationPeriod> periods = chargeDuration.getDurationPeriods();
                // 如果为单期间单时段
                if (CollectionUtils.isNotEmpty(periods)) {
                    if (periods.size() == 1) {
                        return new AloneDurationAlonePeriodDrl(chargeRule);
                    } else {
                        // 单期间多时段
                        return new AloneDurationMultiplePeriodDrl(chargeRule);
                    }
                }
            }
        } else {
            // 时刻类型
            return new MultipleTimeDurationPeriodDrl(chargeRule);
        }
        throw new UnsupportedChargeRuleException("该计费规则暂不支持");
    }

    /**
     *
     * description: 【计费规则-车型】关联关系动态DRL
     * @author mingchenxu
     * @date 2023/3/31 13:38
     * @param pcrv
     * @return com.czdx.parkingcharge.domain.dynamic.DynamicDrl
     */
    public static DynamicDrl newDynamicDrl(ParkChargeVehicleRelationCustom pcvrc) {
        return new ChargeRuleVehicleRelationDrl(pcvrc);
    }

    /**
     * 是否为时长类型
     * @param chargeDuration 计费期间
     * @return
     */
    public static boolean isLengthOfTimeType(ParkChargeDurationCustom chargeDuration) {
        return StringUtils.isEmpty(chargeDuration.getStartTime());
    }

}
