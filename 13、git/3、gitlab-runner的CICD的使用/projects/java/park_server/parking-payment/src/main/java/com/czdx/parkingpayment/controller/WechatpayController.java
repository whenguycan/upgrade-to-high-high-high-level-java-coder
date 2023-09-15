package com.czdx.parkingpayment.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.czdx.grpc.lib.wechatpay.WechatpayTradeH5PayRequestProto;
import com.czdx.grpc.lib.wechatpay.WechatpayTradeH5PayResponseProto;
import com.czdx.grpc.lib.wechatpay.WechatpayTradeJsapiPayRequestProto;
import com.czdx.grpc.lib.wechatpay.WechatpayTradeJsapiPayResponseProto;
import com.czdx.parkingpayment.common.constant.WechatConstants;
import com.czdx.parkingpayment.config.MyWechatpayConfig;
import com.czdx.parkingpayment.domain.WechatpayNotification;
import com.czdx.parkingpayment.service.IRabbitMessageProvider;
import com.czdx.parkingpayment.service.IWechatpayNotificationService;
import com.czdx.parkingpayment.service.grpc.AlipayTradeGrpcServiceImpl;
import com.czdx.parkingpayment.service.grpc.WechatpayTradeGrpcServiceImpl;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.Refund;
import com.wechat.pay.java.service.refund.model.RefundNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/wechat")
public class WechatpayController {

    @Autowired
    private JsapiServiceExtension jsapiServiceExtension;

    @Autowired
    private RefundService refundService;

    @Autowired
    private MyWechatpayConfig myWechatpayConfig;

    @Autowired
    private IRabbitMessageProvider rabbitMessageProvider;

    @Autowired
    private IWechatpayNotificationService wechatpayNotificationService;

    @Autowired
    WechatpayTradeGrpcServiceImpl wechatpayTradeGrpcService;

    @Autowired
    AlipayTradeGrpcServiceImpl alipayTradeGrpcService;

    @PostMapping("/notify")
    public String notifyUrl(HttpServletRequest request, @RequestBody String requestBody) {
        log.info("接收到微信支付异步通知回调，body：{}", requestBody);
        Iterator<String> iterator = request.getHeaderNames().asIterator();
        log.info("接收到微信支付异步通知回调，header：");
        while (iterator.hasNext()) {
            String headerName = iterator.next();
            log.info("header------key：{}，value：{}", headerName, request.getHeader(headerName));
        }
        //获取请求头中的值，构建RequestParam对象
        RequestParam requestParam = new RequestParam.Builder()
                .serialNumber(request.getHeader("Wechatpay-Serial"))
                .nonce(request.getHeader("Wechatpay-Nonce"))
                .signature(request.getHeader("Wechatpay-Signature"))
                .timestamp(request.getHeader("Wechatpay-Timestamp"))
                // 若未设置signType，默认值为 WECHATPAY2-SHA256-RSA2048
                .signType(request.getHeader("Wechatpay-Signature-Type"))
                .body(requestBody)
                .build();
        NotificationParser parser = new NotificationParser(myWechatpayConfig.getInstance());
        JSONObject data = JSON.parseObject(requestBody);
        if (WechatConstants.TRANSACTION_SUCCESS_EVENT_TYPE.equals(data.getString("event_type"))) {
            //支付结果通知解密对象类为 Transaction
            Transaction transaction = parser.parse(requestParam, Transaction.class);
            log.info("支付通知解密对象：{}", transaction);
            //保存回调参数
            WechatpayNotification wechatpayNotification = new WechatpayNotification();
            copy(transaction, wechatpayNotification);
            if (wechatpayNotificationService.add(wechatpayNotification)) {
                rabbitMessageProvider.pushWechatpayResultMessage(transaction, wechatpayNotification.getNotifyId());
            }
        } else if (WechatConstants.REFUND_SUCCESS_EVENT_TYPE.equals(data.getString("event_type"))) {
            //退款结果通知解密对象类为 RefundNotification
            RefundNotification refundNotification = parser.parse(requestParam, RefundNotification.class);
            log.info("退款通知解密对象：{}", refundNotification);
            //保存回调参数
            WechatpayNotification wechatpayNotification = new WechatpayNotification();
            copyRefundNotification(refundNotification, wechatpayNotification);
            if (wechatpayNotificationService.add(wechatpayNotification)) {
                alipayTradeGrpcService.notifyOrderRefundSuccess(wechatpayNotification.getOutTradeNo(), wechatpayNotification.getOutRefundNo());
            }
        }
        //微信支付结果异步通知回调只需HTTP应答状态码为200或204即视为接收通知成功，无需返回应答报文
        return null;
    }


    @PostMapping("/jsapi/prepay")
    public PrepayWithRequestPaymentResponse jsapiPrepay(@RequestBody Map map) {
        WechatpayTradeJsapiPayRequestProto prepayRequest = WechatpayTradeJsapiPayRequestProto.newBuilder()
                .setOutTradeNo((String) map.get("outTradeNo"))
                .setDescription((String) map.get("description"))
                .setOpenid((String) map.get("openid"))
                .setTotal((Double) map.get("total"))
                .build();
//        PrepayRequest prepayRequest = new PrepayRequest();
//        prepayRequest.setDescription("测试商品");
//        prepayRequest.setOutTradeNo(UUID.randomUUID().toString());
//        prepayRequest.setNotifyUrl("http://hy-web.cz19.tk:8080/alipay/notify");
//        Amount amount = new Amount();
//        amount.setTotal(10);
//        prepayRequest.setAmount(amount);
//        Payer payer = new Payer();
//        payer.setOpenid("openId");
//        prepayRequest.setPayer(payer);
//        PrepayResponse response = jsapiService.prepay(prepayRequest);
//        log.info("response：{}", response);
        return wechatpayTradeGrpcService.wechatpayTradeJsapiPay(prepayRequest);
    }

    @PostMapping("/h5/prepay")
    public com.wechat.pay.java.service.payments.h5.model.PrepayResponse h5Prepay(@RequestBody Map map) {
        WechatpayTradeH5PayRequestProto prepayRequest = WechatpayTradeH5PayRequestProto.newBuilder()
                .setOutTradeNo((String) map.get("outTradeNo"))
                .setDescription((String) map.get("description"))
                .setPayerClientIp((String) map.get("payerClientIp"))
                .setH5Type((String) map.get("h5Type"))
                .setTotal((Double) map.get("total"))
                .build();
//        PrepayRequest prepayRequest = new PrepayRequest();
//        prepayRequest.setDescription("测试商品");
//        prepayRequest.setOutTradeNo(UUID.randomUUID().toString());
//        prepayRequest.setNotifyUrl("http://hy-web.cz19.tk:8080/alipay/notify");
//        Amount amount = new Amount();
//        amount.setTotal(10);
//        prepayRequest.setAmount(amount);
//        Payer payer = new Payer();
//        payer.setOpenid("openId");
//        prepayRequest.setPayer(payer);
//        PrepayResponse response = jsapiService.prepay(prepayRequest);
//        log.info("response：{}", response);
        return wechatpayTradeGrpcService.wechatpayTradeH5Pay(prepayRequest);
    }

    @PostMapping("/refund")
    public Refund jsapiRefund(@RequestBody CreateRequest createRequest) {
        return refundService.create(createRequest);
    }

    private void copy(Transaction transaction, WechatpayNotification wechatpayNotification) {
        wechatpayNotification.setOutTradeNo(transaction.getOutTradeNo());
        wechatpayNotification.setTransactionId(transaction.getTransactionId());
        wechatpayNotification.setSuccessTime(LocalDateTime.parse(transaction.getSuccessTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        wechatpayNotification.setNotifyId(UUID.randomUUID().toString());
        wechatpayNotification.setAppId(transaction.getAppid());
        wechatpayNotification.setMchId(transaction.getMchid());
        wechatpayNotification.setTradeType(transaction.getTradeType().name());
        wechatpayNotification.setOpenId(transaction.getPayer().getOpenid());
        wechatpayNotification.setTradeState(transaction.getTradeState().name());
        wechatpayNotification.setTradeStateDesc(transaction.getTradeStateDesc());
        wechatpayNotification.setBankType(transaction.getBankType());
        wechatpayNotification.setAttach(transaction.getAttach());
        wechatpayNotification.setTotal(BigDecimal.valueOf(transaction.getAmount().getTotal()).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING));
        wechatpayNotification.setPayerTotal(BigDecimal.valueOf(transaction.getAmount().getPayerTotal()).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING));
        wechatpayNotification.setCurrency(transaction.getAmount().getCurrency());
        wechatpayNotification.setPayerCurrency(transaction.getAmount().getPayerCurrency());
        wechatpayNotification.setPromotionDetail(JSON.toJSONString(transaction.getPromotionDetail()));
    }

    private void copyRefundNotification(RefundNotification refundNotification, WechatpayNotification wechatpayNotification) {
        wechatpayNotification.setOutTradeNo(refundNotification.getOutTradeNo());
        wechatpayNotification.setTransactionId(refundNotification.getTransactionId());
        wechatpayNotification.setOutRefundNo(refundNotification.getOutRefundNo());
        wechatpayNotification.setRefundId(refundNotification.getRefundId());
        wechatpayNotification.setSuccessTime(LocalDateTime.parse(refundNotification.getSuccessTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        wechatpayNotification.setNotifyId(UUID.randomUUID().toString());
        wechatpayNotification.setRefundStatus(refundNotification.getRefundStatus().name());
        wechatpayNotification.setTotal(BigDecimal.valueOf(refundNotification.getAmount().getTotal()).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING));
        wechatpayNotification.setRefund(BigDecimal.valueOf(refundNotification.getAmount().getRefund()).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING));
        wechatpayNotification.setPayerTotal(BigDecimal.valueOf(refundNotification.getAmount().getPayerTotal()).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING));
        wechatpayNotification.setPayerRefund(BigDecimal.valueOf(refundNotification.getAmount().getPayerRefund()).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING));
        wechatpayNotification.setCurrency(refundNotification.getAmount().getCurrency());
        wechatpayNotification.setPromotionDetail(JSON.toJSONString(refundNotification.getPromotionDetail()));
    }
}
