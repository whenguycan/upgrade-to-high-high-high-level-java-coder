package com.czdx.parkingcharge.service;

import com.czdx.grpc.lib.charge.*;
import com.czdx.parkingcharge.common.constants.BUStr;
import com.czdx.parkingcharge.common.enums.ParkEnums;
import com.czdx.parkingcharge.domain.ParkChargeScheme;
import com.czdx.parkingcharge.domain.custom.RegularCarCustom;
import com.czdx.parkingcharge.domain.pf.ChargeCouponInfo;
import com.czdx.parkingcharge.domain.pf.ParkingFee;
import com.czdx.parkingcharge.domain.pf.ParkingFeeDiscount;
import com.czdx.parkingcharge.domain.pf.ParkingFeeItem;
import com.czdx.parkingcharge.domain.pr.ParkRuleModel;
import com.czdx.parkingcharge.domain.pr.ParkingRecord;
import com.czdx.parkingcharge.domain.pr.ParkingRuleEnums;
import com.czdx.parkingcharge.system.exception.AssembleRPCRespException;
import com.czdx.parkingcharge.utils.ProtoJsonUtil;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * description: 计算停车费服务类
 * @author mingchenxu
 * @date 2023/2/25 10:55
 */
@RequiredArgsConstructor
@Slf4j
@GrpcService
public class ChargeParkingFeeServiceImpl extends ParkingChargeServiceGrpc.ParkingChargeServiceImplBase {

    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern(BUStr.CHARGE_DATE_TIME_FORMATTER_STR);

    private final ParkChargeSchemeService parkChargeSchemeService;
    private final ParkChargeRuleService parkChargeRuleService;

    private final ParkChargeRelationVehicleService parkChargeRelationVehicleService;

    private final ParkChargeRelationHolidayService parkChargeRelationHolidayService;

    private final DroolsService droolsService;

    private final RegularCarService regularCarService;


    @Override
    public void testChargeParkingRule(TestChargeParkingRuleRequest request, StreamObserver<TestChargeParkingRuleResponse> responseObserver) {
        String parkNo = request.getParkNo();
        int ruleId = request.getRuleId();
        LocalDateTime entryTime = null;
        LocalDateTime exitTime = null;
        try {
            entryTime = LocalDateTime.parse(request.getEntryTime(), DEFAULT_TIME_FORMATTER);
            exitTime = LocalDateTime.parse(request.getExitTime(), DEFAULT_TIME_FORMATTER);
        } catch (Exception e) {
            log.error("格式化出入场时间异常：{}", e.getMessage());
            throw e;
        }
        // 计算一下算费时间
        StopWatch sw = new StopWatch(UUID.randomUUID().toString());
        sw.start("计费停车费");
        BigDecimal paringFee = calculateParingFee(parkNo, ruleId, true, entryTime, exitTime);
        sw.stop();
        log.info(sw.prettyPrint());
        TestChargeParkingRuleResponse parkingFee = TestChargeParkingRuleResponse.newBuilder().setPayableAmount(paringFee.doubleValue()).build();
        responseObserver.onNext(parkingFee);
        responseObserver.onCompleted();
    }

    /**
     *
     * description: 刷新计费规则
     * @author mingchenxu
     * @date 2023/3/21 08:53
     * @param request 请求
     * @param responseObserver 回复
     */
    @Override
    public void refreshParkingRule(RefreshParkingRuleRequest request, StreamObserver<RefreshParkingRuleResponse> responseObserver) {
        String parkNo = request.getParkNo();
        int ruleId = request.getRuleId();
        int sucNum = parkChargeRuleService.refreshChargeRule(parkNo, ruleId);
        RefreshParkingRuleResponse refreshParkingRuleResponse = RefreshParkingRuleResponse.newBuilder().setSucNum(sucNum).build();
        responseObserver.onNext(refreshParkingRuleResponse);
        responseObserver.onCompleted();
    }

    /**
     *
     * description: 刷新车场计费约束
     * @author mingchenxu
     * @date 2023/3/21 11:04
     * @param request 请求
     * @param responseObserver 返回
     */
    @Override
    public void refreshParkLotChargeScheme(RefreshParkLotChargeSchemeRequest request, StreamObserver<RefreshParkLotChargeSchemeResponse> responseObserver) {
        String parkNo = request.getParkNo();
        int sucNum = parkChargeSchemeService.refreshParkLotChargeScheme(parkNo);
        RefreshParkLotChargeSchemeResponse response = RefreshParkLotChargeSchemeResponse.newBuilder().setSucNum(sucNum).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     *
     * description: 刷新车场计费规则关系
     * @author mingchenxu
     * @date 2023/3/31 16:33
     * @param request
     * @param responseObserver
     */
    @Override
    public void refreshParkLotChargeRelation(RefreshParkLotChargeRelationRequest request, StreamObserver<RefreshParkLotChargeRelationResponse> responseObserver) {
        String parkNo = request.getParkNo();
        int sucNum = parkChargeRelationVehicleService.refreshParkLotChargeRelation(parkNo);
        RefreshParkLotChargeRelationResponse response = RefreshParkLotChargeRelationResponse.newBuilder().setSucNum(sucNum).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     *
     * description: 计算停车费用
     * @author mingchenxu
     * @date 2023/2/25 11:21
     * @param request 请求
     * @param responseObserver 返回
     */
    @Override
    public void chargeParkingFee(ChargeParkingFeeRequest request, StreamObserver<ChargeParkingFeeResponse> responseObserver) {
        String parkNo = request.getParkNo();
        String carNumber = request.getCarNumber();
        String carTypeCode = request.getCarTypeCode();
        // 停车信息
        List<ParkingItem> parkingItems = request.getParkingItemList();
        // 优惠券信息
        List<CouponInfo> couponItemList = request.getCouponItemList();
        // 是否为切换优惠券Flag
        boolean switchCoupon = request.getSwitchCouponFlag();

        ParkingFee parkingFee = new ParkingFee();

        // 添加一个任务计时监听
        log.info("计算停车费，车场[{}]，车牌[{}]", parkNo, carNumber);
        StopWatch sw = new StopWatch(UUID.randomUUID().toString());
        try {
            // 转换为停车记录
            sw.start("转换停车记录");
            List<ParkingFeeItem> feeItems = new ArrayList<>();
            for(ParkingItem item : parkingItems) {
                try {
                    LocalDateTime entryTime = LocalDateTime.parse(item.getEntryTime(), DEFAULT_TIME_FORMATTER);
                    LocalDateTime exitTime = LocalDateTime.parse(item.getExitTime(), DEFAULT_TIME_FORMATTER);
                    ParkingFeeItem pft = new ParkingFeeItem();
                    pft.setParkFieldId(item.getParkFieldId());
                    pft.setEntryTime(item.getEntryTime());
                    pft.setExitTime(item.getExitTime());
                    pft.setEntryTimeLDT(entryTime);
                    pft.setExitTimeLDT(exitTime);
                    pft.setParkingTime((int) Duration.between(entryTime, exitTime).toMinutes());
                    feeItems.add(pft);
                } catch (Exception ex) {
                    log.error("转换停车记录异常：{}", ex.getMessage());
                    throw new RuntimeException(ex);
                }
            }
            sw.stop();

            // 获取车场规则模型
            sw.start("计算停车应付费用");
            ParkRuleModel prm = getParkRuleModel(parkNo, carNumber, carTypeCode);
            // 计算分段停车费用
            calculateParingFee(prm, feeItems);
            sw.stop();

            // 计算总金额
            BigDecimal payableAmount = feeItems.stream().map(ParkingFeeItem::getPayableAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            parkingFee.setParkNo(parkNo);
            parkingFee.setCarNumber(carNumber);
            // 设置车类型标识
            parkingFee.setCarCategory(prm.getVehicleCategory());
            // 金额
            parkingFee.setPayableAmount(payableAmount);
            parkingFee.setPayAmount(payableAmount);
            // 计费明细
            parkingFee.setFeeItems(feeItems);

            // 应付金额需要大于0且优惠券列表不为空，才需要进行优惠券处理
            sw.start("优惠券处理");
            if (payableAmount.compareTo(BigDecimal.ZERO) > 0 && CollectionUtils.isNotEmpty(couponItemList)) {
                // 优惠券信息
                ParkingFeeDiscount discount = handleCoupon(parkNo, payableAmount, prm, feeItems, couponItemList, switchCoupon);
                parkingFee.setCouponItems(discount.getCouponInfos());
                // 设置优惠金额
                parkingFee.setDiscountAmount(discount.getDiscountAmount());
                // 设置实付金额
                BigDecimal payAmount = payableAmount.subtract(discount.getDiscountAmount());
                parkingFee.setPayAmount(payAmount.compareTo(BigDecimal.ZERO) > 0 ? payAmount : BigDecimal.ZERO);
            }
            sw.stop();
        } catch (Exception e) {
            log.error("计算停车费异常，车场[{}]，车牌[{}], 异常信息[{}]", parkNo, carNumber, e.getMessage(), e);
        } finally {
            log.info(sw.prettyPrint());
            log.info("车场[{}]，车牌[{}]-计算停车费结果：应付金额：{}，优惠金额：{}，实付金额：{}",
                    parkNo, carNumber, parkingFee.getPayableAmount(), parkingFee.getDiscountAmount(), parkingFee.getPayAmount());
            // 组装停车费回复对象
            ChargeParkingFeeResponse parkingFeeResponse = assembleParkingFeeResponse(parkingFee);
            responseObserver.onNext(parkingFeeResponse);
            responseObserver.onCompleted();
        }

    }

    /**
     *
     * description: 计算折扣金额
     * @author mingchenxu
     * @date 2023/4/7 15:57
     * @param parkNo 车场编号
     * @param payableAmount 应付金额
     * @param parkingFeeItems 停车记录
     * @param couponItemList 优惠券列表
     * @return com.czdx.parkingcharge.domain.pf.ParkingFeeDiscount
     */
    private ParkingFeeDiscount handleCoupon(String parkNo, BigDecimal payableAmount, ParkRuleModel prm,
                                            List<ParkingFeeItem> parkingFeeItems, List<CouponInfo> couponItemList, boolean switchCouponFlag) {
        // 优惠券信息
        List<ChargeCouponInfo> cci = new ArrayList<>();
        // 生成返回的优惠券信息（原路拷贝）
        couponItemList.forEach(e -> cci.add(new ChargeCouponInfo(e)));
        // 获取已选择的优惠券
        List<CouponInfo> selectedCoupons = couponItemList.stream()
                .filter(e -> e.getCanUse() && e.getSelected())
                .collect(Collectors.toList());
        // 如果不是主动切换优惠券，且已选优惠券为空，则设置默认优惠券，策略：未选择优惠券，默认使用面额最大的优惠券
        if (!switchCouponFlag && CollectionUtils.isEmpty(selectedCoupons)) {
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
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(selectedCoupons)) {
            // 区分金额和时间优惠券
            List<CouponInfo> amountCoupons = new ArrayList<>();
            List<CouponInfo> timeCoupons = new ArrayList<>();
            selectedCoupons.forEach(e -> {
                if (e.getCouponType() == ParkingRuleEnums.CouponType.AMOUNT_COUPON.getValue()) {
                    amountCoupons.add(e);
                } else {
                    timeCoupons.add(e);
                }
            });
            // 金额券处理
            if (CollectionUtils.isNotEmpty(amountCoupons)) {
                int amountDiscount = amountCoupons.stream().mapToInt(CouponInfo::getCouponValue).sum();
                log.info("使用金额优惠券-优惠金额：{}", amountDiscount);
                discountAmount = discountAmount.add(BigDecimal.valueOf(amountDiscount));
            }
            // 时间券处理
            if (CollectionUtils.isNotEmpty(timeCoupons)) {
                int couponMinutes = timeCoupons.stream().mapToInt(CouponInfo::getCouponValue).sum() * 60;
                // 时间券，计算使用优惠券后的金额
                discountAmount = computeCouponDiscountAmount(parkNo, payableAmount, couponMinutes, prm, parkingFeeItems);
                log.info("使用时刻优惠券-优惠金额：{}", discountAmount);
            }
        }
        return new ParkingFeeDiscount(discountAmount, cci);
    }

    /**
     *
     * description: 计算优惠券折扣金额
     * @author mingchenxu
     * @date 2023/4/7 15:46
     * @param parkNo
     * @return java.math.BigDecimal
     */
    private BigDecimal computeCouponDiscountAmount(String parkNo, BigDecimal payableAmount, int couponMinutes,
                                                   ParkRuleModel prm, List<ParkingFeeItem> parkingFeeItems) {
        // 1.先计算总停车时长，如果总停车时长小于等于优惠分钟数，则优惠金额就等于应付金额
        int totalParkingMinutes = parkingFeeItems.stream()
                .filter(p -> p.getParkingTime() > 0)
                .mapToInt(ParkingFeeItem::getParkingTime)
                .sum();
        if (totalParkingMinutes <= couponMinutes) {
            log.info("时间优惠劵停车时长大于实际停车时长，直接返回应付金额作为优惠金额！");
            return payableAmount;
        }
        BigDecimal discountAmount = BigDecimal.ZERO;

        // 2.获取时间券使用方式
        Optional<ParkChargeScheme> chargeScheme = parkChargeSchemeService.getChargeScheme(parkNo);
        if (chargeScheme.isPresent()) {
            // 3.生成新的停车记录，用于二次算费
            String tcUseWay = chargeScheme.get().getTcUseWay();
            String tcFreeTimeFlag = chargeScheme.get().getTcFreeTimeFlag();
            // 移动入场还是出场
            boolean moveEntryTime = tcUseWay.equals(ParkingRuleEnums.CouponUseWay.ENTER_TIME_MOVE.getValue());
            List<ParkingFeeItem> newParkingFeeItems = moveParingFeeItemsTime(parkingFeeItems, couponMinutes, moveEntryTime);
            // 若优惠券包含了免费时段，则计费无法使用免费时段
            if (tcFreeTimeFlag.equals(ParkingRuleEnums.CouponContainFreeTime.YES.getValue())) {
                prm.setUseFreeTime(false);
            }
            // 计算分段停车费用
            calculateParingFee(prm, newParkingFeeItems);

            // 计算总金额
            BigDecimal useCouponPayableAmount = newParkingFeeItems.stream().map(ParkingFeeItem::getPayableAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            discountAmount = payableAmount.subtract(useCouponPayableAmount);
        }
        return discountAmount;
    }

    /**
     *
     * description: 获取停车规则模型（未填充规则ID）
     * @author mingchenxu
     * @date 2023/3/30 17:08
     * @param parkNo 车场编号
     * @param carNumber 车牌
     * @param carTypeCode 车辆类型
     * @return com.czdx.parkingcharge.domain.pr.ParkRuleModel
     */
    private ParkRuleModel getParkRuleModel(String parkNo, String carNumber, String carTypeCode) {
        // 车类型处理（固定还是临时）
        ParkRuleModel prm = new ParkRuleModel(parkNo, carNumber, ParkEnums.CarVehicleCategory.LS.getValue(), carTypeCode);
        Optional<RegularCarCustom> regularCarInfo = regularCarService.getRegularCarInfo(parkNo, carNumber);
        if (regularCarInfo.isPresent()) {
            RegularCarCustom rcc = regularCarInfo.get();
            // 设置车类型为固定车
            prm.setVehicleCategory(ParkEnums.CarVehicleCategory.GD.getValue());
            // 设置固定车类型ID与分组ID
            prm.setRegularCategoryId(rcc.getCarCategoryId());
            prm.setRegularCategoryGroupId(rcc.getCategoryGroupId());
        }
        // 设置匹配规则前缀
        prm.setRulePrefix(BUStr.D_CAR_GET_PARKING_CHARGE_RULE_PREFIX + prm.getParkNo());
        return prm;
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
    private BigDecimal calculateParingFee(String parkNo, int ruleId, boolean useFreeTime,
                                          LocalDateTime entryTime, LocalDateTime exitTime) {
        // 调用规则引擎计算费用
        String rulePrefix = BUStr.D_PARKING_CHARGE_RULE_PREFIX + parkNo + "_" + ruleId;
        ParkingRecord pr = new ParkingRecord(parkNo, rulePrefix, useFreeTime, entryTime, exitTime);
        return droolsService.chargeParkingFeeEntrance(pr);
    }

    /**
     *
     * description: 计算停车费用
     * @author mingchenxu
     * @date 2023/2/25 11:25
     * @param prm 计费规则模型
     * @param parkingFees
     * @return java.util.List<com.czdx.parkingcharge.domain.pf.ParkingFeeItem>
     */
    private void calculateParingFee(ParkRuleModel prm, List<ParkingFeeItem> parkingFees) {
        // 遍历计算费用
        for(ParkingFeeItem parkingItem : parkingFees) {
            BigDecimal fee = BigDecimal.ZERO;
            if (parkingItem.getParkingTime() > 0) {
                // 通过规则模型匹配出计费规则
                Integer chargeRuleId = getRuleIdByParkRuleModel(prm, String.valueOf(parkingItem.getParkFieldId()), parkingItem.getExitTimeLDT());
                // 计算费用
                if (chargeRuleId != null) {
                    // 匹配到计费规则，才需要计费
                    fee = calculateParingFee(prm.getParkNo(), prm.getChargeRuleId(), prm.isUseFreeTime(), parkingItem.getEntryTimeLDT(), parkingItem.getExitTimeLDT());
                }
            }
            parkingItem.setPayableAmount(fee);
        }
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

    /**
     *
     * description: 通过车场规则模型获取规则ID
     * @author mingchenxu
     * @date 2023/3/31 13:12
     * @param prm
     * @param parkLot
     * @param exitTime
     * @return java.lang.Integer
     */
    private Integer getRuleIdByParkRuleModel(ParkRuleModel prm, String parkLot, LocalDateTime exitTime) {
        prm.setParkLotSign(parkLot);
        // 为了计算方便，只以出场时间那一刻对应的计费规则作为整体时段的计费规则 - by xmc 20230330
        // 查询出场时间是否为节假日，设置到匹配模型中去
        String dayHolidayType = parkChargeRelationHolidayService.getDayHolidayType(prm.getParkNo(), exitTime);
        prm.setHolidayType(dayHolidayType);
        Integer chargeRule = droolsService.getChargeRule(prm);
        log.info("匹配到计费规则ID：[{}]", chargeRule);
        return chargeRule;
    }

    /**
     *
     * description: 停车时段移动
     * @author mingchenxu
     * @date 2023/4/10 14:29
     * @param parkingFeeItems 停车明细
     * @param couponMinutes 优惠总分钟数
     * @return java.util.List<com.czdx.parkingcharge.domain.pf.ParkingFeeItem>
     */
    private List<ParkingFeeItem> moveParingFeeItemsTime(List<ParkingFeeItem> parkingFeeItems, int couponMinutes, boolean moveEntryTime) {
        int remainderMins = couponMinutes;
        List<ParkingFeeItem> newFeeItems = new ArrayList<>();
        // 移动入场时间处理
        if (moveEntryTime) {
            for (ParkingFeeItem item : parkingFeeItems) {
                if (remainderMins > 0) {
                    // 存在剩余时间，扣除本时段停车时间
                    remainderMins = remainderMins - item.getParkingTime();
                    if (remainderMins < 0) {
                        // 扣除后时间小于0，则需要新建记录，设置新的入场时间与停车时长
                        item.setParkingTime(-remainderMins);
                        item.setEntryTimeLDT(item.getExitTimeLDT().plusMinutes(remainderMins));
                        newFeeItems.add(item);
                    }
                } else {
                    newFeeItems.add(item);
                }
            }
        } else {
            // 后移
            for (int i = (parkingFeeItems.size()-1); i > 0; i--) {
                ParkingFeeItem item = parkingFeeItems.get(i);
                if (remainderMins > 0) {
                    // 存在剩余时间，扣除本时段停车时间
                    remainderMins = remainderMins - item.getParkingTime();
                    if (remainderMins < 0) {
                        // 扣除后时间小于0，则需要新建记录，设置新的出场时间与停车时长
                        item.setParkingTime(-remainderMins);
                        item.setExitTimeLDT(item.getEntryTimeLDT().plusMinutes(remainderMins));
                        newFeeItems.add(item);
                    }
                } else {
                    newFeeItems.add(item);
                }
            }
        }
        return newFeeItems;
    }
}
