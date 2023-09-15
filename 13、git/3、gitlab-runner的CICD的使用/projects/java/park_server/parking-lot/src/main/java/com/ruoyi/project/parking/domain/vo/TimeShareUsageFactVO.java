package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TimeShareUsageFactVO {

    /** 时段 */
    private String name;

    /** 利用率 */
    private BigDecimal value;
}
