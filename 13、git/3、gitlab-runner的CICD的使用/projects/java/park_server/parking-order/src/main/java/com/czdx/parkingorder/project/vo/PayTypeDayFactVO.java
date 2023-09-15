package com.czdx.parkingorder.project.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayTypeDayFactVO {

    /** 付费类型【字典表】 */
    private String payType;

    /** 金额（元） */
    private BigDecimal amount;

    /** 占比 */
    private BigDecimal ratio;
}
