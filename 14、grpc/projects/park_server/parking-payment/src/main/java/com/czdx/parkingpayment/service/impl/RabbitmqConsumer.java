package com.czdx.parkingpayment.service.impl;

import com.czdx.common.RabbitmqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 自测能否收到rabbitmq队列消息
 * </p>
 *
 * @author 琴声何来
 * @since 2023/3/2 17:39
 */
@Slf4j
@Component
public class RabbitmqConsumer {
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = RabbitmqConstant.PAYMENT_RESULT_QUEUE, durable = "true"),
//            exchange = @Exchange(value = RabbitmqConstant.PAYMENT_RESULT_EXCHANGE, durable = "true", type = ExchangeTypes.TOPIC),
//            key = RabbitmqConstant.PAYMENT_RESULT_ROUTING_KEY
//    ))
//    public void consumerTestMsg(String data) {
//        log.info("接收到支付异步通知消息提醒，{}", data);
//    }
}
