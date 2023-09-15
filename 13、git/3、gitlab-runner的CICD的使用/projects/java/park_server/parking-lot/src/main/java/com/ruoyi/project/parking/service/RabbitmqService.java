package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.notification.CarEntryNotificationData;
import com.ruoyi.project.parking.domain.notification.CarExitNotificationData;
import com.ruoyi.project.parking.domain.notification.CarPayNotificationData;
import com.ruoyi.project.parking.mq.model.CashPayNotificationData;
import com.ruoyi.project.parking.mq.model.PaymentData;

import java.util.HashMap;

/**
 *
 * description: Rabbitmq 服务类
 * @author mingchenxu
 * @date 2023/2/23 15:12
 */
public interface RabbitmqService {

    /**
     *
     * description: 推送车辆入场提醒
     * @author mingchenxu
     * @date 2023/2/23 15:14
     */
    void pushCarEntryNotification(CarEntryNotificationData carEntryNotificationDTO);


    /**
     * 推送车辆出场提醒
     * @param carExitNotificationDTO 车场退出消息
     */
    void pushCarExitNotification(CarExitNotificationData carExitNotificationDTO);


    /**
     * 推送支付成功提醒
     * @param carPayNotificationData 支付成功消息
     */
    void pushPaySuccessNotification(CarPayNotificationData carPayNotificationData);


    /**
     * 推送现金支付  至 订单系统
     * @param cashPayNotificationData 现金支付成功消息
     */
    void pushCashPayToOrder(CashPayNotificationData cashPayNotificationData);

    /**
     * 推送订单完成  至 闸道设备
     * @param paymentData 现金支付成功消息
     */
    void pushOrderCompleteToDevice(PaymentData paymentData);

    /**
     * 推送0元订单支付完成订单 至 订单
     * 由 order 推送此消息，会有时序上的问题(会先得到消费支付成功，再创建订单成功)
     * @param data 订单完成消息
     */
    void pushZeroOrderPaiedToOrder(HashMap<String, String> data);
}
