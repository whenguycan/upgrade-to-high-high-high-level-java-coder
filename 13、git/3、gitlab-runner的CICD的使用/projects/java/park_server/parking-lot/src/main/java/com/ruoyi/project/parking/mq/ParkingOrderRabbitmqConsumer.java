package com.ruoyi.project.parking.mq;

import com.alibaba.fastjson2.JSONObject;
import com.czdx.common.RabbitmqConstant;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.project.parking.domain.notification.CarPayNotificationData;
import com.ruoyi.project.parking.domain.vo.parkingorder.VehicleParkOrderVO;
import com.ruoyi.project.parking.entity.ParkLiveRecords;
import com.ruoyi.project.parking.mq.model.PaymentData;
import com.ruoyi.project.parking.service.IParkLiveRecordsService;
import com.ruoyi.project.parking.service.ParkingOrderGrpcServiceImpl;
import com.ruoyi.project.parking.service.RabbitmqService;
import com.ruoyi.project.system.service.impl.SysDeptServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.TimerTask;

@Slf4j
@Component
public class ParkingOrderRabbitmqConsumer {

    @Autowired
    private RabbitmqService rabbitmqService;

    @Autowired
    private IParkLiveRecordsService parkLiveRecordsService;

    @Autowired
    private SysDeptServiceImpl sysDeptService;

    @Autowired
    private ParkingOrderGrpcServiceImpl parkingOrderGrpcService;

    /**
     * @apiNote 收到支付成功消息
     * 1 通知闸道开门
     * 2 推送微信公众号消息
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitmqConstant.QUEUE_LOT_PARKING_ORDER_PAYSUCCESS, durable = "true"),
            exchange = @Exchange(value = RabbitmqConstant.EXCHANGE_ORDER_PAYSUCCESS, type = ExchangeTypes.TOPIC),
            key = RabbitmqConstant.ROUTE_LOT_PARKING_ORDER_PAYSUCCESS
    ))
    public void pushNotificationAfterPay(Message message) {
        try {
            String messageInfo = new String(message.getBody());
            log.info("收到支付成功消息:{}", messageInfo);
            JSONObject jsonObject = JSONObject.parseObject(messageInfo);
            String orderNo = jsonObject.getString("orderNo");
            String payTime = jsonObject.getString("payTime");
            String payAmount = jsonObject.getString("payAmount");
            String payableAmount = jsonObject.getString("payableAmount");
            String paidAmount = jsonObject.getString("paidAmount");
            String discountAmount = jsonObject.getString("discountAmount");
            String passageNo = jsonObject.getString("passageNo");
            String parkNo = jsonObject.getString("parkNo");
            String carNumber = jsonObject.getString("carNumber");
            String entryTime = jsonObject.getString("entryTime");

            // 推送 订单消息至 闸道
            PaymentData paymentData = new PaymentData();
            paymentData.setCarNumber(carNumber);
            // 通道信息（闸道订单 有闸道编号，预支付订单 闸道从缓存处理）
            paymentData.setPassageNo(passageNo);
            paymentData.setParkNo(parkNo);
            paymentData.setPayAmount(BigDecimal.valueOf(Double.valueOf(payAmount)));
            paymentData.setOutTradeNo(orderNo);
            paymentData.setEntryTime(entryTime);
            rabbitmqService.pushOrderCompleteToDevice(paymentData);

            // 异步更新 在场记录 关联订单信息
            AsyncManager.me().execute(new TimerTask() {
                @Override
                public void run() {
                    correlateOrderInfo(parkNo, carNumber, entryTime);
                }
            });

            //异步执行支付成功消息通知-10s延时
            AsyncManager.me().execute(new TimerTask() {
                @Override
                public void run() {
                    CarPayNotificationData carPayNotificationData = new CarPayNotificationData();
                    carPayNotificationData.setParkNo(parkNo);
                    carPayNotificationData.setParkName(sysDeptService.selectParkNameByParkNo(parkNo));
                    carPayNotificationData.setCarNumber(carNumber);
                    carPayNotificationData.setEntryTime(DateUtils.toLocalDateTime(entryTime));
                    carPayNotificationData.setPayTime(DateUtils.toLocalDateTime(payTime));
                    carPayNotificationData.setAmount(BigDecimal.valueOf((Double.parseDouble(payableAmount))));
                    // 发送通知 - 支付成功
                    rabbitmqService.pushPaySuccessNotification(carPayNotificationData);
                }
            });
        } catch (Exception e) {
            log.error("消费-订单完成消息队列，异常{}，参数{}", e.getMessage(), new String(message.getBody()), e);
        }

    }


    /**
     * 在场记录 关联对应 订单信息
     *
     * @param parkNo    停车场编号
     * @param carNumber 车牌号
     * @param entryTime 入场时间 标准时间格式
     */
    private void correlateOrderInfo(String parkNo, String carNumber, String entryTime) {
        // 查询在场记录id
        ParkLiveRecords parkLiveRecord = parkLiveRecordsService.queryByParkNoCarNumber(parkNo, carNumber);
        List<VehicleParkOrderVO> list = parkingOrderGrpcService.queryOrderInfoByParkLiveInfo(parkNo, carNumber, entryTime);
        parkLiveRecordsService.insertOrderNoWithLiveId(parkLiveRecord.getId(), list);
    }
}
