package com.czdx.parkingpayment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingpayment.domain.AlipayNotification;
import com.czdx.parkingpayment.mapper.AlipayNotificationMapper;
import com.czdx.parkingpayment.service.IAlipayNotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
* @author 琴声何来
* @description 针对表【t_alipay_notification(支付宝回调通知记录表)】的数据库操作Service实现
* @since 2023-03-08 16:02:20
*/
@Service
public class AlipayNotificationServiceImpl extends ServiceImpl<AlipayNotificationMapper, AlipayNotification>
    implements IAlipayNotificationService {

    /**
     * @apiNote 新增一条支付宝回调通知记录
     */
    @Override
    public boolean add(AlipayNotification alipayNotification) {
        alipayNotification.setCreateTime(LocalDateTime.now());
        return save(alipayNotification);
    }
}




