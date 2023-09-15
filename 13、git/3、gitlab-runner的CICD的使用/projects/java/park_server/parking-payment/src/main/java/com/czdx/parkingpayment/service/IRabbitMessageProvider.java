package com.czdx.parkingpayment.service;

import com.czdx.parkingpayment.domain.AlipayNotification;
import com.wechat.pay.java.service.payments.model.Transaction;

public interface IRabbitMessageProvider {

    /**
     * @apiNote 发送一条支付宝回调通知消息
     */
    void pushAlipayResultMessage(AlipayNotification alipayNotification);

    /**
     * @apiNote 发送一条微信支付回调通知消息
     */
    void pushWechatpayResultMessage(Transaction transaction, String notifyId);
}
