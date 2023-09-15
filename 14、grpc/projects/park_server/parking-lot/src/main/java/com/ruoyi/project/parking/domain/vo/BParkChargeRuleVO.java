package com.ruoyi.project.parking.domain.vo;

import com.ruoyi.project.parking.domain.BParkChargeRule;
import lombok.Data;

import java.util.List;

@Data
public class BParkChargeRuleVO extends BParkChargeRule {

    private List<BParkChargeDurationVO> durationList;
}
