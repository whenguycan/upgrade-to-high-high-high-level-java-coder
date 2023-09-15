package com.czdx.parkingorder.project.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayMethodDayFactVO {

    /** 付费方式【字典表】 */
    private String payMethod;

    /** 金额（元） */
    private BigDecimal amount;

    /** 占比 */
    private BigDecimal ratio;
}
