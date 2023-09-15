package com.ruoyi.project.parking.domain.param;

import com.ruoyi.project.parking.domain.BParkChargeDuration;
import com.ruoyi.project.parking.domain.BParkChargeDurationPeriod;
import lombok.Data;

import java.util.List;

@Data
public class BParkChargeDurationParam extends BParkChargeDuration {

    private List<BParkChargeDurationPeriod> periodList;
}
