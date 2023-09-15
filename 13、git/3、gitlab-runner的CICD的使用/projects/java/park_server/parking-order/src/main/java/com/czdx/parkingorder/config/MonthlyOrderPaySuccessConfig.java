package com.czdx.parkingorder.config;

import com.czdx.common.RabbitmqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: tangwei
 * @Date: 2023/3/17 10:02 AM
 * @Description: 类描述信息
 */
@Configuration
public class MonthlyOrderPaySuccessConfig {

    //声明一个交换机，因为我要使用的是延迟交换机，而内置交换机的类型并不支持所以只能使用自定义的交换机类型CustomExchange了。
    //内置交换机类型 DirectExchange、FanoutExchange、TopicExchange
    @Bean
    public CustomExchange orderExchange(){

        //自定义交换机的参数
        //1. 交换机的名字
        //2. 交换机的类型
        //3. 交换机数据是否需要持久化
        //4. 交换机是否需要自动删除
        //5. 其它配置参数
        return new CustomExchange(RabbitmqConstant.EXCHANGE_ORDER_PAYSUCCESS, "topic", true, false);
    }

    //声明一个Queue
    @Bean
    public Queue merchantQueue(){
        return new Queue(RabbitmqConstant.QUEUE_MEMBER_MERCHANT_ORDER_PAYSUCCESS);
    }

    //将Exchange与Queue进行绑定
    @Bean
    public Binding merchantBinding(
            @Qualifier("merchantQueue") Queue queue,
            @Qualifier("orderExchange") CustomExchange customExchange){
        return BindingBuilder.bind(queue).to(customExchange).with(RabbitmqConstant.ROUTE_MEMBER_MERCHANT_ORDER_PAYSUCCESS).noargs();
    }

    @Bean
    public Queue monthlyQueue(){
        return new Queue(RabbitmqConstant.QUEUE_MEMBER_MONTHLY_ORDER_PAYSUCCESS);
    }

    //将Exchange与Queue进行绑定
    @Bean
    public Binding monthlyBinding(
            @Qualifier("monthlyQueue") Queue queue,
            @Qualifier("orderExchange") CustomExchange customExchange){
        return BindingBuilder.bind(queue).to(customExchange).with(RabbitmqConstant.ROUTE_MEMBER_MONTHLY_ORDER_PAYSUCCESS).noargs();
    }

    @Bean
    public Queue parkingQueue(){
        return new Queue(RabbitmqConstant.QUEUE_LOT_PARKING_ORDER_PAYSUCCESS);
    }

    @Bean
    public Binding parkingOrderBinding(
            @Qualifier("parkingQueue") Queue queue,
            @Qualifier("orderExchange") CustomExchange customExchange
    ){
        return BindingBuilder.bind(queue).to(customExchange).with(RabbitmqConstant.ROUTE_LOT_PARKING_ORDER_PAYSUCCESS).noargs();
    }

}
