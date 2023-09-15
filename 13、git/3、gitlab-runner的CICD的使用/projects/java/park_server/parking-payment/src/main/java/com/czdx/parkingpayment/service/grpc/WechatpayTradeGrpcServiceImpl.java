package com.czdx.parkingpayment.service.grpc;

import com.czdx.grpc.lib.wechatpay.*;
import com.czdx.parkingpayment.common.constant.PayMethodConstants;
import com.czdx.parkingpayment.common.utils.StringUtils;
import com.czdx.parkingpayment.config.MyWechatpayConfig;
import com.czdx.parkingpayment.domain.ParkingPaymentGrpcRecord;
import com.czdx.parkingpayment.service.IParkingPaymentGrpcRecordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.pay.java.service.payments.h5.H5Service;
import com.wechat.pay.java.service.payments.h5.model.H5Info;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.AmountReq;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.Refund;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@GrpcService
public class WechatpayTradeGrpcServiceImpl extends WechatPayServiceGrpc.WechatPayServiceImplBase {

    @Autowired
    private JsapiServiceExtension jsapiServiceExtension;

    @Autowired
    private H5Service h5Service;

    @Autowired
    private RefundService refundService;

    @Autowired
    private IParkingPaymentGrpcRecordService parkingPaymentGrpcRecordService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param request          grpcRequest
     * @param responseObserver grpcResponse 包括前端js拉起支付的参数
     * @apiNote 微信支付JSAPI支付接口
     */
    @Override
    public void wechatpayTradeJsapiPay(WechatpayTradeJsapiPayRequestProto request, StreamObserver<WechatpayTradeJsapiPayResponseProto> responseObserver) {
        ParkingPaymentGrpcRecord record = saveWechatpayTradeJsapiPayRequestRecord(request);
        parkingPaymentGrpcRecordService.save(record);
        try {
            PrepayRequest prepayRequest = new PrepayRequest();
            prepayRequest.setOutTradeNo(request.getOutTradeNo());
            prepayRequest.setNotifyUrl(MyWechatpayConfig.NOTIFY_URL);
            prepayRequest.setDescription(request.getDescription());
            prepayRequest.setAppid(MyWechatpayConfig.APP_ID);
            prepayRequest.setMchid(MyWechatpayConfig.MCH_ID);
            Payer payer = new Payer();
            payer.setOpenid(request.getOpenid());
            Amount amount = new Amount();
            // 微信支付所有请求参数都是以分为单位的int格式，为保持内部一致，需*100进行转换
            amount.setTotal(BigDecimal.valueOf(request.getTotal() * 100).intValue());
            amount.setCurrency("CNY");
            prepayRequest.setPayer(payer);
            prepayRequest.setAmount(amount);
            log.info("wechat request:{}", prepayRequest);
            //请求微信获取预支付id
            PrepayWithRequestPaymentResponse prepayResponse = jsapiServiceExtension.prepayWithRequestPayment(prepayRequest);
            log.info("wechat response:{}", prepayResponse);
            //更新请求记录
            updateJsapiPayRequestRecord(record, prepayResponse);
            parkingPaymentGrpcRecordService.updateById(record);
            WechatpayTradeJsapiPayResponseProto response = WechatpayTradeJsapiPayResponseProto.newBuilder()
                    .setAppId(prepayResponse.getAppId())
                    .setPackage(prepayResponse.getPackageVal())
                    .setTimestamp(prepayResponse.getTimeStamp())
                    .setNonceStr(prepayResponse.getNonceStr())
                    .setSignType(prepayResponse.getSignType())
                    .setPaySign(prepayResponse.getPaySign())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("微信支付Jsapi下单接口请求异常", e);
            responseObserver.onError(e);
            responseObserver.onCompleted();
        }
    }

    /**
     * @param request          grpcRequest
     * @param responseObserver grpcResponse 包括前端js拉起支付的参数
     * @apiNote 微信支付H5支付接口
     */
    @Override
    public void wechatpayTradeH5Pay(WechatpayTradeH5PayRequestProto request, StreamObserver<WechatpayTradeH5PayResponseProto> responseObserver) {
        ParkingPaymentGrpcRecord record = saveWechatpayTradeH5PayRequestRecord(request);
        parkingPaymentGrpcRecordService.save(record);
        try {
            com.wechat.pay.java.service.payments.h5.model.PrepayRequest prepayRequest = new com.wechat.pay.java.service.payments.h5.model.PrepayRequest();
            prepayRequest.setAppid(MyWechatpayConfig.APP_ID);
            prepayRequest.setMchid(MyWechatpayConfig.MCH_ID);
            prepayRequest.setNotifyUrl(MyWechatpayConfig.NOTIFY_URL);
            prepayRequest.setOutTradeNo(request.getOutTradeNo());
            prepayRequest.setDescription(request.getDescription());
            //客户端IP
            com.wechat.pay.java.service.payments.h5.model.SceneInfo sceneInfo = new com.wechat.pay.java.service.payments.h5.model.SceneInfo();
            sceneInfo.setPayerClientIp(request.getPayerClientIp());
            H5Info h5Info = new H5Info();
            h5Info.setType(request.getH5Type());
            sceneInfo.setH5Info(h5Info);
            prepayRequest.setSceneInfo(sceneInfo);
            com.wechat.pay.java.service.payments.h5.model.Amount amount = new com.wechat.pay.java.service.payments.h5.model.Amount();
            // 微信支付所有请求参数都是以分为单位的int格式，为保持内部一致，需*100进行转换
            amount.setTotal(BigDecimal.valueOf(request.getTotal() * 100).intValue());
            amount.setCurrency("CNY");
            prepayRequest.setAmount(amount);
            log.info("wechat request:{}", prepayRequest);
            //请求微信获取预支付id
            com.wechat.pay.java.service.payments.h5.model.PrepayResponse prepayResponse = h5Service.prepay(prepayRequest);
            log.info("wechat response:{}", prepayResponse);
            //更新请求记录
            updateH5PayRequestRecord(record, prepayResponse);
            parkingPaymentGrpcRecordService.updateById(record);
            WechatpayTradeH5PayResponseProto response = WechatpayTradeH5PayResponseProto.newBuilder()
                    .setPayUrl(prepayResponse.getH5Url())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("微信支付H5下单接口请求异常", e);
            responseObserver.onError(e);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void wechatpayTradeRefund(WechatpayTradeRefundRequestProto request, StreamObserver<WechatpayTradeRefundResponseProto> responseObserver) {
        ParkingPaymentGrpcRecord record = saveWechatpayTradeRefundRequestRecord(request);
        parkingPaymentGrpcRecordService.save(record);
        try {
            CreateRequest createRequest = new CreateRequest();
            createRequest.setOutTradeNo(request.getOutTradeNo());
            createRequest.setOutRefundNo(request.getOutRefundNo());
            createRequest.setReason(request.getReason());
            createRequest.setNotifyUrl(MyWechatpayConfig.NOTIFY_URL);
            //退款回调通知地址
//            createRequest.setNotifyUrl();
            // 微信支付所有请求参数都是以分为单位的int格式，为保持内部一致，需*100进行转换
            AmountReq amountReq = new AmountReq();
            amountReq.setRefund(BigDecimal.valueOf(request.getRefund()).multiply(BigDecimal.valueOf(100)).longValue());
            amountReq.setTotal(BigDecimal.valueOf(request.getTotal()).multiply(BigDecimal.valueOf(100)).longValue());
            amountReq.setCurrency("CNY");
            createRequest.setAmount(amountReq);
            log.info("wechat request:{}", createRequest);
            //请求微信获取退款参数
            Refund refund = refundService.create(createRequest);
            log.info("wechat response:{}", refund);
            //更新请求记录
            updateRefundRequestRecord(record, refund);
            parkingPaymentGrpcRecordService.updateById(record);
            WechatpayTradeRefundResponseProto.Builder builder = WechatpayTradeRefundResponseProto.newBuilder();
            if (StringUtils.isNotEmpty(refund.getSuccessTime())) {
                builder.setSuccessTime(refund.getSuccessTime());
            }
            WechatpayTradeRefundResponseProto response = builder
                    .setOutTradeNo(refund.getOutTradeNo())
                    .setOutRefundNo(refund.getOutRefundNo())
                    .setStatus(refund.getStatus().name())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("微信支付退款接口请求异常", e);
            responseObserver.onError(e);
            responseObserver.onCompleted();
        }
    }

    public PrepayWithRequestPaymentResponse wechatpayTradeJsapiPay(WechatpayTradeJsapiPayRequestProto request) {
        PrepayRequest prepayRequest = new PrepayRequest();
        prepayRequest.setOutTradeNo(request.getOutTradeNo());
        prepayRequest.setNotifyUrl(MyWechatpayConfig.NOTIFY_URL);
        prepayRequest.setDescription(request.getDescription());
        prepayRequest.setAppid(MyWechatpayConfig.APP_ID);
        prepayRequest.setMchid(MyWechatpayConfig.MCH_ID);
        Payer payer = new Payer();
        payer.setOpenid(request.getOpenid());
        Amount amount = new Amount();
        // 微信支付所有请求参数都是以分为单位的int格式，为保持内部一致，需*100进行转换
        amount.setTotal(BigDecimal.valueOf(request.getTotal() * 100).intValue());
        amount.setCurrency("CNY");
        prepayRequest.setPayer(payer);
        prepayRequest.setAmount(amount);
        //请求微信获取预支付id
        PrepayWithRequestPaymentResponse prepayResponse = jsapiServiceExtension.prepayWithRequestPayment(prepayRequest);
        WechatpayTradeJsapiPayResponseProto response = WechatpayTradeJsapiPayResponseProto.newBuilder()
                .setAppId(prepayResponse.getAppId())
                .setPackage(prepayResponse.getPackageVal())
                .setTimestamp(prepayResponse.getTimeStamp())
                .setNonceStr(prepayResponse.getNonceStr())
                .setSignType(prepayResponse.getSignType())
                .setPaySign(prepayResponse.getPaySign())
                .build();
        log.info("response:{}", response);
        return prepayResponse;
    }

    public com.wechat.pay.java.service.payments.h5.model.PrepayResponse wechatpayTradeH5Pay(WechatpayTradeH5PayRequestProto request) {
        com.wechat.pay.java.service.payments.h5.model.PrepayRequest prepayRequest = new com.wechat.pay.java.service.payments.h5.model.PrepayRequest();
        prepayRequest.setAppid(MyWechatpayConfig.APP_ID);
        prepayRequest.setMchid(MyWechatpayConfig.MCH_ID);
        prepayRequest.setNotifyUrl(MyWechatpayConfig.NOTIFY_URL);
        prepayRequest.setOutTradeNo(request.getOutTradeNo());
        prepayRequest.setDescription(request.getDescription());
        //客户端IP
        com.wechat.pay.java.service.payments.h5.model.SceneInfo sceneInfo = new com.wechat.pay.java.service.payments.h5.model.SceneInfo();
        sceneInfo.setPayerClientIp(request.getPayerClientIp());
        H5Info h5Info = new H5Info();
        h5Info.setType(request.getH5Type());
        sceneInfo.setH5Info(h5Info);
        prepayRequest.setSceneInfo(sceneInfo);
        com.wechat.pay.java.service.payments.h5.model.Amount amount = new com.wechat.pay.java.service.payments.h5.model.Amount();
        // 微信支付所有请求参数都是以分为单位的int格式，为保持内部一致，需*100进行转换
        amount.setTotal(BigDecimal.valueOf(request.getTotal() * 100).intValue());
        amount.setCurrency("CNY");
        prepayRequest.setAmount(amount);
        //请求微信获取预支付id
        com.wechat.pay.java.service.payments.h5.model.PrepayResponse prepayResponse = h5Service.prepay(prepayRequest);
        WechatpayTradeH5PayResponseProto response = WechatpayTradeH5PayResponseProto.newBuilder()
                .setPayUrl(prepayResponse.getH5Url())
                .build();
        return prepayResponse;
    }

    /**
     * @param request H5GrpcRequest
     * @return com.czdx.parkingpayment.domain.ParkingPaymentGrpcRecord
     * @apiNote 记录请求日志信息
     * @author 琴声何来
     * @since 2023/3/21 13:45
     */
    private ParkingPaymentGrpcRecord saveWechatpayTradeJsapiPayRequestRecord(WechatpayTradeJsapiPayRequestProto request) {
        ParkingPaymentGrpcRecord record = new ParkingPaymentGrpcRecord();
        record.setOutTradeNo(request.getOutTradeNo());
        record.setTotalAmount(BigDecimal.valueOf(request.getTotal()));
        record.setType(PayMethodConstants.WECHAT_PAY);
        record.setMethod("jsapi");
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
     * @param response 微信支付Jsapi下单接口返回结果
     * @apiNote 更新请求日志信息
     * @author 琴声何来
     * @since 2023/3/21 13:54
     */
    private void updateJsapiPayRequestRecord(ParkingPaymentGrpcRecord record, PrepayWithRequestPaymentResponse response) throws JsonProcessingException {
        record.setResponseMessage(objectMapper.writeValueAsString(response));
        record.setUpdateTime(LocalDateTime.now());
    }

    /**
     * @param request JsapiGrpcRequest
     * @return com.czdx.parkingpayment.domain.ParkingPaymentGrpcRecord
     * @apiNote 记录请求日志信息
     * @author 琴声何来
     * @since 2023/3/21 14:01
     */
    private ParkingPaymentGrpcRecord saveWechatpayTradeH5PayRequestRecord(WechatpayTradeH5PayRequestProto request) {
        ParkingPaymentGrpcRecord record = new ParkingPaymentGrpcRecord();
        record.setOutTradeNo(request.getOutTradeNo());
        record.setTotalAmount(BigDecimal.valueOf(request.getTotal()));
        record.setType(PayMethodConstants.WECHAT_PAY);
        record.setMethod("h5");
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
     * @param response 微信支付H5下单接口返回结果
     * @apiNote 更新请求日志信息
     * @author 琴声何来
     * @since 2023/3/21 14:00
     */
    private void updateH5PayRequestRecord(ParkingPaymentGrpcRecord record, com.wechat.pay.java.service.payments.h5.model.PrepayResponse response) throws JsonProcessingException {
        record.setResponseMessage(objectMapper.writeValueAsString(response));
        record.setUpdateTime(LocalDateTime.now());
    }

    /**
     * @param request JsapiGrpcRequest
     * @return com.czdx.parkingpayment.domain.ParkingPaymentGrpcRecord
     * @apiNote 记录请求日志信息
     * @author 琴声何来
     * @since 2023/3/21 14:01
     */
    private ParkingPaymentGrpcRecord saveWechatpayTradeRefundRequestRecord(WechatpayTradeRefundRequestProto request) {
        ParkingPaymentGrpcRecord record = new ParkingPaymentGrpcRecord();
        record.setOutTradeNo(request.getOutTradeNo());
        record.setTotalAmount(BigDecimal.valueOf(request.getTotal()));
        record.setRefundAmount(BigDecimal.valueOf(request.getRefund()));
        record.setType(PayMethodConstants.WECHAT_PAY);
        record.setMethod("refund");
        try {
            record.setRequestMessage(objectMapper.writeValueAsString(request.getAllFields()));
        } catch (JsonProcessingException e) {
            log.error("写入请求信息失败", e);
        }
        record.setCreateTime(LocalDateTime.now());
        return record;
    }

    /**
     * @param record 请求日志信息
     * @param refund 微信支付退款接口返回结果
     * @apiNote 更新请求日志信息
     * @author 琴声何来
     * @since 2023/3/21 14:00
     */
    private void updateRefundRequestRecord(ParkingPaymentGrpcRecord record, Refund refund) throws JsonProcessingException {
        record.setResponseMessage(objectMapper.writeValueAsString(refund));
        record.setUpdateTime(LocalDateTime.now());
    }
}
