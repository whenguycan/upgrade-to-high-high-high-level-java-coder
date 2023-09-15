package com.ruoyi.project.parking.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.czdx.grpc.lib.lot.*;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.dahuatech.hutool.json.JSONUtil;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.project.merchant.service.ITCouponDetailService;
import com.ruoyi.project.parking.domain.vo.*;
import com.ruoyi.project.parking.enums.SettleTypeEnum;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 调用 Lot停车订单 grpc 服务
 */
@Slf4j
@Service("lotParkingGrpcService")
public class LotParkingGrpcServiceImpl {

    @GrpcClient("parking-lot-server")
    private LotParkingServiceGrpc.LotParkingServiceBlockingStub lotParkingServiceBlockingStub;

    @Autowired
    private ITCouponDetailService tCouponDetailService;

    @Autowired
    private RedisCache redisCache;

    // 错误状态码 200 正常
    static final String STATUS_OK = "200";
    static final String CODE_NO_CREATE = "201";

    /**
     * 构建 创建订单、重计算金额 请求消息体
     *
     * @param parkNo       停车场编号
     * @param carNumber    车牌号
     * @param passageNo    通道编号
     * @param couponList   优惠券列表 空 默认则查询当前车牌下可用的优惠券
     * @param orderNo      订单号 有则重计算金额
     * @param notUseCoupon 不使用优惠券被标记 默认false=> 使用优惠券
     */
    private CreateLotOrderRequest.Builder builderCreateOrReCalcParam(String parkNo, String carNumber, String passageNo, List<ParkingOrderCouponVO> couponList, String orderNo, boolean notUseCoupon) {
        CreateLotOrderRequest.Builder request = CreateLotOrderRequest.newBuilder()
                .setParkNo(parkNo)
                .setCarNumber(carNumber)
                .setPassageNo(passageNo);
        if (SecurityUtils.isLogin()) {
            request.setMemberId(SecurityUtils.getUserId().toString());
        }
        if (StringUtils.isNotEmpty(orderNo)) {
            request.setOrderNo(orderNo);
        }
        // 使用优惠券 默认使用 最优优惠券
        if (!notUseCoupon) {
            // 无优惠券时候，默认查询用户可用优惠券
            if (StringUtils.isEmpty(couponList)) {
                // 查询会员优惠券
                List<MemberCouponVO> MemberCouponVOList = tCouponDetailService.selectMemberCouponVOListByCarNumber(carNumber);
                if (StringUtils.isNotEmpty(MemberCouponVOList)) {
                    request.addAllCouponList(
                            MemberCouponVOList.stream().map(m ->
                                    ParkingOrder.CouponInfo.newBuilder()
                                            .setCouponCode(m.getCouponCode())
                                            .setCouponMold(Integer.parseInt(m.getCouponMold().getValue()))
                                            .setCouponType(Integer.parseInt(m.getCouponType().getValue()))
                                            .setCouponValue(m.getCouponValue().intValue())
                                            .setChoosed(false)
                                            .setCanUse(false)
                                            .build()
                            ).toList());
                }
            } else {
                // 校验优惠券有效性
                if (!tCouponDetailService.verifyCouponBatchByCouponCode(couponList.stream().map(ParkingOrderCouponVO::getCouponCode).toList())) {
                    throw new ServiceException("当前使用的优惠券已失效");
                }
                request.addAllCouponList(couponList.stream().map(m ->
                        ParkingOrder.CouponInfo.newBuilder()
                                .setCouponCode(m.getCouponCode())
                                .setCouponMold(m.getCouponMold())
                                .setCouponType(m.getCouponType())
                                .setCouponValue(m.getCouponValue())
                                .build()
                ).toList());
            }
        }
        return request;

    }

    /**
     * 创建/重计算 lot的订单
     *
     * @param parkNo       停车场编号
     * @param carNumber    车牌号
     * @param passageNo    通道编号
     * @param couponList   优惠券列表 空 默认则查询当前车牌下可用的优惠券
     * @param orderNo      订单号 有则重计算金额
     * @param notUseCoupon 不使用优惠券被标记 默认false=> 使用优惠券
     */
    public Triple<Boolean, VehicleParkOrderVO, List<ParkingOrderCouponShowVO>> createOrReCalcLotParkingOrder(String parkNo, String carNumber, String passageNo, List<ParkingOrderCouponVO> couponList, String orderNo, boolean notUseCoupon) {
        log.info("接受传参parkNo={},carNumber={},passageNo={},couponList={},orderNo={},notUseCoupon={}", parkNo, carNumber, passageNo, couponList, orderNo, notUseCoupon);
        List<ParkingOrderCouponShowVO> couponListPicked = new ArrayList<>();

        // 有岗亭编码，则手动查询缓存中的车牌号
        if (StringUtils.isNotEmpty(passageNo)) {
            String carNumberKey = CacheConstants.PARKNO_PASSAGE_KEY +
                    parkNo +
                    "_" +
                    passageNo;
            // 检查 岗亭码支付时 车辆是否到岗亭
            carNumber = redisCache.getCacheObject(carNumberKey);
            if (StringUtils.isEmpty(carNumber)) {
                throw new ServiceException("当前车辆不在通道内!");
            }
            Triple<Boolean, VehicleParkOrderVO, List<ParkingOrderCouponShowVO>> triple = getLotParkingOrderByRedis(parkNo, carNumber);
            if (uniformCoupon(triple.getRight(), couponList, notUseCoupon)) {
                log.info("读取缓存-岗亭订单");
                if (triple.getLeft() == null) {
                    throw new ServiceException("当前闸道内无对应车 " + carNumber + " 信息，请重新进入闸道");
                } else {
                    return triple;
                }
            } else {
                log.info("重计算-岗亭订单");
                return getLotParkingOrderByGrpc(SettleTypeEnum.PAVILION_CODE_PAY, parkNo, carNumber, passageNo, couponList, orderNo, notUseCoupon);
            }
        } else {
            log.info("创建/重计算-预支付订单");
            return getLotParkingOrderByGrpc(SettleTypeEnum.PREPAY_CODE_PAY, parkNo, carNumber, passageNo, couponList, orderNo, notUseCoupon);
        }
    }

    /**
     * 检查是优惠券使用情况 否一致
     *
     * @param couponListRedis 缓存中的优惠券
     * @param couponList      传参中的优惠券
     * @param notUseCoupon    不使用优惠券被标记 默认false=> 使用优惠券
     */
    private boolean uniformCoupon(List<ParkingOrderCouponShowVO> couponListRedis, List<ParkingOrderCouponVO> couponList, boolean notUseCoupon) {
        // 判断是否 岗亭是否切换优惠券（缓存的优惠券 与 传参是否一致）
        // 判断是否使用优惠券
        if (notUseCoupon) {
            return StringUtils.isEmpty(couponListRedis);
        } else {
            // 使用优惠券
            String redisCouponCodes = "";
            String curCouponCodes = "";
            if (StringUtils.isNotEmpty(couponListRedis)) {
                redisCouponCodes = String.join(",", couponListRedis.stream().sorted(Comparator.comparing(ParkingOrderCouponShowVO::getCouponCode)).filter(ParkingOrderCouponShowVO::isChoosed).map(ParkingOrderCouponShowVO::getCouponCode).toList());
            }
            if (StringUtils.isNotEmpty(couponList)) {
                curCouponCodes = String.join(",", couponList.stream().sorted(Comparator.comparing(ParkingOrderCouponVO::getCouponCode)).filter(ParkingOrderCouponVO::isChoosed).map(ParkingOrderCouponVO::getCouponCode).toList());
            }
            return redisCouponCodes.equals(curCouponCodes);
        }
    }

    /**
     * 调用 grpc
     *
     * @param type         创建订单类型
     * @param parkNo       停车场编号
     * @param carNumber    车牌号
     * @param passageNo    通道编号
     * @param couponList   优惠券列表 空 默认则查询当前车牌下可用的优惠券
     * @param orderNo      订单号 有则重计算金额
     * @param notUseCoupon 不使用优惠券被标记 默认false=> 使用优惠券
     */
    private Triple<Boolean, VehicleParkOrderVO, List<ParkingOrderCouponShowVO>> getLotParkingOrderByGrpc(SettleTypeEnum type, String parkNo, String carNumber, String passageNo, List<ParkingOrderCouponVO> couponList, String orderNo, boolean notUseCoupon) {
        // 是否创建订单
        boolean flagCreateOrderNo = true;
        VehicleParkOrderVO vehicleParkOrderVO = null;
        List<ParkingOrderCouponShowVO> couponListPicked = new ArrayList<>();
        CreateLotOrderRequest.Builder request = builderCreateOrReCalcParam(parkNo, carNumber, passageNo, couponList, orderNo, notUseCoupon);
        try {
            log.info("构建lot-grpc传参:{}", JSONObject.toJSONString(request.build()));
            ParkingOrderReponse reponse = lotParkingServiceBlockingStub.createParkingOrder(request.build());
            log.info("接受lot-grpc响应:{}", JSONObject.toJSONString(reponse));
            if (STATUS_OK.equals(reponse.getStatus())) {
                LotParkingOrder lotParkingOrder = reponse.getOrderData();
                vehicleParkOrderVO = ProtoJsonUtil.toPojoBean(VehicleParkOrderVO.class, lotParkingOrder);
                couponListPicked = buildCouponShowVO(reponse.getCouponlistList());
            } else if (CODE_NO_CREATE.equals(reponse.getStatus())) {
                if (SettleTypeEnum.PREPAY_CODE_PAY.getValue() == type.getValue()) {
                    // 预支付创建订单 15min 内 不返回订单信息
                    flagCreateOrderNo = false;
                } else {
                    // 岗亭订单 需要返回订单信息
                    LotParkingOrder lotParkingOrder = reponse.getOrderData();
                    vehicleParkOrderVO = ProtoJsonUtil.toPojoBean(VehicleParkOrderVO.class, lotParkingOrder);
                    couponListPicked = buildCouponShowVO(reponse.getCouponlistList());
                }
            } else {
                log.error("lot-grpc 异常 {}", reponse.getMess());
                throw new ServiceException(reponse.getMess());
            }
        } catch (Exception e) {
            log.error("getLotParkingOrderByGrpc 异常{} ", e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return new ImmutableTriple<>(flagCreateOrderNo, vehicleParkOrderVO, couponListPicked);
    }

    /**
     * 读取缓存
     *
     * @param parkNo    停车场编号
     * @param carNumber 车牌号
     */
    private Triple<Boolean, VehicleParkOrderVO, List<ParkingOrderCouponShowVO>> getLotParkingOrderByRedis(String parkNo, String carNumber) {
        VehicleParkOrderVO vehicleParkOrderVO = null;
        List<ParkingOrderCouponShowVO> couponListPicked = new ArrayList<>();
        // 对于不切换 优惠券的岗亭订单 使用 缓存中的岗亭订单，否则重计算
        String parkPavilionOrderKey = CacheConstants.PARKING_ORDER + parkNo + "_" + carNumber;
        String parkPavilionOrderCouponKey = parkPavilionOrderKey + "_coupon";
        JSONObject jsonObject = redisCache.getCacheObject(parkPavilionOrderKey);
        vehicleParkOrderVO = JSONObject.parseObject(JSONObject.toJSONString(jsonObject), VehicleParkOrderVO.class);
        JSONArray jsonObjectCoupon = redisCache.getCacheObject(parkPavilionOrderCouponKey);
        List<ParkingOrderCouponVO> listCoupon = JSON.parseArray(JSONObject.toJSONString(jsonObjectCoupon), ParkingOrderCouponVO.class);
        couponListPicked = buildCouponShowVO2(listCoupon);
        return new ImmutableTriple<>(true, vehicleParkOrderVO, couponListPicked);
    }


    /**
     * 查询优惠券展示信息
     *
     * @param list 优惠券
     */
    public List<ParkingOrderCouponShowVO> buildCouponShowVO(List<ParkingOrder.CouponInfo> list) {
        List<ParkingOrderCouponShowVO> couponList = new ArrayList<>();
        if (StringUtils.isNotEmpty(list)) {
            // 查询优惠券信息
            List<MemberCouponVO> couponShowList = tCouponDetailService.selectMemberCouponVOBatchByCode(list.stream().map(ParkingOrder.CouponInfo::getCouponCode).toList());
            Map<String, MemberCouponVO> couponMap = couponShowList.stream().collect(Collectors.toMap(MemberCouponVO::getCouponCode, e -> e, (v1, v2) -> v2));
            couponList = list.stream().map(m -> {
                ParkingOrderCouponShowVO couponShowVO = new ParkingOrderCouponShowVO();
                couponShowVO.setCouponCode(m.getCouponCode());
                couponShowVO.setChoosed(m.getChoosed());
                couponShowVO.setCoupon(couponMap.get(m.getCouponCode()));
                couponShowVO.setCanUse(m.getCanUse());
                return couponShowVO;
            }).toList();
        }
        return couponList;
    }

    /**
     * 查询优惠券展示信息
     *
     * @param list 优惠券
     */
    private List<ParkingOrderCouponShowVO> buildCouponShowVO2(List<ParkingOrderCouponVO> list) {
        List<ParkingOrderCouponShowVO> couponList = new ArrayList<>();
        if (StringUtils.isNotEmpty(list)) {
            // 查询优惠券信息
            List<MemberCouponVO> couponShowList = tCouponDetailService.selectMemberCouponVOBatchByCode(list.stream().map(ParkingOrderCouponVO::getCouponCode).toList());
            Map<String, MemberCouponVO> couponMap = couponShowList.stream().collect(Collectors.toMap(MemberCouponVO::getCouponCode, e -> e, (v1, v2) -> v2));
            couponList = list.stream().map(m -> {
                ParkingOrderCouponShowVO couponShowVO = new ParkingOrderCouponShowVO();
                couponShowVO.setCouponCode(m.getCouponCode());
                couponShowVO.setChoosed(m.isChoosed());
                couponShowVO.setCoupon(couponMap.get(m.getCouponCode()));
                couponShowVO.setCanUse(m.isCanUse());
                return couponShowVO;
            }).toList();
        }
        return couponList;
    }

    /**
     * 查询 lot 停车订单
     *
     * @param orderNo 订单编号
     */
    public Pair<VehicleParkOrderVO, List<ParkingOrderCouponShowVO>> queryParkingOrderByOrderNo(String orderNo) {
        log.info("接受传参:{}", orderNo);
        VehicleParkOrderVO vehicleParkOrderVO = null;
        List<ParkingOrderCouponShowVO> couponListPicked = new ArrayList<>();
        try {
            ParkingOrder.OrderDetailRequest.Builder request = ParkingOrder.OrderDetailRequest.newBuilder()
                    .setOrderNo(orderNo);
            ParkingOrderReponse reponse = lotParkingServiceBlockingStub.queryParkingOrderByOrderNo(request.build());
            log.info("接受 lot 返回值:{}", JSONObject.toJSONString(reponse));
            if (STATUS_OK.equals(reponse.getStatus())) {
                LotParkingOrder lotParkingOrder = reponse.getOrderData();
                vehicleParkOrderVO = ProtoJsonUtil.toPojoBean(VehicleParkOrderVO.class, lotParkingOrder);
                couponListPicked = buildCouponShowVO(reponse.getCouponlistList());
            } else {
                throw new ServiceException(reponse.getMess());
            }
        } catch (Exception e) {
            log.error("FAILED with " + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return new ImmutablePair<>(vehicleParkOrderVO, couponListPicked);
    }

    /**
     * 查询历史订单记录
     *
     * @param memberId  会员id
     * @param carNumber 车牌号
     */
    public HistoryParkingOrderReponseVO queryHistoryParkingOrder(String memberId, String carNumber, Integer pageNum, Integer pageSize) {
        log.info("接受传参:memberId={},carNumber={},pageNum={},pageSize={}", memberId, carNumber, pageNum, pageSize);
        try {
            HistoryParkingOrderRequest.Builder request = HistoryParkingOrderRequest.newBuilder()
                    .setMemberId(memberId)
                    .setCarNumber(carNumber)
                    .setPageNum(pageNum)
                    .setPageSize(pageSize);
            log.info("构建传参:{}", JSONUtil.toJsonStr(request.build()));
            HistoryParkingOrderReponse reponse = lotParkingServiceBlockingStub.queryHistoryParkingOrder(request.build());
            if (!STATUS_OK.equals(reponse.getStatus())) {
                throw new ServiceException(reponse.getMess());
            }
            return ProtoJsonUtil.toPojoBean(HistoryParkingOrderReponseVO.class, reponse);
        } catch (Exception e) {
            log.error("FAILED with " + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 查询用户 停车使用过的车牌号
     *
     * @param memberId 会员id
     */
    public List<String> queryCarNumberHistoryUsedByMemberId(Long memberId) {
        log.info("接受传参:memberId={}", memberId);
        List<String> list = new ArrayList<>();
        try {
            QueryHistoryCarNumberRequest.Builder request = QueryHistoryCarNumberRequest.newBuilder()
                    .setMemberId(memberId.toString());
            log.info("构建传参:{}", JSONUtil.toJsonStr(request.build()));
            QueryHistoryCarNumberReponse reponse = lotParkingServiceBlockingStub.queryHistoryCarNumber(request.build());
            if (!STATUS_OK.equals(reponse.getStatus())) {
                throw new ServiceException(reponse.getMess());
            }
            QueryHistoryCarNumberReponseVO queryHistoryCarNumberReponseVO = ProtoJsonUtil.toPojoBean(QueryHistoryCarNumberReponseVO.class, reponse);
            if (StringUtils.isNotEmpty(queryHistoryCarNumberReponseVO.getData())) {
                list = queryHistoryCarNumberReponseVO.getData();
            }
        } catch (Exception e) {
            log.error("FAILED with " + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return list;
    }
}
