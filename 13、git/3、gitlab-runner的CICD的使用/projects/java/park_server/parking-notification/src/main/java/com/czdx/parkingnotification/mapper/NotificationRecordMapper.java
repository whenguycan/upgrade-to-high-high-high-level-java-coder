package com.czdx.parkingnotification.mapper;

import com.czdx.parkingnotification.domain.NotificationRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 琴声何来
* @description 针对表【t_notification_record(消息推送记录表)】的数据库操作Mapper
* @since 2023-04-06 09:55:10
* @Entity com.czdx.parkingnotification.domain.NotificationRecord
*/
@Mapper
public interface NotificationRecordMapper extends BaseMapper<NotificationRecord> {

}




