package com.czdx.parkingpayment.domain.notification;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RabbitmqNotification {

    /**
     * 原支付请求的商户订单号。
     */
    private String outTradeNo;

    /**
     * 第三方交易凭证号。
     */
    private String tradeNo;

    /**
     * 交易目前所处的状态。
     */
    private String tradeStatus;

    /**
     * 本次交易支付的订单金额，单位为人民币（元）。
     */
    private BigDecimal totalAmount;

    /**
     * 该笔交易 的买家付款时间。
     */
    private LocalDateTime payTime;

    /**
     * 付款方式。1：支付宝 2：微信支付 3：银联
     */
    private String payMethod;
}
