package com.czdx.parkingorder.service;

import java.util.Date;
import java.time.LocalDateTime;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czdx.common.RabbitmqConstant;
import com.czdx.grpc.lib.WechatMessageTemplateSend.WechatMessageTemplateSendServiceGrpc;
import com.czdx.grpc.lib.alipay.*;
import com.czdx.grpc.lib.charge.*;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import com.czdx.grpc.lib.wechatpay.*;
import com.czdx.parkingorder.common.utils.StringUtils;
import com.czdx.parkingorder.domain.nuonuo.BillingNewRequest;
import com.czdx.parkingorder.domain.nuonuo.InvoiceDetail;
import com.czdx.parkingorder.enums.NNEnums;
import com.czdx.parkingorder.enums.PayEnums;
import com.czdx.parkingorder.project.entity.*;
import com.czdx.parkingorder.project.service.*;
import com.czdx.parkingorder.project.service.MerchantOrderService;
import com.czdx.parkingorder.project.service.MonthlyOrderService;
import com.czdx.parkingorder.project.vo.*;
import com.czdx.parkingorder.utils.NNOpenUtil;
import com.czdx.parkingorder.utils.ProtoJsonUtil;
import com.czdx.parkingorder.utils.SnowflakeIdWorker;
import com.google.protobuf.ProtocolStringList;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.JarOutputStream;

/**
 * @Auther: tangwei
 * @Date: 2023/2/24 9:48 AM
 * @Description: 类描述信息
 */
@GrpcService
@Slf4j
public class ParkingOrderService extends ParkingOrderServiceGrpc.ParkingOrderServiceImplBase {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    com.czdx.parkingorder.project.service.ParkingOrderService parkingOrderService;

    @Autowired
    ParkingOrderItemService parkingOrderItemService;

    @Autowired
    SnowflakeIdWorker snowflakeIdWorker;

    @GrpcClient("parking-charge-grpc-server")
    private ParkingChargeServiceGrpc.ParkingChargeServiceBlockingStub parkingChargeServiceBlockingStub;

    @GrpcClient("parking-pay-grpc-server")
    private AliPayServiceGrpc.AliPayServiceBlockingStub aliPayServiceBlockingStub;

    @GrpcClient("parking-pay-grpc-server")
    private WechatPayServiceGrpc.WechatPayServiceBlockingStub wechatPayServiceBlockingStub;

    @GrpcClient("parking-pay-grpc-server")
    private WechatMessageTemplateSendServiceGrpc.WechatMessageTemplateSendServiceFutureStub wechatMessageTemplateSendServiceFutureStub;


    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ParkingOrderDelayedMessageService parkingOrderDelayedMessageService;

    @Autowired
    MerchantOrderService merchantOrderService;


    @Autowired
    MonthlyOrderService monthlyOrderService;

    @Autowired
    ParkingBillService parkingBillService;

    @Autowired
    RefundOrderService refundOrderService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void getOneParkMultiOrder(ParkingOrder.parkMultiOrderRequest request, StreamObserver<ParkingOrder.parkMultiOrderResponse> responseObserver) {
        String carNum = request.getCarNum();
        String entryTime = request.getEntryTime();
        String parkNo = request.getParkNo();

        List<ParkingOrderEntity> parkingOrderList = null;
        try {
            parkingOrderList = parkingOrderService.getBaseMapper().selectList(
                    new QueryWrapper<ParkingOrderEntity>()
                            .eq("car_number", carNum)
                            .eq("entry_time", sdf.parse(entryTime))
                            .eq("park_no", parkNo)
                            .eq("order_status", "03")
            );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        log.info("获取到的订单为：" + JSON.toJSONString(parkingOrderList));


        ParkingOrder.parkMultiOrderResponse.Builder responseBuild = ParkingOrder.parkMultiOrderResponse.newBuilder();
        responseBuild.setMess("获取数据成功");
        responseBuild.setStatus("200");

        if(!parkingOrderList.isEmpty()){
            parkingOrderList.stream().forEach(everyOrder->{
                ParkingOrder.OrderDetail.Builder orderDetailBuilder = ParkingOrder.OrderDetail.newBuilder();
                orderDetailBuilder.setOrderNo(everyOrder.getOrderNo());
                orderDetailBuilder.setParkNo(everyOrder.getParkNo());
                orderDetailBuilder.setPassageNo(everyOrder.getPassageNo());
                orderDetailBuilder.setCarNumber(everyOrder.getCarNumber());
                orderDetailBuilder.setCarTypeCode(everyOrder.getCarTypeCode());
                orderDetailBuilder.setEntryTime(String.valueOf(everyOrder.getEntryTime().getTime()));
                orderDetailBuilder.setPayMethod(everyOrder.getPayMethod());
                orderDetailBuilder.setPayTime( String.valueOf(everyOrder.getPayTime().getTime()));
                orderDetailBuilder.setPayStatus(everyOrder.getPayStatus());
                orderDetailBuilder.setPayNumber(everyOrder.getPayNumber());
                orderDetailBuilder.setExpireTime(String.valueOf(everyOrder.getExpireTime().getTime()));
                orderDetailBuilder.setRemark(everyOrder.getRemark());
                orderDetailBuilder.setCreateTime(String.valueOf(everyOrder.getCreateTime().getTime()));
                orderDetailBuilder.setUpdateTime(String.valueOf(everyOrder.getUpdateTime().getTime()));
                orderDetailBuilder.setPayableAmount(everyOrder.getPayableAmount().doubleValue());
                orderDetailBuilder.setDiscountAmount(everyOrder.getDiscountAmount().doubleValue());
                orderDetailBuilder.setPaidAmount(everyOrder.getPaidAmount().doubleValue());
                orderDetailBuilder.setPayAmount(everyOrder.getPayAmount().doubleValue());
                orderDetailBuilder.setCoupons(everyOrder.getCoupons());
                responseBuild.addOrderDetail(orderDetailBuilder.build());
            });
        }else{
            ParkingOrder.OrderDetail.Builder orderDetailBuilder = ParkingOrder.OrderDetail.newBuilder();
            responseBuild.addOrderDetail(orderDetailBuilder.build());
        }
        responseObserver.onNext(responseBuild.build());
        responseObserver.onCompleted();
    }

    @Override
    public void changeOrderCoupon(ParkingOrder.changeOrderCouponRequest request, StreamObserver<ParkingOrder.changeOrderCouponResponse> responseObserver) {
        log.info("开始切换优惠券");
        ValueOperations<String, String> opsValue = redisTemplate.opsForValue(); //redis操作句柄

        ParkingOrder.changeOrderCouponResponse.Builder builder = ParkingOrder.changeOrderCouponResponse.newBuilder();
        //获取到订单号，判断订单是否存在
        String orderNo = request.getOrderNo();
        ParkingOrderEntity parkingOrderEntity = parkingOrderService.getBaseMapper().selectOne(new QueryWrapper<ParkingOrderEntity>().eq("order_no", orderNo));
        if (parkingOrderEntity == null) {
            builder.setStatus("500");
            builder.setMess("没有该订单无法切换优惠券");
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
            return;
        }

        if (!parkingOrderEntity.getOrderStatus().equals("01")) {
            builder.setStatus("500");
            builder.setMess("订单状态不满足切换优惠券的条件");
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
            return;
        }

        //获取优惠券列表
        LinkedList<CreateParkingOrderVo.CouponInfo> couponList = new LinkedList();
        for (ParkingOrder.CouponInfo everyCoupon : request.getCouponListList()) {
            CreateParkingOrderVo.CouponInfo couponInfoTmp = new CreateParkingOrderVo.CouponInfo();
            couponInfoTmp.setCouponCode(everyCoupon.getCouponCode());
            couponInfoTmp.setCouponValue(everyCoupon.getCouponValue());
            couponInfoTmp.setCouponType(everyCoupon.getCouponType());
            couponInfoTmp.setCouponMold(everyCoupon.getCouponMold());
            couponInfoTmp.setChoosed(everyCoupon.getChoosed());
            couponList.add(couponInfoTmp);
        }
        log.info("获取到请求切换的优惠券信息" + JSON.toJSONString(couponList));

        //在本次创建订单之前判断是否创建过订单并完成了支付
        Boolean canUseMerchantCoupon = true; //能否再次使用商家优惠券标志位
        ParkingOrderEntity finishedOrder = parkingOrderService.getBaseMapper().selectOne(new QueryWrapper<ParkingOrderEntity>()
                .eq("car_number", parkingOrderEntity.getCarNumber())
                .eq("entry_time", parkingOrderEntity.getEntryTime())
                .eq("pay_status", "3")
                .orderByDesc("id").last("limit 1"));
        if (finishedOrder != null) {
            for (String everyUsedCoupon : finishedOrder.getCoupons().split(",")) {
                if (everyUsedCoupon.startsWith("2")) { //本次停车已经使用过商家券了
                    canUseMerchantCoupon = false;
                    break;
                }
            }
        }
        log.info("能否使用商家优惠券" + canUseMerchantCoupon.toString());

        //构造算费系统的请求参数
        ChargeParkingFeeResponse chargeParkingFeeResponse = null;
        try {
            ChargeParkingFeeRequest.Builder chargeParkingFeeRequest = ChargeParkingFeeRequest.newBuilder();
            // 添加手动切换优惠券标志 xmc
            chargeParkingFeeRequest.setSwitchCouponFlag(true);
            chargeParkingFeeRequest.setParkNo(parkingOrderEntity.getParkNo());
            chargeParkingFeeRequest.setCarNumber(parkingOrderEntity.getCarNumber());
            chargeParkingFeeRequest.setCarTypeCode(parkingOrderEntity.getCarTypeCode());
            chargeParkingFeeRequest.setTodayParkCount(request.getTodayParkCount());

            List<ParkingOrderItemEntity> list = parkingOrderItemService.getBaseMapper().selectList(new QueryWrapper<ParkingOrderItemEntity>().eq("order_no", parkingOrderEntity.getOrderNo()));
            for (ParkingOrderItemEntity evertItem : list) {
                ParkingItem.Builder item = ParkingItem.newBuilder();
                item.setParkFieldId(evertItem.getParkFieldId());
                item.setEntryTime(sdf.format(evertItem.getEntryTime()));
                item.setExitTime(sdf.format(evertItem.getExitTime()));
                chargeParkingFeeRequest.addParkingItem(item.build());
            }

            for (CreateParkingOrderVo.CouponInfo everyCoupon : couponList) {
                CouponInfo.Builder coupon = CouponInfo.newBuilder();
                coupon.setCouponCode(everyCoupon.getCouponCode());
                coupon.setCouponMold(everyCoupon.getCouponMold());
                coupon.setCouponValue(everyCoupon.getCouponValue());
                coupon.setCouponType(everyCoupon.getCouponType());
                coupon.setSelected(everyCoupon.getChoosed());
                Boolean canUse = true;
                if (everyCoupon.getCouponMold() == 2 && canUseMerchantCoupon == false) {
                    canUse = false;
                }
                coupon.setCanUse(canUse);
                chargeParkingFeeRequest.addCouponItem(coupon);
            }
            log.info("构造请求算费系统的数据：" + JSON.toJSONString(chargeParkingFeeRequest.build()));
            chargeParkingFeeResponse = parkingChargeServiceBlockingStub.chargeParkingFee(chargeParkingFeeRequest.build());

        } catch (Exception exception) {
            log.error("grpc调用算费系统异常，请联系管理员");
        }

        log.info("切换优惠券后，算费得到的信息" + JSON.toJSONString(chargeParkingFeeResponse));
        //请求算费完毕

        //重新算费之后，更新订单数据，更新redis中订单的优惠券数据，并构建响应
        ParkingOrder.changeOrderCouponResponse.Builder responseBuild = ParkingOrder.changeOrderCouponResponse.newBuilder();
        parkingOrderEntity.setPayableAmount(BigDecimal.valueOf(chargeParkingFeeResponse.getPayableAmount()));
        parkingOrderEntity.setDiscountAmount(BigDecimal.valueOf(chargeParkingFeeResponse.getDiscountAmount()));
        parkingOrderEntity.setPayAmount(BigDecimal.valueOf((chargeParkingFeeResponse.getPayAmount() - finishedOrder.getPayableAmount().doubleValue()) > 0 ? chargeParkingFeeResponse.getPayAmount() - finishedOrder.getPayableAmount().doubleValue() : Double.valueOf(0)));
//        parkingOrderEntity.setPayAmount(BigDecimal.valueOf(chargeParkingFeeResponse.getPayAmount()));
        String couponStr = "";
        LinkedList<Map<String, Object>> coupleData = new LinkedList<>();
        for (ChargeCouponInfo everyCoupon : chargeParkingFeeResponse.getCouponItemsList()) {
            //只记录被选用的优惠券
            if (everyCoupon.getSelected()) {
                couponStr = couponStr + everyCoupon.getCouponMold() + "_" + everyCoupon.getCouponCode() + ",";
            }
            //同时，将优惠券信息放到redis缓存起来。
            HashMap<String, Object> map = new HashMap<>();
            map.put("couponCode", everyCoupon.getCouponCode());
            map.put("couponType", String.valueOf(everyCoupon.getCouponType()));
            map.put("couponMold", String.valueOf(everyCoupon.getCouponMold()));
            map.put("couponValue", String.valueOf(everyCoupon.getCouponValue()));
            map.put("choosed", everyCoupon.getSelected());
            if (everyCoupon.getCouponMold() == 2) {
                map.put("canUse", canUseMerchantCoupon);
            } else {
                map.put("canUse", true);
            }
            coupleData.add(map);

            ParkingOrder.CouponInfo.Builder cp = ParkingOrder.CouponInfo.newBuilder();
            cp.setCouponCode(everyCoupon.getCouponCode());
            cp.setCouponType(everyCoupon.getCouponType());
            cp.setCouponMold(everyCoupon.getCouponMold());
            cp.setCouponValue(everyCoupon.getCouponValue());
            if (everyCoupon.getCouponMold() == 2) {
                cp.setCanUse(canUseMerchantCoupon);
            } else {
                cp.setCanUse(true);
            }
            cp.setChoosed(everyCoupon.getSelected());
            responseBuild.addCouponlist(cp.build());
        }
        parkingOrderEntity.setCoupons(couponStr);
        //更新订单信息
        log.info("更新订单数据为：" + JSON.toJSONString(parkingOrderEntity));
        parkingOrderService.getBaseMapper().updateById(parkingOrderEntity);

        //修改redis中订单的优惠券信息
        log.info("修改redis中订单" + parkingOrderEntity.getOrderNo() + "优惠券信息为：" + JSON.toJSONString(coupleData));
        if (!coupleData.isEmpty()) {
            log.info("订单优惠券信息非空，需写入redis");
            opsValue.getAndSet(parkingOrderEntity.getOrderNo(), JSON.toJSONString(coupleData));
        }


        //构建请求响应
        responseBuild.setStatus("200");
        responseBuild.setMess("success");
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

//        for (ParkingFeeItem everyItem : chargeParkingFeeResponse.getFeeItemsList()) {
//            ParkingOrder.OrderItem.Builder item = ParkingOrder.OrderItem.newBuilder();
//            item.setExitTime(everyItem.getExitTime());
//            item.setEntryTime(everyItem.getEntryTime());
//            item.setParkFieldId(everyItem.getParkFieldId());
//            item.setPayedAmount(everyItem.getPayableAmount());
//            orderDetailBuilder.addItemList(item.build());
//        }

        responseBuild.setOrderDetail(orderDetailBuilder.build());

        log.info("响应数据为：" + JSON.toJSONString(orderDetailBuilder.build()));
        responseObserver.onNext(responseBuild.build());
        responseObserver.onCompleted();


    }

    /**
     * @apiNote 异常订单统计指标 （停车订单未支付数、停车订单支付成功数、停车订单支付总金额、停车订单优惠总金额、）
     * @author 琴声何来
     * @since 2023/4/25 9:59
     */
    @Override
    public void abnormalStatistics(ParkingOrder.AbnormalStatisticsRequestProto request, StreamObserver<ParkingOrder.AbnormalStatisticsResponseProto> responseObserver) {
        ParkingOrder.AbnormalStatisticsResponseProto.Builder builder = ParkingOrder.AbnormalStatisticsResponseProto.newBuilder();
        String parkNo = request.getParkNo();
        String startTime = request.getStartTime();
        String endTime = request.getEndTime();
        //未支付订单数
        long unpaidNum = parkingOrderService.count(new QueryWrapper<ParkingOrderEntity>()
                .eq(StringUtils.isNotEmpty(parkNo), "park_no", parkNo)
                .ge(StringUtils.isNotEmpty(startTime), "create_time", startTime)
                .le(StringUtils.isNotEmpty(endTime), "create_time", endTime)
                .and(wrapper -> {
                            wrapper.or(wp -> wp.eq("order_status", "01").eq("order_status", "02"));
                        }
                ));
        //支付成功订单数
        long payNum = parkingOrderService.count(new QueryWrapper<ParkingOrderEntity>()
                .eq(StringUtils.isNotEmpty(parkNo), "park_no", parkNo)
                .ge(StringUtils.isNotEmpty(startTime), "create_time", startTime)
                .le(StringUtils.isNotEmpty(endTime), "create_time", endTime)
                .eq("order_status", "03"));
        //支付总金额
        BigDecimal payAmount = parkingOrderService.sumPayAmount(parkNo, startTime, endTime);
        //优惠总金额
        BigDecimal discountAmount = parkingOrderService.sumDiscountAmount(parkNo, startTime, endTime);
        //返回数据
        builder.setStatus("200");
        builder.setUnpaidNum((int) unpaidNum);
        builder.setPayNum((int) payNum);
        builder.setPayAmount(payAmount.doubleValue());
        builder.setDiscountAmount(discountAmount.doubleValue());
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    /**
     * @apiNote 查询退款订单
     * @author 琴声何来
     * @since 2023/4/21 15:01
     */
    @Override
    public void searchRefundOrder(ParkingOrder.SearchRefundOrderRequestProto request, StreamObserver<ParkingOrder.SearchRefundOrderResponseProto> responseObserver) {
        String orderNo = request.getOrderNo();
        String parkNo = request.getParkNo();
        Integer pageNum = request.getPageNum() == 0 ? 1 : request.getPageNum();
        Integer pageSize = request.getPageSize() == 0 ? 10 : request.getPageSize();
        String carNumber = request.getCarNumber();

        //查询退款数据
        IPage<RefundOrderEntity> page = refundOrderService.searchOrder(orderNo, parkNo, pageNum, pageSize, carNumber);
        List<String> orderNos = page.getRecords().stream().map(RefundOrderEntity::getOrderNo).toList();
        //组装返回响应
        ParkingOrder.SearchRefundOrderResponseProto.Builder builder = ParkingOrder.SearchRefundOrderResponseProto.newBuilder();
        builder.setStatus("200");
        builder.setMess("success");


        if (page.getRecords().size() == 0) {

            builder.setPageTotal(0);
            builder.setPageCurrent(1);
        } else {
            //查询停车订单数据
            List<ParkingOrderEntity> parkingOrderEntities = parkingOrderService.searchParkingOrderByOrderNo(orderNos);
            builder.setPageTotal(page.getTotal());
            builder.setPageCurrent(page.getCurrent());

            //遍历所有的订单
            page.getRecords().forEach(everyOrder -> {
                //构造订单详情
                ParkingOrder.RefundOrderDetail.Builder detail = ParkingOrder.RefundOrderDetail.newBuilder();
                //对应的退款订单数据
                ParkingOrderEntity parkingOrderEntity = parkingOrderEntities.stream().filter(item -> item.getOrderNo().equals(everyOrder.getOrderNo())).toList().get(0);
                detail.setOrderNo(everyOrder.getOrderNo());
                detail.setParkNo(everyOrder.getParkNo());
                detail.setPayableAmount(everyOrder.getPayableAmount().doubleValue());
                detail.setPayAmount(everyOrder.getPayAmount().doubleValue());
                detail.setPayMethod(everyOrder.getPayMethod());
                detail.setRemark(everyOrder.getRemark());
                detail.setCarNumber(parkingOrderEntity.getCarNumber());
                detail.setEntryTime(sdf.format(parkingOrderEntity.getEntryTime()));
                detail.setPayTime(sdf.format(everyOrder.getPayTime()));
                detail.setPayStatus(parkingOrderEntity.getPayStatus());
                detail.setRefundStatus(everyOrder.getRefundStatus());
                detail.setRefundTime(sdf.format(everyOrder.getRefundTime()));
                detail.setReason(everyOrder.getReason());
                detail.setRefundAmount(everyOrder.getRefundAmount().doubleValue());
                builder.addOrderDetail(detail.build());
            });
        }


        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    /**
     * @apiNote 订单退款查询回调
     * @author 琴声何来
     * @since 2023/4/20 14:58
     */
    @Override
    public void refundOrderNotify(ParkingOrder.RefundOrderNotifyRequestProto request, StreamObserver<ParkingOrder.RefundOrderNotifyResponseProto> responseObserver) {
        ParkingOrder.RefundOrderNotifyResponseProto.Builder response = ParkingOrder.RefundOrderNotifyResponseProto.newBuilder();
        String orderNo = request.getOrderNo();
        String refundNo = request.getRefundNo();
        if (StringUtils.isEmpty(orderNo)) {
            response.setStatus("500");
            response.setMess("未收到订单号，请重试");
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
            log.error("未收到订单号，请重试");
            return;
        }
        RefundOrderEntity refundOrder = refundOrderService.getByOrderNoAndRefundNo(orderNo, refundNo);
        refundOrder.setRefundStatus(PayEnums.RefundStatus.SUCCESS.getValue());
        refundOrder.setUpdateTime(new Date());
        refundOrderService.updateById(refundOrder);
        //判断订单类型
        if (orderNo.startsWith("MO")) {
            //月租车订单
            List<MonthlyOrderEntity> monthlyOrderEntities = monthlyOrderService.searchMonthlyOrderByOrderNo(List.of(orderNo));
            if (monthlyOrderEntities.isEmpty()) {
                response.setStatus("500");
                response.setMess("订单号不存在");
                responseObserver.onNext(response.build());
                responseObserver.onCompleted();
                log.error("订单号{}不存在或状态不为已支付", orderNo);
                return;
            }
            MonthlyOrderEntity monthlyOrderEntity = monthlyOrderEntities.get(0);
            monthlyOrderEntity.setOrderStatus("06");
            monthlyOrderEntity.setPayStatus("5");
            monthlyOrderService.updateById(monthlyOrderEntity);
        } else if (orderNo.startsWith("ME")) {
            //商户订单
            List<MerchantOrderEntity> merchantOrderEntities = merchantOrderService.searchMerchantOrderByOrderNo(List.of(orderNo));
            if (merchantOrderEntities.isEmpty()) {
                response.setStatus("500");
                response.setMess("订单号不存在");
                responseObserver.onNext(response.build());
                responseObserver.onCompleted();
                log.error("订单号{}不存在或状态不为已支付", orderNo);
                return;
            }
            MerchantOrderEntity merchantOrderEntity = merchantOrderEntities.get(0);
            merchantOrderEntity.setOrderStatus("06");
            merchantOrderEntity.setPayStatus("5");
            merchantOrderService.updateById(merchantOrderEntity);
        } else {
            //停车订单
            List<ParkingOrderEntity> parkingOrderEntities = parkingOrderService.searchParkingOrderByOrderNo(List.of(orderNo));
            if (parkingOrderEntities.isEmpty()) {
                response.setStatus("500");
                response.setMess("订单号不存在");
                responseObserver.onNext(response.build());
                responseObserver.onCompleted();
                log.error("订单号{}不存在或状态不为已支付", orderNo);
                return;
            }
            ParkingOrderEntity parkingOrderEntity = parkingOrderEntities.get(0);
            parkingOrderEntity.setOrderStatus("06");
            parkingOrderEntity.setPayStatus("5");
            parkingOrderService.updateById(parkingOrderEntity);
        }
        response.setStatus("200");
        response.setMess("订单退款状态更新成功");
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    /**
     * @apiNote 订单退款
     */
    @Override
    public void refundOrder(ParkingOrder.RefundOrderRequestProto request, StreamObserver<ParkingOrder.RefundOrderResponseProto> responseObserver) {
        log.info("接收到订单退款请求，{}", request);
        ParkingOrder.RefundOrderResponseProto.Builder response = ParkingOrder.RefundOrderResponseProto.newBuilder();
        String orderNo = request.getOrderNo();
        String reason = request.getReason();
        if (StringUtils.isEmpty(orderNo)) {
            response.setStatus("500");
            response.setMess("未收到订单号，请重试");
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
            log.error("未收到订单号，请重试");
            return;
        }
        //判断订单类型
        MonthlyOrderEntity monthlyOrderEntity = new MonthlyOrderEntity();
        MerchantOrderEntity merchantOrderEntity = new MerchantOrderEntity();
        ParkingOrderEntity parkingOrderEntity = new ParkingOrderEntity();
        String payMethod = "";
        double payAmount = 0.00;
        //保存退款请求
        RefundOrderEntity refundOrderEntity = new RefundOrderEntity();
        if (orderNo.startsWith("MO")) {
            //月租车订单
            List<MonthlyOrderEntity> monthlyOrderEntities = monthlyOrderService.searchMonthlyOrderByOrderNo(List.of(orderNo));
            if (monthlyOrderEntities.isEmpty()) {
                response.setStatus("500");
                response.setMess("订单号不存在");
                responseObserver.onNext(response.build());
                responseObserver.onCompleted();
                log.error("订单号{}不存在或状态不为已支付", orderNo);
                return;
            }
            monthlyOrderEntity = monthlyOrderEntities.get(0);
            payMethod = monthlyOrderEntity.getPayMethod();
            payAmount = monthlyOrderEntity.getPayAmount().doubleValue();
            refundOrderEntity.setOrderNo(monthlyOrderEntity.getOrderNo());
            refundOrderEntity.setOrderType(monthlyOrderEntity.getOrderType());
            refundOrderEntity.setRefundNo("RE" + snowflakeIdWorker.nextId());
            refundOrderEntity.setParkNo(monthlyOrderEntity.getParkNo());
            refundOrderEntity.setPayableAmount(monthlyOrderEntity.getPayableAmount());
            refundOrderEntity.setDiscountAmount(monthlyOrderEntity.getDiscountAmount());
            refundOrderEntity.setPaidAmount(monthlyOrderEntity.getPaidAmount());
            refundOrderEntity.setPayAmount(monthlyOrderEntity.getPayAmount());
            refundOrderEntity.setRefundAmount(monthlyOrderEntity.getPayAmount());
            refundOrderEntity.setPayMethod(monthlyOrderEntity.getPayMethod());
            refundOrderEntity.setPayTime(monthlyOrderEntity.getPayTime());
            refundOrderEntity.setRefundTime(new Date());
            refundOrderEntity.setPayNumber(monthlyOrderEntity.getPayNumber());
            refundOrderEntity.setRefundStatus(PayEnums.RefundStatus.REFUNDING.getValue());
            refundOrderEntity.setReason(reason);
            refundOrderEntity.setRemark("");
            refundOrderEntity.setCreateBy(request.getCreateBy());
            refundOrderEntity.setCreateTime(new Date());
        } else if (orderNo.startsWith("ME")) {
            //商户订单
            List<MerchantOrderEntity> merchantOrderEntities = merchantOrderService.searchMerchantOrderByOrderNo(List.of(orderNo));
            if (merchantOrderEntities.isEmpty()) {
                response.setStatus("500");
                response.setMess("订单号不存在");
                responseObserver.onNext(response.build());
                responseObserver.onCompleted();
                log.error("订单号{}不存在或状态不为已支付", orderNo);
                return;
            }
            merchantOrderEntity = merchantOrderEntities.get(0);
            payMethod = merchantOrderEntity.getPayMethod();
            payAmount = merchantOrderEntity.getPayAmount().doubleValue();
            refundOrderEntity.setOrderNo(merchantOrderEntity.getOrderNo());
            refundOrderEntity.setOrderType(merchantOrderEntity.getOrderType());
            refundOrderEntity.setRefundNo("RE" + snowflakeIdWorker.nextId());
            refundOrderEntity.setParkNo(merchantOrderEntity.getParkNo());
            refundOrderEntity.setPayableAmount(merchantOrderEntity.getPayableAmount());
            refundOrderEntity.setDiscountAmount(merchantOrderEntity.getDiscountAmount());
            refundOrderEntity.setPaidAmount(merchantOrderEntity.getPaidAmount());
            refundOrderEntity.setPayAmount(merchantOrderEntity.getPayAmount());
            refundOrderEntity.setRefundAmount(merchantOrderEntity.getPayAmount());
            refundOrderEntity.setPayMethod(merchantOrderEntity.getPayMethod());
            refundOrderEntity.setPayTime(merchantOrderEntity.getPayTime());
            refundOrderEntity.setRefundTime(new Date());
            refundOrderEntity.setPayNumber(merchantOrderEntity.getPayNumber());
            refundOrderEntity.setRefundStatus(PayEnums.RefundStatus.REFUNDING.getValue());
            refundOrderEntity.setReason(reason);
            refundOrderEntity.setRemark("");
            refundOrderEntity.setCreateBy(request.getCreateBy());
            refundOrderEntity.setCreateTime(new Date());
        } else {
            //停车订单
            List<ParkingOrderEntity> parkingOrderEntities = parkingOrderService.searchParkingOrderByOrderNo(List.of(orderNo));
            if (parkingOrderEntities.isEmpty()) {
                response.setStatus("500");
                response.setMess("订单号不存在");
                responseObserver.onNext(response.build());
                responseObserver.onCompleted();
                log.error("订单号{}不存在或状态不为已支付", orderNo);
                return;
            }
            parkingOrderEntity = parkingOrderEntities.get(0);
            if (StringUtils.isNotEmpty(parkingOrderEntity.getBillOutTardeNo())) {
                response.setStatus("500");
                response.setMess("订单号已开票");
                responseObserver.onNext(response.build());
                responseObserver.onCompleted();
                log.error("订单号{}已开票，退款失败", orderNo);
                return;
            }
            payMethod = parkingOrderEntity.getPayMethod();
            payAmount = parkingOrderEntity.getPayAmount().doubleValue();
            refundOrderEntity.setOrderNo(parkingOrderEntity.getOrderNo());
            refundOrderEntity.setOrderType(parkingOrderEntity.getOrderType());
            refundOrderEntity.setRefundNo("RE" + snowflakeIdWorker.nextId());
            refundOrderEntity.setParkNo(parkingOrderEntity.getParkNo());
            refundOrderEntity.setPayableAmount(parkingOrderEntity.getPayableAmount());
            refundOrderEntity.setDiscountAmount(parkingOrderEntity.getDiscountAmount());
            refundOrderEntity.setPaidAmount(parkingOrderEntity.getPaidAmount());
            refundOrderEntity.setPayAmount(parkingOrderEntity.getPayAmount());
            refundOrderEntity.setRefundAmount(parkingOrderEntity.getPayAmount());
            refundOrderEntity.setPayMethod(parkingOrderEntity.getPayMethod());
            refundOrderEntity.setPayTime(parkingOrderEntity.getPayTime());
            refundOrderEntity.setRefundTime(new Date());
            refundOrderEntity.setPayNumber(parkingOrderEntity.getPayNumber());
            refundOrderEntity.setRefundStatus(PayEnums.RefundStatus.REFUNDING.getValue());
            refundOrderEntity.setReason(reason);
            refundOrderEntity.setRemark("");
            refundOrderEntity.setCreateBy(request.getCreateBy());
            refundOrderEntity.setCreateTime(new Date());
        }
        refundOrderService.save(refundOrderEntity);
        //判断支付方式
        switch (payMethod) {
            case "1" -> {
                //支付宝
                AlipayTradeRefundRequestProto alipayTradeRefundRequestProto = AlipayTradeRefundRequestProto.newBuilder()
                        .setOutTradeNo(orderNo)
                        .setRefundAmount(payAmount)
                        .setOutRequestNo(refundOrderEntity.getRefundNo())
                        .setRefundReason(reason)
                        .build();
                AlipayTradeRefundResponseProto alipayTradeRefundResponseProto = aliPayServiceBlockingStub.alipayTradeRefund(alipayTradeRefundRequestProto);
                if (alipayTradeRefundResponseProto.getCode().equals("10000")) {
                    //更新订单状态为已退款
                    refundOrderEntity.setRefundStatus(PayEnums.RefundStatus.SUCCESS.getValue());
                    refundOrderEntity.setUpdateTime(new Date());
                    refundOrderService.updateById(refundOrderEntity);
                    if (orderNo.startsWith("MO")) {
                        monthlyOrderEntity.setOrderStatus("06");
                        monthlyOrderEntity.setPayStatus("5");
                        monthlyOrderService.updateById(monthlyOrderEntity);
                    } else if (orderNo.startsWith("ME")) {
                        merchantOrderEntity.setOrderStatus("06");
                        merchantOrderEntity.setPayStatus("5");
                        merchantOrderService.updateById(merchantOrderEntity);
                    } else {
                        parkingOrderEntity.setOrderStatus("06");
                        parkingOrderEntity.setPayStatus("5");
                        parkingOrderService.updateById(parkingOrderEntity);
                    }
                } else {
                    //更新退款记录为退款失败
                    refundOrderEntity.setRefundStatus(PayEnums.RefundStatus.FAIL.getValue());
                    refundOrderEntity.setUpdateTime(new Date());
                    refundOrderService.updateById(refundOrderEntity);
                    response.setStatus("500");
                    response.setMess("退款失败，请联系技术人员。错误码：" + alipayTradeRefundResponseProto.getCode() + "，错误信息：" + alipayTradeRefundResponseProto.getSubMsg());
                    responseObserver.onNext(response.build());
                    responseObserver.onCompleted();
                    return;
                }
            }
            case "2" -> {
                //微信
                WechatpayTradeRefundRequestProto wechatpayTradeRefundRequestProto = WechatpayTradeRefundRequestProto.newBuilder()
                        .setOutTradeNo(orderNo)
                        .setOutRefundNo(refundOrderEntity.getRefundNo())
                        .setRefund(payAmount)
                        .setTotal(payAmount)
                        .setReason(reason)
                        .build();
                WechatpayTradeRefundResponseProto wechatpayTradeRefundResponseProto = wechatPayServiceBlockingStub.wechatpayTradeRefund(wechatpayTradeRefundRequestProto);
                if ("SUCCESS".equals(wechatpayTradeRefundResponseProto.getStatus())) {
                    //更新订单状态为已退款
                    refundOrderEntity.setRefundStatus(PayEnums.RefundStatus.SUCCESS.getValue());
                    refundOrderEntity.setUpdateTime(new Date());
                    refundOrderService.updateById(refundOrderEntity);
                    if (orderNo.startsWith("MO")) {
                        monthlyOrderEntity.setOrderStatus("06");
                        monthlyOrderEntity.setPayStatus("5");
                        monthlyOrderService.updateById(monthlyOrderEntity);
                    } else if (orderNo.startsWith("ME")) {
                        merchantOrderEntity.setOrderStatus("06");
                        merchantOrderEntity.setPayStatus("5");
                        merchantOrderService.updateById(merchantOrderEntity);
                    } else {
                        parkingOrderEntity.setOrderStatus("06");
                        parkingOrderEntity.setPayStatus("5");
                        parkingOrderService.updateById(parkingOrderEntity);
                    }
                } else if ("PROCESSING".equals(wechatpayTradeRefundResponseProto.getStatus())) {
                    //退款中，不做修改
                    response.setStatus("200");
                    response.setMess("退款中，请稍候");
                    responseObserver.onNext(response.build());
                    responseObserver.onCompleted();
                    return;
                } else {
                    //更新退款记录为退款失败
                    refundOrderEntity.setRefundStatus(PayEnums.RefundStatus.FAIL.getValue());
                    refundOrderEntity.setUpdateTime(new Date());
                    refundOrderService.updateById(refundOrderEntity);
                    response.setStatus("500");
                    response.setMess("退款失败，请联系技术人员。");
                    responseObserver.onNext(response.build());
                    responseObserver.onCompleted();
                    return;
                }
            }
            default -> {
            }
        }
        response.setStatus("200");
        response.setMess("退款成功");
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();

    }

    @Override
    public void cancleOrder(ParkingOrder.cancleOrderRequest request, StreamObserver<ParkingOrder.cancleOrderResponse> responseObserver) {
        String orderNo = request.getOrderNo();

        ParkingOrder.cancleOrderResponse.Builder response = ParkingOrder.cancleOrderResponse.newBuilder();

        ParkingOrderEntity parkingOrderEntity = parkingOrderService.getBaseMapper().selectOne(new QueryWrapper<ParkingOrderEntity>().eq("order_no", orderNo));
        if (parkingOrderEntity != null) {
            parkingOrderEntity.setOrderStatus("05");
            parkingOrderEntity.setPayStatus("1");
            parkingOrderService.getBaseMapper().updateById(parkingOrderEntity);
            response.setStatus("200");
            response.setMess("订单取消成功！");
        } else {
            response.setStatus("500");
            response.setMess("要取消的订单不存在");

        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();

    }

    @Override
    public void getBillDetail(ParkingOrder.BillDetailRequest request, StreamObserver<ParkingOrder.BillDetailResponse> responseObserver) {
        String billOutTradeNo = request.getBillOutTradeNo();

        ParkingBillEntity parkingBillEntity = parkingBillService.getOneBillRecordByOutTradeNo(billOutTradeNo);

        ParkingOrder.BillDetailResponse.Builder response = ParkingOrder.BillDetailResponse.newBuilder();

        if (parkingBillEntity == null) {
            response.setMess("没有该开票记录");
            response.setStatus("500");

        } else {
            response.setStatus("200");
            response.setMess("获取开票详情成功");
            response.setBillNo(parkingBillEntity.getBillNo());
            response.setBillEmail(parkingBillEntity.getBuyerEmail());
            response.setBillCreateTime(sdf.format(parkingBillEntity.getBillCreateTime()));
            response.setBillUsername(parkingBillEntity.getBuyerName());
            response.setBillTaxNum(parkingBillEntity.getBuyerTaxNum());
            response.setBillPhone(parkingBillEntity.getBuyerPhone());
            response.setBillAddress(parkingBillEntity.getBuyerAddress());
            response.setBillDepositBank(parkingBillEntity.getBuyerDepositBank());
            response.setBillDepositAccount(parkingBillEntity.getBuyerDepositAccount());
            response.setOrderNo(parkingBillEntity.getBillOrderNos());
            response.setBillStatus(parkingBillEntity.getBillStatus());
            if (StringUtils.isNotEmpty(parkingBillEntity.getBillPdfUrl())) {
                response.setBillPdfUrl(parkingBillEntity.getBillPdfUrl());
            }
            response.setType(parkingBillEntity.getType());
            response.setBillAmount(parkingBillEntity.getBillAmount().doubleValue());
            response.setUserId(parkingBillEntity.getUserId());
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getBillRecord(ParkingOrder.BillRecordRequest request, StreamObserver<ParkingOrder.BillRecordResponse> responseObserver) {
        Integer userId = request.getUserId();
        Integer pageNum = request.getPageNum() == 0 ? 1 : request.getPageNum();
        Integer pageSize = request.getPageSize() == 0 ? 10 : request.getPageSize();

        IPage<ParkingBillEntity> page = parkingBillService.searchByUserId(userId, pageNum, pageSize);

        //组装返回响应
        ParkingOrder.BillRecordResponse.Builder searchOrderResponse = ParkingOrder.BillRecordResponse.newBuilder();
        searchOrderResponse.setStatus("200");
        searchOrderResponse.setMess("success");


        if (page.getRecords().size() == 0) {

            searchOrderResponse.setPageTotal(0);
            searchOrderResponse.setPageCurrent(1);

        } else {

            searchOrderResponse.setPageTotal(page.getTotal());
            searchOrderResponse.setPageCurrent(page.getCurrent());

            //遍历所有的订单
            page.getRecords().forEach(everyOrder -> {
//            构造订单详情
                ParkingOrder.BillDetail.Builder detail = ParkingOrder.BillDetail.newBuilder();
                detail.setBillOrderNos(everyOrder.getBillOrderNos());
                detail.setBillOutTradeNo(everyOrder.getBillOutTradeNo());
                detail.setBillcreateTime(new SimpleDateFormat("yyyy-MM-dd").format(everyOrder.getBillCreateTime()));
                detail.setType(everyOrder.getType());
                detail.setBillAmount(everyOrder.getBillAmount().doubleValue());
                detail.setBillStatus(everyOrder.getBillStatus());
                if (StringUtils.isNotEmpty(everyOrder.getBillPdfUrl())) {
                    detail.setBillPdfUrl(everyOrder.getBillPdfUrl());
                }
                searchOrderResponse.addBillDetail(detail.build());
            });
        }
        responseObserver.onNext(searchOrderResponse.build());
        responseObserver.onCompleted();
    }

    @Override
    public void applyBill(ParkingOrder.ApplyBillRequest request, StreamObserver<ParkingOrder.ApplyBillResponse> responseObserver) {
        ParkingOrder.ApplyBillResponse.Builder response = ParkingOrder.ApplyBillResponse.newBuilder();
        Integer userId = request.getUserId();
        String type = request.getType();
        String billUsername = request.getBillUsername();//获取开票人姓名
        String billTaxNum = request.getBillTaxNum();//开票人税号
        String billAddress = request.getBillAddress();
        String billPhone = request.getBillPhone();//开票人手机号
        String billEmail = request.getBillEmail();//开票人邮箱
        String billDepositBank = request.getBillDepositBank();//开票人邮箱
        String billDepositAccount = request.getBillDepositAccount();//开票人邮箱
        List<String> orderNoList = request.getOrderNoList();//需要开票的订单号
        if (orderNoList.isEmpty()) {
            response.setMess("未收到订单号，请重试");
            response.setStatus("500");
            response.setOutTradeNo("");
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
            return;
        }

        //遍历所有的订单号，形成字符串
        String orderNos = "";
        for (String everyOrderNo : orderNoList) {
            orderNos += everyOrderNo + ",";
        }

        //能否开票标志位
        AtomicBoolean canBill = new AtomicBoolean(true);
        //开票总金额
        AtomicReference<BigDecimal> amount = new AtomicReference<>(new BigDecimal(0));
        //根据订单号，获取所有需要开票的订单
        if (orderNoList.get(0).startsWith("MO")) {
            //月租车订单金额
            List<MonthlyOrderEntity> monthlyOrderEntities = monthlyOrderService.searchMonthlyOrderByOrderNo(orderNoList);
            monthlyOrderEntities.forEach(everyOrder -> {
                if (!everyOrder.getBillOutTradeNo().equals("")) {
                    canBill.set(false);
                }
                amount.compareAndSet(amount.get(), amount.get().add(everyOrder.getPayAmount()));
            });
        } else {
            List<ParkingOrderEntity> parkingOrderEntities = parkingOrderService.searchParkingOrderByOrderNo(orderNoList);
            parkingOrderEntities.forEach(everyOrder -> {
                if (!everyOrder.getBillOutTardeNo().equals("")) {
                    canBill.set(false);
                }
                amount.compareAndSet(amount.get(), amount.get().add(everyOrder.getPayAmount()));
            });
        }

        if (!canBill.get()) {
            response.setMess("有订单已经开过发票了，请勿重复开票");
            response.setStatus("500");
            response.setOutTradeNo("");
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
            return;
        }

        if (amount.get().doubleValue() <= 0) {
            response.setMess("金额不允许小于0");
            response.setStatus("500");
            response.setOutTradeNo("");
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
            return;
        }

        //雪花获取到开票申请号
        String billOrderNum = String.valueOf(snowflakeIdWorker.nextId());

        //生成开票申请时间
        Date now = new Date();

        //构建申请开票
        BillingNewRequest billingNewRequest = new BillingNewRequest();
        billingNewRequest.setBuyerName(billUsername);
        billingNewRequest.setBuyerTaxNum(billTaxNum);
        billingNewRequest.setBuyerAddress(billAddress);
        billingNewRequest.setBuyerTel(billPhone);
        billingNewRequest.setBuyerPhone(billPhone);
        billingNewRequest.setBuyerAccount(billDepositBank + billDepositAccount);
        billingNewRequest.setEmail(billEmail);
        billingNewRequest.setOrderNo(billOrderNum);
        billingNewRequest.setInvoiceDate(now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        List<InvoiceDetail> details = new LinkedList<>();
        InvoiceDetail invoiceDetail = new InvoiceDetail();
        invoiceDetail.setGoodsName("订单开票");
        invoiceDetail.setTaxIncludedAmount(amount.toString());
        details.add(invoiceDetail);
        billingNewRequest.setInvoiceDetail(details);

        String out_trade_no = NNOpenUtil.requestBillingNew(billingNewRequest);

        //获取到所有信息，开票数据入库
        ParkingBillEntity parkingBillEntity = new ParkingBillEntity();
        parkingBillEntity.setUserId(userId);
        parkingBillEntity.setType(type);
        parkingBillEntity.setBuyerTaxNum(billTaxNum);
        parkingBillEntity.setBuyerName(billUsername);
        parkingBillEntity.setBuyerEmail(billEmail);
        parkingBillEntity.setBuyerPhone(billPhone);
        parkingBillEntity.setBuyerAddress(billAddress);
        parkingBillEntity.setBuyerDepositBank(billDepositBank);
        parkingBillEntity.setBuyerDepositAccount(billDepositAccount);
        parkingBillEntity.setBillNo(billOrderNum);
        parkingBillEntity.setBillAmount(amount.get());
        parkingBillEntity.setBillCreateTime(now);
        parkingBillEntity.setBillType(1);
        parkingBillEntity.setBillOrderNos(orderNos);
        parkingBillEntity.setBillOutTradeNo(out_trade_no);
        //初始设为开票中状态
        parkingBillEntity.setBillStatus(NNEnums.BillStatus.INVOICING.getValue());
        parkingBillService.save(parkingBillEntity);

        //将一经开票成功的订单，标记为已开票
        if (orderNoList.get(0).startsWith("MO")) {
            monthlyOrderService.updateToBilled(orderNoList, out_trade_no);
        } else {
            parkingOrderService.updateToBilled(orderNoList, out_trade_no);
        }

        //响应给调用方
        response.setMess("申请开票成功");
        response.setStatus("200");
        response.setOutTradeNo(out_trade_no);

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    /* 开票前，需要根据车牌号获取停车订单
     *
     * @param * @param request:
     * @param responseObserver:
     * @Auther:
     * @Date: 2023/4/10 4:04 PM
     * @return: * @return: void
     */
    @Override
    public void searchParkingOrderByCarNums(ParkingOrder.SearchParkingOrderRequest request, StreamObserver<ParkingOrder.searchOrderResponse> responseObserver) {
        List<String> carNums = request.getCarNumsList();
        if (carNums.isEmpty()) {
            ParkingOrder.searchOrderResponse.Builder searchOrderResponse = ParkingOrder.searchOrderResponse.newBuilder();
            searchOrderResponse.setStatus("500");
            searchOrderResponse.setMess("查询订单，车牌号不能为空");
            responseObserver.onNext(searchOrderResponse.build());
            responseObserver.onCompleted();
            return;
        }
        Integer pageNum = request.getPageNum() == 0 ? 1 : request.getPageNum();
        Integer pageSize = request.getPageSize() == 0 ? 10 : request.getPageSize();
        boolean billable = request.getBillable();

        IPage<ParkingOrderEntity> page = parkingOrderService.searchParkingOrder(carNums, pageNum, pageSize,billable);

        //组装返回响应
        ParkingOrder.searchOrderResponse.Builder searchOrderResponse = ParkingOrder.searchOrderResponse.newBuilder();
        searchOrderResponse.setStatus("200");
        searchOrderResponse.setMess("success");


        if (page.getRecords().size() == 0) {

            searchOrderResponse.setPageTotal(0);
            searchOrderResponse.setPageCurrent(1);

        } else {

            searchOrderResponse.setPageTotal(page.getTotal());
            searchOrderResponse.setPageCurrent(page.getCurrent());

            //遍历所有的订单
            page.getRecords().forEach(everyOrder -> {
//            构造订单详情
                ParkingOrder.OrderDetail.Builder detail = ParkingOrder.OrderDetail.newBuilder();
                detail.setOrderNo(everyOrder.getOrderNo());
                detail.setParkNo(everyOrder.getParkNo());
                detail.setCarNumber(everyOrder.getCarNumber());
                detail.setEntryTime(sdf.format(everyOrder.getEntryTime()));
                detail.setPayableAmount(everyOrder.getPayableAmount().doubleValue());
                detail.setPayAmount(everyOrder.getPayAmount().doubleValue());
                detail.setPayMethod(everyOrder.getPayMethod());
                detail.setRemark(everyOrder.getRemark());
                detail.setBillOutTradeNo(everyOrder.getBillOutTardeNo());
                searchOrderResponse.addOrderDetail(detail.build());
            });
        }


        responseObserver.onNext(searchOrderResponse.build());
        responseObserver.onCompleted();
    }

    @Override
    public void searchOrder(ParkingOrder.searchOrderRequest request, StreamObserver<ParkingOrder.searchOrderResponse> responseObserver) {
        String orderNo = request.getOrderNo();
        String parkNo = request.getParkNo();
        Integer payMethod = request.getPaymetnod();
        String startDate = request.getStartDate();
        String endDate = request.getEndDate();
        String orderStatus = request.getOrderStatus();
        String orderType = request.getOrderType();
        Integer pageNum = request.getPageNum() == 0 ? 1 : request.getPageNum();
        Integer pageSize = request.getPageSize() == 0 ? 10 : request.getPageSize();
        String carNumber = request.getCarNumber();

        //根据查询条件查询数据
        IPage<ParkingOrderEntity> page = parkingOrderService.searchOrder(orderNo, parkNo, payMethod, pageNum, pageSize, carNumber, startDate, endDate, orderStatus, orderType);

        //组装返回响应
        ParkingOrder.searchOrderResponse.Builder searchOrderResponse = ParkingOrder.searchOrderResponse.newBuilder();
        searchOrderResponse.setStatus("200");
        searchOrderResponse.setMess("success");


        if (page.getRecords().size() == 0) {

            searchOrderResponse.setPageTotal(0);
            searchOrderResponse.setPageCurrent(1);

        } else {

            searchOrderResponse.setPageTotal(page.getTotal());
            searchOrderResponse.setPageCurrent(page.getCurrent());

            //遍历所有的订单
            page.getRecords().forEach(everyOrder -> {
//            构造订单详情
                ParkingOrder.OrderDetail.Builder detail = ParkingOrder.OrderDetail.newBuilder();
                detail.setOrderNo(everyOrder.getOrderNo());
                detail.setEntryTime(sdf.format(everyOrder.getEntryTime()));
                detail.setPayableAmount(everyOrder.getPayableAmount().doubleValue());
                detail.setPayAmount(everyOrder.getPayAmount().doubleValue());
                detail.setPayMethod(everyOrder.getPayMethod());
                detail.setRemark(everyOrder.getRemark());
                detail.setCarNumber(everyOrder.getCarNumber());
                detail.setPayTime(sdf.format(everyOrder.getPayTime()));
                detail.setPayStatus(everyOrder.getPayStatus());
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
        if (parkingOrderEntity == null) {
            response.setMess("订单信息不存在");
            response.setStatus("500");

            response.setOrderDetail(orderDetailBuilder.build());
        } else {

            if (parkingOrderEntity.getOrderStatus().equals("01") || parkingOrderEntity.getOrderStatus().equals("02")) {
                parkingOrderEntity.setOrderStatus("03");
                parkingOrderService.updateById(parkingOrderEntity);

                response.setMess("修改订单状态成功");
                response.setStatus("200");
                response.setOrderDetail(orderDetailBuilder.build());
            } else {
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
        String openId = request.getOpenid();
        Integer weChatPayMethod = request.getWeChatPayMethod();
        String PayerClientIP = request.getPayerClientIp();
        String H5Type = request.getH5Type();
        String discountReason = request.getDiscountReason();

        //构建响应信息
        ParkingOrder.ConfirmPayResponse.Builder confirmPayResponse = ParkingOrder.ConfirmPayResponse.newBuilder();
        Double totalAmount = Double.valueOf(0);


        MerchantOrderEntity merchantOrderEntity = null;
        MonthlyOrderEntity monthlyOrderEntity = null;
        ParkingOrderEntity parkingOrderEntity = null;
        Boolean needStartPay = true;
        //拿到订单信息
        if (orderNo.startsWith("ME")) {
            merchantOrderEntity = merchantOrderService.getBaseMapper().selectOne(new QueryWrapper<MerchantOrderEntity>().eq("order_no", orderNo));
            if (merchantOrderEntity == null) {
                confirmPayResponse.setMess("订单信息不存在");
                confirmPayResponse.setStatus("500");
                confirmPayResponse.setPayUrl("");
                responseObserver.onNext(confirmPayResponse.build());
            }
            totalAmount = merchantOrderEntity.getPayAmount().doubleValue();

            //如果需要支付的金额小于等于0，则直接修改订单状态
            if (totalAmount <= 0) {
                needStartPay = false;
                merchantOrderEntity.setPayStatus("3");
                merchantOrderEntity.setOrderStatus("03");
                merchantOrderEntity.setPayMethod(payType.toString());
                merchantOrderEntity.setPayTime(new Date());

                merchantOrderService.updateById(merchantOrderEntity);

                //扔一条消息到消息队列，告知商户微服务，订单已经支付成功，需要将优惠券状态修改
                MessageProperties messageProperties = new MessageProperties();//设置消息持久化存储到磁盘上
                messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

                HashMap<String, String> msgHash = new HashMap<>();
                msgHash.put("orderNo", orderNo);
                String mesContent = JSON.toJSONString(msgHash);

                rabbitTemplate.convertAndSend(RabbitmqConstant.EXCHANGE_ORDER_PAYSUCCESS, RabbitmqConstant.ROUTE_MEMBER_MERCHANT_ORDER_PAYSUCCESS, new Message(mesContent.getBytes(), messageProperties));


                confirmPayResponse.setMess("订单需支付金额小于0元，无需支付");
                confirmPayResponse.setStatus("200");
                confirmPayResponse.setPayUrl("");
                responseObserver.onNext(confirmPayResponse.build());
            }
        } else if (orderNo.startsWith("MO")) {
            monthlyOrderEntity = monthlyOrderService.getBaseMapper().selectOne(new QueryWrapper<MonthlyOrderEntity>().eq("order_no", orderNo));
            if (monthlyOrderEntity == null) {
                confirmPayResponse.setMess("订单信息不存在");
                confirmPayResponse.setStatus("500");
                confirmPayResponse.setPayUrl("");
                responseObserver.onNext(confirmPayResponse.build());
            }
            totalAmount = monthlyOrderEntity.getPayAmount().doubleValue();
            //如果需要支付的金额小于等于0，则直接修改订单状态
            if (totalAmount <= 0) {
                needStartPay = false;
                monthlyOrderEntity.setPayStatus("3");
                monthlyOrderEntity.setOrderStatus("03");
                monthlyOrderEntity.setPayMethod(payType.toString());
                monthlyOrderEntity.setPayTime(new Date());
                monthlyOrderService.updateById(monthlyOrderEntity);

                //扔一条消息到消息队列，告知月租车微服务，订单已经支付成功，需要将包月时间修改
                MessageProperties messageProperties = new MessageProperties();//设置消息持久化存储到磁盘上
                messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

                rabbitTemplate.convertAndSend(RabbitmqConstant.EXCHANGE_ORDER_PAYSUCCESS, RabbitmqConstant.ROUTE_MEMBER_MONTHLY_ORDER_PAYSUCCESS, new Message(orderNo.getBytes(), messageProperties));

                confirmPayResponse.setMess("订单需支付金额小于0元，无需支付");
                confirmPayResponse.setStatus("200");
                confirmPayResponse.setPayUrl("");
                responseObserver.onNext(confirmPayResponse.build());
            }
        } else {
            parkingOrderEntity = parkingOrderService.getParkingOrderByOrderNo(orderNo);
            if (parkingOrderEntity == null) {
                confirmPayResponse.setMess("订单信息不存在");
                confirmPayResponse.setStatus("500");
                confirmPayResponse.setPayUrl("");
                responseObserver.onNext(confirmPayResponse.build());
            }

            //在本次创建订单之前判断是否创建过订单并完成了支付
            Boolean canUseMerchantCoupon = true; //能否再次使用商家优惠券标志位
            List<ParkingOrderEntity> finishedOrder = parkingOrderService.getBaseMapper().selectList(new QueryWrapper<ParkingOrderEntity>()
                    .eq("car_number", parkingOrderEntity.getCarNumber())
                    .eq("entry_time", parkingOrderEntity.getEntryTime())
                    .eq("pay_status", "3"));
            for (ParkingOrderEntity everyOrder : finishedOrder) { //遍历所有的已支付订单
                for (String everyUsedCoupon : everyOrder.getCoupons().split(",")) {
                    if (everyUsedCoupon.startsWith("2")) { //本次停车已经使用过商家券了
                        canUseMerchantCoupon = false;
                        break;
                    }
                }
            }

            //获取优惠券列表
            LinkedList<CreateParkingOrderVo.CouponInfo> couponList = new LinkedList();
            for (ParkingOrder.CouponInfo everyCoupon : request.getCouponListList()) {
                CreateParkingOrderVo.CouponInfo couponInfoTmp = new CreateParkingOrderVo.CouponInfo();
                couponInfoTmp.setCouponCode(everyCoupon.getCouponCode());
                couponInfoTmp.setCouponValue(everyCoupon.getCouponValue());
                couponInfoTmp.setCouponType(everyCoupon.getCouponType());
                couponInfoTmp.setCouponMold(everyCoupon.getCouponMold());
                couponInfoTmp.setChoosed(everyCoupon.getChoosed());
                couponList.add(couponInfoTmp);
            }
            //构造算费系统的请求参数
            ChargeParkingFeeResponse chargeParkingFeeResponse = null;
            try {
                ChargeParkingFeeRequest.Builder chargeParkingFeeRequest = ChargeParkingFeeRequest.newBuilder();
                chargeParkingFeeRequest.setParkNo(parkingOrderEntity.getParkNo());
                chargeParkingFeeRequest.setCarNumber(parkingOrderEntity.getCarNumber());
                chargeParkingFeeRequest.setCarTypeCode(parkingOrderEntity.getCarTypeCode());
                for (CreateParkingOrderVo.CouponInfo everyCoupon : couponList) {
                    CouponInfo.Builder coupon = CouponInfo.newBuilder();
                    coupon.setCouponCode(everyCoupon.getCouponCode());
                    coupon.setCouponMold(everyCoupon.getCouponMold());
                    coupon.setCouponValue(everyCoupon.getCouponValue());
                    coupon.setCouponType(everyCoupon.getCouponType());
                    coupon.setSelected(everyCoupon.getChoosed());
                    Boolean canUse = true;
                    if (everyCoupon.getCouponMold() == 2 && canUseMerchantCoupon == false) {
                        canUse = false;
                    }
                    coupon.setCanUse(canUse);
                    chargeParkingFeeRequest.addCouponItem(coupon);
                }
                chargeParkingFeeResponse = parkingChargeServiceBlockingStub.chargeParkingFee(chargeParkingFeeRequest.build());
            } catch (Exception exception) {
                log.error("grpc调用算费系统异常，请联系管理员");
            }
            //请求算费完毕

            //根据算费响应修改订单信息
//            parkingOrderEntity.setDiscountAmount(BigDecimal.valueOf(chargeParkingFeeResponse.getDiscountAmount()));
//            parkingOrderEntity.setPayAmount(parkingOrderEntity.getPayableAmount().subtract(BigDecimal.valueOf(chargeParkingFeeResponse.getDiscountAmount())));

            String couponStr = "";
            List<ChargeCouponInfo> chargeCouponInfo = chargeParkingFeeResponse.getCouponItemsList();
            for (ChargeCouponInfo everyCoupon : chargeCouponInfo) {
                if (everyCoupon.getSelected()) {
                    couponStr += everyCoupon.getCouponMold() + "_" + everyCoupon.getCouponCode() + ",";
                }
            }
//            parkingOrderEntity.setCoupons(couponStr);
//            parkingOrderEntity.setDiscountReason(discountReason);


            log.info("算费计算得到的订单应支付金额：" + parkingOrderEntity.getPayAmount().doubleValue());
            parkingOrderEntity.setPayMethod(payType.toString());
            parkingOrderService.updateById(parkingOrderEntity);

            totalAmount = parkingOrderEntity.getPayAmount().doubleValue();
            //如果需要支付的金额小于等于0，则直接修改订单状态
            if (totalAmount <= 0) {
                needStartPay = false;
                parkingOrderEntity.setPayStatus("3");
                parkingOrderEntity.setOrderStatus("03");
                parkingOrderEntity.setPayMethod(payType.toString());
                parkingOrderEntity.setPayTime(new Date());
                parkingOrderService.updateById(parkingOrderEntity);

                //往消息队列中扔一条消息，告诉客户（lot）支付成功了
                MessageProperties messagePropertiesLot = new MessageProperties();//设置消息持久化存储到磁盘上
                messagePropertiesLot.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

                HashMap<String, String> msgHash = new HashMap<>();
                msgHash.put("orderNo", orderNo);
                msgHash.put("payTime", sdf.format(parkingOrderEntity.getPayTime()));
                msgHash.put("amount", "0");
                String mesContent = JSON.toJSONString(msgHash);

                rabbitTemplate.convertAndSend(RabbitmqConstant.EXCHANGE_ORDER_PAYSUCCESS, RabbitmqConstant.ROUTE_LOT_PARKING_ORDER_PAYSUCCESS, new Message(mesContent.getBytes(), messagePropertiesLot));


                //扔一条消息到消息队列，告知商户微服务，订单已经支付成功，需要将优惠券状态修改
                MessageProperties messageProperties = new MessageProperties();//设置消息持久化存储到磁盘上
                messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

                HashMap<String, String> msgHasht = new HashMap<>();
                msgHasht.put("usedCoupons", parkingOrderEntity.getCoupons());
                msgHasht.put("usedTime", sdf.format(new Date()));
                String msgHashStr = JSON.toJSONString(msgHasht);
                rabbitTemplate.convertAndSend(RabbitmqConstant.EXCHANGE_ORDER_PAYSUCCESS, RabbitmqConstant.ROUTE_MEMBER_MERCHANT_ORDER_PAYSUCCESS, new Message(msgHashStr.getBytes(), messageProperties));


                confirmPayResponse.setMess("订单需支付金额小于0元，无需支付");
                confirmPayResponse.setStatus("200");
                confirmPayResponse.setPayUrl("");
                responseObserver.onNext(confirmPayResponse.build());
            }
        }
        //需要拉起支付
        if (needStartPay) {
            //构造发起支付的请求参数
            if (payType == 1) {
                //支付宝支付

                //构建生成支付宝支付的请求参数
                AlipayTradeWapPayRequestProto.Builder alipayRequest = AlipayTradeWapPayRequestProto.newBuilder();
                alipayRequest.setOutTradeNo(orderNo);
                alipayRequest.setTotalAmount(totalAmount);
                alipayRequest.setSubject("出场订单支付");

                AlipayTradeWapPayResponseProto response = aliPayServiceBlockingStub.alipayTradeWapPay(alipayRequest.build());

                if (response.getCode().equals("10000")) {
                    //将订单状态改为"已确认"
                    if (orderNo.startsWith("ME")) {
                        merchantOrderEntity.setOrderStatus("02");
                        merchantOrderService.updateById(merchantOrderEntity);
                    } else if (orderNo.startsWith("MO")) {
                        monthlyOrderEntity.setOrderStatus("02");
                        monthlyOrderService.updateById(monthlyOrderEntity);
                    } else {
                        parkingOrderEntity.setOrderStatus("02");
                        parkingOrderService.updateById(parkingOrderEntity);
                    }

                    //调用支付宝成功
                    confirmPayResponse.setMess("ok");
                    confirmPayResponse.setStatus("200");
                    confirmPayResponse.setPayUrl(response.getBody());
                    responseObserver.onNext(confirmPayResponse.build());
                } else {
                    //调用出现问题
                    confirmPayResponse.setMess(response.getMsg());
                    confirmPayResponse.setStatus(response.getCode());
                    confirmPayResponse.setPayUrl("");
                    responseObserver.onNext(confirmPayResponse.build());
                }
            } else if (payType == 2) {

                //如果选择JSAPI支付
                if (weChatPayMethod == 1) {
                    //创建请求体
                    WechatpayTradeJsapiPayRequestProto.Builder jsapiPayRequest = WechatpayTradeJsapiPayRequestProto.newBuilder();
                    jsapiPayRequest.setOutTradeNo(orderNo);
                    jsapiPayRequest.setDescription("用户支付");
                    jsapiPayRequest.setTotal(totalAmount);
                    jsapiPayRequest.setOpenid(openId);
                    log.info("订单号：" + orderNo + "，订单金额：" + totalAmount + "，openId：" + openId);
                    WechatpayTradeJsapiPayResponseProto jsapiPayResponse = wechatPayServiceBlockingStub.wechatpayTradeJsapiPay(jsapiPayRequest.build());

                    //构建响应体
                    confirmPayResponse.setMess("微信jsapi支付申请成功");
                    confirmPayResponse.setStatus("200");
                    confirmPayResponse.setPayUrl("");
                    confirmPayResponse.setAppId(openId);
                    confirmPayResponse.setTimestamp(jsapiPayResponse.getTimestamp());
                    confirmPayResponse.setNonceStr(jsapiPayResponse.getNonceStr());
                    confirmPayResponse.setPackageVal(jsapiPayResponse.getPackage());
                    confirmPayResponse.setSignType(jsapiPayResponse.getSignType());
                    confirmPayResponse.setPaySign(jsapiPayResponse.getPaySign());
                    responseObserver.onNext(confirmPayResponse.build());

                } else {//如果是H5支付
                    //创建请求体
                    WechatpayTradeH5PayRequestProto.Builder h5PayRequest = WechatpayTradeH5PayRequestProto.newBuilder();
                    h5PayRequest.setOutTradeNo(orderNo);
                    h5PayRequest.setDescription("用户支付");
                    h5PayRequest.setTotal(totalAmount);
                    h5PayRequest.setPayerClientIp(PayerClientIP);
                    h5PayRequest.setH5Type(H5Type);
                    WechatpayTradeH5PayResponseProto h5PayResponse = wechatPayServiceBlockingStub.wechatpayTradeH5Pay(h5PayRequest.build());
                    //构建响应体
                    confirmPayResponse.setMess("微信h5支付申请成功");
                    confirmPayResponse.setStatus("200");
                    confirmPayResponse.setPayUrl(h5PayResponse.getPayUrl());
                    responseObserver.onNext(confirmPayResponse.build());

                }
            } else {
                //不存在的支付方式
                confirmPayResponse.setMess("请选择正确的支付方式");
                confirmPayResponse.setStatus("500");
                confirmPayResponse.setPayUrl("");
                responseObserver.onNext(confirmPayResponse.build());
            }
        }


        responseObserver.onCompleted();
    }

    @Override
    public void getParkingOrderDetail(ParkingOrder.OrderDetailRequest request, StreamObserver<ParkingOrder.CreateOrderReponse> responseObserver) {
        //TODO  这儿以后需要优化成传入多个参数，返回多个值

        ValueOperations<String, String> opsValue = redisTemplate.opsForValue(); //redis操作句柄
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
        orderDetailBuilder.setPayTime(parkingOrderEntity.getPayTime()!= null ? String.valueOf(parkingOrderEntity.getPayTime().getTime()): "");
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
        for (ParkingOrderItemEntity everyEntity : parkingOrderItemList) {
            ParkingOrder.OrderItem.Builder item = ParkingOrder.OrderItem.newBuilder();
            item.setExitTime(sdf.format(everyEntity.getExitTime()));
            item.setEntryTime(sdf.format(everyEntity.getEntryTime()));
            item.setParkFieldId(everyEntity.getParkFieldId());
            item.setPayedAmount(everyEntity.getPayableAmount().doubleValue());
            orderDetailBuilder.addItemList(item.build());
        }

        //需要从redis中取出订单的优惠券信息
        String couponInfo = (String) opsValue.get(parkingOrderEntity.getOrderNo());
        LinkedList<HashMap<String, Object>> couponData = JSON.parseObject(couponInfo, new TypeReference<LinkedList<HashMap<String, Object>>>() {
        });
        if (couponData != null) {
            couponData.stream().forEach(everyCouponInfo -> {
                ParkingOrder.CouponInfo.Builder cp = ParkingOrder.CouponInfo.newBuilder();
                cp.setCouponCode(everyCouponInfo.get("couponCode").toString());
                cp.setCouponType(Integer.valueOf((String) everyCouponInfo.get("couponType")));
                cp.setCouponMold(Integer.valueOf((String) everyCouponInfo.get("couponMold")));
                cp.setCouponValue(Integer.valueOf((String) everyCouponInfo.get("couponValue")));
                cp.setCanUse((boolean) everyCouponInfo.get("canUse"));
                cp.setChoosed((boolean) everyCouponInfo.get("choosed"));
                responseBuild.addCouponlist(cp.build());
            });
        }

        responseBuild.setOrderDetail(orderDetailBuilder.build());
        responseObserver.onNext(responseBuild.build());
        responseObserver.onCompleted();
    }

    @Override
    public void createParkingOrder(ParkingOrder.CreateOrderRequest request, StreamObserver<ParkingOrder.CreateOrderReponse> responseObserver) {
        ValueOperations<String, String> opsValue = redisTemplate.opsForValue(); //redis操作句柄

        //开始接收传入的参
        CreateParkingOrderVo createParkingOrderVo = new CreateParkingOrderVo();
        createParkingOrderVo.setCarNumber(request.getCarNumber());
        createParkingOrderVo.setParkNo(request.getParkNo());
        createParkingOrderVo.setCarTypeCode(request.getCarTypeCode());
        createParkingOrderVo.setEntryTime(request.getEntryTime());
        createParkingOrderVo.setPassageNo(request.getPassageNo());
        createParkingOrderVo.setExitTime(request.getExitTime());
        createParkingOrderVo.setPayMethod("1");

        List<CreateParkingOrderVo.OrderItem> list = new LinkedList<>();
        for (ParkingOrder.OrderItem everyItem : request.getItemListList()) {
            CreateParkingOrderVo.OrderItem orderItem = new CreateParkingOrderVo.OrderItem();
            orderItem.setParkFieldId(everyItem.getParkFieldId());
            orderItem.setExitTime(everyItem.getExitTime());
            orderItem.setEntryTime(everyItem.getEntryTime());
            list.add(orderItem);
        }

        //获取优惠券列表
        LinkedList<CreateParkingOrderVo.CouponInfo> couponList = new LinkedList();
        for (ParkingOrder.CouponInfo everyCoupon : request.getCouponListList()) {
            CreateParkingOrderVo.CouponInfo couponInfoTmp = new CreateParkingOrderVo.CouponInfo();
            couponInfoTmp.setCouponCode(everyCoupon.getCouponCode());
            couponInfoTmp.setCouponValue(everyCoupon.getCouponValue());
            couponInfoTmp.setCouponType(everyCoupon.getCouponType());
            couponInfoTmp.setCouponMold(everyCoupon.getCouponMold());
            couponInfoTmp.setChoosed(everyCoupon.getChoosed());
            couponList.add(couponInfoTmp);
        }
        createParkingOrderVo.setOrderNo(request.getOrderNo());
        log.info("获取到请求的参数：" + JSON.toJSONString(createParkingOrderVo));
        //至此接收数据完毕

        //如果是第二次进来，先把原订单设置为已经取消
        if (!createParkingOrderVo.getOrderNo().equals("")) {
            log.info("带着订单号" + createParkingOrderVo.getOrderNo() + "进来，先取消原订单");
            ParkingOrderEntity parkingOrder = parkingOrderService.getBaseMapper().selectOne(new QueryWrapper<ParkingOrderEntity>().eq("order_no", createParkingOrderVo.getOrderNo()));
            parkingOrder.setOrderStatus("05");
            parkingOrder.setPayStatus("4");
            parkingOrderService.updateById(parkingOrder);
        }

        //订单在15分钟内不会重复生成
        ParkingOrderEntity unPayedOrder = parkingOrderService.getBaseMapper().selectOne(new QueryWrapper<ParkingOrderEntity>()
                .eq("car_number", createParkingOrderVo.getCarNumber())
                .eq("entry_time", new Date(Long.valueOf(createParkingOrderVo.getEntryTime())))
                .eq("order_status", "01")
                .orderByDesc("id").last("limit 1"));
        if (unPayedOrder != null) {
            ParkingOrder.CreateOrderReponse.Builder responseBuild = ParkingOrder.CreateOrderReponse.newBuilder();
            log.info("未支付订单在15分钟内不会再次生成订单：" + unPayedOrder.getOrderNo());
            responseBuild.setStatus("200");
//            responseBuild.setReasonCode("201");
            responseBuild.setMess("未支付订单在15分钟内不会再次生成订单");
            ParkingOrder.OrderDetail.Builder orderDetailBuilder = ParkingOrder.OrderDetail.newBuilder();
            orderDetailBuilder.setOrderNo(unPayedOrder.getOrderNo());
            orderDetailBuilder.setParkNo(unPayedOrder.getParkNo());
            orderDetailBuilder.setPassageNo(unPayedOrder.getPassageNo());
            orderDetailBuilder.setCarNumber(unPayedOrder.getCarNumber());
            orderDetailBuilder.setCarTypeCode(unPayedOrder.getCarTypeCode());
            orderDetailBuilder.setEntryTime(sdf.format(unPayedOrder.getEntryTime()));
            orderDetailBuilder.setPayableAmount(unPayedOrder.getPayableAmount().doubleValue());
            orderDetailBuilder.setDiscountAmount(unPayedOrder.getDiscountAmount().doubleValue());
            orderDetailBuilder.setPaidAmount(unPayedOrder.getPaidAmount().doubleValue());
            orderDetailBuilder.setPayAmount(unPayedOrder.getPayAmount().doubleValue());
            orderDetailBuilder.setPayMethod(unPayedOrder.getPayMethod());
            orderDetailBuilder.setPayStatus(unPayedOrder.getPayStatus());
            orderDetailBuilder.setRemark(unPayedOrder.getRemark());
            orderDetailBuilder.setCreateTime(sdf.format(unPayedOrder.getCreateTime()));
            orderDetailBuilder.setExpireTime(String.valueOf(unPayedOrder.getExpireTime().getTime()));
            orderDetailBuilder.setUpdateTime(String.valueOf(unPayedOrder.getUpdateTime().getTime()));
            orderDetailBuilder.setPayTime(unPayedOrder.getPayTime() != null ? String.valueOf(unPayedOrder.getPayTime().getTime()) : "");
            responseBuild.setOrderDetail(orderDetailBuilder.build());

            //需要从redis中取出订单的优惠券信息
            String couponInfo = (String) opsValue.get(unPayedOrder.getOrderNo());
            LinkedList<HashMap<String, Object>> couponData = JSON.parseObject(couponInfo, new TypeReference<LinkedList<HashMap<String, Object>>>() {
            });
            if (couponData != null) {
                couponData.stream().forEach(everyCouponInfo -> {
                    ParkingOrder.CouponInfo.Builder cp = ParkingOrder.CouponInfo.newBuilder();
                    cp.setCouponCode(everyCouponInfo.get("couponCode").toString());
                    cp.setCouponType(Integer.valueOf((String) everyCouponInfo.get("couponType")));
                    cp.setCouponMold(Integer.valueOf((String) everyCouponInfo.get("couponMold")));
                    cp.setCouponValue(Integer.valueOf((String) everyCouponInfo.get("couponValue")));
                    cp.setCanUse((boolean) everyCouponInfo.get("canUse"));
                    cp.setChoosed((boolean) everyCouponInfo.get("choosed"));
                    responseBuild.addCouponlist(cp.build());
                });
            }

            log.info("15分钟内不需要生成订单，直接响应数据：" + JSON.toJSONString(responseBuild.build()));

            responseObserver.onNext(responseBuild.build());
            responseObserver.onCompleted();
            return;
        }


        //在本次创建订单之前判断是否创建过订单并完成了支付
        Boolean canUseMerchantCoupon = true; //能否再次使用商家优惠券标志位
        Boolean needCreateOrderAgain = false; //是否需要再次生成订单
        ParkingOrderEntity finishedOrder = parkingOrderService.getBaseMapper().selectOne(new QueryWrapper<ParkingOrderEntity>()
                .eq("car_number", createParkingOrderVo.getCarNumber())
                .eq("entry_time", new Date(Long.valueOf(createParkingOrderVo.getEntryTime())))
                .eq("pay_status", "3")
                .orderByDesc("id").last("limit 1"));
        if (finishedOrder != null) {
            log.info("本次停车已经生成过订单了！");
            for (String everyUsedCoupon : finishedOrder.getCoupons().split(",")) {
                if (everyUsedCoupon.startsWith("2")) { //本次停车已经使用过商家券了
                    log.info("本次停车已经使用了商家券：" + finishedOrder.getOrderNo() + "，" + finishedOrder.getCoupons());
                    canUseMerchantCoupon = false;
                    break;
                }
            }
            //判断是否有30分钟内支付的订单
            if (finishedOrder.getPayTime() != null && new Date().after(new Date(finishedOrder.getPayTime().getTime() + (30 * 60 * 1000)))) {
                needCreateOrderAgain = true;
            }

            //无需再次生成订单
            if (!needCreateOrderAgain) {
                log.info("30分钟内不需要再次生成订单" + finishedOrder.getOrderNo());
                ParkingOrder.CreateOrderReponse.Builder responseBuild = ParkingOrder.CreateOrderReponse.newBuilder();
                log.info("已经完成支付，30分钟不再生成订单，可直接离场");
                responseBuild.setStatus("200");
                responseBuild.setReasonCode("201");
                responseBuild.setMess("已经完成支付，30分钟不再生成订单，可直接离场");
                ParkingOrder.OrderDetail.Builder orderDetailBuilder = ParkingOrder.OrderDetail.newBuilder();


                orderDetailBuilder.setOrderNo(finishedOrder.getOrderNo());
                orderDetailBuilder.setParkNo(finishedOrder.getParkNo());
                orderDetailBuilder.setPassageNo(finishedOrder.getPassageNo());
                orderDetailBuilder.setCarNumber(finishedOrder.getCarNumber());
                orderDetailBuilder.setCarTypeCode(finishedOrder.getCarTypeCode());
                orderDetailBuilder.setEntryTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(finishedOrder.getEntryTime()));
                orderDetailBuilder.setPayableAmount(finishedOrder.getPayableAmount().doubleValue());
                orderDetailBuilder.setDiscountAmount(finishedOrder.getDiscountAmount().doubleValue());
                orderDetailBuilder.setPaidAmount(finishedOrder.getPaidAmount().doubleValue());
                orderDetailBuilder.setPayAmount(finishedOrder.getPayAmount().doubleValue());
                orderDetailBuilder.setPayMethod(finishedOrder.getPayMethod());
                orderDetailBuilder.setPayStatus(finishedOrder.getPayStatus());
                orderDetailBuilder.setRemark(finishedOrder.getRemark());
                orderDetailBuilder.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(finishedOrder.getCreateTime()));
                orderDetailBuilder.setExpireTime(String.valueOf(finishedOrder.getExpireTime().getTime()));
                orderDetailBuilder.setUpdateTime(String.valueOf(finishedOrder.getUpdateTime().getTime()));
                orderDetailBuilder.setPayTime(finishedOrder.getPayTime() != null? String.valueOf(finishedOrder.getPayTime().getTime()) : "");


                //需要从redis中取出订单的优惠券信息
                String couponInfo = (String) opsValue.get(finishedOrder.getOrderNo());
                LinkedList<HashMap<String, Object>> couponData = JSON.parseObject(couponInfo, new TypeReference<LinkedList<HashMap<String, Object>>>() {
                });
                if (couponData != null) {
                    couponData.stream().forEach(everyCouponInfo -> {
                        ParkingOrder.CouponInfo.Builder cp = ParkingOrder.CouponInfo.newBuilder();
                        cp.setCouponCode(everyCouponInfo.get("couponCode").toString());
                        cp.setCouponType(Integer.valueOf((String) everyCouponInfo.get("couponType")));
                        cp.setCouponMold(Integer.valueOf((String) everyCouponInfo.get("couponMold")));
                        cp.setCouponValue(Integer.valueOf((String) everyCouponInfo.get("couponValue")));
                        cp.setCanUse((boolean) everyCouponInfo.get("canUse"));
                        cp.setChoosed((boolean) everyCouponInfo.get("choosed"));
                        responseBuild.addCouponlist(cp.build());
                    });
                }

                responseBuild.setOrderDetail(orderDetailBuilder.build());
                log.info("30分钟内无需生成订单，直接返回响应：" + JSON.toJSONString(orderDetailBuilder.build()));
                responseObserver.onNext(responseBuild.build());
                responseObserver.onCompleted();
                return;
            }
        }


        //构造算费系统的请求参数
        log.info("开始请求算费系统");
        ChargeParkingFeeResponse chargeParkingFeeResponse = null;
        try {
            ChargeParkingFeeRequest.Builder chargeParkingFeeRequest = ChargeParkingFeeRequest.newBuilder();
            chargeParkingFeeRequest.setParkNo(createParkingOrderVo.getParkNo());
            chargeParkingFeeRequest.setCarNumber(createParkingOrderVo.getCarNumber());
            chargeParkingFeeRequest.setCarTypeCode(createParkingOrderVo.getCarTypeCode());
            chargeParkingFeeRequest.setTodayParkCount(request.getTodayParkCount());

            for (CreateParkingOrderVo.OrderItem evertItem : list) {
                ParkingItem.Builder item = ParkingItem.newBuilder();
                item.setParkFieldId(evertItem.getParkFieldId());
                item.setEntryTime(sdf.format(new Date(Long.valueOf(evertItem.getEntryTime()))));
                item.setExitTime(sdf.format(new Date(Long.valueOf(evertItem.getExitTime()))));
                chargeParkingFeeRequest.addParkingItem(item.build());
            }
            for (CreateParkingOrderVo.CouponInfo everyCoupon : couponList) {
                CouponInfo.Builder coupon = CouponInfo.newBuilder();
                coupon.setCouponCode(everyCoupon.getCouponCode());
                coupon.setCouponMold(everyCoupon.getCouponMold());
                coupon.setCouponValue(everyCoupon.getCouponValue());
                coupon.setCouponType(everyCoupon.getCouponType());
                coupon.setSelected(everyCoupon.getChoosed());
                Boolean canUse = true;
                if (everyCoupon.getCouponMold()== 2 && canUseMerchantCoupon == false) {
                    canUse = false;
                }
                coupon.setCanUse(canUse);
                chargeParkingFeeRequest.addCouponItem(coupon);
            }
            chargeParkingFeeResponse = parkingChargeServiceBlockingStub.chargeParkingFee(chargeParkingFeeRequest.build());

        } catch (Exception exception) {
            log.error("grpc调用算费系统异常，请联系管理员");
        }
        log.info("算费系统响应：" + JSON.toJSONString(chargeParkingFeeResponse));
        //请求算费完毕

        //将算费信息并入到CreateParkingOrderVo中
        createParkingOrderVo.setPayableAmount(chargeParkingFeeResponse.getPayableAmount());
        createParkingOrderVo.setDiscountAmount(chargeParkingFeeResponse.getDiscountAmount());
        createParkingOrderVo.setPayAmount(needCreateOrderAgain ? ((chargeParkingFeeResponse.getPayAmount() - finishedOrder.getPayableAmount().doubleValue()) > 0 ? chargeParkingFeeResponse.getPayAmount() - finishedOrder.getPayableAmount().doubleValue() : Double.valueOf(0)) : chargeParkingFeeResponse.getPayAmount());
        createParkingOrderVo.setPaidAmount(needCreateOrderAgain ? finishedOrder.getPayableAmount().doubleValue() : Double.valueOf(0));
        List<CreateParkingOrderVo.OrderItem> newlist = new LinkedList<>();

        for (ParkingFeeItem everyItem : chargeParkingFeeResponse.getFeeItemsList()) {
            CreateParkingOrderVo.OrderItem orderItem = new CreateParkingOrderVo.OrderItem();
            orderItem.setParkFieldId(everyItem.getParkFieldId());
            try {
                orderItem.setExitTime(String.valueOf(sdf.parse(everyItem.getExitTime()).getTime()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            try {
                orderItem.setEntryTime(String.valueOf(sdf.parse(everyItem.getEntryTime()).getTime()));
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
        for (ChargeCouponInfo everyCoupon : chargeParkingFeeResponse.getCouponItemsList()) {
            //只记录被选用的优惠券
            if (everyCoupon.getSelected()) {
                CreateParkingOrderVo.CouponInfo tmpCoupon = new CreateParkingOrderVo.CouponInfo();
                tmpCoupon.setChoosed(everyCoupon.getSelected());
                tmpCoupon.setCouponCode(everyCoupon.getCouponCode());
                tmpCoupon.setCouponMold(everyCoupon.getCouponMold());
                tmpCoupon.setCouponType(everyCoupon.getCouponType());
                tmpCoupon.setCouponValue(everyCoupon.getCouponValue());
                usedCoupon.add(tmpCoupon);
                discount.add(BigDecimal.valueOf(everyCoupon.getCouponValue()));
                createParkingOrderVo.setDiscountReason("优惠券优惠");
            }
        }
        createParkingOrderVo.setCouponList(usedCoupon);


        //预支付金额为0，不要生成订单
        if (createParkingOrderVo.getPassageNo().equals("") && createParkingOrderVo.getPayableAmount().compareTo(Double.valueOf("0")) == 0) {
            log.info("预支付下金额为0，不生成订单");
            ParkingOrder.CreateOrderReponse.Builder responseBuild = ParkingOrder.CreateOrderReponse.newBuilder();
            responseBuild.setStatus("200");
            responseBuild.setReasonCode("201");
            responseBuild.setMess("无需生成订单");
            ParkingOrder.OrderDetail.Builder orderDetailBuilder = ParkingOrder.OrderDetail.newBuilder();
            responseBuild.setOrderDetail(orderDetailBuilder.build());
            responseObserver.onNext(responseBuild.build());
            responseObserver.onCompleted();
        } else {
            //岗亭支付，需要支付金额为0，则直接生成已支付订单
            ParkingOrderEntity parkingOrderEntity = null;
            if (createParkingOrderVo.getPayableAmount().compareTo(Double.valueOf("0")) == 0) {
                log.info("岗亭支付，订单金额为0，直接生成已支付订单");
                createParkingOrderVo.setPayed(true);
                createParkingOrderVo.setPayTime(new Date());
                log.info("岗亭支付，0元订单写入订单表的数据为：" + JSON.toJSONString(createParkingOrderVo));
                parkingOrderEntity = parkingOrderService.createParkingOrder(createParkingOrderVo);


                //往消息队列中扔一条消息，告诉客户（lot）支付成功了
                MessageProperties messagePropertiesLot = new MessageProperties();//设置消息持久化存储到磁盘上
                messagePropertiesLot.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                HashMap<String, String> msgHash = new HashMap<>();
                msgHash.put("orderNo", parkingOrderEntity.getOrderNo());
                msgHash.put("payTime", sdf.format(parkingOrderEntity.getPayTime()));
                msgHash.put("amount", parkingOrderEntity.getPayAmount().toString());
                msgHash.put("passageNo", parkingOrderEntity.getPassageNo());
                msgHash.put("carNumber", parkingOrderEntity.getCarNumber());
                msgHash.put("payableAmount", parkingOrderEntity.getPayableAmount().toString());
                msgHash.put("payAmount", parkingOrderEntity.getPayAmount().toString());
                msgHash.put("paidAmount", parkingOrderEntity.getPaidAmount().toString());
                msgHash.put("discountAmount", parkingOrderEntity.getDiscountAmount().toString());
                msgHash.put("parkNo", parkingOrderEntity.getParkNo());
                msgHash.put("entryTime", sdf.format(parkingOrderEntity.getEntryTime()));
                String mesContent = JSON.toJSONString(msgHash);
                rabbitTemplate.convertAndSend(RabbitmqConstant.EXCHANGE_ORDER_PAYSUCCESS, RabbitmqConstant.ROUTE_LOT_PARKING_ORDER_PAYSUCCESS, new Message(mesContent.getBytes(), messagePropertiesLot));

            } else {
                log.info("岗亭支付非0元订单");
                //生成订单数据写入数据库
                createParkingOrderVo.setPayed(false);
                createParkingOrderVo.setPayTime(null);
                log.info("岗亭支付，非0元订单写入订单表的数据为：" + JSON.toJSONString(createParkingOrderVo));
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
            if (parkingOrderEntity != null) {
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
                orderDetailBuilder.setPayTime(parkingOrderEntity.getPayTime() != null ? String.valueOf(parkingOrderEntity.getPayTime().getTime()): "");
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

                for (ParkingFeeItem everyItem : chargeParkingFeeResponse.getFeeItemsList()) {
                    ParkingOrder.OrderItem.Builder item = ParkingOrder.OrderItem.newBuilder();
                    item.setExitTime(everyItem.getExitTime());
                    item.setEntryTime(everyItem.getEntryTime());
                    item.setParkFieldId(everyItem.getParkFieldId());
                    item.setPayedAmount(everyItem.getPayableAmount());
                    orderDetailBuilder.addItemList(item.build());
                }

                //遍历所有给我的优惠券，加上能不能用的标识和选没选用的标志
                LinkedList<Map<String, Object>> coupleData = new LinkedList<>();

                for (ChargeCouponInfo everyCoupon : chargeParkingFeeResponse.getCouponItemsList()) {

                    //同时，将优惠券信息放到redis缓存起来。
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("couponCode", everyCoupon.getCouponCode());
                    map.put("couponType", String.valueOf(everyCoupon.getCouponType()));
                    map.put("couponMold", String.valueOf(everyCoupon.getCouponMold()));
                    map.put("couponValue", String.valueOf(everyCoupon.getCouponValue()));
                    map.put("choosed", everyCoupon.getSelected());
                    if (everyCoupon.getCouponMold() == 2) {
                        map.put("canUse", canUseMerchantCoupon);
                    } else {
                        map.put("canUse", true);
                    }
                    coupleData.add(map);

                    ParkingOrder.CouponInfo.Builder cp = ParkingOrder.CouponInfo.newBuilder();
                    cp.setCouponCode(everyCoupon.getCouponCode());
                    cp.setCouponType(everyCoupon.getCouponType());
                    cp.setCouponMold(everyCoupon.getCouponMold());
                    cp.setCouponValue(everyCoupon.getCouponValue());
                    if (everyCoupon.getCouponMold() == 2) {
                        cp.setCanUse(canUseMerchantCoupon);
                    } else {
                        cp.setCanUse(true);
                    }
                    cp.setChoosed(everyCoupon.getSelected());

                    responseBuild.addCouponlist(cp.build());
                }
                //优惠券信息入redis
                if (!coupleData.isEmpty()) {
                    log.info("订单优惠券信息写入redis：" + JSON.toJSONString(coupleData));
                    opsValue.setIfAbsent(parkingOrderEntity.getOrderNo(), JSON.toJSONString(coupleData), 15, TimeUnit.MINUTES);
                }


                responseBuild.setOrderDetail(orderDetailBuilder.build());
                log.info("创建订单最终响应数据：" + JSON.toJSONString(orderDetailBuilder.build()));
                responseObserver.onNext(responseBuild.build());
                responseObserver.onCompleted();
            } else {
                ParkingOrder.CreateOrderReponse.Builder responseBuild = ParkingOrder.CreateOrderReponse.newBuilder();
                responseBuild.setStatus("500");
                responseBuild.setMess("并发系统异常");
                ParkingOrder.OrderDetail.Builder orderDetailBuilder = ParkingOrder.OrderDetail.newBuilder();
                responseBuild.setOrderDetail(orderDetailBuilder.build());
                responseObserver.onNext(responseBuild.build());
                responseObserver.onCompleted();
            }
        }

    }

    @Override
    public void statisticIncome(ParkingOrder.StatisticIncomeRequest request, StreamObserver<ParkingOrder.StatisticIncomeResponse> responseObserver) {
        List<String> parkNos = new ArrayList<>(request.getParkNosList());
        StatisticIncomeVo incomeVo = parkingOrderService.statisticIncome(parkNos);
        ParkingOrder.StatisticIncomeResponse builder = ParkingOrder.StatisticIncomeResponse.newBuilder()
                .setTodayIncome(incomeVo.getTodayIncome().doubleValue())
                .setTotalIncome(incomeVo.getTotalIncome().doubleValue())
                .build();
        responseObserver.onNext(builder);
        responseObserver.onCompleted();
    }

    @Override
    public void analysePayTypeDayFact(ParkingOrder.AnalysePayTypeDayFactRequest request, StreamObserver<ParkingOrder.AnalysePayTypeDayFactResponse> responseObserver) {
        List<String> parkNos = new ArrayList<>(request.getParkNosList());
        String day = request.getDay();
        List<PayTypeDayFactVO> payTypeDayFactVOList = parkingOrderService.analysePayTypeDayFact(day, parkNos);
        List<ParkingOrder.AnalysePayTypeDayFactProto> protos = new ArrayList<>();
        payTypeDayFactVOList.forEach(item -> {
            ParkingOrder.AnalysePayTypeDayFactProto proto = assembleAnalysePayTypeDayFactProto(item);
            protos.add(proto);
        });
        responseObserver.onNext(ParkingOrder.AnalysePayTypeDayFactResponse.newBuilder().addAllRows(protos).build());
        responseObserver.onCompleted();
    }

    @Override
    public void analysePayMethodDayFact(ParkingOrder.AnalysePayMethodDayFactRequest request, StreamObserver<ParkingOrder.AnalysePayMethodDayFactResponse> responseObserver) {
        List<String> parkNos = new ArrayList<>(request.getParkNosList());
        String day = request.getDay();
        List<PayMethodDayFactVO> payMethodDayFactVOList = parkingOrderService.analysePayMethodDayFact(day, parkNos);
        List<ParkingOrder.AnalysePayMethodDayFactProto> protos = new ArrayList<>();
        payMethodDayFactVOList.forEach(item -> {
            ParkingOrder.AnalysePayMethodDayFactProto proto = assembleAnalysePayMethodDayFactProto(item);
            protos.add(proto);
        });
        responseObserver.onNext(ParkingOrder.AnalysePayMethodDayFactResponse.newBuilder().addAllRows(protos).build());
        responseObserver.onCompleted();
    }

    @Override
    public void analyseOrderSituationDayFact(ParkingOrder.AnalyseOrderSituationDayFactRequest request, StreamObserver<ParkingOrder.AnalyseOrderSituationDayFactResponse> responseObserver) {
        List<String> parkNos = new ArrayList<>(request.getParkNosList());
        String day = request.getDay();
        List<OrderSituationDayFactVO> orderSituationDayFactVOList = parkingOrderService.analyseOrderSituationDayFact(day, parkNos);
        List<ParkingOrder.AnalyseOrderSituationDayFactProto> protos = new ArrayList<>();
        orderSituationDayFactVOList.forEach(item -> {
            ParkingOrder.AnalyseOrderSituationDayFactProto proto = assembleAnalyseOrderSituationDayFactProto(item);
            protos.add(proto);
        });
        responseObserver.onNext(ParkingOrder.AnalyseOrderSituationDayFactResponse.newBuilder().addAllRows(protos).build());
        responseObserver.onCompleted();
    }

    @Override
    public void analyseRevenueStatisticsDayFact(ParkingOrder.AnalyseRevenueStatisticsRequest request, StreamObserver<ParkingOrder.AnalyseRevenueStatisticsResponse> responseObserver) {
        List<String> parkNos = new ArrayList<>(request.getParkNosList());
        String day = request.getDay();
        RevenueStatisticsDayFactVO revenueStatisticsDayFactVO = parkingOrderService.analyseRevenueStatisticsDayFact(day, parkNos);
        ParkingOrder.AnalyseRevenueStatisticsResponse builder = assembleAnalyseRevenueStatisticsResponse(revenueStatisticsDayFactVO);
        responseObserver.onNext(builder);
        responseObserver.onCompleted();
    }

    /**
     * 组装首页付费类型事实回复对象
     *
     * @param payTypeDayFactVO 首页付费类型事实
     * @return com.czdx.grpc.lib.order.AnalysePayTypeDayFactProto
     */
    private ParkingOrder.AnalysePayTypeDayFactProto assembleAnalysePayTypeDayFactProto(PayTypeDayFactVO payTypeDayFactVO) {
        // 组装返回对象
        ParkingOrder.AnalysePayTypeDayFactProto.Builder responseBuilder = ParkingOrder.AnalysePayTypeDayFactProto.newBuilder();
        try {
            ProtoJsonUtil.toProtoBean(responseBuilder, payTypeDayFactVO);
        } catch (IOException e) {
            log.error("组装首页付费类型事实异常，{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return responseBuilder.build();
    }

    /**
     * 组装首页付费方式事实回复对象
     *
     * @param payMethodDayFactVO 首页付费方式事实
     * @return com.czdx.grpc.lib.order.AnalysePayMethodDayFactProto
     */
    private ParkingOrder.AnalysePayMethodDayFactProto assembleAnalysePayMethodDayFactProto(PayMethodDayFactVO payMethodDayFactVO) {
        // 组装返回对象
        ParkingOrder.AnalysePayMethodDayFactProto.Builder responseBuilder = ParkingOrder.AnalysePayMethodDayFactProto.newBuilder();
        try {
            ProtoJsonUtil.toProtoBean(responseBuilder, payMethodDayFactVO);
        } catch (IOException e) {
            log.error("组装首页付费方式事实异常，{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return responseBuilder.build();
    }

    /**
     * 组装首页订单情况事实回复对象
     *
     * @param orderSituationDayFactVO 首页订单情况事实
     * @return com.czdx.grpc.lib.order.AnalyseOrderSituationDayFactProto
     */
    private ParkingOrder.AnalyseOrderSituationDayFactProto assembleAnalyseOrderSituationDayFactProto(OrderSituationDayFactVO orderSituationDayFactVO) {
        // 组装返回对象
        ParkingOrder.AnalyseOrderSituationDayFactProto.Builder responseBuilder = ParkingOrder.AnalyseOrderSituationDayFactProto.newBuilder();
        try {
            ProtoJsonUtil.toProtoBean(responseBuilder, orderSituationDayFactVO);
        } catch (IOException e) {
            log.error("组装首页订单情况事实异常，{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return responseBuilder.build();
    }

    /**
     * 组装首页收入统计事实对象回复对象
     *
     * @param revenueStatisticsDayFactVO 首页收入统计事实对象
     * @return com.czdx.grpc.lib.order.AnalyseRevenueStatisticsResponse
     */
    private ParkingOrder.AnalyseRevenueStatisticsResponse assembleAnalyseRevenueStatisticsResponse(RevenueStatisticsDayFactVO revenueStatisticsDayFactVO) {
        // 组装返回对象
        ParkingOrder.AnalyseRevenueStatisticsResponse.Builder responseBuilder = ParkingOrder.AnalyseRevenueStatisticsResponse.newBuilder();
        try {
            ProtoJsonUtil.toProtoBean(responseBuilder, revenueStatisticsDayFactVO);
        } catch (IOException e) {
            log.error("组装首页收入统计事实对象异常，{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return responseBuilder.build();
    }

    /**
     * @apiNote 查询指定日期指定车场订单总实付金额
     */
    @Override
    public void analyseAmountAnalysisDayFact(ParkingOrder.AnalyseAmountAnalysisDayFactRequest request, StreamObserver<ParkingOrder.AnalyseAmountAnalysisDayFactResponse> responseObserver) {
        ProtocolStringList parkNos = request.getParkNosList();
        String day = request.getDay();
        //查询商户订单总实付金额
        BigDecimal merchantOnlineIncome = merchantOrderService.sumOnlineIncome(parkNos, day);
        //查询商户订单总优惠金额
        BigDecimal merchantOnlineDeduction = merchantOrderService.sumOnlineDeduction(parkNos, day);
        //查询月租车订单总实付金额
        BigDecimal monthlyOnlineIncome = monthlyOrderService.sumOnlineIncome(parkNos, day);
        //查询月租车订单总优惠金额
        BigDecimal monthlyOnlineDeduction = monthlyOrderService.sumOnlineDeduction(parkNos, day);
        //查询停车订单总实付金额
        BigDecimal parkingOnlineIncome = parkingOrderService.sumOnlineIncome(parkNos, day);
        //查询停车订单总优惠金额
        BigDecimal parkingOnlineDeduction = parkingOrderService.sumOnlineDeduction(parkNos, day);
        responseObserver.onNext(ParkingOrder.AnalyseAmountAnalysisDayFactResponse.newBuilder()
                .setOnlineIncome(merchantOnlineIncome.add(monthlyOnlineIncome).add(parkingOnlineIncome).doubleValue())
                .setDeductionAmount(merchantOnlineDeduction.add(monthlyOnlineDeduction).add(parkingOnlineDeduction).doubleValue())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void createManualParkingOrder(ParkingOrder.ManualParkingOrderRequest request, StreamObserver<ParkingOrder.ManualParkingOrderReponse> responseObserver) {
        //开始接收传入的参
        CreateParkingOrderVo createParkingOrderVo = new CreateParkingOrderVo();
        createParkingOrderVo.setCarNumber(request.getCarNumber());
        createParkingOrderVo.setParkNo(request.getParkNo());
        createParkingOrderVo.setCarTypeCode(request.getCarTypeCode());
        createParkingOrderVo.setEntryTime(request.getEntryTime());
        createParkingOrderVo.setExitTime(request.getExitTime());
        createParkingOrderVo.setPayAmount(request.getManualAmount());
        createParkingOrderVo.setDiscountReason(request.getManualReason());
        createParkingOrderVo.setPayMethod("4");
        createParkingOrderVo.setPassageNo("");
        createParkingOrderVo.setPayed(true);
        createParkingOrderVo.setPayableAmount(Double.valueOf(0));
        createParkingOrderVo.setDiscountAmount(Double.valueOf(0));
        createParkingOrderVo.setPaidAmount(Double.valueOf(0));
        createParkingOrderVo.setCouponList(null);

        //创建订单
        ParkingOrderEntity parkingOrderEntity = parkingOrderService.createParkingOrder(createParkingOrderVo);
        //构建响应
        ParkingOrder.ManualParkingOrderReponse.Builder responseBuild = ParkingOrder.ManualParkingOrderReponse.newBuilder();
        responseBuild.setStatus("200");
        responseBuild.setMess("手动创建停车订单成功");
        responseBuild.setOrderNo(parkingOrderEntity.getOrderNo());
        responseObserver.onNext(responseBuild.build());
        responseObserver.onCompleted();
    }
}
