package com.ruoyi.project.parking.service;

import com.alibaba.fastjson2.JSONObject;
import com.czdx.grpc.lib.lot.*;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.parking.domain.vo.parkingorder.HistoryParkingOrderReponseVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.HistoryParkingOrderVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.VehicleParkCreateOrderRequestVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.VehicleParkOrderVO;
import com.ruoyi.project.parking.entity.ParkLiveRecords;
import com.ruoyi.project.parking.enums.SettleTypeEnum;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import com.ruoyi.project.system.service.ISysDeptService;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 提供 Lot停车订单 对外grpc 服务
 */
@Slf4j
@GrpcService
public class LotParkingServiceImpl extends LotParkingServiceGrpc.LotParkingServiceImplBase {

    @Autowired
    private IParkLiveRecordsService parkLiveRecordsService;

    @Autowired
    private ITEntryRecordsService entryRecordsService;

    @Autowired
    private ParkingOrderGrpcServiceImpl parkingOrderGrpcService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private BFieldService bFieldService;

    @GrpcClient("parking-order-server")
    private ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub parkingOrderServiceBlockingStub;

    @Autowired
    private RabbitmqService rabbitmqService;

    // 错误状态码 200 正常
    static final String STATUS_OK = "200";
    static final String STATUS_ERR = "500";
    static final String CODE_NO_CREATE = "201";

    @Transactional
    @Override
    public void createParkingOrder(CreateLotOrderRequest request, StreamObserver<ParkingOrderReponse> responseObserver) {
        log.info("接受传参:{}", JSONObject.toJSONString(request));
        String memberId = request.getMemberId();
        String parkNo = request.getParkNo();
        String carNumber = request.getCarNumber();
        String passageNo = request.getPassageNo();

        // 结算类型 无通道数据 为 预支付类型
        SettleTypeEnum settleTypeEnum = StringUtils.isEmpty(passageNo) ? SettleTypeEnum.PREPAY_CODE_PAY : SettleTypeEnum.PAVILION_CODE_PAY;
        ParkingOrderReponse.Builder reponseBuilder = ParkingOrderReponse.newBuilder();
        reponseBuilder.setStatus(STATUS_OK);
        reponseBuilder.setMess("操作成功");
        try {
            VehicleParkCreateOrderRequestVO param = parkLiveRecordsService.buildCreateOrderRequest(settleTypeEnum, parkNo, carNumber, passageNo, StringUtils.EMPTY);
            // 转发至订单系统的创建订单
            ParkingOrder.CreateOrderRequest.Builder builder = ParkingOrder.CreateOrderRequest.newBuilder();
            ProtoJsonUtil.toProtoBean(builder, param);
            builder.addAllCouponList(request.getCouponListList());
            log.info("构建order-grpc传参:{}", JSONObject.toJSONString(builder.build()));
            ParkingOrder.CreateOrderReponse reponse = parkingOrderServiceBlockingStub.createParkingOrder(builder.build());
            log.info("接受order-grpc响应:{}", JSONObject.toJSONString(reponse));
            if (STATUS_OK.equals(reponse.getStatus())) {
                // 判断 不创建订单情况
                if (CODE_NO_CREATE.equals(reponse.getReasonCode())) {
                    reponseBuilder.setStatus(CODE_NO_CREATE);
                    reponseBuilder.setMess("无需创建订单!");
                } else {
                    log.info("更新在场记录-会员id");
                    parkLiveRecordsService.updateMemberIdByParkNoCarNumber(memberId, parkNo, carNumber);
                    reponseBuilder.setOrderData(convertParkingOrder(reponse.getOrderDetail()));
                    reponseBuilder.addAllCouponlist(reponse.getCouponlistList());
                }
            } else {
                reponseBuilder.setStatus(STATUS_ERR);
                reponseBuilder.setMess(reponse.getMess());
            }
        } catch (Exception e) {
            reponseBuilder.setStatus(STATUS_ERR);
            reponseBuilder.setMess(e.getMessage());
            log.error("lot grpc:" + e.getMessage());
        }
        responseObserver.onNext(reponseBuilder.build());
        responseObserver.onCompleted();
    }

    /**
     * 转化 订单系统订单 => lot系统订单
     *
     * @param order 订单系统订单
     * @return lot系统订单
     */
    private LotParkingOrder convertParkingOrder(ParkingOrder.OrderDetail order) throws IOException {
        VehicleParkOrderVO vehicleParkOrderVO = ProtoJsonUtil.toPojoBean(VehicleParkOrderVO.class, order);
        // 关联额外 停车信息
        vehicleParkOrderVO.setParkName(deptService.selectParkNameByParkNo(order.getParkNo()));
        // 首次创建订单的时候 订单号还未更新
        ParkLiveRecords parkLiveRecords = parkLiveRecordsService.queryByParkNoCarNumber(order.getParkNo(), order.getCarNumber());
        vehicleParkOrderVO.setParkLiveId(parkLiveRecords.getId());
        vehicleParkOrderVO.setCarImgUrlFrom(entryRecordsService.selectEntryRecordsCarImgUrlByParkLiveId(parkLiveRecords.getId()));
        vehicleParkOrderVO.setDurationTime(DateUtils.getDatePoor(vehicleParkOrderVO.getEntryTime(), LocalDateTime.now()));
        // 转换 类型为 LotParkingOrder 携带额外属性
        LotParkingOrder.Builder lotParkingOrderBuilder = LotParkingOrder.newBuilder();
        // 跳过额外属性的无法copy
        if (StringUtils.isNotEmpty(vehicleParkOrderVO.getItemList())) {
            List<OrderItem> itemList = vehicleParkOrderVO.getItemList().stream()
                    .map(m -> OrderItem.newBuilder()
                            .setParkFieldId(m.getParkFieldId())
                            .setEntryTime(m.getEntryTime())
                            .setExitTime(m.getExitTime())
                            .setFieldName(bFieldService.queryById(m.getParkFieldId()).getFieldName()).build())
                    .toList();
            vehicleParkOrderVO.setItemList(null);
            ProtoJsonUtil.toProtoBean(lotParkingOrderBuilder, vehicleParkOrderVO);
            lotParkingOrderBuilder.addAllItemList(itemList);
        } else {
            ProtoJsonUtil.toProtoBean(lotParkingOrderBuilder, vehicleParkOrderVO);
        }
        return lotParkingOrderBuilder.build();
    }

    @Override
    public void queryParkingOrderByOrderNo(ParkingOrder.OrderDetailRequest request, StreamObserver<ParkingOrderReponse> responseObserver) {
        log.info("接受传参:{}", JSONObject.toJSONString(request));
        ParkingOrderReponse.Builder reponseBuilder = ParkingOrderReponse.newBuilder();
        reponseBuilder.setStatus(STATUS_OK);
        reponseBuilder.setMess("操作成功");
        try {
            // 转发至订单系统的查询
            ParkingOrder.CreateOrderReponse reponse = parkingOrderGrpcService.queryParkingOrderByOrderNo(request);
            log.info("接受order 返回值:{}", JSONObject.toJSONString(reponse));
            if (STATUS_OK.equals(reponse.getStatus())) {
                reponseBuilder.setOrderData(convertParkingOrder(reponse.getOrderDetail()));
                reponseBuilder.addAllCouponlist(reponse.getCouponlistList());
            } else {
                reponseBuilder.setStatus(STATUS_ERR);
                reponseBuilder.setMess(reponse.getMess());
            }
        } catch (Exception e) {
            reponseBuilder.setStatus(STATUS_ERR);
            reponseBuilder.setMess(e.getMessage());
            log.error("queryParkingOrderByOrderNo FAILED with " + e.getMessage());
        }
        responseObserver.onNext(reponseBuilder.build());
        responseObserver.onCompleted();
    }


    /**
     * 查询历史停车订单
     */
    @Override
    public void queryHistoryParkingOrder(HistoryParkingOrderRequest request, StreamObserver<HistoryParkingOrderReponse> responseObserver) {
        log.info("接受传参:{}", JSONObject.toJSONString(request));
        String memberId = request.getMemberId();
        String carNumber = request.getCarNumber();
        HistoryParkingOrderReponse.Builder reponseBuilder = HistoryParkingOrderReponse.newBuilder();
        // 查询本地loc服务
        Pair<List<HistoryParkingOrderVO>, Long> pair = parkLiveRecordsService.queryHistoryParkingOrderByMemberIdCarNumber(memberId, carNumber, request.getPageNum(), request.getPageSize());
        HistoryParkingOrderReponseVO historyParkingOrderReponseVO = new HistoryParkingOrderReponseVO();
        historyParkingOrderReponseVO.setStatus(STATUS_OK);
        historyParkingOrderReponseVO.setMess("操作成功");
        historyParkingOrderReponseVO.setRows(pair.getLeft());
        historyParkingOrderReponseVO.setTotal(pair.getRight().intValue());
        try {
            ProtoJsonUtil.toProtoBean(reponseBuilder, historyParkingOrderReponseVO);
        } catch (IOException e) {
            log.error("组装历史订单记录异常，{}", e.getMessage());
        }
        responseObserver.onNext(reponseBuilder.build());
        responseObserver.onCompleted();
    }

    /**
     * 查询会员停车订单历史车牌号
     */
    @Override
    public void queryHistoryCarNumber(QueryHistoryCarNumberRequest request, StreamObserver<QueryHistoryCarNumberReponse> responseObserver) {
        log.info("接受传参:{}", JSONObject.toJSONString(request));
        String memberId = request.getMemberId();
        List<String> list = parkLiveRecordsService.queryHistoryCarNumberByMemberId(memberId);

        QueryHistoryCarNumberReponse.Builder reponseBuilder = QueryHistoryCarNumberReponse.newBuilder()
                .setStatus(STATUS_OK)
                .setMess("操作成功")
                .addAllData(list);
        responseObserver.onNext(reponseBuilder.build());
        responseObserver.onCompleted();
    }
}
