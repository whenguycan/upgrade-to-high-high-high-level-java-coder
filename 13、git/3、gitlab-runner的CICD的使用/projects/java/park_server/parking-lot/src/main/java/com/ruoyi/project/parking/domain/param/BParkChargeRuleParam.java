package com.ruoyi.project.parking.domain.param;

import com.ruoyi.project.parking.domain.BParkChargeRule;
import lombok.Data;

import java.util.List;

@Data
public class BParkChargeRuleParam extends BParkChargeRule {

    private List<BParkChargeDurationParam> durationList;
}
