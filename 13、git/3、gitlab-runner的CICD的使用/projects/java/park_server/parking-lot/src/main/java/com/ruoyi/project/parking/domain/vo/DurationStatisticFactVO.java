package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DurationStatisticFactVO {

    /** 标签 */
    private String name;

    /** 车辆数 */
    private Integer value;

    /** 占比 */
    private BigDecimal ratio;
}
