package com.czdx.parkingnotification.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingnotification.domain.NotificationRecord;
import com.czdx.parkingnotification.service.INotificationRecordService;
import com.czdx.parkingnotification.mapper.NotificationRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author 琴声何来
* @description 针对表【t_notification_record(消息推送记录表)】的数据库操作Service实现
* @since 2023-03-13 08:49:15
*/
@Service
public class NotificationRecordServiceImpl extends ServiceImpl<NotificationRecordMapper, NotificationRecord>
    implements INotificationRecordService {

}




