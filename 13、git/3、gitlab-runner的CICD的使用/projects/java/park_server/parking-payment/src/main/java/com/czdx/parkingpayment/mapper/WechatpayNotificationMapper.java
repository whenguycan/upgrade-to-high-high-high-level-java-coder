package com.czdx.parkingpayment.mapper;

import com.czdx.parkingpayment.domain.WechatpayNotification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 琴声何来
* @description 针对表【t_wechatpay_notification(微信支付回调通知记录表)】的数据库操作Mapper
* @since 2023-03-08 17:33:48
* @Entity com.czdx.parkingpayment.domain.WechatpayNotification
*/
@Mapper
public interface WechatpayNotificationMapper extends BaseMapper<WechatpayNotification> {

}




