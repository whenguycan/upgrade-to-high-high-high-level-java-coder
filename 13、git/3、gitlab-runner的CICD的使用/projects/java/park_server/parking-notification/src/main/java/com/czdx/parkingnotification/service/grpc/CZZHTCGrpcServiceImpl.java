package com.czdx.parkingnotification.service.grpc;

import com.alibaba.fastjson2.JSON;
import com.czdx.grpc.lib.czzhtc.*;
import com.czdx.parkingnotification.common.constant.CacheConstants;
import com.czdx.parkingnotification.domain.czzhtc.CZZHTCResponse;
import com.czdx.parkingnotification.domain.czzhtc.request.BillRecordRequest;
import com.czdx.parkingnotification.domain.czzhtc.request.SyncParkInfoRequest;
import com.czdx.parkingnotification.domain.czzhtc.request.TurnoverRecordRequest;
import com.czdx.parkingnotification.utils.CZZHTCUtils;
import com.czdx.parkingnotification.utils.JsonUtil;
import com.czdx.parkingnotification.utils.ProtoJsonUtil;
import com.czdx.parkingnotification.utils.RedisCache;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 常州智慧停车接口相关 grpc服务类
 * </p>
 *
 * @author 琴声何来
 * @since 2023/4/4 10:50
 */
@Slf4j
@GrpcService
public class CZZHTCGrpcServiceImpl extends CZZHTCServiceGrpc.CZZHTCServiceImplBase {

    @Autowired
    CZZHTCUtils czzhtcUtils;

    @Autowired
    RedisCache redisCache;

    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * @param request          车场信息同步请求参数
     * @param responseObserver 车场信息同步响应参数
     * @apiNote 车场信息同步接口
     * @author 琴声何来
     * @since 2023/4/4 14:00
     */
    @Override
    public void syncParkInfo(SyncParkInfoRequestProto request, StreamObserver<CZZHTCResponseProto> responseObserver) {
        SyncParkInfoRequest syncParkInfoRequest = new SyncParkInfoRequest();
        syncParkInfoRequest.setParkName(request.getParkName());
        syncParkInfoRequest.setRateInfo(request.getRateInfo());
        syncParkInfoRequest.setCityCode(request.getCityCode());
        syncParkInfoRequest.setAddress(request.getAddress());
        syncParkInfoRequest.setCoordinateX(request.getCoordinateX());
        syncParkInfoRequest.setCoordinateY(request.getCoordinateY());
        try {
            //心跳检测状态正常时，正常调用接口
            if (CZZHTCUtils.flag) {
                CZZHTCResponse<String> response = czzhtcUtils.syncParkInfo(syncParkInfoRequest);
                responseObserver.onNext(CZZHTCResponseProto.newBuilder()
                        .setSuccess(response.isSuccess())
                        .setMessage(response.getMessage())
                        .build());
            } else {
                //心跳检测状态不正常，保存数据，等心跳恢复后，重新上传数据
                List<SyncParkInfoRequest> cacheList = redisCache.getCacheList(CacheConstants.CZZHTC_SYNC_PARK_INFO_KEY);
                if (cacheList == null) {
                    cacheList = new ArrayList<>();
                }
                cacheList.add(syncParkInfoRequest);
                redisCache.setCacheList(CacheConstants.CZZHTC_SYNC_PARK_INFO_KEY, cacheList);
                responseObserver.onNext(CZZHTCResponseProto.newBuilder()
                        .setSuccess(false)
                        .setMessage("上行服务心跳异常，缓存数据。")
                        .build());
            }
        } catch (Exception e) {
            //上传数据异常，保存数据，等心跳恢复后，重新上传数据
            List<SyncParkInfoRequest> cacheList = redisCache.getCacheList(CacheConstants.CZZHTC_SYNC_PARK_INFO_KEY);
            if (cacheList == null) {
                cacheList = new ArrayList<>();
            }
            cacheList.add(syncParkInfoRequest);
            redisCache.setCacheList(CacheConstants.CZZHTC_SYNC_PARK_INFO_KEY, cacheList);
            responseObserver.onError(e);
        }
        responseObserver.onCompleted();
    }

    /**
     * @param request          进出记录上传请求参数
     * @param responseObserver 进出记录上传响应参数
     * @apiNote 进出记录上传接口
     * @author 琴声何来
     * @since 2023/4/4 14:00
     */
    @Override
    public void turnoverRecord(TurnoverRecordRequestProto request, StreamObserver<CZZHTCResponseProto> responseObserver) {
        TurnoverRecordRequest turnoverRecordRequest = new TurnoverRecordRequest();
        turnoverRecordRequest.setRecordID(request.getRecordID());
        turnoverRecordRequest.setRecordTime(LocalDateTime.parse(request.getRecordTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        turnoverRecordRequest.setPlate(request.getPlate());
        turnoverRecordRequest.setColor(request.getColor());
        turnoverRecordRequest.setRecordType(request.getRecordType());
        turnoverRecordRequest.setDevNum(request.getDevNum());
        turnoverRecordRequest.setConfirmFlag(request.getConfirmFlag());
        turnoverRecordRequest.setBillID(request.getBillID());
        try {
            //心跳检测状态正常时，正常调用接口
            if (CZZHTCUtils.flag) {
                CZZHTCResponse<String> response = czzhtcUtils.turnoverRecord(turnoverRecordRequest);
                responseObserver.onNext(CZZHTCResponseProto.newBuilder()
                        .setSuccess(response.isSuccess())
                        .setMessage(response.getMessage())
                        .build());
            } else {
                //心跳检测状态不正常，保存数据，等心跳恢复后，重新上传数据
                List<TurnoverRecordRequest> cacheList = redisCache.getCacheList(CacheConstants.CZZHTC_TURNOVER_RECORD_KEY);
                if (cacheList == null) {
                    cacheList = new ArrayList<>();
                }
                cacheList.add(turnoverRecordRequest);
                redisCache.setCacheList(CacheConstants.CZZHTC_TURNOVER_RECORD_KEY, cacheList);
                responseObserver.onNext(CZZHTCResponseProto.newBuilder()
                        .setSuccess(false)
                        .setMessage("上行服务心跳异常，缓存数据。")
                        .build());
            }
        } catch (Exception e) {
            //上传数据异常，保存数据，等心跳恢复后，重新上传数据
            List<TurnoverRecordRequest> cacheList = redisCache.getCacheList(CacheConstants.CZZHTC_TURNOVER_RECORD_KEY);
            if (cacheList == null) {
                cacheList = new ArrayList<>();
            }
            cacheList.add(turnoverRecordRequest);
            redisCache.setCacheList(CacheConstants.CZZHTC_TURNOVER_RECORD_KEY, cacheList);
            responseObserver.onError(e);
        }
        responseObserver.onCompleted();
    }

    /**
     * @param request          收费记录上传请求参数
     * @param responseObserver 收费记录上传响应参数
     * @apiNote 收费记录上传接口
     * @author 琴声何来
     * @since 2023/4/4 14:00
     */
    @Override
    public void billRecord(BillRecordRequestProto request, StreamObserver<CZZHTCResponseProto> responseObserver) {
        BillRecordRequest billRecordRequest = new BillRecordRequest();
        billRecordRequest.setBillID(request.getBillID());
        billRecordRequest.setBillTime(LocalDateTime.parse(request.getBillTime(), DEFAULT_FORMATTER));
        billRecordRequest.setPlate(request.getPlate());
        billRecordRequest.setColor(request.getColor());
        billRecordRequest.setStartTime(LocalDateTime.parse(request.getStartTime(), DEFAULT_FORMATTER));
        billRecordRequest.setEndTime(LocalDateTime.parse(request.getEndTime(), DEFAULT_FORMATTER));
        billRecordRequest.setBillType(request.getBillType());
        billRecordRequest.setPayType(request.getPayType());
        billRecordRequest.setPayChannel(request.getPayChannel());
        billRecordRequest.setShould(request.getShould());
        billRecordRequest.setPaid(request.getPaid());
        billRecordRequest.setCarType(request.getCarType());
        billRecordRequest.setFreeMoney(request.getFreeMoney());
        billRecordRequest.setPrePaid(request.getPrePaid());
        billRecordRequest.setAdvPaid(request.getAdvPaid());
        billRecordRequest.setMemberFreeMoney(request.getMemberFreeMoney());
        billRecordRequest.setRegularFreeMoney(request.getRegularFreeMoney());
        billRecordRequest.setOrderCode(request.getOrderCode());
        billRecordRequest.setDiscountCodes(request.getDiscountCodes());
        billRecordRequest.setRemark(request.getRemark());
        try {
            //心跳检测状态正常时，正常调用接口
            if (CZZHTCUtils.flag) {
                CZZHTCResponse<String> response = czzhtcUtils.billRecord(billRecordRequest);
                responseObserver.onNext(CZZHTCResponseProto.newBuilder()
                        .setSuccess(response.isSuccess())
                        .setMessage(response.getMessage())
                        .build());
            } else {
                //心跳检测状态不正常，保存数据，等心跳恢复后，重新上传数据
                List<BillRecordRequest> cacheList = redisCache.getCacheList(CacheConstants.CZZHTC_BILL_RECORD_KEY);
                if (cacheList == null) {
                    cacheList = new ArrayList<>();
                }
                cacheList.add(billRecordRequest);
                redisCache.setCacheList(CacheConstants.CZZHTC_BILL_RECORD_KEY, cacheList);
                responseObserver.onNext(CZZHTCResponseProto.newBuilder()
                        .setSuccess(false)
                        .setMessage("上行服务心跳异常，缓存数据。")
                        .build());
            }
        } catch (Exception e) {
            //上传数据异常，保存数据，等心跳恢复后，重新上传数据
            List<BillRecordRequest> cacheList = redisCache.getCacheList(CacheConstants.CZZHTC_BILL_RECORD_KEY);
            if (cacheList == null) {
                cacheList = new ArrayList<>();
            }
            cacheList.add(billRecordRequest);
            redisCache.setCacheList(CacheConstants.CZZHTC_BILL_RECORD_KEY, cacheList);
            responseObserver.onError(e);
        }
        responseObserver.onCompleted();
    }
}
