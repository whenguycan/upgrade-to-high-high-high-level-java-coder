package com.czdx.parkingpayment.service;

import com.czdx.parkingpayment.domain.AlipayNotification;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 琴声何来
* @description 针对表【t_alipay_notification(支付宝回调通知记录表)】的数据库操作Service
* @since 2023-03-08 15:55:25
*/
public interface IAlipayNotificationService extends IService<AlipayNotification> {

    /**
     * @apiNote 新增一条支付宝回调通知记录
     */
    boolean add(AlipayNotification alipayNotification);
}
