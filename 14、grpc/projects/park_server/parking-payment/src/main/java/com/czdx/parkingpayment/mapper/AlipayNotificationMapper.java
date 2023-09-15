package com.czdx.parkingpayment.mapper;

import com.czdx.parkingpayment.domain.AlipayNotification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 琴声何来
* @description 针对表【t_alipay_notification(支付宝回调通知记录表)】的数据库操作Mapper
* @since 2023-03-08 16:02:20
* @Entity com.czdx.parkingpayment.domain.AlipayNotification
*/
@Mapper
public interface AlipayNotificationMapper extends BaseMapper<AlipayNotification> {

}




