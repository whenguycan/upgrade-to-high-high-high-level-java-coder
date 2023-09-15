package com.czdx.parkingpayment.domain.notification;

import lombok.Data;

/**
 * <p>
 * 支付宝异步通知消息（原格式）
 * 验签只能通过Map<String,String>格式传参
 * </p>
 *
 * @author 琴声何来
 * @since 2023/3/2 13:37
 */
@Data
public class AlipayNotificationStr {

    private String app_id;
    private String gmt_create;
    private String charset;
    private String seller_email;
    private String subject;
    private String sign;
    private String buyer_id;
    private String invoice_amount;
    private String notify_id;
    private String fund_bill_list;
    private String notify_type;
    private String trade_status;
    private String receipt_amount;
    private String buyer_pay_amount;
    private String sign_type;
    private String seller_id;
    private String gmt_payment;
    private String notify_time;
    private String version;
    private String out_trade_no;
    private String total_amount;
    private String trade_no;
    private String auth_app_id;
    private String buyer_logon_id;
    private String point_amount;
    private String out_biz_no;
    private String gmt_refund;
}
