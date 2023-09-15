package com.czdx.parkingpayment.controller;

import com.alibaba.fastjson.JSON;
import com.czdx.parkingpayment.config.MyWechatpayConfig;
import com.czdx.parkingpayment.domain.WechatpayNotification;
import com.czdx.parkingpayment.domain.notification.AlipayNotificationStr;
import com.czdx.parkingpayment.service.IRabbitMessageProvider;
import com.czdx.parkingpayment.service.IWechatpayNotificationService;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.Notification;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
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
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/wechat")
public class WechatpayController {

//    @Autowired
//    private JsapiService jsapiService;

//    @Autowired
//    private MyWechatpayConfig myWechatpayConfig;

    @Autowired
    private IRabbitMessageProvider rabbitMessageProvider;

    @Autowired
    private IWechatpayNotificationService wechatpayNotificationService;

//    @PostMapping("/notify")
//    public String notifyUrl(HttpServletRequest request, @RequestBody String requestBody) {
//        //获取请求头中的值，构建RequestParam对象
//        RequestParam requestParam = new RequestParam.Builder()
//                .serialNumber(request.getHeader("Wechatpay-Serial"))
//                .nonce(request.getHeader("Wechatpay-Nonce"))
//                .signature(request.getHeader("Wechatpay-Signature"))
//                .timestamp(request.getHeader("Wechatpay-Timestamp"))
//// 若未设置signType，默认值为 WECHATPAY2-SHA256-RSA2048
//                .signType("Wechatpay-Signature-Type")
//                .body(requestBody)
//                .build();
//        //支付结果通知解密对象类为 Transaction
//        NotificationParser parser = new NotificationParser(myWechatpayConfig.getInstance());
//        Transaction transaction = parser.parse(requestParam, Transaction.class);
//        //保存回调参数
//        WechatpayNotification wechatpayNotification=new WechatpayNotification();
//        copy(transaction, wechatpayNotification);
//        wechatpayNotificationService.add(wechatpayNotification);
//        rabbitMessageProvider.pushWechatpayResultMessage(transaction,wechatpayNotification.getNotifyId());
//        return null;
//    }

//
//    @PostMapping("/jsapi/prepay")
//    public String jsapiPrepay() {
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
//        return null;
//    }

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
}
