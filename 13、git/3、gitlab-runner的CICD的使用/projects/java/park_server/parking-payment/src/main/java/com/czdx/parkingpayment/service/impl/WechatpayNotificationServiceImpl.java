package com.czdx.parkingpayment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingpayment.domain.WechatpayNotification;
import com.czdx.parkingpayment.service.IWechatpayNotificationService;
import com.czdx.parkingpayment.mapper.WechatpayNotificationMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author 琴声何来
 * @description 针对表【t_wechatpay_notification(微信支付回调通知记录表)】的数据库操作Service实现
 * @since 2023-03-08 17:33:48
 */
@Service
public class WechatpayNotificationServiceImpl extends ServiceImpl<WechatpayNotificationMapper, WechatpayNotification>
        implements IWechatpayNotificationService {

    /**
     * @apiNote 新增一条微信支付回调通知记录
     */
    @Override
    public boolean add(WechatpayNotification wechatpayNotification) {
        wechatpayNotification.setCreateTime(LocalDateTime.now());
        return save(wechatpayNotification);
    }
}




