package com.ruoyi.project.grpclient;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import com.ruoyi.project.parking.domain.vo.ConfirmPayRequestVO;
import com.ruoyi.project.parking.domain.vo.ConfirmPayResponseVO;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service("parkingOrderGrpcClientServiceImpl")
public class MerchantOrderGrpcClientServiceImpl {

    @GrpcClient("parking-order-server")
    private ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub parkingOrderServiceBlockingStub;

    // 错误状态码 200 正常
    static final String STATUS_OK = "200";

    /**
     * 拉取支付链接
     * @param orderNo 订单号
     * @param payType 支付类型
     * @return 支付链接
     */
    public String pullOrderToReadyPay(String orderNo, Integer payType) {
        try {
            ParkingOrder.ConfirmPayRequest confirmPayRequest = ParkingOrder.ConfirmPayRequest.newBuilder()
                    .setOrderNo(orderNo)
                    .setPayType(payType)
                    .build();
            ParkingOrder.ConfirmPayResponse confirmPayResponse = parkingOrderServiceBlockingStub.confirmPay(confirmPayRequest);
            if (STATUS_OK.equals(confirmPayResponse.getStatus())) {
                return confirmPayResponse.getPayUrl();
            }
        } catch (StatusRuntimeException e) {
            log.error("FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("FAILED with " + e.getMessage());
        }
        return null;
    }

    /**
     * 拉取支付链接
     * @param requestVO 拉起支付参数
     * @return 支付链接
     */
    public ConfirmPayResponseVO pullOrderToReadyPay(ConfirmPayRequestVO requestVO) {
        try {
            ParkingOrder.ConfirmPayRequest.Builder builder = ParkingOrder.ConfirmPayRequest.newBuilder();
            ProtoJsonUtil.toProtoBean(builder,requestVO);
            log.info("ConfirmPayRequest:"+builder.build().toString());
            ParkingOrder.ConfirmPayResponse confirmPayResponse = parkingOrderServiceBlockingStub.confirmPay(builder.build());
            if (STATUS_OK.equals(confirmPayResponse.getStatus())) {
                return ProtoJsonUtil.toPojoBean(ConfirmPayResponseVO.class,confirmPayResponse);
            }
        } catch (StatusRuntimeException e) {
            log.error("FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("FAILED with " + e.getMessage());
        }
        return null;
    }
}
