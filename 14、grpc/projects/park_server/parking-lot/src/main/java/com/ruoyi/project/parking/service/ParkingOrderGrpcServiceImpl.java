package com.ruoyi.project.parking.service;

import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrder.CreateOrderRequest;
import com.czdx.grpc.lib.order.ParkingOrder.searchOrderRequest;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import com.ruoyi.project.parking.domain.vo.parkingorder.SearchOrderRequestVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.SearchOrderResponseVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.VehicleParkCreateOrderRequestVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.VehicleParkOrderVO;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 访问 订单系统 grpc 服务
 */
@Slf4j
@Service("parkingOrderGrpcService")
public class ParkingOrderGrpcServiceImpl {

    @GrpcClient("parking-order-server")
    private ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub parkingOrderServiceBlockingStub;

    // 错误状态码 200 正常
    static final String STATUS_OK = "200";

    /**
     * 创建停车订单
     *
     * @param param 创建参数
     */
    public VehicleParkOrderVO createParkingOrder(VehicleParkCreateOrderRequestVO param) {
        VehicleParkOrderVO vehicleParkOrderVO = null;
        try {
            CreateOrderRequest.Builder builder = CreateOrderRequest.newBuilder();
            ProtoJsonUtil.toProtoBean(builder, param);
            ParkingOrder.CreateOrderReponse createOrderReponse = parkingOrderServiceBlockingStub.createParkingOrder(builder.build());
            if (STATUS_OK.equals(createOrderReponse.getStatus())) {
                ParkingOrder.OrderDetail orderDetail = createOrderReponse.getOrderDetail();
                vehicleParkOrderVO = ProtoJsonUtil.toPojoBean(VehicleParkOrderVO.class, orderDetail);
            }
        } catch (StatusRuntimeException e) {
            log.error("FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("FAILED with " + e.getMessage());
        }
        return vehicleParkOrderVO;
    }

    /**
     * 创建停车订单
     *
     * @param param 创建参数
     */
    public ParkingOrder.CreateOrderReponse createParkingOrderWithOrderReponse(VehicleParkCreateOrderRequestVO param) {
        ParkingOrder.CreateOrderReponse createOrderReponse = null;
        try {
            CreateOrderRequest.Builder builder = CreateOrderRequest.newBuilder();
            ProtoJsonUtil.toProtoBean(builder, param);
            createOrderReponse = parkingOrderServiceBlockingStub.createParkingOrder(builder.build());
            if (STATUS_OK.equals(createOrderReponse.getStatus())) {
                return createOrderReponse;
            }
        } catch (StatusRuntimeException e) {
            log.error("FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("FAILED with " + e.getMessage());
        }
        return null;
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
            }
        } catch (StatusRuntimeException e) {
            log.error("FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("FAILED with " + e.getMessage());
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
            log.error("FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("FAILED with " + e.getMessage());
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
            log.error("FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("FAILED with " + e.getMessage());
        }
        return flag;
    }


    /**
     * 查询订单列表
     */
    public SearchOrderResponseVO queryOrderListWithPage(SearchOrderRequestVO param) {
        SearchOrderResponseVO searchOrderResponseVO = null;
        try {
            searchOrderRequest.Builder builder = searchOrderRequest.newBuilder();
            ProtoJsonUtil.toProtoBean(builder, param);
            ParkingOrder.searchOrderResponse response = parkingOrderServiceBlockingStub.searchOrder(builder.build());
            if (STATUS_OK.equals(response.getStatus())) {
                searchOrderResponseVO = ProtoJsonUtil.toPojoBean(SearchOrderResponseVO.class, response);
            }

        } catch (StatusRuntimeException e) {
            log.error("FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("FAILED with " + e.getMessage());
        }
        return searchOrderResponseVO;
    }
}
