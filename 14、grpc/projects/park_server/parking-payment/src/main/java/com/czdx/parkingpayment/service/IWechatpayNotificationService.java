package com.czdx.parkingpayment.service;

import com.czdx.parkingpayment.domain.WechatpayNotification;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 琴声何来
* @description 针对表【t_wechatpay_notification(微信支付回调通知记录表)】的数据库操作Service
* @since 2023-03-08 15:55:59
*/
public interface IWechatpayNotificationService extends IService<WechatpayNotification> {

    /**
     * @apiNote 新增一条微信支付回调通知记录
     */
    void add(WechatpayNotification wechatpayNotification);
}
