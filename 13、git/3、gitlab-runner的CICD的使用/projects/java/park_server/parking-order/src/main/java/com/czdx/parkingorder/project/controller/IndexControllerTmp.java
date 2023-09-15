package com.czdx.parkingorder.project.controller;

import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: tangwei
 * @Date: 2023/4/17 10:49 AM
 * @Description: 类描述信息
 */
@RestController
public class IndexControllerTmp {

    @GetMapping("/create-order")
    public String index(){
        //创建连接
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9088).usePlaintext().build();
        ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub blockingStub = ParkingOrderServiceGrpc.newBlockingStub(channel);

        //构建请求数据
        ParkingOrder.CreateOrderRequest.Builder request = ParkingOrder.CreateOrderRequest.newBuilder();
        request.setParkNo("p20230221113000");
        request.setPassageNo("TD004");
        request.setCarNumber("苏T1235");
        request.setCarTypeCode("XXL");
        request.setEntryTime("1677110400000");
        request.setExitTime("1677734224000");


        ParkingOrder.OrderItem.Builder orderItem1 = ParkingOrder.OrderItem.newBuilder();
        orderItem1.setParkFieldId(4);
        orderItem1.setEntryTime("1677229664000");
        orderItem1.setExitTime("1677234224000");
        orderItem1.setPayedAmount(20.0);
        request.addItemList(orderItem1);

        ParkingOrder.OrderItem.Builder orderItem2 = ParkingOrder.OrderItem.newBuilder();
        orderItem2.setParkFieldId(1);
        orderItem2.setEntryTime("1677234224000");
        orderItem2.setExitTime("1677634935000");
        orderItem2.setPayedAmount(20.0);
        request.addItemList(orderItem2);


        ParkingOrder.OrderItem.Builder orderItem3 = ParkingOrder.OrderItem.newBuilder();
        orderItem3.setParkFieldId(2);
        orderItem3.setEntryTime("1677634935000");
        orderItem3.setExitTime("1677634935000");
        orderItem3.setPayedAmount(20.0);
        request.addItemList(orderItem3);

        ParkingOrder.CouponInfo.Builder coupon1 = ParkingOrder.CouponInfo.newBuilder();
        coupon1.setCouponValue(5);
        coupon1.setCouponMold(1);
        coupon1.setCouponType(1);
        coupon1.setCouponCode("xxxxx");
        request.addCouponList(coupon1);

        ParkingOrder.CouponInfo.Builder coupon3 = ParkingOrder.CouponInfo.newBuilder();
        coupon3.setCouponValue(10);
        coupon3.setCouponMold(1);
        coupon3.setCouponType(1);
        coupon3.setCouponCode("zzzz");
        request.addCouponList(coupon3);

        ParkingOrder.CouponInfo.Builder coupon2 = ParkingOrder.CouponInfo.newBuilder();
        coupon2.setCouponValue(10);
        coupon2.setCouponMold(2);
        coupon2.setCouponType(2);
        coupon2.setCouponCode("yyyyy");
        request.addCouponList(coupon2);

        ParkingOrder.CreateOrderRequest req = request.build();

        //发起请求，获取响应对象
        ParkingOrder.CreateOrderReponse reponse = blockingStub.createParkingOrder(req);
        System.out.println(reponse);// 获取到响应数据

        //关闭连接
        channel.shutdown();

        return "success";
    }
}
