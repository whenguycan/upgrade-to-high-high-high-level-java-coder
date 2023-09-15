package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.parking.domain.NotificationRecord;
import com.ruoyi.project.parking.domain.param.NotificationRecordParam;
import com.ruoyi.project.parking.service.INotificationRecordService;
import com.ruoyi.project.parking.mapper.NotificationRecordMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 琴声何来
 * @description 针对表【t_notification_record(消息推送记录表)】的数据库操作Service实现
 * @since 2023-03-10 17:09:08
 */
@Service
public class NotificationRecordServiceImpl extends ServiceImpl<NotificationRecordMapper, NotificationRecord>
        implements INotificationRecordService {

    /**
     * @apiNote 获取消息推送记录列表
     */
    @Override
    public List<NotificationRecord> listNotificationRecords(NotificationRecordParam notificationRecordParam) {
        LambdaQueryWrapper<NotificationRecord> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(notificationRecordParam.getSendTime()), NotificationRecord::getSendTime, notificationRecordParam.getSendTime())
                .ge(notificationRecordParam.getStartTime() != null, NotificationRecord::getNotifyTime, notificationRecordParam.getStartTime())
                .le(notificationRecordParam.getEndTime() != null, NotificationRecord::getNotifyTime, notificationRecordParam.getEndTime())
                .like(StringUtils.isNotEmpty(notificationRecordParam.getUserPhone()), NotificationRecord::getUserPhone, notificationRecordParam.getUserPhone());
        return list(qw);
    }
}




