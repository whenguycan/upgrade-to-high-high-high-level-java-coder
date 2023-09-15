package com.ruoyi.project.parking.service.impl;

import com.alibaba.fastjson2.JSON;
import com.czdx.common.RabbitmqConstant;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.project.parking.domain.notification.CarEntryNotificationData;
import com.ruoyi.project.parking.domain.notification.CarExitNotificationData;
import com.ruoyi.project.parking.domain.notification.CarPayNotificationData;
import com.ruoyi.project.parking.mq.model.CashPayNotificationData;
import com.ruoyi.project.parking.mq.model.PaymentData;
import com.ruoyi.project.parking.service.RabbitmqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * description: mq服务实现类
 *
 * @author mingchenxu
 * @date 2023/2/23 15:13
 */
@Slf4j
@Service("rabbitmqService")
public class RabbitmqServiceImpl implements RabbitmqService {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitmqServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * description: 推送车辆入场提醒
     *
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
     *
     * @param carExitNotificationDTO 车场退出消息
     */
    @Override
    public void pushCarExitNotification(CarExitNotificationData carExitNotificationDTO) {
        push(RabbitmqConstant.EXCHANGE_PARKING_CAR_EXIT_NOTIFICATION,
                RabbitmqConstant.ROUTE_PARKING_CAR_EXIT_NOTIFICATION,
                carExitNotificationDTO);
    }

    /**
     * 推送支付成功提醒
     *
     * @param carPayNotificationData 支付成功消息
     */
    @Override
    public void pushPaySuccessNotification(CarPayNotificationData carPayNotificationData) {
        push(RabbitmqConstant.EXCHANGE_LOT_PARKING_PAYSUCCESS,
                RabbitmqConstant.ROUTE_NOTIFICATION_PARKING_PAYSUCCESS,
                carPayNotificationData);
    }

    @Override
    public void pushCashPayToOrder(CashPayNotificationData cashPayNotificationData) {
        log.info("发送现金结算至订单：{}", JSON.toJSONString(cashPayNotificationData));
        push(RabbitmqConstant.EXCHANGE_PAYMENT_ORDER_RESULT,
                RabbitmqConstant.ROUTE_ORDER_PAYMENT_RESULT,
                cashPayNotificationData);
    }

    @Override
    public void pushOrderCompleteToDevice(PaymentData paymentData) {
        log.info("推送订单完成至闸道：{}", JSON.toJSONString(paymentData));
        push(RabbitmqConstant.EXCHANGE_LOT_PARKING_PAYSUCCESS,
                RabbitmqConstant.ROUTE_DEVICE_PARKING_PAYSUCCESS,
                paymentData);
    }


    @Override
    public void pushZeroOrderPaiedToOrder(HashMap<String, String> data) {
        log.info("推送0元订单支付完成：{}", JSON.toJSONString(data));
        push(RabbitmqConstant.EXCHANGE_ORDER_PAYSUCCESS,
                RabbitmqConstant.ROUTE_LOT_PARKING_ORDER_PAYSUCCESS,
                data);
    }

    /**
     * description: 推送
     *
     * @param exchange 交换机
     * @param route    路由
     * @param data     推送的数据
     * @author mingchenxu
     * @date 2023/2/23 15:20
     */
    private void push(String exchange, String route, Object data) {
        CorrelationData correlationId = new CorrelationData(UUID.fastUUID().toString());
        rabbitTemplate.convertAndSend(exchange, route, JSON.toJSONString(data), correlationId);
    }

}
