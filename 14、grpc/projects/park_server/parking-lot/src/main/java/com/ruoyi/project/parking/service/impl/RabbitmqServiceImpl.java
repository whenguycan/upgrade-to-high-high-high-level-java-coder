package com.ruoyi.project.parking.service.impl;

import com.czdx.common.RabbitmqConstant;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.project.parking.domain.notification.CarEntryNotificationData;
import com.ruoyi.project.parking.domain.notification.CarExitNotificationData;
import com.ruoyi.project.parking.service.RabbitmqService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * description: mq服务实现类
 * @author mingchenxu
 * @date 2023/2/23 15:13
 */
@Service("rabbitmqService")
public class RabbitmqServiceImpl implements RabbitmqService {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitmqServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     *
     * description: 推送车辆入场提醒
     * @author mingchenxu
     * @date 2023/2/23 15:14
     */
    @Override
    public void pushCarEntryNotification(CarEntryNotificationData carEntryNotificationDTO) {
        push(RabbitmqConstant.EXCHANGE_PARKING_CAR_ENTRY_NOTIFICATION,
                RabbitmqConstant.ROUTE_PARKING_CAR_ENTRY_NOTIFICATION,
                carEntryNotificationDTO);
    }


    /**
     * 推送车辆出场提醒
     * @param carExitNotificationDTO 车场退出消息
     */
    @Override
    public void pushCarExitNotification(CarExitNotificationData carExitNotificationDTO) {
        push(RabbitmqConstant.EXCHANGE_PARKING_CAR_ENTRY_NOTIFICATION,
                RabbitmqConstant.ROUTE_PARKING_CAR_ENTRY_NOTIFICATION,
                carExitNotificationDTO);
    }

    /**
     *
     * description: 推送
     * @author mingchenxu
     * @date 2023/2/23 15:20
     * @param exchange 交换机
     * @param route 路由
     * @param data 推送的数据
     */
    private void push(String exchange, String route, Object data) {
        CorrelationData correlationId = new CorrelationData(UUID.fastUUID().toString());
        rabbitTemplate.convertAndSend(exchange, route, data, correlationId);
    }
}
