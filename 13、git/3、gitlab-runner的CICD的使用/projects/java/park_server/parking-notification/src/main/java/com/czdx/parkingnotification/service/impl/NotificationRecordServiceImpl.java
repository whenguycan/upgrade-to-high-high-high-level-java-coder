package com.czdx.parkingnotification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingnotification.domain.NotificationRecord;
import com.czdx.parkingnotification.service.INotificationRecordService;
import com.czdx.parkingnotification.mapper.NotificationRecordMapper;
import org.springframework.stereotype.Service;

/**
 * @author 琴声何来
 * @description 针对表【t_notification_record(消息推送记录表)】的数据库操作Service实现
 * @since 2023-04-06 09:55:10
 */
@Service
public class NotificationRecordServiceImpl extends ServiceImpl<NotificationRecordMapper, NotificationRecord>
        implements INotificationRecordService {

    /**
     * @apiNote 根据微信公众号的推送消息id查询推送记录
     * @author 琴声何来
     * @since 2023/4/6 14:42
     * @param msgID 消息id
     * @return com.czdx.parkingnotification.domain.NotificationRecord
     */
    @Override
    public NotificationRecord getByMessageId(Long msgID) {
        LambdaQueryWrapper<NotificationRecord> qw = new LambdaQueryWrapper<>();
        qw.eq(NotificationRecord::getMessageId, msgID)
                .last("limit 1");
        return getOne(qw);
    }
}




