package com.czdx.parkingorder.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czdx.common.RabbitmqConstant;
import com.czdx.grpc.lib.order.MerchantOrder;
import com.czdx.grpc.lib.order.MonthlyOrder;
import com.czdx.grpc.lib.order.MonthlyOrderServiceGrpc;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.parkingorder.project.entity.MonthlyOrderEntity;
import com.czdx.parkingorder.project.entity.ParkingOrderEntity;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;

/**
 * @Auther: tangwei
 * @Date: 2023/3/13 2:13 PM
 * @Description: 类描述信息
 */
@GrpcService
public class MonthlyOrderService extends MonthlyOrderServiceGrpc.MonthlyOrderServiceImplBase {

    @Autowired
    private com.czdx.parkingorder.project.service.MonthlyOrderService monthlyOrderService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void searchMonthlyOrder(MonthlyOrder.searchMonthlyOrderRequest request, StreamObserver<MonthlyOrder.searchMonthlyOrderResponse> responseObserver) {
        Integer orderUserId = request.getOrderUserId();
        String orderStatus = request.getOrderStatus();
        String payStatus = request.getPayStatus();
        Integer pageNum = request.getPageNum();
        Integer pageSize = request.getPageSize();
        String orderNo = request.getOrderNo();

        IPage<MonthlyOrderEntity> page = monthlyOrderService.searchOrder(orderUserId, orderStatus, payStatus, pageNum, pageSize, orderNo);

        MonthlyOrder.searchMonthlyOrderResponse.Builder response = MonthlyOrder.searchMonthlyOrderResponse.newBuilder();
        response.setStatus("200");
        response.setMess("success");

        if (page.getRecords().size() == 0){

            response.setPageTotal(1);
            response.setPageCurrent(1);

            MonthlyOrder.MonthlyOrderDetail.Builder detail = MonthlyOrder.MonthlyOrderDetail.newBuilder();
            response.addOrderDetail(detail.build());
        }else{
            response.setPageTotal(page.getTotal());
            response.setPageCurrent(page.getCurrent());

            //遍历所有的订单
            page.getRecords().forEach(everyOrder->{
                //构造订单详情
                MonthlyOrder.MonthlyOrderDetail.Builder detail = MonthlyOrder.MonthlyOrderDetail.newBuilder();

                detail.setOrderNo(everyOrder.getOrderNo());
                detail.setParkNo(everyOrder.getParkNo());
                detail.setOrderUserId(everyOrder.getOrderUserId());
                detail.setDiscountAmount(everyOrder.getDiscountAmount().doubleValue());
                detail.setPayAmount(everyOrder.getPayAmount().doubleValue());
                detail.setPayStatus(everyOrder.getPayStatus());
                detail.setRemark(everyOrder.getRemark());
                detail.setPayMethod(everyOrder.getPayMethod() == null ? "" : everyOrder.getPayMethod());
                detail.setPayTime(everyOrder.getPayTime() == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(everyOrder.getPayTime()));
                detail.setOrderStatus(everyOrder.getOrderStatus());
                response.addOrderDetail(detail.build());
            });
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void createMonthlyOrder(MonthlyOrder.createMonthlyOrderRequest request, StreamObserver<MonthlyOrder.monthlyOrderResponse> responseObserver) {
        //获取请求参数
        String parkNo = request.getParkNo();
        Integer orderUserId = request.getOrderUserId();
        Double discountAmount = request.getDiscountAmount();
        Double payAmount = request.getPayAmount();

        //插入月租订单表
        MonthlyOrderEntity monthlyOrderEntity = monthlyOrderService.createOrder(parkNo, orderUserId, discountAmount, payAmount);

        //往rabbitmq中写入延时消息，用于15分钟后订单如果还是未确认状态需要取消订单
        CorrelationData correlationData = new CorrelationData(monthlyOrderEntity.getOrderNo());
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDelay(15 * 60 * 1000); //设置消息延迟时间，单位是毫秒
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);//设置消息持久化存储到磁盘上
        rabbitTemplate.convertAndSend(RabbitmqConstant.ORDER_DELAYED_MESSAGE_EXCHANGE,
                RabbitmqConstant.ORDER_DELAYED_ROUTING_KEY,
                new Message(monthlyOrderEntity.getOrderNo().getBytes(), messageProperties),
                correlationData
        ); //只有消息用Message对象包裹才能实现持久化
        //消息写入完毕

        //写入数据成功，需要响应给调用方
        MonthlyOrder.monthlyOrderResponse.Builder response = MonthlyOrder.monthlyOrderResponse.newBuilder();
        response.setStatus("200");
        response.setMess("success");
        //构建merchantOrderDetail响应体
        MonthlyOrder.MonthlyOrderDetail.Builder detail =  MonthlyOrder.MonthlyOrderDetail.newBuilder();
        detail.setOrderNo(monthlyOrderEntity.getOrderNo());
        detail.setParkNo(monthlyOrderEntity.getParkNo());
        detail.setOrderUserId(monthlyOrderEntity.getOrderUserId());
        detail.setDiscountAmount(monthlyOrderEntity.getDiscountAmount().doubleValue());
        detail.setPayAmount(monthlyOrderEntity.getPayAmount().doubleValue());
        detail.setPayStatus(monthlyOrderEntity.getPayStatus());
        detail.setRemark(monthlyOrderEntity.getRemark());
        response.setOrderDetail(detail.build());

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
