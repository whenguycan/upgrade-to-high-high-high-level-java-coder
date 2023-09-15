package com.czdx.parkingpayment.service.grpc;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.czdx.grpc.lib.alipay.*;
import com.czdx.parkingpayment.common.constant.PayTypeConstants;
import com.czdx.parkingpayment.config.MyAlipayConfig;
import com.czdx.parkingpayment.domain.ParkingPaymentGrpcRecord;
import com.czdx.parkingpayment.service.IParkingPaymentGrpcRecordService;
import com.czdx.parkingpayment.utils.ProtoJsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * grpc调用支付子系统处理逻辑
 * </p>
 *
 * @author 琴声何来
 * @since 2023/3/2 17:40
 */
@Slf4j
@GrpcService
public class AlipayTradeGrpcServiceImpl extends AliPayServiceGrpc.AliPayServiceImplBase {
    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    IParkingPaymentGrpcRecordService parkingPaymentGrpcRecordService;

    @Autowired
    private AlipayTradeWapPayRequest alipayTradeWapPayRequest;

    @Autowired
    private AlipayTradeQueryRequest alipayTradeQueryRequest;

    @Autowired
    private AlipayTradeCloseRequest alipayTradeCloseRequest;

    @Autowired
    private AlipayTradeRefundRequest alipayTradeRefundRequest;

    @Autowired
    private AlipayTradeFastpayRefundQueryRequest alipayTradeFastpayRefundQueryRequest;

    @Autowired
    private AlipayDataDataserviceBillDownloadurlQueryRequest alipayDataDataserviceBillDownloadurlQueryRequest;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param request          grpcRequest
     * @param responseObserver grpcResponse
     * @apiNote 支付宝手机网站支付接口
     * @author 琴声何来
     * @since 2023/2/22 15:34
     */
    @Override
    public void alipayTradeWapPay(AlipayTradeWapPayRequestProto request, StreamObserver<AlipayTradeWapPayResponseProto> responseObserver) {
        //保存请求记录
        ParkingPaymentGrpcRecord record = saveAlipayTradeWapPayRequestRecord(request);
        parkingPaymentGrpcRecordService.save(record);
        //请求支付宝接口
        try {
            alipayTradeWapPayRequest.setBizContent(ProtoJsonUtil.toJson(request));
            //加GET参数，只获取支付链接
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipayTradeWapPayRequest, "GET");
            if (response.isSuccess()) {
                log.info("支付宝手机网站支付接口请求成功，responseBody：{}", response.getBody());
                response.setCode("10000");
            } else {
                log.error("支付宝手机网站支付接口请求失败，responseBody：{}", response.getBody());
            }
            //更新请求记录
            updateRequestRecord(record, response);
            parkingPaymentGrpcRecordService.updateById(record);
            // grpc返回结果
            AlipayTradeWapPayResponseProto.Builder builder = AlipayTradeWapPayResponseProto.newBuilder();
            responseObserver.onNext((AlipayTradeWapPayResponseProto) ProtoJsonUtil.toObject(builder, objectMapper.writeValueAsString(response)));
            responseObserver.onCompleted();
        } catch (AlipayApiException | IOException e) {
            log.error("支付宝手机网站支付接口请求异常", e);
        }
    }

    /**
     * @param request          grpcRequest
     * @param responseObserver grpcResponse
     * @apiNote 支付宝统一收单交易查询接口
     * @author 琴声何来
     * @since 2023/2/22 15:34
     */
    @Override
    public void alipayTradeQuery(AlipayTradeQueryRequestProto request, StreamObserver<AlipayTradeQueryResponseProto> responseObserver) {
        //保存请求记录
        ParkingPaymentGrpcRecord record = saveAlipayTradeQueryRequestRecord(request);
        parkingPaymentGrpcRecordService.save(record);
        //请求支付宝接口
        try {
            alipayTradeQueryRequest.setBizContent(ProtoJsonUtil.toJson(request));
            AlipayTradeQueryResponse response = alipayClient.execute(alipayTradeQueryRequest);
            if (response.isSuccess()) {
                log.info("支付宝统一收单交易查询接口请求成功，responseBody：{}", response.getBody());
            } else {
                log.error("支付宝统一收单交易查询接口请求失败，responseBody：{}", response.getBody());
            }
            //更新请求记录
            updateRequestRecord(record, response);
            parkingPaymentGrpcRecordService.updateById(record);
            // grpc返回结果
            AlipayTradeQueryResponseProto.Builder builder = AlipayTradeQueryResponseProto.newBuilder();
            responseObserver.onNext((AlipayTradeQueryResponseProto) ProtoJsonUtil.toObject(builder, objectMapper.writeValueAsString(response)));
            responseObserver.onCompleted();
        } catch (AlipayApiException | IOException e) {
            log.error("支付宝统一收单交易查询接口请求异常", e);
        }
    }

    /**
     * @param request          grpcRequest
     * @param responseObserver grpcResponse
     * @apiNote 支付宝统一收单交易关闭接口
     * @author 琴声何来
     * @since 2023/2/22 15:34
     */
    @Override
    public void alipayTradeClose(AlipayTradeCloseRequestProto request, StreamObserver<AlipayTradeCloseResponseProto> responseObserver) {
        //保存请求记录
        ParkingPaymentGrpcRecord record = saveAlipayTradeCloseRequestRecord(request);
        parkingPaymentGrpcRecordService.save(record);
        //请求支付宝接口
        try {
            alipayTradeCloseRequest.setBizContent(ProtoJsonUtil.toJson(request));
            AlipayTradeCloseResponse response = alipayClient.execute(alipayTradeCloseRequest);
            if (response.isSuccess()) {
                log.info("支付宝统一收单交易关闭接口请求成功，responseBody：{}", response.getBody());
            } else {
                log.error("支付宝统一收单交易关闭接口请求失败，responseBody：{}", response.getBody());
            }
            //更新请求记录
            updateRequestRecord(record, response);
            parkingPaymentGrpcRecordService.updateById(record);
            // grpc返回结果
            AlipayTradeCloseResponseProto.Builder builder = AlipayTradeCloseResponseProto.newBuilder();
            responseObserver.onNext((AlipayTradeCloseResponseProto) ProtoJsonUtil.toObject(builder, objectMapper.writeValueAsString(response)));
            responseObserver.onCompleted();
        } catch (AlipayApiException | IOException e) {
            log.error("支付宝统一收单交易关闭接口请求异常", e);
        }
    }

    /**
     * @param request          grpcRequest
     * @param responseObserver grpcResponse
     * @apiNote 支付宝统一收单交易退款接口
     * @author 琴声何来
     * @since 2023/2/22 15:34
     */
    @Override
    public void alipayTradeRefund(AlipayTradeRefundRequestProto request, StreamObserver<AlipayTradeRefundResponseProto> responseObserver) {
        //保存请求记录
        ParkingPaymentGrpcRecord record = saveAlipayTradeRefundRequestRecord(request);
        parkingPaymentGrpcRecordService.save(record);
        //请求支付宝接口
        try {
            alipayTradeRefundRequest.setBizContent(ProtoJsonUtil.toJson(request));
            AlipayTradeRefundResponse response = alipayClient.execute(alipayTradeRefundRequest);
            if (response.isSuccess() && "Y".equals(response.getFundChange())) {
                log.info("支付宝统一收单交易退款接口请求成功，responseBody：{}", response.getBody());
            } else {
                log.error("支付宝统一收单交易退款接口请求失败，responseBody：{}", response.getBody());
            }
            //更新请求记录
            updateRequestRecord(record, response);
            parkingPaymentGrpcRecordService.updateById(record);
            // grpc返回结果
            AlipayTradeRefundResponseProto.Builder builder = AlipayTradeRefundResponseProto.newBuilder();
            responseObserver.onNext((AlipayTradeRefundResponseProto) ProtoJsonUtil.toObject(builder, objectMapper.writeValueAsString(response)));
            responseObserver.onCompleted();
        } catch (AlipayApiException | IOException e) {
            log.error("支付宝统一收单交易退款接口请求异常", e);
        }
    }

    /**
     * @param request          grpcRequest
     * @param responseObserver grpcResponse
     * @apiNote 支付宝统一收单交易退款查询接口
     * @author 琴声何来
     * @since 2023/2/22 15:34
     */
    @Override
    public void alipayTradeFastpayRefundQuery(AlipayTradeFastpayRefundQueryRequestProto request, StreamObserver<AlipayTradeFastpayRefundQueryResponseProto> responseObserver) {
        //保存请求记录
        ParkingPaymentGrpcRecord record = saveAlipayTradeFastpayRefundQueryRequestRecord(request);
        parkingPaymentGrpcRecordService.save(record);
        //请求支付宝接口
        try {
            alipayTradeFastpayRefundQueryRequest.setBizContent(ProtoJsonUtil.toJson(request));
            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(alipayTradeFastpayRefundQueryRequest);
            if (response.isSuccess()) {
                log.info("支付宝统一收单交易退款查询接口请求成功，responseBody：{}", response.getBody());
            } else {
                log.error("支付宝统一收单交易退款查询接口请求失败，responseBody：{}", response.getBody());
            }
            //更新请求记录
            updateRequestRecord(record, response);
            parkingPaymentGrpcRecordService.updateById(record);
            // grpc返回结果
            AlipayTradeFastpayRefundQueryResponseProto.Builder builder = AlipayTradeFastpayRefundQueryResponseProto.newBuilder();
            responseObserver.onNext((AlipayTradeFastpayRefundQueryResponseProto) ProtoJsonUtil.toObject(builder, objectMapper.writeValueAsString(response)));
            responseObserver.onCompleted();
        } catch (AlipayApiException | IOException e) {
            log.error("支付宝统一收单交易退款查询接口请求异常", e);
        }
    }

    /**
     * @param request          grpcRequest
     * @param responseObserver grpcResponse
     * @apiNote 支付宝查询对账单下载地址接口
     * @author 琴声何来
     * @since 2023/2/22 15:34
     */
    @Override
    public void alipayDataDataserviceBillDownloadurlQuery(AlipayDataDataserviceBillDownloadurlQueryRequestProto request, StreamObserver<AlipayDataDataserviceBillDownloadurlQueryResponseProto> responseObserver) {
        //保存请求记录
        ParkingPaymentGrpcRecord record = saveAlipayDataDataserviceBillDownloadurlQueryRequestRecord(request);
        parkingPaymentGrpcRecordService.save(record);
        //请求支付宝接口
        try {
            alipayDataDataserviceBillDownloadurlQueryRequest.setBizContent(ProtoJsonUtil.toJson(request));
            AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.pageExecute(alipayDataDataserviceBillDownloadurlQueryRequest);
            if (response.isSuccess()) {
                log.info("支付宝查询对账单下载地址接口请求成功，responseBody：{}", response.getBody());
            } else {
                log.error("支付宝查询对账单下载地址接口请求失败，responseBody：{}", response.getBody());
            }
            //更新请求记录
            updateRequestRecord(record, response);
            parkingPaymentGrpcRecordService.updateById(record);
            // grpc返回结果
            AlipayDataDataserviceBillDownloadurlQueryResponseProto.Builder builder = AlipayDataDataserviceBillDownloadurlQueryResponseProto.newBuilder();
            responseObserver.onNext((AlipayDataDataserviceBillDownloadurlQueryResponseProto) ProtoJsonUtil.toObject(builder, objectMapper.writeValueAsString(response)));
            responseObserver.onCompleted();
        } catch (AlipayApiException | IOException e) {
            log.error("支付宝查询对账单下载地址接口请求异常", e);
        }
    }

    /**
     * @param request grpcRequest
     * @return com.czdx.parkingpayment.domain.ParkingPaymentGrpcRecord
     * @apiNote 记录请求日志信息
     * @author 琴声何来
     * @since 2023/3/2 14:24
     */
    private ParkingPaymentGrpcRecord saveAlipayTradeWapPayRequestRecord(AlipayTradeWapPayRequestProto request) {
        ParkingPaymentGrpcRecord record = new ParkingPaymentGrpcRecord();
        record.setOutTradeNo(request.getOutTradeNo());
        record.setTotalAmount(BigDecimal.valueOf(request.getTotalAmount()));
        record.setType(PayTypeConstants.ALI_PAY_TYPE);
        record.setMethod(alipayTradeWapPayRequest.getApiMethodName());
        try {
            record.setRequestMessage(objectMapper.writeValueAsString(request.getAllFields()));
        } catch (JsonProcessingException e) {
            log.error("写入请求信息失败", e);
        }
        record.setCreateTime(LocalDateTime.now());
        return record;
    }

    /**
     * @param request grpcRequest
     * @return com.czdx.parkingpayment.domain.ParkingPaymentGrpcRecord
     * @apiNote 记录请求日志信息
     * @author 琴声何来
     * @since 2023/3/2 14:24
     */
    private ParkingPaymentGrpcRecord saveAlipayTradeQueryRequestRecord(AlipayTradeQueryRequestProto request) {
        ParkingPaymentGrpcRecord record = new ParkingPaymentGrpcRecord();
        record.setOutTradeNo(request.getOutTradeNo());
        record.setTradeNo(request.getTradeNo());
        record.setType(PayTypeConstants.ALI_PAY_TYPE);
        record.setMethod(alipayTradeQueryRequest.getApiMethodName());
        try {
            record.setRequestMessage(objectMapper.writeValueAsString(request.getAllFields()));
        } catch (JsonProcessingException e) {
            log.error("写入请求信息失败", e);
        }
        record.setCreateTime(LocalDateTime.now());
        return record;
    }

    /**
     * @param request grpcRequest
     * @return com.czdx.parkingpayment.domain.ParkingPaymentGrpcRecord
     * @apiNote 记录请求日志信息
     * @author 琴声何来
     * @since 2023/3/2 14:24
     */
    private ParkingPaymentGrpcRecord saveAlipayTradeCloseRequestRecord(AlipayTradeCloseRequestProto request) {
        ParkingPaymentGrpcRecord record = new ParkingPaymentGrpcRecord();
        record.setOutTradeNo(request.getOutTradeNo());
        record.setTradeNo(request.getTradeNo());
        record.setType(PayTypeConstants.ALI_PAY_TYPE);
        record.setMethod(alipayTradeCloseRequest.getApiMethodName());
        try {
            record.setRequestMessage(objectMapper.writeValueAsString(request.getAllFields()));
        } catch (JsonProcessingException e) {
            log.error("写入请求信息失败", e);
        }
        record.setCreateTime(LocalDateTime.now());
        return record;
    }

    /**
     * @param request grpcRequest
     * @return com.czdx.parkingpayment.domain.ParkingPaymentGrpcRecord
     * @apiNote 记录请求日志信息
     * @author 琴声何来
     * @since 2023/3/2 14:24
     */
    private ParkingPaymentGrpcRecord saveAlipayTradeRefundRequestRecord(AlipayTradeRefundRequestProto request) {
        ParkingPaymentGrpcRecord record = new ParkingPaymentGrpcRecord();
        record.setOutTradeNo(request.getOutTradeNo());
        record.setRefundAmount(BigDecimal.valueOf(request.getRefundAmount()));
        record.setType(PayTypeConstants.ALI_PAY_TYPE);
        record.setMethod(alipayTradeRefundRequest.getApiMethodName());
        try {
            record.setRequestMessage(objectMapper.writeValueAsString(request.getAllFields()));
        } catch (JsonProcessingException e) {
            log.error("写入请求信息失败", e);
        }
        record.setCreateTime(LocalDateTime.now());
        return record;
    }

    /**
     * @param request grpcRequest
     * @return com.czdx.parkingpayment.domain.ParkingPaymentGrpcRecord
     * @apiNote 记录请求日志信息
     * @author 琴声何来
     * @since 2023/3/2 14:24
     */
    private ParkingPaymentGrpcRecord saveAlipayTradeFastpayRefundQueryRequestRecord(AlipayTradeFastpayRefundQueryRequestProto request) {
        ParkingPaymentGrpcRecord record = new ParkingPaymentGrpcRecord();
        record.setOutTradeNo(request.getOutTradeNo());
        record.setTradeNo(request.getTradeNo());
        record.setType(PayTypeConstants.ALI_PAY_TYPE);
        record.setMethod(alipayTradeFastpayRefundQueryRequest.getApiMethodName());
        try {
            record.setRequestMessage(objectMapper.writeValueAsString(request.getAllFields()));
        } catch (JsonProcessingException e) {
            log.error("写入请求信息失败", e);
        }
        record.setCreateTime(LocalDateTime.now());
        return record;
    }

    /**
     * @param request grpcRequest
     * @return com.czdx.parkingpayment.domain.ParkingPaymentGrpcRecord
     * @apiNote 记录请求日志信息
     * @author 琴声何来
     * @since 2023/3/2 14:24
     */
    private ParkingPaymentGrpcRecord saveAlipayDataDataserviceBillDownloadurlQueryRequestRecord(AlipayDataDataserviceBillDownloadurlQueryRequestProto request) {
        ParkingPaymentGrpcRecord record = new ParkingPaymentGrpcRecord();
        record.setType(PayTypeConstants.ALI_PAY_TYPE);
        record.setMethod(alipayDataDataserviceBillDownloadurlQueryRequest.getApiMethodName());
        try {
            record.setRequestMessage(objectMapper.writeValueAsString(request.getAllFields()));
        } catch (JsonProcessingException e) {
            log.error("写入请求信息失败", e);
        }
        record.setCreateTime(LocalDateTime.now());
        return record;
    }

    /**
     * @param record   请求日志信息
     * @param response 支付宝接口返回结果
     * @apiNote 更新请求日志信息
     * @author 琴声何来
     * @since 2023/3/2 15:10
     */
    private void updateRequestRecord(ParkingPaymentGrpcRecord record, AlipayResponse response) throws JsonProcessingException {
        record.setResponseMessage(objectMapper.writeValueAsString(response));
        record.setUpdateTime(LocalDateTime.now());
    }
}
