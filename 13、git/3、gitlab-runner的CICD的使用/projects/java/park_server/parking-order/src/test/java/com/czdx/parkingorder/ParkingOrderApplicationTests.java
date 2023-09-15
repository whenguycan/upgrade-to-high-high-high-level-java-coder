package com.czdx.parkingorder;

import com.czdx.grpc.lib.alipay.AliPayServiceGrpc;
import com.czdx.grpc.lib.alipay.AlipayTradeWapPayRequestProto;
import com.czdx.grpc.lib.alipay.AlipayTradeWapPayResponseProto;
import com.czdx.grpc.lib.order.*;
import com.czdx.parkingorder.project.service.MonthlyOrderService;
import com.czdx.parkingorder.project.service.ParkingOrderService;
import com.czdx.parkingorder.project.vo.CreateParkingOrderVo;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.UUID;

@SpringBootTest
class ParkingOrderApplicationTests {

	@Autowired
	ParkingOrderService parkingOrderService;
	@Test
	void contextLoads() {

	}

	@Test
	void getBillDetail(){
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9088).usePlaintext().build();
		ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub blockingStub = ParkingOrderServiceGrpc.newBlockingStub(channel);
		ParkingOrder.BillDetailRequest.Builder request = ParkingOrder.BillDetailRequest.newBuilder();
		request.setBillOutTradeNo("230411092614021366478");

		ParkingOrder.BillDetailResponse response = blockingStub.getBillDetail(request.build());
		System.out.println(response);
	}

	@Test
	void getBillRecordTest(){
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9088).usePlaintext().build();
		ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub blockingStub = ParkingOrderServiceGrpc.newBlockingStub(channel);
		ParkingOrder.BillRecordRequest.Builder request = ParkingOrder.BillRecordRequest.newBuilder();
		request.setUserId(1);
		ParkingOrder.BillRecordResponse response = blockingStub.getBillRecord(request.build());
		System.out.println(response);
	}

	@Test
	void applyBillTest(){
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9088).usePlaintext().build();
		ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub blockingStub = ParkingOrderServiceGrpc.newBlockingStub(channel);
		ParkingOrder.ApplyBillRequest.Builder request = ParkingOrder.ApplyBillRequest.newBuilder();
		request.setUserId(1);
		request.setBillUsername("tangwei");
		request.setBillEmail("2268676560@qq.com");
		request.setBillPhone("18168820608");
		request.setBillTaxNum("sdfsdfxvxbdfg1233445");
		request.addOrderNo("PL1095008291245002752");
		request.addOrderNo("PL1095008276594298880");

		ParkingOrder.ApplyBillResponse response = blockingStub.applyBill(request.build());
		System.out.println(response);
	}

	@Test
	void seachParkingOrder(){
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9088).usePlaintext().build();
		ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub blockingStub = ParkingOrderServiceGrpc.newBlockingStub(channel);

		ParkingOrder.SearchParkingOrderRequest.Builder request = ParkingOrder.SearchParkingOrderRequest.newBuilder();
		request.setPageNum(1);
		request.setPageSize(10);
//		request.setPaymetnod(1);
//		request.setOrderNo("PL");
		request.addCarNums("苏G67898");
		request.addCarNums("苏G67898");

		ParkingOrder.searchOrderResponse response = blockingStub.searchParkingOrderByCarNums(request.build());
		System.out.println(response);
	}

	@Test
	void searchMonthlyOrder(){
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9088).usePlaintext().build();
		MonthlyOrderServiceGrpc.MonthlyOrderServiceBlockingStub blockingStub = MonthlyOrderServiceGrpc.newBlockingStub(channel);
		MonthlyOrder.searchMonthlyOrderRequest.Builder request = MonthlyOrder.searchMonthlyOrderRequest.newBuilder();
//		request.setOrderUserId(124);
//		request.setOrderStatus("05");
//		request.setPayStatus("01");
//		request.setPageNum(1);
//		request.setPageSize(10);
//		request.setOrderNo("380070912");

		MonthlyOrder.searchMonthlyOrderResponse response = blockingStub.searchMonthlyOrder(request.build());
		System.out.println(response);
	}

	@Test
	void createMonthlyOrder(){

		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9088).usePlaintext().build();
		MonthlyOrderServiceGrpc.MonthlyOrderServiceBlockingStub blockingStub = MonthlyOrderServiceGrpc.newBlockingStub(channel);
		MonthlyOrder.createMonthlyOrderRequest.Builder request = MonthlyOrder.createMonthlyOrderRequest.newBuilder();
		request.setParkNo("p20230221113000");
		request.setOrderUserId(1);
		request.setDiscountAmount(50);
		request.setPayAmount(150);

		MonthlyOrder.monthlyOrderResponse response = blockingStub.createMonthlyOrder(request.build());
		System.out.println(response);
	}

	@Test
	void createMerchantOrder(){
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9088).usePlaintext().build();
		MerchantOrderServiceGrpc.MerchantOrderServiceBlockingStub blockingStub =  MerchantOrderServiceGrpc.newBlockingStub(channel);
		MerchantOrder.createMerchantOrderRequest.Builder request = MerchantOrder.createMerchantOrderRequest.newBuilder();

		request.setParkNo("p20230221113000");
		request.setErchantId(1);
		request.setDiscountAmount(50);
		request.setPayAmount(150);

		MerchantOrder.merchantOrderResponse response = blockingStub.createMerchantOrder(request.build());
		System.out.println(response);
	}

	@Test
	void searchOrder(){
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9088).usePlaintext().build();
		ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub blockingStub = ParkingOrderServiceGrpc.newBlockingStub(channel);

		ParkingOrder.searchOrderRequest.Builder request = ParkingOrder.searchOrderRequest.newBuilder();
//		request.setPageNum(1);
//		request.setPageSize(10);
//		request.setPaymetnod(1);
//		request.setOrderNo("PL");
//		request.setCarNumber("苏T12360");

		ParkingOrder.searchOrderResponse response = blockingStub.searchOrder(request.build());
		System.out.println(response);

	}

	@Test
	void changeOrderStatus(){
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9088).usePlaintext().build();
		ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub blockingStub = ParkingOrderServiceGrpc.newBlockingStub(channel);

		ParkingOrder.ChangeOrderRequest.Builder request = ParkingOrder.ChangeOrderRequest.newBuilder();
		request.setOrderNo("PL1080520819169562624");

		ParkingOrder.CreateOrderReponse reponse = blockingStub.changeOrderStatus(request.build());
		System.out.println(reponse);// 获取到响应数据
		//关闭连接
		channel.shutdown();
	}

	@Test
	void confirmPay(){
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9088).usePlaintext().build();
		ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub blockingStub = ParkingOrderServiceGrpc.newBlockingStub(channel);

		ParkingOrder.ConfirmPayRequest.Builder request = ParkingOrder.ConfirmPayRequest.newBuilder();
		request.setOrderNo("PL1090277492268470272");
		request.setPayType(1);

		ParkingOrder.CouponInfo.Builder coupon1 = ParkingOrder.CouponInfo.newBuilder();
		coupon1.setCouponValue(5);
		coupon1.setCouponMold(1);
		coupon1.setCouponType(1);
		coupon1.setCouponCode("xxxxx");
		coupon1.setChoosed(true);
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

		ParkingOrder.ConfirmPayResponse reponse = blockingStub.confirmPay(request.build());
		System.out.println(reponse);// 获取到响应数据
		//关闭连接
		channel.shutdown();
	}

	@Test
	void createOrder(){
		CreateParkingOrderVo createParkingOrderVo = new CreateParkingOrderVo();
		createParkingOrderVo.setCarNumber("苏D8RL03");
		createParkingOrderVo.setParkNo("1");
		createParkingOrderVo.setCarTypeCode("1");
		createParkingOrderVo.setEntryTime("1677467289");
		createParkingOrderVo.setPassageNo("1");
		//传递给service层，将数据入库
		parkingOrderService.createParkingOrder(createParkingOrderVo);
	}

	@Test
	void createOrderClientByGrpc(){
		//创建连接
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9088).usePlaintext().build();
		ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub blockingStub = ParkingOrderServiceGrpc.newBlockingStub(channel);

		//构建请求数据
		ParkingOrder.CreateOrderRequest.Builder request = ParkingOrder.CreateOrderRequest.newBuilder();
		request.setParkNo("p20230221113000");
		request.setPassageNo("TD004");
		request.setCarNumber("苏T1238");
		request.setCarTypeCode("XXL");
		request.setEntryTime("1677110400000");
		request.setExitTime("1679734224000");


		ParkingOrder.OrderItem.Builder orderItem1 = ParkingOrder.OrderItem.newBuilder();
		orderItem1.setParkFieldId(4);
		orderItem1.setEntryTime("1677229664000");
		orderItem1.setExitTime("1679234224000");
		orderItem1.setPayedAmount(20.0);
		request.addItemList(orderItem1);

		ParkingOrder.OrderItem.Builder orderItem2 = ParkingOrder.OrderItem.newBuilder();
		orderItem2.setParkFieldId(1);
		orderItem2.setEntryTime("1677234224000");
		orderItem2.setExitTime("1677934935000");
		orderItem2.setPayedAmount(20.0);
		request.addItemList(orderItem2);


		ParkingOrder.OrderItem.Builder orderItem3 = ParkingOrder.OrderItem.newBuilder();
		orderItem3.setParkFieldId(2);
		orderItem3.setEntryTime("1677634935000");
		orderItem3.setExitTime("1677934935000");
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
	}

	@Test
	void getParkingOrderDetail(){
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9088).usePlaintext().build();
		ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub blockingStub = ParkingOrderServiceGrpc.newBlockingStub(channel);
		ParkingOrder.OrderDetailRequest.Builder request = ParkingOrder.OrderDetailRequest.newBuilder();
		request.setOrderNo("PL1085974574992199680");
		//发起请求，获取响应对象
		ParkingOrder.CreateOrderReponse reponse = blockingStub.getParkingOrderDetail(request.build());
		System.out.println(reponse);// 获取到响应数据
		//关闭连接
		channel.shutdown();
	}

	@Test
	void getParkMultiOrder(){
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9088).usePlaintext().build();
		ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub blockingStub = ParkingOrderServiceGrpc.newBlockingStub(channel);
		ParkingOrder.parkMultiOrderRequest.Builder request = ParkingOrder.parkMultiOrderRequest.newBuilder();
		request.setCarNum("苏T1231");
		request.setEntryTime("2023-02-23 08:00:00");
		request.setParkNo("p20230221113000");

		ParkingOrder.parkMultiOrderResponse response = blockingStub.getOneParkMultiOrder(request.build());
		System.out.println(response);
		//关闭连接
		channel.shutdown();
	}


}
