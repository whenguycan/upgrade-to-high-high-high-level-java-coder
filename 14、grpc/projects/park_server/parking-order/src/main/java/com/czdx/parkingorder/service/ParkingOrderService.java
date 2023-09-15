package com.czdx.parkingorder.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czdx.common.RabbitmqConstant;
import com.czdx.grpc.lib.WechatMessageTemplateSend.WechatMessageTemplateSendServiceGrpc;
import com.czdx.grpc.lib.alipay.AliPayServiceGrpc;
import com.czdx.grpc.lib.alipay.AlipayTradeWapPayRequestProto;
import com.czdx.grpc.lib.alipay.AlipayTradeWapPayResponseProto;
import com.czdx.grpc.lib.charge.*;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import com.czdx.parkingorder.project.entity.*;
import com.czdx.parkingorder.project.service.MerchantOrderService;
import com.czdx.parkingorder.project.service.MonthlyOrderService;
import com.czdx.parkingorder.project.service.ParkingOrderDelayedMessageService;
import com.czdx.parkingorder.project.service.ParkingOrderItemService;
import com.czdx.parkingorder.project.vo.CreateParkingOrderVo;
import com.czdx.parkingorder.project.vo.ParkingOrderDetailVo;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: tangwei
 * @Date: 2023/2/24 9:48 AM
 * @Description: 类描述信息
 */
@GrpcService
@Slf4j
public class ParkingOrderService extends ParkingOrderServiceGrpc.ParkingOrderServiceImplBase {

    @Autowired
    com.czdx.parkingorder.project.service.ParkingOrderService parkingOrderService;

    @Autowired
    ParkingOrderItemService parkingOrderItemService;

    @GrpcClient("parking-charge-grpc-server")
    private ParkingChargeServiceGrpc.ParkingChargeServiceBlockingStub parkingChargeServiceBlockingStub;

    @GrpcClient("parking-pay-grpc-server")
    private AliPayServiceGrpc.AliPayServiceBlockingStub aliPayServiceBlockingStub;

    @GrpcClient("parking-pay-grpc-server")
    private WechatMessageTemplateSendServiceGrpc.WechatMessageTemplateSendServiceFutureStub wechatMessageTemplateSendServiceFutureStub;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ParkingOrderDelayedMessageService parkingOrderDelayedMessageService;

    @Autowired
    MerchantOrderService merchantOrderService;


    @Autowired
    MonthlyOrderService monthlyOrderService;

    @Override
    public void searchOrder(ParkingOrder.searchOrderRequest request, StreamObserver<ParkingOrder.searchOrderResponse> responseObserver) {
        String orderNo = request.getOrderNo();
        Integer payMethod = request.getPaymetnod();
        Integer pageNum = request.getPageNum();
        Integer pageSize = request.getPageSize();
        String carNumber = request.getCarNumber();

        //根据查询条件查询数据
        IPage<ParkingOrderEntity> page = parkingOrderService.searchOrder(orderNo, payMethod, pageNum, pageSize, carNumber);

        //组装返回响应
        ParkingOrder.searchOrderResponse.Builder searchOrderResponse = ParkingOrder.searchOrderResponse.newBuilder();
        searchOrderResponse.setStatus("200");
        searchOrderResponse.setMess("success");


        if (page.getRecords().size() == 0){

            searchOrderResponse.setPageTotal(1);
            searchOrderResponse.setPageCurrent(1);

            ParkingOrder.OrderDetail.Builder detail = ParkingOrder.OrderDetail.newBuilder();
            searchOrderResponse.addOrderDetail(detail.build());
        }else{

            searchOrderResponse.setPageTotal(page.getTotal());
            searchOrderResponse.setPageCurrent(page.getCurrent());

            //遍历所有的订单
            page.getRecords().forEach(everyOrder->{
//            构造订单详情
                ParkingOrder.OrderDetail.Builder detail = ParkingOrder.OrderDetail.newBuilder();
                detail.setOrderNo(everyOrder.getOrderNo());
                detail.setPayableAmount(everyOrder.getPayableAmount().doubleValue());
                detail.setPayAmount(everyOrder.getPayAmount().doubleValue());
                detail.setPayMethod(everyOrder.getPayMethod());
                detail.setRemark(everyOrder.getRemark());
                searchOrderResponse.addOrderDetail(detail.build());
            });
        }


        responseObserver.onNext(searchOrderResponse.build());
        responseObserver.onCompleted();
    }

    @Override
    public void changeOrderStatus(ParkingOrder.ChangeOrderRequest request, StreamObserver<ParkingOrder.CreateOrderReponse> responseObserver) {
        String orderNo = request.getOrderNo();
        //拿到订单信息
        ParkingOrderEntity parkingOrderEntity = parkingOrderService.getParkingOrderByOrderNo(orderNo);
        //构建响应信息
        ParkingOrder.CreateOrderReponse.Builder response = ParkingOrder.CreateOrderReponse.newBuilder();

        ParkingOrder.OrderDetail.Builder orderDetailBuilder = ParkingOrder.OrderDetail.newBuilder();
        if(parkingOrderEntity == null){
            response.setMess("订单信息不存在");
            response.setStatus("500");

            response.setOrderDetail(orderDetailBuilder.build());
        }else{

            if (parkingOrderEntity.getOrderStatus().equals("01") || parkingOrderEntity.getOrderStatus().equals("02")){
                parkingOrderEntity.setOrderStatus("03");
                parkingOrderService.updateById(parkingOrderEntity);

                response.setMess("修改订单状态成功");
                response.setStatus("200");
                response.setOrderDetail(orderDetailBuilder.build());
            }else{
                response.setMess("该订单目前不支持修改订单状态");
                response.setStatus("500");

                response.setOrderDetail(orderDetailBuilder.build());
            }


        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();

    }

    @Override
    public void confirmPay(ParkingOrder.ConfirmPayRequest request, StreamObserver<ParkingOrder.ConfirmPayResponse> responseObserver) {
        String orderNo = request.getOrderNo();
        Integer payType = request.getPayType();


        //构建响应信息
        ParkingOrder.ConfirmPayResponse.Builder confirmPayResponse = ParkingOrder.ConfirmPayResponse.newBuilder();
        Double totalAmount = Double.valueOf(0);


        MerchantOrderEntity merchantOrderEntity = null;
        MonthlyOrderEntity monthlyOrderEntity = null;
        ParkingOrderEntity parkingOrderEntity = null;
        //拿到订单信息
        if (orderNo.startsWith("ME")){
            merchantOrderEntity = merchantOrderService.getBaseMapper().selectOne(new QueryWrapper<MerchantOrderEntity>().eq("order_no", orderNo));
            if(merchantOrderEntity == null){
                confirmPayResponse.setMess("订单信息不存在");
                confirmPayResponse.setStatus("500");
                confirmPayResponse.setPayUrl("");
                responseObserver.onNext(confirmPayResponse.build());
            }
            totalAmount = merchantOrderEntity.getPayableAmount().doubleValue();
        }else if(orderNo.startsWith("MO")){
            monthlyOrderEntity = monthlyOrderService.getBaseMapper().selectOne(new QueryWrapper<MonthlyOrderEntity>().eq("order_no", orderNo));
            if(monthlyOrderEntity == null){
                confirmPayResponse.setMess("订单信息不存在");
                confirmPayResponse.setStatus("500");
                confirmPayResponse.setPayUrl("");
                responseObserver.onNext(confirmPayResponse.build());
            }
            totalAmount = monthlyOrderEntity.getPayableAmount().doubleValue();
        }else{
            parkingOrderEntity = parkingOrderService.getParkingOrderByOrderNo(orderNo);
            if(parkingOrderEntity == null){
                confirmPayResponse.setMess("订单信息不存在");
                confirmPayResponse.setStatus("500");
                confirmPayResponse.setPayUrl("");
                responseObserver.onNext(confirmPayResponse.build());
            }
            totalAmount = parkingOrderEntity.getPayableAmount().doubleValue();
        }

        //构造发起支付的请求参数
        if(payType == 1){
            //支付宝支付

            //构建生成支付宝支付的请求参数
            AlipayTradeWapPayRequestProto.Builder alipayRequest = AlipayTradeWapPayRequestProto.newBuilder();
            alipayRequest.setOutTradeNo(orderNo);
            alipayRequest.setTotalAmount(totalAmount);
            alipayRequest.setSubject("出场订单支付");

            AlipayTradeWapPayResponseProto response = aliPayServiceBlockingStub.alipayTradeWapPay(alipayRequest.build());

            if(response.getCode().equals("10000")){
                //将订单状态改为"已确认"
                if(orderNo.startsWith("ME")){
                    merchantOrderEntity.setOrderStatus("02");
                    merchantOrderService.updateById(merchantOrderEntity);
                } else if (orderNo.startsWith("MO")) {
                    monthlyOrderEntity.setOrderStatus("02");
                    monthlyOrderService.updateById(monthlyOrderEntity);
                }else{
                    parkingOrderEntity.setOrderStatus("02");
                    parkingOrderService.updateById(parkingOrderEntity);
                }

                //调用支付宝成功
                confirmPayResponse.setMess("ok");
                confirmPayResponse.setStatus("200");
                confirmPayResponse.setPayUrl(response.getBody());
                responseObserver.onNext(confirmPayResponse.build());
            }else{
                //调用出现问题
                confirmPayResponse.setMess(response.getMsg());
                confirmPayResponse.setStatus(response.getCode());
                confirmPayResponse.setPayUrl("");
                responseObserver.onNext(confirmPayResponse.build());
            }
        } else if (payType == 2){
            //微信支付
            // TODO 微信支付待开发
            confirmPayResponse.setMess("微信支付尚未开发");
            confirmPayResponse.setStatus("500");
            confirmPayResponse.setPayUrl("");
            responseObserver.onNext(confirmPayResponse.build());
        }else{
            //不存在的支付方式
            confirmPayResponse.setMess("请选择正确的支付方式");
            confirmPayResponse.setStatus("500");
            confirmPayResponse.setPayUrl("");
            responseObserver.onNext(confirmPayResponse.build());
        }


        responseObserver.onCompleted();
    }

    @Override
    public void getParkingOrderDetail(ParkingOrder.OrderDetailRequest request, StreamObserver<ParkingOrder.CreateOrderReponse> responseObserver) {
        //TODO  这儿以后需要优化成传入多个参数，返回多个值
        String orderNum = request.getOrderNo();

        ParkingOrderEntity parkingOrderEntity = parkingOrderService.getParkingOrderByOrderNo(orderNum);
        ParkingOrder.CreateOrderReponse.Builder responseBuild = ParkingOrder.CreateOrderReponse.newBuilder();
        responseBuild.setStatus("200");
        ParkingOrder.OrderDetail.Builder orderDetailBuilder = ParkingOrder.OrderDetail.newBuilder();
        orderDetailBuilder.setOrderNo(parkingOrderEntity.getOrderNo());
        orderDetailBuilder.setParkNo(parkingOrderEntity.getParkNo());
        orderDetailBuilder.setPassageNo(parkingOrderEntity.getPassageNo());
        orderDetailBuilder.setCarNumber(parkingOrderEntity.getCarNumber());
        orderDetailBuilder.setCarTypeCode(parkingOrderEntity.getCarTypeCode());
        orderDetailBuilder.setEntryTime(String.valueOf(parkingOrderEntity.getEntryTime().getTime()));
        orderDetailBuilder.setPayMethod(parkingOrderEntity.getPayMethod());
        orderDetailBuilder.setPayTime(String.valueOf(parkingOrderEntity.getPayTime().getTime()));
        orderDetailBuilder.setPayStatus(parkingOrderEntity.getPayStatus());
        orderDetailBuilder.setPayNumber(parkingOrderEntity.getPayNumber());
        orderDetailBuilder.setExpireTime(String.valueOf(parkingOrderEntity.getExpireTime().getTime()));
        orderDetailBuilder.setRemark(parkingOrderEntity.getRemark());
        orderDetailBuilder.setCreateTime(String.valueOf(parkingOrderEntity.getCreateTime().getTime()));
        orderDetailBuilder.setUpdateTime(String.valueOf(parkingOrderEntity.getUpdateTime().getTime()));
        orderDetailBuilder.setPayableAmount(parkingOrderEntity.getPayableAmount().doubleValue());
        orderDetailBuilder.setDiscountAmount(parkingOrderEntity.getDiscountAmount().doubleValue());
        orderDetailBuilder.setPaidAmount(parkingOrderEntity.getPaidAmount().doubleValue());
        orderDetailBuilder.setPayAmount(parkingOrderEntity.getPayAmount().doubleValue());

        List<ParkingOrderItemEntity> parkingOrderItemList = parkingOrderItemService.getBaseMapper().selectList(new QueryWrapper<ParkingOrderItemEntity>().eq("order_no", orderNum));
        for (ParkingOrderItemEntity everyEntity: parkingOrderItemList){
            ParkingOrder.OrderItem.Builder item = ParkingOrder.OrderItem.newBuilder();
            item.setExitTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(everyEntity.getExitTime()));
            item.setEntryTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(everyEntity.getEntryTime()));
            item.setParkFieldId(everyEntity.getParkFieldId());
            item.setPayedAmount(everyEntity.getPayableAmount().doubleValue());
            orderDetailBuilder.addItemList(item.build());
        }
        responseBuild.setOrderDetail(orderDetailBuilder.build());
        responseObserver.onNext(responseBuild.build());
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void createParkingOrder(ParkingOrder.CreateOrderRequest request, StreamObserver<ParkingOrder.CreateOrderReponse> responseObserver) {
        //开始接收传入的参
        CreateParkingOrderVo createParkingOrderVo = new CreateParkingOrderVo();
        createParkingOrderVo.setCarNumber(request.getCarNumber());
        createParkingOrderVo.setParkNo(request.getParkNo());
        createParkingOrderVo.setCarTypeCode(request.getCarTypeCode());
        createParkingOrderVo.setEntryTime(request.getEntryTime());
        createParkingOrderVo.setPassageNo(request.getPassageNo());
        createParkingOrderVo.setExitTime(request.getExitTime());
        List<CreateParkingOrderVo.OrderItem> list = new LinkedList<>();
        for(ParkingOrder.OrderItem everyItem: request.getItemListList()){
            CreateParkingOrderVo.OrderItem orderItem = new CreateParkingOrderVo.OrderItem();
            orderItem.setParkFieldId(everyItem.getParkFieldId());
            orderItem.setExitTime(everyItem.getExitTime());
            orderItem.setEntryTime(everyItem.getEntryTime());
            list.add(orderItem);
        }

        //获取优惠券列表
        LinkedList<CreateParkingOrderVo.CouponInfo> couponList = new LinkedList();
        for (ParkingOrder.CouponInfo everyCoupon: request.getCouponListList()){
            CreateParkingOrderVo.CouponInfo couponInfoTmp = new CreateParkingOrderVo.CouponInfo();
            couponInfoTmp.setCouponCode(everyCoupon.getCouponCode());
            couponInfoTmp.setCouponValue(everyCoupon.getCouponValue());
            couponInfoTmp.setCouponType(everyCoupon.getCouponType());
            couponInfoTmp.setCouponMold(everyCoupon.getCouponMold());
            couponInfoTmp.setChoosed(everyCoupon.getChoosed());
            couponList.add(couponInfoTmp);
        }
        createParkingOrderVo.setOrderNo(request.getOrderNo());
        //至此接收数据完毕

        //如果是第二次进来，先把原订单设置为已经取消
        if(!createParkingOrderVo.getOrderNo().equals("")){
            ParkingOrderEntity parkingOrder = parkingOrderService.getBaseMapper().selectOne(new QueryWrapper<ParkingOrderEntity>().eq("order_no", createParkingOrderVo.getOrderNo()));
            parkingOrder.setOrderStatus("05");
            parkingOrder.setPayStatus("4");
            parkingOrderService.updateById(parkingOrder);
        }

        //在本次创建订单之前判断是否创建过订单并完成了支付
        Boolean canUseMerchantCoupon = true; //能否再次使用商家优惠券标志位
        List<ParkingOrderEntity> finishedOrder = parkingOrderService.getBaseMapper().selectList(new QueryWrapper<ParkingOrderEntity>()
                .eq("car_number", createParkingOrderVo.getCarNumber())
                .eq("entry_time", new Date(Long.valueOf(createParkingOrderVo.getEntryTime())))
                .eq("pay_status", "3"));
        for (ParkingOrderEntity everyOrder: finishedOrder){ //遍历所有的已支付订单
            for (String everyUsedCoupon: everyOrder.getCoupons().split(",")){
                if (everyUsedCoupon.startsWith("2")){ //本次停车已经使用过商家券了
                    canUseMerchantCoupon = false;
                    break;
                }
            }
        }

        //构造算费系统的请求参数
        ChargeParkingFeeResponse chargeParkingFeeResponse = null;
        try{
            ChargeParkingFeeRequest.Builder chargeParkingFeeRequest = ChargeParkingFeeRequest.newBuilder();
            chargeParkingFeeRequest.setParkNo(createParkingOrderVo.getParkNo());
            chargeParkingFeeRequest.setCarNumber(createParkingOrderVo.getCarNumber());
            chargeParkingFeeRequest.setCarTypeCode(createParkingOrderVo.getCarTypeCode());
            for (CreateParkingOrderVo.OrderItem evertItem: list){
                ParkingItem.Builder item = ParkingItem.newBuilder();
                item.setParkFieldId(evertItem.getParkFieldId());
                item.setEntryTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(evertItem.getEntryTime()))));
                item.setExitTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(evertItem.getExitTime()))));
                chargeParkingFeeRequest.addParkingItem(item.build());
            }
            for(CreateParkingOrderVo.CouponInfo everyCoupon :couponList){
                CouponInfo.Builder coupon = CouponInfo.newBuilder();
                coupon.setCouponCode(everyCoupon.getCouponCode());
                coupon.setCouponMold(everyCoupon.getCouponMold());
                coupon.setCouponValue(everyCoupon.getCouponValue());
                coupon.setCouponType(everyCoupon.getCouponType());
                coupon.setSelected(everyCoupon.getChoosed());
                Boolean canUse = true;
                if(everyCoupon.getCouponType() == 2 && canUseMerchantCoupon == false){
                    canUse = false;
                }
                coupon.setCanUse(canUse);
                chargeParkingFeeRequest.addCouponItem(coupon);
            }
            chargeParkingFeeResponse = parkingChargeServiceBlockingStub.chargeParkingFee(chargeParkingFeeRequest.build());

        }catch (Exception exception){
            log.error("grpc调用算费系统异常，请联系管理员");
        }
        //请求算费完毕

        //将算费信息并入到CreateParkingOrderVo中
        createParkingOrderVo.setPayableAmount(chargeParkingFeeResponse.getPayableAmount());
        createParkingOrderVo.setDiscountAmount(chargeParkingFeeResponse.getDiscountAmount());
        createParkingOrderVo.setPayAmount(chargeParkingFeeResponse.getPayAmount());
        List<CreateParkingOrderVo.OrderItem> newlist = new LinkedList<>();

        for(ParkingFeeItem everyItem: chargeParkingFeeResponse.getFeeItemsList()){
            CreateParkingOrderVo.OrderItem orderItem = new CreateParkingOrderVo.OrderItem();
            orderItem.setParkFieldId(everyItem.getParkFieldId());
            try {
                orderItem.setExitTime(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(everyItem.getExitTime()).getTime()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            try {
                orderItem.setEntryTime(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(everyItem.getEntryTime()).getTime()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            orderItem.setParkingTimet(everyItem.getParkingTime());
            orderItem.setPayableAmount(everyItem.getPayableAmount());
            newlist.add(orderItem);
        }
        createParkingOrderVo.setItemList(newlist);

        BigDecimal discount = new BigDecimal(0);
        List<CreateParkingOrderVo.CouponInfo> usedCoupon = new LinkedList<>();
        for(ChargeCouponInfo everyCoupon: chargeParkingFeeResponse.getCouponItemsList()){
            //只记录被选用的优惠券
            if(everyCoupon.getSelected()){
                CreateParkingOrderVo.CouponInfo tmpCoupon = new CreateParkingOrderVo.CouponInfo();
                tmpCoupon.setChoosed(everyCoupon.getSelected());
                tmpCoupon.setCouponCode(everyCoupon.getCouponCode());
                tmpCoupon.setCouponMold(everyCoupon.getCouponMold());
                tmpCoupon.setCouponType(everyCoupon.getCouponType());
                tmpCoupon.setCouponValue(everyCoupon.getCouponValue());
                usedCoupon.add(tmpCoupon);
                discount.add(BigDecimal.valueOf(everyCoupon.getCouponValue()));

            }
        }
        createParkingOrderVo.setCouponList(usedCoupon);


        //预支付金额为0，不要生成订单
        if(createParkingOrderVo.getPassageNo().equals("") && createParkingOrderVo.getPayableAmount().compareTo(Double.valueOf("0")) == 0){
            ParkingOrder.CreateOrderReponse.Builder responseBuild = ParkingOrder.CreateOrderReponse.newBuilder();
            responseBuild.setStatus("200");
            responseBuild.setMess("无需生成订单");
            ParkingOrder.OrderDetail.Builder orderDetailBuilder = ParkingOrder.OrderDetail.newBuilder();
            responseBuild.setOrderDetail(orderDetailBuilder.build());
            responseObserver.onNext(responseBuild.build());
            responseObserver.onCompleted();
        }else{

            //岗亭支付，需要支付金额为0，则直接生成已支付订单
            ParkingOrderEntity parkingOrderEntity = null;
            if(createParkingOrderVo.getPayableAmount().compareTo(Double.valueOf("0")) == 0){
                createParkingOrderVo.setPayed(true);
                parkingOrderEntity = parkingOrderService.createParkingOrder(createParkingOrderVo);
            }else{

                //生成订单数据写入数据库
                createParkingOrderVo.setPayed(false);
                parkingOrderEntity = parkingOrderService.createParkingOrder(createParkingOrderVo);

                //往rabbitmq中写入延时消息，用于15分钟后订单如果还是未确认状态需要取消订单
                CorrelationData correlationData = new CorrelationData(parkingOrderEntity.getOrderNo());
                MessageProperties messageProperties = new MessageProperties();
                messageProperties.setDelay(15 * 60 * 1000); //设置消息延迟时间，单位是毫秒
                messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);//设置消息持久化存储到磁盘上
                rabbitTemplate.convertAndSend(RabbitmqConstant.ORDER_DELAYED_MESSAGE_EXCHANGE,
                        RabbitmqConstant.ORDER_DELAYED_ROUTING_KEY,
                        new Message(parkingOrderEntity.getOrderNo().getBytes(), messageProperties),
                        correlationData
                ); //只有消息用Message对象包裹才能实现持久化
                //消息写入完毕
            }
            //数据写入成功，需要造响应数据给调用方
            if(parkingOrderEntity != null){
                ParkingOrder.CreateOrderReponse.Builder responseBuild = ParkingOrder.CreateOrderReponse.newBuilder();
                responseBuild.setStatus("200");
                ParkingOrder.OrderDetail.Builder orderDetailBuilder = ParkingOrder.OrderDetail.newBuilder();
                orderDetailBuilder.setOrderNo(parkingOrderEntity.getOrderNo());
                orderDetailBuilder.setParkNo(parkingOrderEntity.getParkNo());
                orderDetailBuilder.setPassageNo(parkingOrderEntity.getPassageNo());
                orderDetailBuilder.setCarNumber(parkingOrderEntity.getCarNumber());
                orderDetailBuilder.setCarTypeCode(parkingOrderEntity.getCarTypeCode());
                orderDetailBuilder.setEntryTime(String.valueOf(parkingOrderEntity.getEntryTime().getTime()));
                orderDetailBuilder.setPayMethod(parkingOrderEntity.getPayMethod());
                orderDetailBuilder.setPayTime(String.valueOf(parkingOrderEntity.getPayTime().getTime()));
                orderDetailBuilder.setPayStatus(parkingOrderEntity.getPayStatus());
                orderDetailBuilder.setPayNumber(parkingOrderEntity.getPayNumber());
                orderDetailBuilder.setExpireTime(String.valueOf(parkingOrderEntity.getExpireTime().getTime()));
                orderDetailBuilder.setRemark(parkingOrderEntity.getRemark());
                orderDetailBuilder.setCreateTime(String.valueOf(parkingOrderEntity.getCreateTime().getTime()));
                orderDetailBuilder.setUpdateTime(String.valueOf(parkingOrderEntity.getUpdateTime().getTime()));
                orderDetailBuilder.setPayableAmount(parkingOrderEntity.getPayableAmount().doubleValue());
                orderDetailBuilder.setDiscountAmount(parkingOrderEntity.getDiscountAmount().doubleValue());
                orderDetailBuilder.setPaidAmount(parkingOrderEntity.getPaidAmount().doubleValue());
                orderDetailBuilder.setPayAmount(parkingOrderEntity.getPayAmount().doubleValue());
                orderDetailBuilder.setCoupons(parkingOrderEntity.getCoupons());

                for(ParkingFeeItem everyItem: chargeParkingFeeResponse.getFeeItemsList()){
                    ParkingOrder.OrderItem.Builder item = ParkingOrder.OrderItem.newBuilder();
                    item.setExitTime(everyItem.getExitTime());
                    item.setEntryTime(everyItem.getEntryTime());
                    item.setParkFieldId(everyItem.getParkFieldId());
                    item.setPayedAmount(everyItem.getPayableAmount());
                    orderDetailBuilder.addItemList(item.build());
                }

                //遍历所有给我的优惠券，加上能不能用的标识和选没选用的标志
                for (ChargeCouponInfo everyCoupon: chargeParkingFeeResponse.getCouponItemsList()){
                    ParkingOrder.CouponInfo.Builder cp = ParkingOrder.CouponInfo.newBuilder();
                    cp.setCouponCode(everyCoupon.getCouponCode());
                    cp.setCouponType(everyCoupon.getCouponType());
                    cp.setCouponMold(everyCoupon.getCouponMold());
                    cp.setCouponValue(everyCoupon.getCouponValue());
                    if(everyCoupon.getCouponMold() == 2){
                        cp.setCanUse(canUseMerchantCoupon);
                    }else{
                        cp.setCanUse(true);
                    }
                    cp.setChoosed(everyCoupon.getSelected());

                    responseBuild.addCouponlist(cp.build());
                }


                responseBuild.setOrderDetail(orderDetailBuilder.build());
                responseObserver.onNext(responseBuild.build());
                responseObserver.onCompleted();
            }else{
                ParkingOrder.CreateOrderReponse.Builder responseBuild = ParkingOrder.CreateOrderReponse.newBuilder();
                responseBuild.setStatus("500");
                responseBuild.setMess("并发系统异常");
                ParkingOrder.OrderDetail.Builder orderDetailBuilder = ParkingOrder.OrderDetail.newBuilder();
                responseBuild.setOrderDetail(orderDetailBuilder.build());
                responseObserver.onNext(responseBuild.build());
            }
        }

    }
}
