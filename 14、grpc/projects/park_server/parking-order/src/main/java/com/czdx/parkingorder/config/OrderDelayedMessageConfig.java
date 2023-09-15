package com.czdx.parkingorder.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.czdx.common.RabbitmqConstant;
import com.czdx.parkingorder.project.entity.ParkingOrderDelayedMessageEntity;
import com.czdx.parkingorder.project.service.ParkingOrderDelayedMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: tangwei
 * @Date: 2023/3/1 1:20 PM
 * @Description: 类描述信息
 */
@Configuration
@Slf4j
public class OrderDelayedMessageConfig {


    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ParkingOrderDelayedMessageService parkingOrderDelayedMessageService;

    //声明一个交换机
    @Bean
    public CustomExchange delayedExchange(){
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");//声明延迟类型，即延迟队列中的数据到时间了，根据什么样的Routing Key规则投递到Queue中。

        //自定义交换机的参数
        //1. 交换机的名字
        //2. 交换机的类型
        //3. 交换机数据是否需要持久化
        //4. 交换机是否需要自动删除
        //5. 其它配置参数
        return new CustomExchange(RabbitmqConstant.ORDER_DELAYED_MESSAGE_EXCHANGE, "x-delayed-message", true, false, arguments);
    }


    //声明一个队列
    @Bean
    public Queue delayedQueue(){
        return new Queue(RabbitmqConstant.ORDER_DELAYED_MESSAGE_QUEUE);
    }

    //将交换机和路由进行板顶
    @Bean
    public Binding delayedBinding(
            @Qualifier("delayedExchange") CustomExchange exchange,
            @Qualifier("delayedQueue") Queue queue
    ){
        return BindingBuilder.bind(queue).to(exchange).with(RabbitmqConstant.ORDER_DELAYED_ROUTING_KEY).noargs();
    }

    @PostConstruct
    public void setRabbitConfirmCallback(){
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (ack) {
                    ParkingOrderDelayedMessageEntity parkingOrderDelayedMessageEntity = parkingOrderDelayedMessageService.getBaseMapper().selectOne(new QueryWrapper<ParkingOrderDelayedMessageEntity>().eq("order_no", correlationData.getId()));
                    if(parkingOrderDelayedMessageEntity == null){
                        parkingOrderDelayedMessageEntity = new ParkingOrderDelayedMessageEntity();
                        parkingOrderDelayedMessageEntity.setOrderNo(correlationData.getId());
                    }
                    parkingOrderDelayedMessageEntity.setStatus(1);
                    parkingOrderDelayedMessageService.save(parkingOrderDelayedMessageEntity);
                    System.out.println("消息成功发送到Exchange");
                } else {

                    ParkingOrderDelayedMessageEntity parkingOrderDelayedMessageEntity = parkingOrderDelayedMessageService.getBaseMapper().selectOne(new QueryWrapper<ParkingOrderDelayedMessageEntity>().eq("order_no", correlationData.getId()));
                    if(parkingOrderDelayedMessageEntity == null){
                        parkingOrderDelayedMessageEntity = new ParkingOrderDelayedMessageEntity();
                        parkingOrderDelayedMessageEntity.setOrderNo(correlationData.getId());
                    }
                    parkingOrderDelayedMessageEntity.setStatus(0);
                    parkingOrderDelayedMessageService.save(parkingOrderDelayedMessageEntity);

                    log.info("消息发送到Exchange失败, {}, cause: {}", correlationData, cause);
                }
            }
        });
    }

    @PostConstruct
    public void setRabbitReturnCallback(){
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                System.out.println("消息没有到达："+returnedMessage + "因为是延时消息，无法直达queue");
            }
        });
    }

}
