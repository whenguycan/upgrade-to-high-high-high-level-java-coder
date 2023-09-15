package com.czdx.parkingcharge.service;

import com.czdx.grpc.lib.charge.*;
import com.czdx.parkingcharge.domain.pf.ChargeCouponInfo;
import com.czdx.parkingcharge.domain.pf.ParkingFee;
import com.czdx.parkingcharge.domain.pf.ParkingFeeItem;
import com.czdx.parkingcharge.system.exception.AssembleRPCRespException;
import com.czdx.parkingcharge.utils.ProtoJsonUtil;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * description: 计算停车费服务类
 * @author mingchenxu
 * @date 2023/2/25 10:55
 */
@Slf4j
@GrpcService
public class ChargeParkingFeeServiceImpl extends ParkingChargeServiceGrpc.ParkingChargeServiceImplBase {

    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public void testChargeParkingRule(TestChargeParkingRuleRequest request, StreamObserver<TestChargeParkingRuleResponse> responseObserver) {
        int ruleId = request.getRuleId();
        BigDecimal paringFee = calculateRuleParingFee(ruleId, request.getEntryTime(), request.getExitTime());
        TestChargeParkingRuleResponse parkingFee = TestChargeParkingRuleResponse.newBuilder().setPayableAmount(paringFee.doubleValue()).build();
        responseObserver.onNext(parkingFee);
        responseObserver.onCompleted();
    }

    /**
     *
     * description: 计算停车费用
     * @author mingchenxu
     * @date 2023/2/25 11:21
     * @param request
     * @param responseObserver
     */
    @Override
    public void chargeParkingFee(ChargeParkingFeeRequest request, StreamObserver<ChargeParkingFeeResponse> responseObserver) {
        String parkNo = request.getParkNo();
        String carNumber = request.getCarNumber();
        String carTypeCode = request.getCarTypeCode();
        // 优惠券信息
        List<CouponInfo> couponItemList = request.getCouponItemList();
        List<ChargeCouponInfo> cci = new ArrayList<>();
        List<CouponInfo> selectedCoupons = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(couponItemList)) {
            // 生成返回的优惠券信息（原路拷贝）
            couponItemList.forEach(e -> cci.add(new ChargeCouponInfo(e)));
            // 获取已选择的优惠券
            selectedCoupons = couponItemList.stream()
                    .filter(e -> e.getCanUse() && e.getSelected())
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(selectedCoupons)) {
                // 默认使用面额最大的优惠券
                Optional<CouponInfo> sci = couponItemList.stream()
                        .filter(CouponInfo::getCanUse).max(Comparator.comparingDouble(CouponInfo::getCouponValue));
                if (sci.isPresent()) {
                    selectedCoupons.add(sci.get());
                    // 设置选择的优惠券
                    cci.forEach(e -> {
                        if (e.getCouponCode().equals(sci.get().getCouponCode())) {
                            e.setSelected(true);
                        }
                    });
                }
            }

        }

        List<ParkingItem> parkingItems = request.getParkingItemList();
        // 计算分段停车费用
        List<ParkingFeeItem> feeItems = calculateParingFee(parkNo, carNumber, carTypeCode, parkingItems);

        // 计算总金额
        BigDecimal payableAmount = feeItems.stream().map(ParkingFeeItem::getPayableAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        ParkingFee parkingFee = new ParkingFee();
        parkingFee.setParkNo(parkNo);
        parkingFee.setCarNumber(carNumber);
        parkingFee.setPayableAmount(payableAmount);
        parkingFee.setPayAmount(payableAmount);
        parkingFee.setFeeItems(feeItems);

        // todo 优惠券处理
        parkingFee.setCouponItems(cci);
        if (CollectionUtils.isNotEmpty(selectedCoupons)) {
            BigDecimal discountAmount = BigDecimal.ZERO;
            CouponInfo couponInfo = selectedCoupons.get(0);
            // 金额券
            if (couponInfo.getCouponType() == 1) {
                discountAmount = discountAmount.add(BigDecimal.valueOf(couponInfo.getCouponValue()));
            }
            // 设置优惠金额
            parkingFee.setDiscountAmount(discountAmount);
            // 设置实付金额
            BigDecimal payAmount = payableAmount.subtract(discountAmount);
            parkingFee.setPayAmount(payAmount.compareTo(BigDecimal.ZERO) > 0 ? payAmount : BigDecimal.ZERO);
        }

        // 组装停车费回复对象
        ChargeParkingFeeResponse parkingFeeResponse = assembleParkingFeeResponse(parkingFee);
        responseObserver.onNext(parkingFeeResponse);
        responseObserver.onCompleted();
    }

    /**
     *
     * description: 计算规则的停车费用
     * @author mingchenxu
     * @date 2023/2/25 14:39
     * @param ruleId 规则ID
     * @param entryTime 入场时间
     * @param exitTime 出场时间
     * @return java.math.BigDecimal
     */
    private BigDecimal calculateRuleParingFee(int ruleId, String entryTime, String exitTime) {
        // todo 调用规则引擎计算费用
        return BigDecimal.TEN;
    }

    /**
     *
     * description: 计算停车费用
     * @author mingchenxu
     * @date 2023/2/25 11:25
     * @param parkNo
     * @param carNumber
     * @param carTypeCode
     * @param parkingItems
     * @return java.util.List<com.czdx.parkingcharge.domain.pf.ParkingFeeItem>
     */
    private List<ParkingFeeItem> calculateParingFee(String parkNo, String carNumber,
                                                           String carTypeCode, List<ParkingItem> parkingItems) {
        // todo 调用规则引擎计算费用
        List<ParkingFeeItem> feeItems = parkingItems.stream().map(e -> {
            String entryTime = e.getEntryTime();
            String exitTime = e.getExitTime();
            int parkingMinutes = (int)ChronoUnit.MINUTES.between(
                    LocalDateTime.parse(entryTime, DEFAULT_TIME_FORMATTER),
                    LocalDateTime.parse(exitTime, DEFAULT_TIME_FORMATTER));
            ParkingFeeItem item = new ParkingFeeItem();
            item.setParkFieldId(e.getParkFieldId());
            item.setEntryTime(entryTime);
            item.setExitTime(exitTime);
            item.setParkingTime(parkingMinutes);
            item.setPayableAmount(BigDecimal.valueOf(5));
            return item;
        }).collect(Collectors.toList());
        return feeItems;
    }

    /**
     *
     * description: 组装停车费回复对象
     * @author mingchenxu
     * @date 2023/2/25 11:20
     * @param parkingFee 停车费用
     * @return com.czdx.grpc.lib.charge.ChargeParkingFeeResponse
     */
    private ChargeParkingFeeResponse assembleParkingFeeResponse(ParkingFee parkingFee) {
        // 组装返回对象
        ChargeParkingFeeResponse.Builder responseBuilder = ChargeParkingFeeResponse.newBuilder();
        try {
            ProtoJsonUtil.toProtoBean(responseBuilder, parkingFee);
        } catch (IOException e) {
            log.error("组装停车费用异常，{}", e.getMessage(), e);
            throw new AssembleRPCRespException(e.getMessage());
        }
        return responseBuilder.build();
    }
}
