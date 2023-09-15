package com.ruoyi.project.merchant.rabbitmq;

import com.czdx.common.RabbitmqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitmqConsumer {
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = RabbitmqConstant.PAYMENT_RESULT_QUEUE, durable = "true"),
//            exchange = @Exchange(value = RabbitmqConstant.PAYMENT_RESULT_EXCHANGE, durable = "true", type = ExchangeTypes.TOPIC),
//            key = RabbitmqConstant.PAYMENT_RESULT_ROUTING_KEY
//    ))
    public void consumerMsg(String data) {
        log.info("接收到订单回调异步通知消息提醒，{}", data);
        // todo 根据通知结果修改订单状态
    }
}
