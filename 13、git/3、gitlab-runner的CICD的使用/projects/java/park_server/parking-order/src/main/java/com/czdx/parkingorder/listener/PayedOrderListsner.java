package com.czdx.parkingorder.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.czdx.common.RabbitmqConstant;
import com.czdx.parkingorder.project.entity.MerchantOrderEntity;
import com.czdx.parkingorder.project.entity.MonthlyOrderEntity;
import com.czdx.parkingorder.project.entity.ParkingOrderEntity;
import com.czdx.parkingorder.project.service.MerchantOrderService;
import com.czdx.parkingorder.project.service.MonthlyOrderService;
import com.czdx.parkingorder.project.service.ParkingOrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * @Auther: tangwei
 * @Date: 2023/3/3 9:53 AM
 * @Description: 类描述信息
 */
@Component
@RabbitListener(queues = {"payment.notify.queue"})
@Slf4j
public class PayedOrderListsner {

    @Autowired
    ParkingOrderService parkingOrderService;

    @Autowired
    MonthlyOrderService monthlyOrderService;

    @Autowired
    MerchantOrderService merchantOrderService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitHandler
    public void getPayedOrderInfo(Message message, String msg, Channel channel) throws InterruptedException, ParseException {
        log.info("正在读取已支付订单队列......");
        HashMap<String, Object> mess = JSON.parseObject(msg, new TypeReference<HashMap>(){});
        String orderNo = (String) mess.get("outTradeNo");
        log.info("获取到订单号为：" + orderNo);
        if (orderNo.startsWith("ME")){
            MerchantOrderEntity merchantOrderEntity = merchantOrderService.getOne(new QueryWrapper<MerchantOrderEntity>().eq("order_no", orderNo));
            if(merchantOrderEntity == null){
                log.info("订单号："+orderNo+"，数据库中没有该订单，跳过");
            }else{
                merchantOrderEntity.setOrderStatus("03");
                merchantOrderEntity.setPayStatus("3");
                merchantOrderEntity.setPayMethod((String)mess.get("payMethod"));
                merchantOrderEntity.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String)mess.get("payTime")));
                merchantOrderService.updateById(merchantOrderEntity);
                log.info("订单号："+orderNo+"，修改订单状态成功");

                //扔一条消息到消息队列，告知商户微服务，订单已经支付成功，需要将优惠券状态修改
                MessageProperties messageProperties = new MessageProperties();//设置消息持久化存储到磁盘上
                messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

                HashMap<String, String> msgHash = new HashMap<>();
                msgHash.put("orderNo", orderNo);
                String mesContent = JSON.toJSONString(msgHash);

                rabbitTemplate.convertAndSend(RabbitmqConstant.EXCHANGE_ORDER_PAYSUCCESS, RabbitmqConstant.ROUTE_MEMBER_MERCHANT_ORDER_PAYSUCCESS, new Message(mesContent.getBytes(), messageProperties));


            }


        }else if(orderNo.startsWith("MO")){
            MonthlyOrderEntity monthlyOrderEntity = monthlyOrderService.getOne(new QueryWrapper<MonthlyOrderEntity>().eq("order_no", orderNo));
            if(monthlyOrderEntity == null){
                log.info("订单号："+orderNo+"，数据库中没有该订单，跳过");
            }else{
                monthlyOrderEntity.setOrderStatus("03");
                monthlyOrderEntity.setPayStatus("3");
                monthlyOrderEntity.setPayMethod((String)mess.get("payMethod"));
                monthlyOrderEntity.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String)mess.get("payTime")));
                monthlyOrderService.updateById(monthlyOrderEntity);
                log.info("订单号："+orderNo+"，修改订单状态成功");
            }

            //扔一条消息到消息队列，告知月租车微服务，订单已经支付成功，需要将包月时间修改
            MessageProperties messageProperties = new MessageProperties();//设置消息持久化存储到磁盘上
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            rabbitTemplate.convertAndSend(RabbitmqConstant.EXCHANGE_ORDER_PAYSUCCESS, RabbitmqConstant.ROUTE_MEMBER_MONTHLY_ORDER_PAYSUCCESS, new Message(orderNo.getBytes(), messageProperties));


        }else{
            ParkingOrderEntity parkingOrderEntity = parkingOrderService.getOne(new QueryWrapper<ParkingOrderEntity>().eq("order_no", orderNo));
            if (parkingOrderEntity == null){
                log.info("订单号："+orderNo+"，数据库中没有该订单，跳过");
            }else{

                String payMethod = (String) mess.get("payMethod");
                if(payMethod.equals("4")){
                    String freeReason = (String) mess.get("freeReason");
                    parkingOrderEntity.setRemark(parkingOrderEntity.getRemark() + freeReason);
                }

                parkingOrderEntity.setOrderStatus("03");
                parkingOrderEntity.setPayStatus("3");
                parkingOrderEntity.setPayMethod(payMethod);
                parkingOrderEntity.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String)mess.get("payTime")));
                parkingOrderService.updateById(parkingOrderEntity);
                log.info("订单号："+orderNo+"，修改订单状态成功");

                //往消息队列中扔一条消息，告诉客户（lot）支付成功了
                MessageProperties messagePropertiesLot = new MessageProperties();//设置消息持久化存储到磁盘上
                messagePropertiesLot.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

                HashMap<String, String> msgHash = new HashMap<>();
                msgHash.put("orderNo", orderNo);
                msgHash.put("payTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(parkingOrderEntity.getPayTime()));
                msgHash.put("amount", parkingOrderEntity.getPayAmount().toString());
                msgHash.put("passageNo", parkingOrderEntity.getPassageNo());
                msgHash.put("carNumber", parkingOrderEntity.getCarNumber());
                msgHash.put("payableAmount", parkingOrderEntity.getPayableAmount().toString());
                msgHash.put("payAmount", parkingOrderEntity.getPayAmount().toString());
                msgHash.put("paidAmount", parkingOrderEntity.getPaidAmount().toString());
                msgHash.put("discountAmount", parkingOrderEntity.getDiscountAmount().toString());
                msgHash.put("parkNo", parkingOrderEntity.getParkNo());
                msgHash.put("entryTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(parkingOrderEntity.getEntryTime()));
                String mesContent = JSON.toJSONString(msgHash);

                rabbitTemplate.convertAndSend(RabbitmqConstant.EXCHANGE_ORDER_PAYSUCCESS, RabbitmqConstant.ROUTE_LOT_PARKING_ORDER_PAYSUCCESS, new Message(mesContent.getBytes(), messagePropertiesLot));

                //扔一条消息到消息队列，告知商户微服务，订单已经支付成功，需要将优惠券状态修改
                MessageProperties messageProperties = new MessageProperties();//设置消息持久化存储到磁盘上
                messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

                HashMap<String, String> msgHasht = new HashMap<>();
                msgHasht.put("usedCoupons", parkingOrderEntity.getCoupons());
                msgHasht.put("usedTime", (String) mess.get("payTime"));
                String msgHashStr = JSON.toJSONString(msgHasht);

                rabbitTemplate.convertAndSend(RabbitmqConstant.EXCHANGE_ORDER_PAYSUCCESS, RabbitmqConstant.ROUTE_MEMBER_MERCHANT_ORDER_PAYSUCCESS, new Message(msgHashStr.getBytes(), messageProperties));
            }
        }


    }
}
