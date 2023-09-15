package com.ruoyi.project.parking.service;

import com.czdx.grpc.lib.lot.*;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.merchant.service.ITCouponDetailService;
import com.ruoyi.project.parking.domain.vo.*;
import com.ruoyi.project.parking.enums.CouponStatusEnum;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    // 错误状态码 200 正常
    static final String STATUS_OK = "200";

    /**
     * 创建lot的订单
     *
     * @param memberId  会员id
     * @param parkNo    停车场编号
     * @param carNumber 车牌号
     */
    public Pair<VehicleParkOrderVO,List<ParkingOrderCouponVO>> createLotParkingOrder(Long memberId, String parkNo, String carNumber, String passageNo, List<ParkingOrderCouponVO> couponList) {
        VehicleParkOrderVO vehicleParkOrderVO = null;
        List<ParkingOrderCouponVO> couponListPicked = new ArrayList<>();
        try {
            CreateLotOrderRequest.Builder request = CreateLotOrderRequest.newBuilder()
                    .setMemberId(memberId.toString())
                    .setParkNo(parkNo)
                    .setCarNumber(carNumber)
                    .setPassageNo(passageNo);
            // 无优惠券时候，默认查询用户可用优惠券
            if (StringUtils.isNotEmpty(couponList)) {
                // 查询会员优惠券
                List<MemberCouponVO> MemberCouponVOList = tCouponDetailService.selectMemberCouponVOList(memberId, CouponStatusEnum.ALLOCATED).getLeft();
                if (StringUtils.isNotEmpty(MemberCouponVOList)) {
                    request.addAllCouponList(
                            MemberCouponVOList.stream().map(m ->
                                            ParkingOrder.CouponInfo.newBuilder()
                                            .setCouponCode(m.getCouponCode())
                                            .setCouponMold(Integer.parseInt(m.getCouponMold().getValue()))
                                            .setCouponType(Integer.parseInt(m.getCouponType().getValue()))
                                            .setCouponValue(m.getCouponValue().intValue())
                                            .setChoosed(true)
                                            .setCanUse(true)
                                            .build()
                            ).toList());
                }
            } else {
                // 校验优惠券有效性
                if(!tCouponDetailService.verifyCouponBatchByCouponCode(couponList.stream().map(ParkingOrderCouponVO::getCouponCode).toList())){
                    throw new ServiceException("当前使用的优惠券已失效");
                };
                request.addAllCouponList(couponList.stream().map(m ->
                        ParkingOrder.CouponInfo.newBuilder()
                                .setCouponCode(m.getCouponCode())
                                .setCouponMold(m.getCouponMold())
                                .setCouponType(m.getCouponType())
                                .setCouponValue(m.getCouponValue())
                                .build()
                ).toList());
            }

            ParkingOrderReponse reponse = lotParkingServiceBlockingStub.createParkingOrder(request.build());
            if (STATUS_OK.equals(reponse.getStatus())) {
                LotParkingOrder lotParkingOrder = reponse.getOrderData();
                vehicleParkOrderVO = ProtoJsonUtil.toPojoBean(VehicleParkOrderVO.class, lotParkingOrder);
                couponListPicked = reponse.getCouponlistList().stream().map(m -> {
                    try {
                        return ProtoJsonUtil.toPojoBean(ParkingOrderCouponVO.class, m);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
            }
        } catch (StatusRuntimeException e) {
            log.error("FAILED with " + e.getStatus().getCode().name());
            throw new ServiceException(e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("FAILED with " + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return new ImmutablePair<>(vehicleParkOrderVO,couponListPicked) ;
    }

    /**
     * 查询 lot 停车订单
     *
     * @param orderNo 订单编号
     */
    public VehicleParkOrderVO queryParkingOrderByOrderNo(String orderNo) {
        VehicleParkOrderVO vehicleParkOrderVO = null;
        try {
            ParkingOrder.OrderDetailRequest.Builder request = ParkingOrder.OrderDetailRequest.newBuilder()
                    .setOrderNo(orderNo);
            ParkingOrderReponse reponse = lotParkingServiceBlockingStub.queryParkingOrderByOrderNo(request.build());
            if (STATUS_OK.equals(reponse.getStatus())) {
                LotParkingOrder lotParkingOrder = reponse.getOrderData();
                vehicleParkOrderVO = ProtoJsonUtil.toPojoBean(VehicleParkOrderVO.class, lotParkingOrder);
            }
        } catch (StatusRuntimeException e) {
            log.error("FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("FAILED with " + e.getMessage());
        }
        return vehicleParkOrderVO;
    }

    /**
     * 查询历史订单记录
     *
     * @param memberId  会员id
     * @param carNumber 车牌号
     */
    public List<HistoryParkingOrderVO> queryHistoryParkingOrder(String memberId, String carNumber, Integer pageNum, Integer pageSize) {
        List<HistoryParkingOrderVO> list = new ArrayList<>();
        try {
            HistoryParkingOrderRequest.Builder request = HistoryParkingOrderRequest.newBuilder()
                    .setMemberId(memberId)
                    .setCarNumber(carNumber)
                    .setPageNum(pageNum)
                    .setPageSize(pageSize);
            HistoryParkingOrderReponse reponse = lotParkingServiceBlockingStub.queryHistoryParkingOrder(request.build());
            if (!STATUS_OK.equals(reponse.getStatus())) {
                throw new ServiceException(reponse.getMess());
            }
            HistoryParkingOrderReponseVO historyParkingOrderReponseVO = ProtoJsonUtil.toPojoBean(HistoryParkingOrderReponseVO.class, reponse);
            list = historyParkingOrderReponseVO.getRows();
        } catch (StatusRuntimeException e) {
            log.error("FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("FAILED with " + e.getMessage());
        }
        return list;
    }

    /**
     * 查询用户 停车使用过的车牌号
     *
     * @param memberId 会员id
     */
    public List<String> queryCarNumberHistoryUsedByMemberId(Long memberId) {
        List<String> list = new ArrayList<>();
        try {
            QueryHistoryCarNumberRequest.Builder request = QueryHistoryCarNumberRequest.newBuilder()
                    .setMemberId(memberId.toString());
            QueryHistoryCarNumberReponse reponse = lotParkingServiceBlockingStub.queryHistoryCarNumber(request.build());
            if (!STATUS_OK.equals(reponse.getStatus())) {
                throw new ServiceException(reponse.getMess());
            }
            QueryHistoryCarNumberReponseVO queryHistoryCarNumberReponseVO = ProtoJsonUtil.toPojoBean(QueryHistoryCarNumberReponseVO.class, reponse);
            if (StringUtils.isNotEmpty(queryHistoryCarNumberReponseVO.getData())) {
                list = queryHistoryCarNumberReponseVO.getData();
            }
        } catch (StatusRuntimeException e) {
            log.error("FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("FAILED with " + e.getMessage());
        }
        return list;
    }
}
