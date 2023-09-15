package com.czdx.parkingpayment.config;

import com.czdx.common.RabbitmqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 支付子系统消息队列配置类
 * </p>
 *
 * @author 琴声何来
 * @since 2023/3/22 10:28
 */
@Configuration
public class PaymentRabbitmqConfig {

    //内置交换机类型 DirectExchange、FanoutExchange、TopicExchange
    @Bean
    public CustomExchange paymentExchange() {

        //自定义交换机的参数
        //1. 交换机的名字
        //2. 交换机的类型
        //3. 交换机数据是否需要持久化
        //4. 交换机是否需要自动删除
        //5. 其它配置参数
        return new CustomExchange(RabbitmqConstant.EXCHANGE_PAYMENT_ORDER_RESULT, "topic", true, false);
    }

    //声明一个Queue
    @Bean
    public Queue paymentQueue() {
        return new Queue(RabbitmqConstant.QUEUE_ORDER_PAYMENT_RESULT);
    }

    //将Exchange与Queue进行绑定
    @Bean
    public Binding paymentBinding(
            @Qualifier("paymentQueue") Queue queue,
            @Qualifier("paymentExchange") CustomExchange customExchange) {
        return BindingBuilder.bind(queue).to(customExchange).with(RabbitmqConstant.ROUTE_ORDER_PAYMENT_RESULT).noargs();
    }
}
