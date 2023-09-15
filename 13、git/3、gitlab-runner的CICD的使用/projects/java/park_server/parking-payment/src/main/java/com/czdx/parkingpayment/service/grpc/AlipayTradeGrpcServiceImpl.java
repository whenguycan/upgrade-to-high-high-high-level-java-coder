package com.czdx.parkingpayment.service.grpc;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.czdx.grpc.lib.alipay.*;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import com.czdx.parkingpayment.common.constant.PayMethodConstants;
import com.czdx.parkingpayment.domain.ParkingPaymentGrpcRecord;
import com.czdx.parkingpayment.service.IParkingPaymentGrpcRecordService;
import com.czdx.parkingpayment.utils.ProtoJsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    @GrpcClient("parking-order-server")
    ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub parkingOrderServiceBlockingStub;

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

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
            responseObserver.onError(e);
            responseObserver.onCompleted();
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
            responseObserver.onError(e);
            responseObserver.onCompleted();
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
            responseObserver.onError(e);
            responseObserver.onCompleted();
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
            if (response.isSuccess()) {
                log.info("支付宝统一收单交易退款接口请求成功，responseBody：{}", response.getBody());
                if ("Y".equals(response.getFundChange())) {
                    log.info("支付宝退款成功，资金变动成功");
                } else {
                    log.info("资金变动失败，调用查询接口");
                    Thread thread = new Thread(() -> {
                        try {
                            //查询接口需等待退款接口10s以上，不然有可能出现先查询后退款的情况
                            log.info("开始调用退款查询接口");
                            AlipayTradeFastpayRefundQueryRequestProto requestProto = AlipayTradeFastpayRefundQueryRequestProto.newBuilder()
                                    .setOutTradeNo(request.getOutTradeNo())
                                    .setOutRequestNo(request.getOutRequestNo())
                                    .build();
                            //请求支付宝接口
                            alipayTradeFastpayRefundQueryRequest.setBizContent(ProtoJsonUtil.toJson(requestProto));
                            AlipayTradeFastpayRefundQueryResponse alipayTradeFastpayRefundQueryResponse = alipayClient.execute(alipayTradeFastpayRefundQueryRequest);
                            if (alipayTradeFastpayRefundQueryResponse.isSuccess()) {
                                if (alipayTradeFastpayRefundQueryResponse.getRefundStatus().equals("REFUND_SUCCESS")) {
                                    log.info("资金变动失败后调用查询接口，查询接口返回退款成功，responseBody：{}", response.getBody());
                                    //grpc调用订单系统，通知退款成功
                                    notifyOrderRefundSuccess(request.getOutTradeNo(), request.getOutRequestNo());
                                } else {
                                    log.info("资金变动失败后调用查询接口，查询接口返回退款失败，responseBody：{}", response.getBody());
                                }
                            } else {
                                log.error("支付宝统一收单交易退款查询接口请求失败，responseBody：{}", response.getBody());
                            }
                        } catch (InvalidProtocolBufferException e) {
                            log.error("proto转换出现异常", e);
                        } catch (AlipayApiException e) {
                            log.error("alipayClient调用出现异常", e);
                        }
                    });
                    executorService.schedule(thread, 15, TimeUnit.SECONDS);
                }
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
            responseObserver.onError(e);
            responseObserver.onCompleted();
        }
    }

    /**
     * @param orderNo  支付订单号
     * @param refundNo 退款单号
     * @apiNote 主动通知订单系统退款成功
     * @author 琴声何来
     * @since 2023/4/20 17:16
     */
    public void notifyOrderRefundSuccess(String orderNo, String refundNo) {
        log.info("通知订单系统退款成功，orderNo：{}，refundNo：{}", orderNo, refundNo);
        ParkingOrder.RefundOrderNotifyResponseProto refundOrderNotifyResponseProto = parkingOrderServiceBlockingStub.refundOrderNotify(ParkingOrder.RefundOrderNotifyRequestProto.newBuilder()
                .setOrderNo(orderNo)
                .setRefundNo(refundNo)
                .build());
        if (refundOrderNotifyResponseProto.getStatus().equals("200")) {
            log.info("grpc调用订单系统修改退款状态成功");
        } else {
            log.error("grpc调用订单系统修改退款状态失败，orderNo：{}，refundNo：{}", orderNo, refundNo);
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
            responseObserver.onError(e);
            responseObserver.onCompleted();
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
            responseObserver.onError(e);
            responseObserver.onCompleted();
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
        record.setType(PayMethodConstants.ALI_PAY);
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
        record.setType(PayMethodConstants.ALI_PAY);
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
        record.setType(PayMethodConstants.ALI_PAY);
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
        record.setType(PayMethodConstants.ALI_PAY);
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
        record.setType(PayMethodConstants.ALI_PAY);
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
        record.setType(PayMethodConstants.ALI_PAY);
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
