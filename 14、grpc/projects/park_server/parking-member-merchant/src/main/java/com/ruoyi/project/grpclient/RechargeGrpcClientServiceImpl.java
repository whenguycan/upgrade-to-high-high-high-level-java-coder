package com.ruoyi.project.grpclient;

import com.czdx.grpc.lib.order.MerchantOrder;
import com.czdx.grpc.lib.order.MerchantOrderServiceGrpc;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import com.ruoyi.project.grpclient.domain.MerchantOrderDetail;
import com.ruoyi.project.merchant.domain.vo.RechargeVo;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

/**
 * 调用订单接口，生成订单数据
 */
@Slf4j
@Service("RechargeGrpcClientService")
public class RechargeGrpcClientServiceImpl {

    @GrpcClient("parking-order-server")
    private MerchantOrderServiceGrpc.MerchantOrderServiceBlockingStub merchantOrderServiceBlockingStub;

    // 错误状态码 200 正常
    static final String STATUS_OK = "200";

    //调用充值订单的接口
    public MerchantOrderDetail createMerchantOrder(RechargeVo rechargeVo) {
        MerchantOrderDetail merchantOrderDetail = null;
        try {
            MerchantOrder.createMerchantOrderRequest.Builder builder = MerchantOrder.createMerchantOrderRequest.newBuilder();
            builder.setErchantId(rechargeVo.getErchantId().intValue());
            builder.setParkNo(rechargeVo.getParkNo());
            builder.setDiscountAmount(rechargeVo.getDiscountAmount() != null ? rechargeVo.getDiscountAmount().doubleValue() : 0);
            builder.setPayAmount(rechargeVo.getPayAmount() != null ? rechargeVo.getPayAmount().doubleValue() : null);
            MerchantOrder.merchantOrderResponse merchantOrder = merchantOrderServiceBlockingStub.createMerchantOrder(builder.build());
            if (STATUS_OK.equals(merchantOrder.getStatus())) {
                MerchantOrder.MerchantOrderDetail orderDetail = merchantOrder.getOrderDetail();
                merchantOrderDetail = ProtoJsonUtil.toPojoBean(MerchantOrderDetail.class, orderDetail);

            }
        } catch (StatusRuntimeException e) {
            log.error("FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("FAILED with " + e.getMessage());
        }
        return merchantOrderDetail;
    }
}
