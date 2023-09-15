package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.NotificationRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.parking.domain.param.NotificationRecordParam;

import java.util.List;

/**
* @author 琴声何来
* @description 针对表【t_notification_record(消息推送记录表)】的数据库操作Service
* @since 2023-03-10 17:09:08
*/
public interface INotificationRecordService extends IService<NotificationRecord> {

    /**
     * @apiNote 获取消息推送记录列表
     */
    List<NotificationRecord> listNotificationRecords(NotificationRecordParam notificationRecordParam);
}
