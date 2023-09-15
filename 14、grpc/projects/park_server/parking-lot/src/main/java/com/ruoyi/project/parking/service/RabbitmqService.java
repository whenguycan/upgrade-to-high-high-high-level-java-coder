package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.notification.CarEntryNotificationData;
import com.ruoyi.project.parking.domain.notification.CarExitNotificationData;

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
}
