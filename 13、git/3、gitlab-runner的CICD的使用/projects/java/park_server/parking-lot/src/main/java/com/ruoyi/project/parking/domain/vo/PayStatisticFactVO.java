package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayStatisticFactVO {

    /** 标签 */
    private String name;

    /** 金额(元) */
    private BigDecimal value;

    /** 占比 */
    private BigDecimal ratio;
}
