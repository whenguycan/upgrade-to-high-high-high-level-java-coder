package com.czdx.parkingnotification.mq;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czdx.common.RabbitmqConstant;
import com.czdx.grpc.lib.merchant.GetMemberByCarRequest;
import com.czdx.grpc.lib.merchant.GetMemberByCarResponse;
import com.czdx.grpc.lib.merchant.MemberCarServiceGrpc;
import com.czdx.parkingnotification.common.utils.StringUtils;
import com.czdx.parkingnotification.config.MyWechatConfig;
import com.czdx.parkingnotification.domain.*;
import com.czdx.parkingnotification.enums.NotificationRecordEnums;
import com.czdx.parkingnotification.service.IBNotifyTemplateService;
import com.czdx.parkingnotification.service.INotificationRecordService;
import com.czdx.parkingnotification.utils.DateLocalDateUtils;
import com.czdx.parkingnotification.utils.WechatAccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

/**
 * description: Rabbitmq消费者
 *
 * @author mingchenxu
 * @date 2023/2/23 14:43
 */
@Slf4j
@Component
public class RabbitmqConsumer {

    @Autowired
    WechatAccessTokenUtil wechatAccessTokenUtil;

    @Autowired
    IBNotifyTemplateService notifyTemplateService;

    @Autowired
    INotificationRecordService notificationRecordService;

    @Autowired
    private RestTemplate restTemplate;

    @GrpcClient("parking-member-merchant-server")
    MemberCarServiceGrpc.MemberCarServiceBlockingStub memberCarServiceBlockingStub;

    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * @apiNote 发送车辆入场消息提醒
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitmqConstant.QUEUE_PARKING_CAR_ENTRY_NOTIFICATION, durable = "true"),
            exchange = @Exchange(value = RabbitmqConstant.EXCHANGE_PARKING_CAR_ENTRY_NOTIFICATION, type = ExchangeTypes.TOPIC),
            key = RabbitmqConstant.ROUTE_PARKING_CAR_ENTRY_NOTIFICATION
    ))
    public void consumerEntryMsg(String message) {
        try {
            CarEntryNotificationData data = JSON.parseObject(message, CarEntryNotificationData.class);
            log.info("接收到发送车辆入场消息提醒，{}", data);
            // 根据场库编号获取对应的入场消息模板
            LambdaQueryWrapper<BNotifyTemplate> qw = new LambdaQueryWrapper<>();
            qw.eq(BNotifyTemplate::getParkNo, data.getParkNo())
                    .eq(BNotifyTemplate::getSendTime, NotificationRecordEnums.SendTime.ENTRY.getValue())
                    .last("limit 1");
            BNotifyTemplate template = notifyTemplateService.getOne(qw);
            //判断是否需要推送
            if (template.getStatus().equals("0")) {
                return;
            }
            //根据车牌号获取通知用户信息
            GetMemberByCarRequest getMemberByCarRequest = GetMemberByCarRequest.newBuilder().setCarNo(data.getCarNumber()).build();
            GetMemberByCarResponse getMemberByCarResponse = memberCarServiceBlockingStub.getMemberByCar(getMemberByCarRequest);
            if (StringUtils.isEmpty(getMemberByCarResponse.getOpenId())) {
                return;
            }
            //构建模板data数据
            log.info("构建模板data数据...");
            WechatNotificationData wechatNotificationData = new WechatNotificationData();
            wechatNotificationData.setFirst(Map.of("value", template.getFirstData()));
            wechatNotificationData.setKeyword1(Map.of("value", data.getParkName()));
            wechatNotificationData.setKeyword2(Map.of("value", data.getCarNumber()));
            wechatNotificationData.setKeyword3(Map.of("value", data.getEntryTime().format(df)));
            wechatNotificationData.setRemark(Map.of("value", template.getRemarkData()));
            log.info("模板data数据构建完成，{}", wechatNotificationData);
            log.info("记录通知日志...");
            //记录通知日志
            NotificationRecord notificationRecord = new NotificationRecord();
            notificationRecord.setParkNo(data.getParkNo());
            notificationRecord.setSendTime(NotificationRecordEnums.SendTime.ENTRY.getValue());
            notificationRecord.setUserPhone(getMemberByCarResponse.getPhonenumber());
            notificationRecord.setNotifyTime(LocalDateTime.now());
            notificationRecord.setComment(buildEntryComment(wechatNotificationData));
            notificationRecord.setStatus(NotificationRecordEnums.Status.WAIT.getValue());
            notificationRecord.setCreateBy(getMemberByCarResponse.getUserName());
            notificationRecord.setCreateTime(LocalDateTime.now());
            notificationRecordService.save(notificationRecord);
            log.info("记录通知日志完成，{}", notificationRecord);
            //构建微信推送模板消息请求
            log.info("构建微信推送模板消息请求...");
            WechatNotificationRequest wechatNotificationRequest = new WechatNotificationRequest();
            wechatNotificationRequest.setTouser(getMemberByCarResponse.getOpenId());
            wechatNotificationRequest.setTemplate_id(MyWechatConfig.ENTRY_TEMPLATE_ID);
            wechatNotificationRequest.setData(wechatNotificationData);
            wechatNotificationRequest.setAppid(MyWechatConfig.APP_ID);
            String messageTemplateSendUrl = MyWechatConfig.MESSAGE_TEMPLATE_SEND_URL.replace("{accessToken}", wechatAccessTokenUtil.getToken());
            log.info("微信推送模板消息请求构建完成，{}", wechatNotificationRequest);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(messageTemplateSendUrl, wechatNotificationRequest, String.class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("收到微信推送模板消息响应：{}", responseEntity.getBody());
                JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
                assert jsonObject != null;
                if (jsonObject.getInteger("errcode") == 0) {
                    notificationRecord.setMessageId(jsonObject.getLong("msgid"));
                    notificationRecordService.updateById(notificationRecord);
                }
            } else {
                log.error("发送微信推送模板消息请求失败，code：{}，body：{}", responseEntity.getStatusCodeValue(), responseEntity.getBody());
            }
        } catch (Exception e) {
            log.error("发送车辆入场消息提醒出现未知异常", e);
        }
    }

    /**
     * @apiNote 发送车辆离场消息提醒
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitmqConstant.QUEUE_PARKING_CAR_EXIT_NOTIFICATION, durable = "true"),
            exchange = @Exchange(value = RabbitmqConstant.EXCHANGE_PARKING_CAR_EXIT_NOTIFICATION, type = ExchangeTypes.TOPIC),
            key = RabbitmqConstant.ROUTE_PARKING_CAR_EXIT_NOTIFICATION
    ))
    public void consumerExitMsg(String message) {
        try {
            CarExitNotificationData data = JSON.parseObject(message, CarExitNotificationData.class);
            log.info("接收到发送车辆离场消息提醒，{}", data);
            // 根据场库编号获取对应的入场消息模板
            LambdaQueryWrapper<BNotifyTemplate> qw = new LambdaQueryWrapper<>();
            qw.eq(BNotifyTemplate::getParkNo, data.getParkNo())
                    .eq(BNotifyTemplate::getSendTime, NotificationRecordEnums.SendTime.EXIT.getValue())
                    .last("limit 1");
            BNotifyTemplate template = notifyTemplateService.getOne(qw);
            //判断是否需要推送
            if (template.getStatus().equals("0")) {
                return;
            }
            //根据车牌号获取通知用户信息
            GetMemberByCarRequest getMemberByCarRequest = GetMemberByCarRequest.newBuilder().setCarNo(data.getCarNumber()).build();
            GetMemberByCarResponse getMemberByCarResponse = memberCarServiceBlockingStub.getMemberByCar(getMemberByCarRequest);
            if (StringUtils.isEmpty(getMemberByCarResponse.getOpenId())) {
                return;
            }
            //构建模板data数据
            log.info("构建模板data数据...");
            WechatNotificationData wechatNotificationData = new WechatNotificationData();
            wechatNotificationData.setFirst(Map.of("value", template.getFirstData()));
            wechatNotificationData.setKeyword1(Map.of("value", data.getParkName()));
            wechatNotificationData.setKeyword2(Map.of("value", data.getCarNumber()));
            wechatNotificationData.setKeyword3(Map.of("value", data.getExitTime().format(df)));
            //根据入场时间和离场时间计算停车时长
            LocalDateTime parkingDuration = DateLocalDateUtils.dateToLocalDateTime(new Date(DateLocalDateUtils.localDateTimeToDate(data.getExitTime()).getTime() - DateLocalDateUtils.localDateTimeToDate(data.getEntryTime()).getTime()));
            wechatNotificationData.setKeyword4(Map.of("value", (parkingDuration.getDayOfMonth() - 1) + "天" +
                    parkingDuration.getHour() + "时" +
                    parkingDuration.getMinute() + "分" +
                    parkingDuration.getSecond() + "秒"));
            wechatNotificationData.setKeyword5(Map.of("value", "￥" + data.getAmount()));
            wechatNotificationData.setRemark(Map.of("value", template.getRemarkData()));
            log.info("模板data数据构建完成，{}", wechatNotificationData);
            log.info("记录通知日志...");
            //记录通知日志
            NotificationRecord notificationRecord = new NotificationRecord();
            notificationRecord.setParkNo(data.getParkNo());
            notificationRecord.setSendTime(NotificationRecordEnums.SendTime.EXIT.getValue());
            notificationRecord.setUserPhone(getMemberByCarResponse.getPhonenumber());
            notificationRecord.setNotifyTime(LocalDateTime.now());
            notificationRecord.setComment(buildExitComment(wechatNotificationData));
            notificationRecord.setStatus(NotificationRecordEnums.Status.WAIT.getValue());
            notificationRecord.setCreateBy(getMemberByCarResponse.getUserName());
            notificationRecord.setCreateTime(LocalDateTime.now());
            notificationRecordService.save(notificationRecord);
            log.info("记录通知日志完成，{}", notificationRecord);
            //构建微信推送模板消息请求
            log.info("构建微信推送模板消息请求...");
            WechatNotificationRequest wechatNotificationRequest = new WechatNotificationRequest();
            wechatNotificationRequest.setTouser(getMemberByCarResponse.getOpenId());
            wechatNotificationRequest.setTemplate_id(MyWechatConfig.EXIT_TEMPLATE_ID);
            wechatNotificationRequest.setData(wechatNotificationData);
            wechatNotificationRequest.setAppid(MyWechatConfig.APP_ID);
            String messageTemplateSendUrl = MyWechatConfig.MESSAGE_TEMPLATE_SEND_URL.replace("{accessToken}", wechatAccessTokenUtil.getToken());
            log.info("微信推送模板消息请求构建完成，{}", wechatNotificationRequest);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(messageTemplateSendUrl, wechatNotificationRequest, String.class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("收到微信推送模板消息响应：{}", responseEntity.getBody());
                JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
                assert jsonObject != null;
                if (jsonObject.getInteger("errcode") == 0) {
                    notificationRecord.setMessageId(jsonObject.getLong("msgid"));
                    notificationRecordService.updateById(notificationRecord);
                }
            } else {
                log.error("发送微信推送模板消息请求失败，code：{}，body：{}", responseEntity.getStatusCodeValue(), responseEntity.getBody());
            }
        } catch (Exception e) {
            log.error("发送车辆离场消息提醒出现未知异常", e);
        }
    }

    /**
     * @apiNote 发送车辆支付消息提醒
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitmqConstant.QUEUE_NOTIFICATION_PARKING_PAYSUCCESS, durable = "true"),
            exchange = @Exchange(value = RabbitmqConstant.EXCHANGE_LOT_PARKING_PAYSUCCESS, type = ExchangeTypes.TOPIC),
            key = RabbitmqConstant.ROUTE_NOTIFICATION_PARKING_PAYSUCCESS
    ))
    public void consumerPayMsg(String message) {
        try {
            CarPayNotificationData data = JSON.parseObject(message, CarPayNotificationData.class);
            log.info("接收到发送支付消息提醒，{}", data);
            // 根据场库编号获取对应的入场消息模板
            LambdaQueryWrapper<BNotifyTemplate> qw = new LambdaQueryWrapper<>();
            qw.eq(BNotifyTemplate::getParkNo, data.getParkNo())
                    .eq(BNotifyTemplate::getSendTime, NotificationRecordEnums.SendTime.PAY.getValue())
                    .last("limit 1");
            BNotifyTemplate template = notifyTemplateService.getOne(qw);
            //判断是否需要推送
            if (template.getStatus().equals("0")) {
                return;
            }
            //根据车牌号获取通知用户信息
            GetMemberByCarRequest getMemberByCarRequest = GetMemberByCarRequest.newBuilder().setCarNo(data.getCarNumber()).build();
            GetMemberByCarResponse getMemberByCarResponse = memberCarServiceBlockingStub.getMemberByCar(getMemberByCarRequest);
            if (StringUtils.isEmpty(getMemberByCarResponse.getOpenId())) {
                return;
            }
            //构建模板data数据
            log.info("构建模板data数据...");
            WechatNotificationData wechatNotificationData = new WechatNotificationData();
            wechatNotificationData.setFirst(Map.of("value", template.getFirstData()));
            wechatNotificationData.setKeyword1(Map.of("value", data.getCarNumber()));
            wechatNotificationData.setKeyword2(Map.of("value", data.getParkName()));
            wechatNotificationData.setKeyword3(Map.of("value", data.getEntryTime().format(df)));
            wechatNotificationData.setKeyword4(Map.of("value", data.getPayTime().format(df)));
            wechatNotificationData.setKeyword5(Map.of("value", "￥" + data.getAmount()));
            wechatNotificationData.setRemark(Map.of("value", template.getRemarkData()));
            log.info("模板data数据构建完成，{}", wechatNotificationData);
            log.info("记录通知日志...");
            //记录通知日志
            NotificationRecord notificationRecord = new NotificationRecord();
            notificationRecord.setParkNo(data.getParkNo());
            notificationRecord.setSendTime(NotificationRecordEnums.SendTime.PAY.getValue());
            notificationRecord.setUserPhone(getMemberByCarResponse.getPhonenumber());
            notificationRecord.setNotifyTime(LocalDateTime.now());
            notificationRecord.setComment(buildPayComment(wechatNotificationData));
            notificationRecord.setStatus(NotificationRecordEnums.Status.WAIT.getValue());
            notificationRecord.setCreateBy(getMemberByCarResponse.getUserName());
            notificationRecord.setCreateTime(LocalDateTime.now());
            notificationRecordService.save(notificationRecord);
            log.info("记录通知日志完成，{}", notificationRecord);
            //构建微信推送模板消息请求
            log.info("构建微信推送模板消息请求...");
            WechatNotificationRequest wechatNotificationRequest = new WechatNotificationRequest();
            wechatNotificationRequest.setTouser(getMemberByCarResponse.getOpenId());
            wechatNotificationRequest.setTemplate_id(MyWechatConfig.PAY_TEMPLATE_ID);
            wechatNotificationRequest.setData(wechatNotificationData);
            wechatNotificationRequest.setAppid(MyWechatConfig.APP_ID);
            String messageTemplateSendUrl = MyWechatConfig.MESSAGE_TEMPLATE_SEND_URL.replace("{accessToken}", wechatAccessTokenUtil.getToken());
            log.info("微信推送模板消息请求构建完成，{}", wechatNotificationRequest);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(messageTemplateSendUrl, wechatNotificationRequest, String.class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("收到微信推送模板消息响应：{}", responseEntity.getBody());
                JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
                assert jsonObject != null;
                if (jsonObject.getInteger("errcode") == 0) {
                    notificationRecord.setMessageId(jsonObject.getLong("msgid"));
                    notificationRecordService.updateById(notificationRecord);
                }
            } else {
                log.error("发送微信推送模板消息请求失败，code：{}，body：{}", responseEntity.getStatusCodeValue(), responseEntity.getBody());
            }
        } catch (Exception e) {
            log.error("发送支付消息提醒出现未知异常", e);
        }
    }

    /**
     * @apiNote 构建入场消息推送模板实际发送的消息格式
     */
    private String buildEntryComment(WechatNotificationData wechatNotificationData) {
        return wechatNotificationData.getFirst().get("value") + "\n" +
                "停车场名称：" + wechatNotificationData.getKeyword1().get("value") + "\n" +
                "车牌号：" + wechatNotificationData.getKeyword2().get("value") + "\n" +
                "进场时间：" + wechatNotificationData.getKeyword3().get("value") + "\n" +
                wechatNotificationData.getRemark().get("value");
    }

    /**
     * @apiNote 构建离场消息推送模板实际发送的消息格式
     */
    private String buildExitComment(WechatNotificationData wechatNotificationData) {
        return wechatNotificationData.getFirst().get("value") + "\n" +
                "停车场名：" + wechatNotificationData.getKeyword1().get("value") + "\n" +
                "车牌号码：" + wechatNotificationData.getKeyword2().get("value") + "\n" +
                "离场时间：" + wechatNotificationData.getKeyword3().get("value") + "\n" +
                "停车时长：" + wechatNotificationData.getKeyword4().get("value") + "\n" +
                "停车费用：" + wechatNotificationData.getKeyword5().get("value") + "\n" +
                wechatNotificationData.getRemark().get("value");
    }

    /**
     * @apiNote 构建支付消息推送模板实际发送的消息格式
     */
    private String buildPayComment(WechatNotificationData wechatNotificationData) {
        return wechatNotificationData.getFirst().get("value") + "\n" +
                "车牌号：" + wechatNotificationData.getKeyword1().get("value") + "\n" +
                "停车场：" + wechatNotificationData.getKeyword2().get("value") + "\n" +
                "进场时间：" + wechatNotificationData.getKeyword3().get("value") + "\n" +
                "支付时间：" + wechatNotificationData.getKeyword4().get("value") + "\n" +
                "支付金额：" + wechatNotificationData.getKeyword5().get("value") + "\n" +
                wechatNotificationData.getRemark().get("value");
    }
}
