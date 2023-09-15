package com.ruoyi.project.parking.service;

import com.czdx.grpc.lib.order.MonthlyOrder;
import com.czdx.grpc.lib.order.MonthlyOrderServiceGrpc;
import com.ruoyi.project.parking.domain.vo.monthlyOrder.SearchMonthlyOrderRequestVO;
import com.ruoyi.project.parking.domain.vo.monthlyOrder.SearchMonthlyOrderResponseVO;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 访问 订单系统 grpc 服务
 */
@Slf4j
@Service("monthlyOrderGrpcService")
public class MonthlyOrderGrpcServiceImpl {

    @GrpcClient("parking-order-server")
    private MonthlyOrderServiceGrpc.MonthlyOrderServiceBlockingStub monthlyOrderServiceBlockingStub;

    // 错误状态码 200 正常
    static final String STATUS_OK = "200";

    /**
     * 查询订单列表
     */
    public SearchMonthlyOrderResponseVO queryOrderListWithPage(SearchMonthlyOrderRequestVO param) {
        MonthlyOrder.searchMonthlyOrderRequest.Builder builder = MonthlyOrder.searchMonthlyOrderRequest.newBuilder();
        try {
            ProtoJsonUtil.toProtoBean(builder, param);
            MonthlyOrder.searchMonthlyOrderResponse response = monthlyOrderServiceBlockingStub.searchMonthlyOrder(builder.build());
            if (STATUS_OK.equals(response.getStatus())) {
                return ProtoJsonUtil.toPojoBean(SearchMonthlyOrderResponseVO.class, response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
