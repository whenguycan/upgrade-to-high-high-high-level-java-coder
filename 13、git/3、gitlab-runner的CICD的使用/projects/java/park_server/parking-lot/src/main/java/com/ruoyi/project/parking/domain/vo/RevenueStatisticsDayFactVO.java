package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RevenueStatisticsDayFactVO {

    /** 应收金额（元） */
    private BigDecimal payableAmount;

    /** 线上实收金额（元） */
    private BigDecimal onlinePayAmount;

    /** 现金实收金额（元） */
    private BigDecimal cashPayAmount;

    /** 车场优惠（笔） */
    private Integer concession;

    /** 交易（笔） */
    private Integer transactionNumber;
}
