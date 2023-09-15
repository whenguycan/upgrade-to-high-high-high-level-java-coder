package com.ruoyi.project.parking.service;

import com.alibaba.fastjson2.JSONObject;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.parking.domain.vo.*;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("parkingOrderGrpcService")
public class ParkingOrderGrpcServiceImpl {

    @GrpcClient("parking-order-server")
    private ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub parkingOrderServiceBlockingStub;

    @Autowired
    private LotParkingGrpcServiceImpl lotParkingGrpcService;

    // 错误状态码 200 正常
    static final String STATUS_OK = "200";

    /**
     * 拉取支付链接
     *
     * @param requestVO 拉起支付参数
     * @return 支付链接
     */
    public ConfirmPayResponseVO pullOrderToReadyPay(ConfirmPayRequestVO requestVO) {
        if (requestVO.getPayType() == 2 && requestVO.getWeChatPayMethod() == 1 && StringUtils.isEmpty(requestVO.getOpenid())) {
            throw new ServiceException("支付未授权!");
        }
        // 用户登录的情况下 使用用户绑定的openid
        if (SecurityUtils.isLogin()) {
            requestVO.setOpenid(SecurityUtils.getLoginUser().getUser().getOpenId());
        }
        try {
            ParkingOrder.ConfirmPayRequest.Builder builder = ParkingOrder.ConfirmPayRequest.newBuilder();
            ProtoJsonUtil.toProtoBean(builder, requestVO);
            ParkingOrder.ConfirmPayResponse confirmPayResponse = parkingOrderServiceBlockingStub.confirmPay(builder.build());
            if (STATUS_OK.equals(confirmPayResponse.getStatus())) {
                return ProtoJsonUtil.toPojoBean(ConfirmPayResponseVO.class, confirmPayResponse);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 手动取消 订单
     *
     * @param orderNo 预支付订单 编号
     */
    public boolean cancleOrderByOrderNo(String orderNo) {
        log.info("接受传参:{}", orderNo);
        VehicleParkOrderVO vehicleParkOrderVO = null;
        List<ParkingOrderCouponShowVO> couponListPicked = new ArrayList<>();
        try {
            ParkingOrder.cancleOrderRequest.Builder request = ParkingOrder.cancleOrderRequest.newBuilder()
                    .setOrderNo(orderNo);
            ParkingOrder.cancleOrderResponse reponse = parkingOrderServiceBlockingStub.cancleOrder(request.build());
            if (STATUS_OK.equals(reponse.getStatus())) {
                return true;
            } else {
                throw new ServiceException(reponse.getMess());
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 手动切换优惠券
     *
     * @param requestVO 手动切换优惠券 参数
     */
    public Pair<VehicleParkOrderVO, List<ParkingOrderCouponShowVO>> switchOrderCoupon(ChangeOrderCouponRquestVO requestVO) {
        log.info("手动切换优惠券 接受传参:{}", JSONObject.toJSONString(requestVO));

        VehicleParkOrderVO vehicleParkOrderVO = null;
        List<ParkingOrderCouponShowVO> couponListPicked = new ArrayList<>();
        try {
            ParkingOrder.changeOrderCouponRequest.Builder request = ParkingOrder.changeOrderCouponRequest.newBuilder();
            ProtoJsonUtil.toProtoBean(request, requestVO);
            ParkingOrder.changeOrderCouponResponse reponse = parkingOrderServiceBlockingStub.changeOrderCoupon(request.build());
            log.info("手动切换优惠券 接受返回值:{}", JSONObject.toJSONString(reponse));
            if (STATUS_OK.equals(reponse.getStatus())) {
                vehicleParkOrderVO = ProtoJsonUtil.toPojoBean(VehicleParkOrderVO.class, reponse.getOrderDetail());
                couponListPicked = lotParkingGrpcService.buildCouponShowVO(reponse.getCouponlistList());
            } else {
                throw new ServiceException(reponse.getMess());
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        return new ImmutablePair<>(vehicleParkOrderVO, couponListPicked);
    }

}
