package com.ruoyi.project.parking.service;

import com.alibaba.fastjson2.JSONObject;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrder.CreateOrderRequest;
import com.czdx.grpc.lib.order.ParkingOrder.searchOrderRequest;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.parking.domain.bo.AbnormalOrderStatisticsBO;
import com.ruoyi.project.parking.domain.vo.RefundOrderVO;
import com.ruoyi.project.parking.domain.vo.StatisticsSheetVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.*;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 访问 订单系统 grpc 服务
 */
@Slf4j
@Service("parkingOrderGrpcService")
public class ParkingOrderGrpcServiceImpl {

    @GrpcClient("parking-order-server")
    private ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub parkingOrderServiceBlockingStub;

    @Autowired
    private BPassageService bPassageService;

    // 错误状态码 200 正常
    static final String STATUS_OK = "200";
    static final String CODE_NO_CREATE = "201";

    /**
     * 创建停车订单
     *
     * @param param 创建参数
     */
    public Triple<Boolean, VehicleParkOrderVO, List<ParkingOrder.CouponInfo>> createParkingOrderGrpc(VehicleParkCreateOrderRequestVO param) {
        // 是否创建订单
        boolean flagCreateOrderNo = true;
        // 创建的订单信息
        VehicleParkOrderVO vehicleParkOrderVO = null;
        // 订单使用的优惠券信息
        List<ParkingOrder.CouponInfo> list = new ArrayList<>();
        try {
            CreateOrderRequest.Builder builder = CreateOrderRequest.newBuilder();
            ProtoJsonUtil.toProtoBean(builder, param);
            ParkingOrder.CreateOrderReponse createOrderReponse = parkingOrderServiceBlockingStub.createParkingOrder(builder.build());
            if (STATUS_OK.equals(createOrderReponse.getStatus())) {
                // 判断 不创建订单情况
                if (CODE_NO_CREATE.equals(createOrderReponse.getReasonCode())) {
                    flagCreateOrderNo = false;
                    // 岗亭这里正常不会出现这种情况，异常情况待处理(付完钱不走，15min又回到闸道)
                } else {
                    ParkingOrder.OrderDetail orderDetail = createOrderReponse.getOrderDetail();
                    vehicleParkOrderVO = ProtoJsonUtil.toPojoBean(VehicleParkOrderVO.class, orderDetail);
                    list = createOrderReponse.getCouponlistList();
                }
            }
        } catch (StatusRuntimeException e) {
            log.error("createParkingOrderGrpc 1 FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("createParkingOrderGrpc 2 FAILED with " + e.getMessage());
        }
        return new ImmutableTriple<>(flagCreateOrderNo, vehicleParkOrderVO, list);
    }

    /**
     * 创建停车订单 - 手动结算订单
     *
     * @param param 创建参数
     */
    public String createManualParkingOrderGrpc(ManualParkingOrderRequestVO param) {
        try {
            ParkingOrder.ManualParkingOrderRequest.Builder builder = ParkingOrder.ManualParkingOrderRequest.newBuilder();
            ProtoJsonUtil.toProtoBean(builder, param);
            log.info("手动创建订单参数{}", JSONObject.toJSONString(builder.build()));
            ParkingOrder.ManualParkingOrderReponse response = parkingOrderServiceBlockingStub.createManualParkingOrder(builder.build());
            log.info("接受手动创建订单返回值{}", JSONObject.toJSONString(response));
            if (STATUS_OK.equals(response.getStatus())) {
                return response.getOrderNo();
            } else {
                return StringUtils.EMPTY;
            }
        } catch (IOException e) {
            log.error("手动结算订单入参异常" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询停车订单信息 通过订单号
     *
     * @param orderNo 订单号
     */
    public VehicleParkOrderVO queryParkingOrderByOrderNo(String orderNo) {
        VehicleParkOrderVO vehicleParkOrderVO = null;
        try {
            ParkingOrder.OrderDetailRequest queryOrderRequest = ParkingOrder.OrderDetailRequest.newBuilder()
                    .setOrderNo(orderNo)
                    .build();
            ParkingOrder.CreateOrderReponse queryOrderReponse = parkingOrderServiceBlockingStub.getParkingOrderDetail(queryOrderRequest);
            if (STATUS_OK.equals(queryOrderReponse.getStatus())) {
                ParkingOrder.OrderDetail orderDetail = queryOrderReponse.getOrderDetail();
                vehicleParkOrderVO = ProtoJsonUtil.toPojoBean(VehicleParkOrderVO.class, orderDetail);
                vehicleParkOrderVO.setPassageName(bPassageService.selectPassageNameByPassageNo(vehicleParkOrderVO.getPassageNo()));
            }
        } catch (StatusRuntimeException e) {
            log.error("queryParkingOrderByOrderNo 1 FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("queryParkingOrderByOrderNo 2 FAILED with " + e.getMessage());
        }
        return vehicleParkOrderVO;
    }

    /**
     * 查询停车订单信息 通过订单号
     *
     * @param request 查询订单 请求
     */
    public ParkingOrder.CreateOrderReponse queryParkingOrderByOrderNo(ParkingOrder.OrderDetailRequest request) {
        try {
            return parkingOrderServiceBlockingStub.getParkingOrderDetail(request);
        } catch (StatusRuntimeException e) {
            log.error("queryParkingOrderByOrderNo 1 FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("queryParkingOrderByOrderNo2 FAILED with " + e.getMessage());
        }
        return null;
    }

    /**
     * 查询停车订单列表信息 通过订单号列表
     *
     * @param orderNoList 订单列表
     */
    public List<VehicleParkOrderVO> queryParkingOrderListByOrderNoList(List<String> orderNoList) {
        return orderNoList.stream().map(this::queryParkingOrderByOrderNo).toList();
    }

    /**
     * 完成停车订单
     *
     * @param orderNo 订单号
     * @return 操作是否成功
     */
    public boolean updateParkingOrderStatus(String orderNo) {
        boolean flag = false;
        try {
            ParkingOrder.ChangeOrderRequest changeOrderRequest = ParkingOrder.ChangeOrderRequest.newBuilder()
                    .setOrderNo(orderNo)
                    .build();
            ParkingOrder.CreateOrderReponse changeOrderReponse = parkingOrderServiceBlockingStub.changeOrderStatus(changeOrderRequest);
            flag = STATUS_OK.equals(changeOrderReponse.getStatus());
        } catch (StatusRuntimeException e) {
            log.error("updateParkingOrderStatus 1 FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("updateParkingOrderStatus 2 FAILED with " + e.getMessage());
        }
        return flag;
    }


    /**
     * 查询订单列表
     */
    public SearchOrderResponseVO queryOrderListWithPage(SearchOrderRequestVO param) {
        try {
            searchOrderRequest.Builder builder = searchOrderRequest.newBuilder();
            ProtoJsonUtil.toProtoBean(builder, param);
            ParkingOrder.searchOrderResponse response = parkingOrderServiceBlockingStub.searchOrder(builder.build());
            if (STATUS_OK.equals(response.getStatus())) {
                return ProtoJsonUtil.toPojoBean(SearchOrderResponseVO.class, response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 订单退款
     */
    public RefundOrderResponseVO refund(RefundOrderVO refundOrderVO) {
        try {
            ParkingOrder.RefundOrderRequestProto request = ParkingOrder.RefundOrderRequestProto.newBuilder()
                    .setOrderNo(refundOrderVO.getOrderNo())
                    .setReason(DictUtils.getDictLabel("refund_reason", refundOrderVO.getReason()))
                    .build();
            ParkingOrder.RefundOrderResponseProto response = parkingOrderServiceBlockingStub.refundOrder(request);
            return ProtoJsonUtil.toPojoBean(RefundOrderResponseVO.class, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SearchRefundOrderRecordsResponseVO queryRefundOrderListWithPage(SearchRefundOrderRecordsRequestVO param) {
        try {
            ParkingOrder.SearchRefundOrderRequestProto.Builder builder = ParkingOrder.SearchRefundOrderRequestProto.newBuilder();
            ProtoJsonUtil.toProtoBean(builder, param);
            ParkingOrder.SearchRefundOrderResponseProto response = parkingOrderServiceBlockingStub.searchRefundOrder(builder.build());
            if (STATUS_OK.equals(response.getStatus())) {
                return ProtoJsonUtil.toPojoBean(SearchRefundOrderRecordsResponseVO.class, response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * @apiNote 异常订单统计指标 （停车订单未支付数、停车订单支付成功数、停车订单支付总金额、停车订单优惠总金额、）
     * @author 琴声何来
     * @since 2023/4/25 10:51
     */
    public AbnormalOrderStatisticsBO abnormalStatistics(StatisticsSheetVO statisticsSheetVO) {
        ParkingOrder.AbnormalStatisticsRequestProto.Builder builder = ParkingOrder.AbnormalStatisticsRequestProto.newBuilder();
        try {
            ProtoJsonUtil.toProtoBean(builder, statisticsSheetVO);
            //查询订单系统获取未支付数、支付成功数、支付总金额、优惠总金额
            ParkingOrder.AbnormalStatisticsResponseProto response = parkingOrderServiceBlockingStub.abnormalStatistics(builder.build());
            if (STATUS_OK.equals(response.getStatus())) {
                return ProtoJsonUtil.toPojoBean(AbnormalOrderStatisticsBO.class, response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.error("请求订单系统异常");
        return new AbnormalOrderStatisticsBO();
    }

    /**
     * 查询 订单信息列表 通过以下参数
     *
     * @param parkNo    停车场编号
     * @param carNumber 车牌号
     * @param entryTime 入场时间 标准时间格式
     */
    public List<VehicleParkOrderVO> queryOrderInfoByParkLiveInfo(String parkNo, String carNumber, String entryTime) {
        log.info("接受查询参数{},{},{}", parkNo, carNumber, entryTime);
        List<VehicleParkOrderVO> list = new ArrayList<>();
        ParkingOrder.parkMultiOrderRequest request = ParkingOrder.parkMultiOrderRequest.newBuilder()
                .setParkNo(parkNo)
                .setCarNum(carNumber)
                .setEntryTime(entryTime)
                .build();
        ParkingOrder.parkMultiOrderResponse response = parkingOrderServiceBlockingStub.getOneParkMultiOrder(request);
        if (STATUS_OK.equals(response.getStatus())) {
            for (ParkingOrder.OrderDetail order : response.getOrderDetailList()) {
                VehicleParkOrderVO vehicleParkOrderVO = new VehicleParkOrderVO();
                vehicleParkOrderVO.setOrderNo(order.getOrderNo());
                vehicleParkOrderVO.setPayableAmount(order.getPayableAmount());
                vehicleParkOrderVO.setPaidAmount(order.getPaidAmount());
                vehicleParkOrderVO.setPayAmount(order.getPayAmount());
                vehicleParkOrderVO.setDiscountAmount(order.getDiscountAmount());
                list.add(vehicleParkOrderVO);
            }
        }
        return list;
    }
}
