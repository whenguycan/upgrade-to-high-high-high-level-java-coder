package com.czdx.parkingorder.service;

import com.czdx.common.RabbitmqConstant;
import com.czdx.grpc.lib.order.MerchantOrder;
import com.czdx.grpc.lib.order.MerchantOrderServiceGrpc;
import com.czdx.parkingorder.project.entity.MerchantOrderEntity;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: tangwei
 * @Date: 2023/3/8 9:08 AM
 * @Description: 类描述信息
 */
@GrpcService
public class MerchantOrderService extends MerchantOrderServiceGrpc.MerchantOrderServiceImplBase {

    @Autowired
    com.czdx.parkingorder.project.service.MerchantOrderService merchantOrderService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void createMerchantOrder(MerchantOrder.createMerchantOrderRequest request, StreamObserver<MerchantOrder.merchantOrderResponse> responseObserver) {

        //TODO 所有的方法参数校验暂时未做
        //接收参数
        String parkNo = request.getParkNo();
        Integer erchantId = request.getErchantId();
        Double discountAmount = request.getDiscountAmount();
        Double payAmount = request.getPayAmount();

        //插入订单表
        MerchantOrderEntity merchantOrderEntity = merchantOrderService.createOrder(parkNo, erchantId, discountAmount, payAmount);

        //往rabbitmq中写入延时消息，用于15分钟后订单如果还是未确认状态需要取消订单
        CorrelationData correlationData = new CorrelationData(merchantOrderEntity.getOrderNo());
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDelay(15 * 60 * 1000); //设置消息延迟时间，单位是毫秒
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);//设置消息持久化存储到磁盘上
        rabbitTemplate.convertAndSend(RabbitmqConstant.ORDER_DELAYED_MESSAGE_EXCHANGE,
                RabbitmqConstant.ORDER_DELAYED_ROUTING_KEY,
                new Message(merchantOrderEntity.getOrderNo().getBytes(), messageProperties),
                correlationData
        ); //只有消息用Message对象包裹才能实现持久化
        //消息写入完毕

        //写入数据成功，需要响应给调用方
        MerchantOrder.merchantOrderResponse.Builder response = MerchantOrder.merchantOrderResponse.newBuilder();
        response.setStatus("200");
        response.setMess("success");
        //构建merchantOrderDetail响应体
        MerchantOrder.MerchantOrderDetail.Builder detail =  MerchantOrder.MerchantOrderDetail.newBuilder();
        detail.setOrderNo(merchantOrderEntity.getOrderNo());
        detail.setParkNo(merchantOrderEntity.getParkNo());
        detail.setErchantId(merchantOrderEntity.getErchantId());
        detail.setDiscountAmount(merchantOrderEntity.getDiscountAmount().doubleValue());
        detail.setPayAmount(merchantOrderEntity.getPayAmount().doubleValue());
        detail.setPayStatus(merchantOrderEntity.getPayStatus());
        detail.setRemark(merchantOrderEntity.getRemark());
        response.setOrderDetail(detail.build());

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
