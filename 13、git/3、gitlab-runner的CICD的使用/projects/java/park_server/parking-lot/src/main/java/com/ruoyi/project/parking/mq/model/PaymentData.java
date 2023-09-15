package com.ruoyi.project.parking.mq.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * MQ数据格式
 */
@Data
public class PaymentData {

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 闸道编号
     */
    private String passageNo;

    /**
     * 车场
     */
    private String parkNo;

    /**
     *金额
     */
    private BigDecimal payAmount;

    /**
     * 订单号
     */
    private String outTradeNo;

    /**
     * 进场时间
     */
    private String entryTime;

    /**
     * 出场时间
     */
    private String exitTime;

    /**
     * 在场时间
     */
    private String durationTime;

}
