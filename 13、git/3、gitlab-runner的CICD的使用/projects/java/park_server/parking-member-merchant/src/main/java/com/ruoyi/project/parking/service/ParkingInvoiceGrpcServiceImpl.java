package com.ruoyi.project.parking.service;

import com.czdx.grpc.lib.order.MonthlyOrder;
import com.czdx.grpc.lib.order.MonthlyOrderServiceGrpc;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import com.dahuatech.hutool.json.JSONUtil;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.merchant.domain.bo.MonthlyCarRentalOrderBO;
import com.ruoyi.project.parking.domain.vo.SearchMonthlyOrderResponseVO;
import com.ruoyi.project.parking.domain.vo.SearchOrderResponseVO;
import com.ruoyi.project.parking.domain.vo.invoice.*;
import com.ruoyi.project.parking.enums.BillEnums;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 调用 order模块的开票接口 grpc 服务
 */
@Slf4j
@Service("parkingInvoiceGrpcService")
public class ParkingInvoiceGrpcServiceImpl {

    @GrpcClient("parking-order-server")
    private ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub parkingOrderServiceBlockingStub;

    @GrpcClient("parking-order-server")
    private MonthlyOrderServiceGrpc.MonthlyOrderServiceBlockingStub monthlyOrderServiceBlockingStub;


    // 错误状态码 200 正常
    static final String STATUS_OK = "200";

    /**
     * 查询用户可开票 停车记录
     *
     * @param requestVO 查询参数
     */
    public SearchOrderResponseVO pageBillableParkingOrder(BillableParkingOrderRequestVO requestVO) {
        log.info("查询用户可开票停车记录请求参数：{}", JSONUtil.toJsonStr(requestVO));
        try {
            requestVO.setBillable(true);
            ParkingOrder.SearchParkingOrderRequest.Builder builder = ParkingOrder.SearchParkingOrderRequest.newBuilder();
            ProtoJsonUtil.toProtoBean(builder, requestVO);
            ParkingOrder.searchOrderResponse response = parkingOrderServiceBlockingStub.searchParkingOrderByCarNums(builder.build());
            if (STATUS_OK.equals(response.getStatus())) {
                return ProtoJsonUtil.toPojoBean(SearchOrderResponseVO.class, response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 查询用户可开票 月租记录
     *
     * @param requestVO 查询参数
     */
    public SearchMonthlyOrderResponseVO pageBillableMonthlyOrder(BillableMonthlyOrderRequestVO requestVO) {
        log.info("查询用户可开票月租记录请求参数：{}", JSONUtil.toJsonStr(requestVO));
        try {
            requestVO.setBillable(true);
            MonthlyOrder.searchMonthlyOrderRequest.Builder builder = MonthlyOrder.searchMonthlyOrderRequest.newBuilder();
            ProtoJsonUtil.toProtoBean(builder, requestVO);
            builder.setOrderStatus("03");
            MonthlyOrder.searchMonthlyOrderResponse response = monthlyOrderServiceBlockingStub.searchMonthlyOrder(builder.build());
            if (STATUS_OK.equals(response.getStatus())) {
                return ProtoJsonUtil.toPojoBean(SearchMonthlyOrderResponseVO.class, response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 订单 开票
     *
     * @param requestVO 请求参数
     */
    public ApplyBillResponseVO confirmParkingOrderInvoice(ApplyBillRequestVO requestVO) {
        log.info("订单开票请求参数：{}", JSONUtil.toJsonStr(requestVO));
        try {
            ParkingOrder.ApplyBillRequest.Builder builder = ParkingOrder.ApplyBillRequest.newBuilder();
            ProtoJsonUtil.toProtoBean(builder, requestVO);
            ParkingOrder.ApplyBillResponse response = parkingOrderServiceBlockingStub.applyBill(builder.build());
            if (STATUS_OK.equals(response.getStatus())) {
                return ProtoJsonUtil.toPojoBean(ApplyBillResponseVO.class, response);
            } else {
                throw new ServiceException(response.getMess());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取用户 开票记录
     *
     * @param requestVO 查询参数
     */
    public BillRecordResponseVO pageHistoryParkingOrderBill(BillRecordRequestVO requestVO) {
        log.info("获取用户开票记录请求参数：{}", JSONUtil.toJsonStr(requestVO));
        try {
            ParkingOrder.BillRecordRequest.Builder builder = ParkingOrder.BillRecordRequest.newBuilder();
            ProtoJsonUtil.toProtoBean(builder, requestVO);
            ParkingOrder.BillRecordResponse response = parkingOrderServiceBlockingStub.getBillRecord(builder.build());
            if (STATUS_OK.equals(response.getStatus())) {
                return ProtoJsonUtil.toPojoBean(BillRecordResponseVO.class, response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 获取 发票 详情
     *
     * @param requestVO 请求参数
     */
    public BillDetailResponseVO queryParkingOrderInvoiceDetail(BillDetailRequestVO requestVO) {
        log.info("获取发票详情请求参数：{}", JSONUtil.toJsonStr(requestVO));
        try {
            ParkingOrder.BillDetailRequest.Builder builder = ParkingOrder.BillDetailRequest.newBuilder();
            ProtoJsonUtil.toProtoBean(builder, requestVO);
            ParkingOrder.BillDetailResponse response = parkingOrderServiceBlockingStub.getBillDetail(builder.build());
            if (STATUS_OK.equals(response.getStatus())) {
                return ProtoJsonUtil.toPojoBean(BillDetailResponseVO.class, response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
