package com.czdx.parkingpayment.service.impl;

import com.alibaba.fastjson.JSON;
import com.czdx.common.RabbitmqConstant;
import com.czdx.parkingpayment.domain.AlipayNotification;
import com.czdx.parkingpayment.domain.notification.RabbitmqNotification;
import com.czdx.parkingpayment.enums.PayMethodEnum;
import com.czdx.parkingpayment.service.IRabbitMessageProvider;
import com.wechat.pay.java.service.payments.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * rabbitmq发送异步通知消息
 * </p>
 *
 * @author 琴声何来
 * @since 2023/3/2 17:39
 */
@Slf4j
@Service
public class RabbitMessageProviderImpl implements IRabbitMessageProvider {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * @apiNote 发送一条支付宝回调通知消息
     */
    @Async
    @Override
    public void pushAlipayResultMessage(AlipayNotification alipayNotification) {
        log.info("rabbitmq发送支付宝异步消息通知：{}", JSON.toJSONString(alipayNotification));
        RabbitmqNotification rabbitmqNotification = new RabbitmqNotification();
        rabbitmqNotification.setOutTradeNo(alipayNotification.getOutTradeNo());
        rabbitmqNotification.setTradeNo(alipayNotification.getTradeNo());
        rabbitmqNotification.setTotalAmount(alipayNotification.getTotalAmount());
        rabbitmqNotification.setTradeStatus(alipayNotification.getTradeStatus());
        rabbitmqNotification.setPayTime(alipayNotification.getGmtPayment());
        rabbitmqNotification.setPayMethod(PayMethodEnum.ALIPAY.getValue());
        // 支付宝针对同一条异步通知重试时，异步通知参数中的 notify_id 是不变的。
        CorrelationData correlationData = new CorrelationData(alipayNotification.getNotifyId());
        rabbitTemplate.convertAndSend(RabbitmqConstant.PAYMENT_RESULT_EXCHANGE, RabbitmqConstant.PAYMENT_RESULT_ROUTING_KEY, JSON.toJSONString(alipayNotification), correlationData);
    }

    /**
     * @apiNote 发送一条微信支付回调通知消息
     */
    @Async
    @Override
    public void pushWechatpayResultMessage(Transaction transaction, String notifyId) {
        log.info("rabbitmq发送微信支付异步消息通知：{}", JSON.toJSONString(transaction));
        RabbitmqNotification rabbitmqNotification = new RabbitmqNotification();
        rabbitmqNotification.setOutTradeNo(transaction.getOutTradeNo());
        rabbitmqNotification.setTradeNo(transaction.getTransactionId());
        //微信支付默认单位为分，需转换成元返回
        rabbitmqNotification.setTotalAmount(BigDecimal.valueOf(transaction.getAmount().getTotal()).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING));
        rabbitmqNotification.setTradeStatus(transaction.getTradeState().name());
        //微信支付返回的时间格式为 yyyy-MM-DDTHH:mm:ss+TIMEZONE 例：2015-05-20T13:29:35+08:00
        rabbitmqNotification.setPayTime(LocalDateTime.parse(transaction.getSuccessTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        rabbitmqNotification.setPayMethod(PayMethodEnum.WECHAT.getValue());
        CorrelationData correlationData = new CorrelationData(notifyId);
        rabbitTemplate.convertAndSend(RabbitmqConstant.PAYMENT_RESULT_EXCHANGE, RabbitmqConstant.PAYMENT_RESULT_ROUTING_KEY, JSON.toJSONString(rabbitmqNotification), correlationData);
    }
}
