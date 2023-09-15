package com.czdx.parkingcharge.domain.custom;

import com.czdx.parkingcharge.domain.ParkChargeDuration;
import com.czdx.parkingcharge.domain.ParkChargeDurationPeriod;
import lombok.Data;

import java.util.List;

/**
 *
 * description: 停车计费区间扩展类
 * @author mingchenxu
 * @date 2023/3/14 09:52
 */
@Data
public class ParkChargeDurationCustom extends ParkChargeDuration {

    /**
     * 时段
     */
    private List<ParkChargeDurationPeriod> durationPeriods;

}
