package com.czdx.parkingorder.controller;

import com.czdx.grpc.lib.order.MerchantOrder;
import com.czdx.grpc.lib.order.MerchantOrderServiceGrpc;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: tangwei
 * @Date: 2023/3/9 5:17 PM
 * @Description: 类描述信息
 */
@RestController
public class IndexController {

    @GetMapping("/index")
    public void index(){

        ManagedChannel channel = ManagedChannelBuilder.forAddress("order",9088).usePlaintext().build();
        ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub blockingStub = ParkingOrderServiceGrpc.newBlockingStub(channel);

        //构建请求数据
        ParkingOrder.CreateOrderRequest.Builder request = ParkingOrder.CreateOrderRequest.newBuilder();
        request.setParkNo("p20230221113000");
        request.setPassageNo("TD004");
        request.setCarNumber("苏T1235");
        request.setCarTypeCode("XXL");
        request.setEntryTime("1677110400000");
        request.setExitTime("1677234224000");


        ParkingOrder.OrderItem.Builder orderItem1 = ParkingOrder.OrderItem.newBuilder();
        orderItem1.setParkFieldId(4);
        orderItem1.setEntryTime("1677229664000");
        orderItem1.setExitTime("1677234224000");
        orderItem1.setPayedAmount(0.0);
        request.addItemList(orderItem1);

        ParkingOrder.OrderItem.Builder orderItem2 = ParkingOrder.OrderItem.newBuilder();
        orderItem2.setParkFieldId(1);
        orderItem2.setEntryTime("1677234224000");
        orderItem2.setExitTime("1677634935000");
        orderItem2.setPayedAmount(0.0);
        request.addItemList(orderItem2);


        ParkingOrder.OrderItem.Builder orderItem3 = ParkingOrder.OrderItem.newBuilder();
        orderItem3.setParkFieldId(2);
        orderItem3.setEntryTime("1677634935000");
        orderItem3.setExitTime("1677634935000");
        orderItem3.setPayedAmount(0.0);
        request.addItemList(orderItem3);

        ParkingOrder.CreateOrderRequest req = request.build();

        //发起请求，获取响应对象
        ParkingOrder.CreateOrderReponse reponse = blockingStub.createParkingOrder(req);
        System.out.println(reponse.getOrderDetail().getPayableAmount());// 获取到响应数据

        //关闭连接
        channel.shutdown();

    }
}
