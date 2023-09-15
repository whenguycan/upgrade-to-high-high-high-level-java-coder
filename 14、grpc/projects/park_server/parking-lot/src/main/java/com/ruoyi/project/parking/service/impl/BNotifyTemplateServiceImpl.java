package com.ruoyi.project.parking.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.common.RabbitmqConstant;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.parking.domain.BNotifyTemplate;
import com.ruoyi.project.parking.domain.notification.CarEntryNotificationData;
import com.ruoyi.project.parking.domain.notification.CarExitNotificationData;
import com.ruoyi.project.parking.domain.notification.CarPayNotificationData;
import com.ruoyi.project.parking.domain.param.BNotifyTemplateParam;
import com.ruoyi.project.parking.service.IBNotifyTemplateService;
import com.ruoyi.project.parking.mapper.BNotifyTemplateMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author 琴声何来
 * @description 针对表【b_notify_template(消息推送模板表)】的数据库操作Service实现
 * @since 2023-03-10 13:59:57
 */
@Slf4j
@Service
public class BNotifyTemplateServiceImpl extends ServiceImpl<BNotifyTemplateMapper, BNotifyTemplate>
        implements IBNotifyTemplateService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * @apiNote 根据场库编号获取消息推送模板列表
     */
    @Override
    public List<BNotifyTemplate> getBNotifyTemplateByParkNo() {
        LambdaQueryWrapper<BNotifyTemplate> qw = new LambdaQueryWrapper<>();
        qw.eq(BNotifyTemplate::getParkNo, SecurityUtils.getParkNo());
        return list(qw);
    }

    /**
     * @apiNote 编辑消息推送模板
     */
    @Override
    public boolean editById(BNotifyTemplateParam notifyTemplateParam) {
        BNotifyTemplate notifyTemplate = new BNotifyTemplate();
        BeanUtils.copyBeanProp(notifyTemplate, notifyTemplateParam);
        notifyTemplate.setUpdateBy(SecurityUtils.getUsername());
        notifyTemplate.setUpdateTime(LocalDateTime.now());
        return updateById(notifyTemplate);
    }

    @Override
    public boolean testCarEntry(CarEntryNotificationData carEntryNotificationData) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitmqConstant.EXCHANGE_PARKING_CAR_ENTRY_NOTIFICATION, RabbitmqConstant.ROUTE_PARKING_CAR_ENTRY_NOTIFICATION, JSON.toJSONString(carEntryNotificationData), correlationData);
        log.info("发送入场消息提醒，data：{}，id：{}", carEntryNotificationData, correlationData.getId());
        return true;
    }

    @Override
    public boolean testCarExit(CarExitNotificationData carExitNotificationData) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message = new Message(JSON.toJSONString(carExitNotificationData).getBytes(), messageProperties);
        rabbitTemplate.convertAndSend(RabbitmqConstant.EXCHANGE_PARKING_CAR_EXIT_NOTIFICATION, RabbitmqConstant.ROUTE_PARKING_CAR_EXIT_NOTIFICATION, message, correlationData);
        log.info("发送出场消息提醒，data：{}，id：{}", carExitNotificationData, correlationData.getId());
        return true;
    }

    @Override
    public boolean testCarPay(CarPayNotificationData carPayNotificationData) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message = new Message(JSON.toJSONString(carPayNotificationData).getBytes(), messageProperties);
        rabbitTemplate.convertAndSend(RabbitmqConstant.EXCHANGE_PARKING_PAYMENT, RabbitmqConstant.ROUTE_PARKING_PAYMENT, message, correlationData);
        log.info("发送支付消息提醒，data：{}，id：{}", carPayNotificationData, correlationData.getId());
        return true;
    }
}




