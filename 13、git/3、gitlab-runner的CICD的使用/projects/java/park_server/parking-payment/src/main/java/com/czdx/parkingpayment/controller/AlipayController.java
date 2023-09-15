package com.czdx.parkingpayment.controller;


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.czdx.grpc.lib.alipay.*;
import com.czdx.parkingpayment.common.utils.StringUtils;
import com.czdx.parkingpayment.config.MyAlipayConfig;
import com.czdx.parkingpayment.domain.AlipayNotification;
import com.czdx.parkingpayment.domain.ParkingPaymentGrpcRecord;
import com.czdx.parkingpayment.domain.notification.AlipayNotificationStr;
import com.czdx.parkingpayment.service.IAlipayNotificationService;
import com.czdx.parkingpayment.service.IParkingPaymentGrpcRecordService;
import com.czdx.parkingpayment.service.IRabbitMessageProvider;
import com.czdx.parkingpayment.service.grpc.AlipayTradeGrpcServiceImpl;
import com.czdx.parkingpayment.utils.ProtoJsonUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * date: 2023/2/21 11:30
 *
 * @author 琴声何来
 * @apiNote 支付宝支付相关
 */
@Slf4j
@RestController
@RequestMapping("/alipay")
public class AlipayController {
    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private IRabbitMessageProvider rabbitMessageProvider;

    @Autowired
    private IParkingPaymentGrpcRecordService parkingPaymentGrpcRecordService;

    @Autowired
    private IAlipayNotificationService alipayNotificationService;

    @Autowired
    AlipayTradeWapPayRequest alipayTradeWapPayRequest;

    @Autowired
    AlipayTradeGrpcServiceImpl alipayTradeGrpcService;

    /**
     * @param alipayNotificationStr 支付宝异步回调消息内容，因验签需要，全部由String进行接收，在copy()方法中转成AlipayNotification格式进行正常使用
     * @return 支付宝只接收两种响应信息，在不返回success时，支付宝将会进行重试。success：成功 fail：失败
     * @apiNote 支付宝异步回调消息通知
     * @author 琴声何来
     * @since 2023/3/2 16:18
     */
    @PostMapping("/notify")
    public String notifyUrl(AlipayNotificationStr alipayNotificationStr) {
        log.info("支付宝异步消息通知str:{}", JSON.toJSONString(alipayNotificationStr));
        //校验请求是否来自支付宝
        if (!verify(alipayNotificationStr)) {
            log.error("验签失败");
            //请求非法，直接忽略
            return "fail";
        }
        // 转换请求参数格式
        AlipayNotification alipayNotification = new AlipayNotification();
        copy(alipayNotificationStr, alipayNotification);
        log.info("支付宝异步消息通知notify:{}", alipayNotification);
        //保存回调参数，根据notifyId设置唯一判断是否重复通知
        if ("TRADE_SUCCESS".equals(alipayNotification.getTradeStatus()) && alipayNotificationService.add(alipayNotification)) {
            //交易成功通知
            //触发业务回调通知
            rabbitMessageProvider.pushAlipayResultMessage(alipayNotification);
        } else if ("TRADE_CLOSED".equals(alipayNotification.getTradeStatus()) && alipayNotification.getGmtRefund() != null && alipayNotificationService.add(alipayNotification)) {
            //订单为关闭状态，且退款时间不为空，则为全额退款通知
            alipayTradeGrpcService.notifyOrderRefundSuccess(alipayNotification.getOutTradeNo(), alipayNotification.getOutBizNo());
        }
        // 通知支付宝正确响应回调
        return "success";
    }

    @GetMapping("/health")
    public String health() {
        return "{\"status\":\"UP\"}";
    }

    /**
     * @param aaa 1
     * @return java.lang.String 网站支付跳转链接
     * @apiNote 支付宝手机网站支付接口
     */
    @GetMapping("/trade/wap/pay")
    public String tradeWapPay(String aaa) {

//        AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.URL, AliPayConfig.APPID, AliPayConfig.RSA_PRIVATE_KEY, AliPayConfig.FORMAT, AliPayConfig.CHARSET, AliPayConfig.ALIPAY_PUBLIC_KEY, AliPayConfig.SIGNTYPE);
//        alipayTradeWapPayRequest.setNotifyUrl(MyAlipayConfig.NOTIFY_URL);
        AlipayTradeWapPayRequestProto proto = AlipayTradeWapPayRequestProto.newBuilder()
                .setOutTradeNo(UUID.randomUUID().toString())
                .setTotalAmount(100)
                .setSubject("测试商品")
                .setQuitUrl("https://www.hao123.com")
                .build();
        log.info("outTradeNo:{}", proto.getOutTradeNo());
        AlipayTradeWapPayResponse response = null;
        try {
            alipayTradeWapPayRequest.setBizContent(ProtoJsonUtil.toJson(proto));
            response = alipayClient.pageExecute(alipayTradeWapPayRequest, "GET");
            log.info("response:{}", response.getBody());
            log.info("response status:{}", response.isSuccess());
        } catch (InvalidProtocolBufferException | AlipayApiException e) {
            e.printStackTrace();
        }
        return "<a href=\"" + response.getBody() + "\">点击</a>";
    }

    /**
     * @apiNote 支付宝统一收单交易查询接口
     */
    @GetMapping("/trade/query")
    public String tradeQuery(@RequestParam("outTradeNo") String outTradeNo) {
//        AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.URL, AliPayConfig.APPID, AliPayConfig.RSA_PRIVATE_KEY, AliPayConfig.FORMAT, AliPayConfig.CHARSET, AliPayConfig.ALIPAY_PUBLIC_KEY, AliPayConfig.SIGNTYPE);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryRequestProto proto = AlipayTradeQueryRequestProto.newBuilder()
                .setOutTradeNo(outTradeNo)
                .build();
        AlipayTradeQueryResponse response = null;
        try {
            request.setBizContent(ProtoJsonUtil.toJson(proto));
            response = alipayClient.execute(request);
            log.info("response:{}", response.getBody());
            log.info("response status:{}", response.isSuccess());
        } catch (InvalidProtocolBufferException | AlipayApiException e) {
            e.printStackTrace();
        }
        return response.getBody();
    }

    /**
     * @apiNote 支付宝统一收单交易关闭接口
     */
    @GetMapping("/trade/close")
    public String tradeClose(@RequestParam("outTradeNo") String outTradeNo) {
//        AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.URL, AliPayConfig.APPID, AliPayConfig.RSA_PRIVATE_KEY, AliPayConfig.FORMAT, AliPayConfig.CHARSET, AliPayConfig.ALIPAY_PUBLIC_KEY, AliPayConfig.SIGNTYPE);
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        AlipayTradeCloseRequestProto proto = AlipayTradeCloseRequestProto.newBuilder()
                .setOutTradeNo(outTradeNo)
                .build();
        AlipayTradeCloseResponse response = null;
        try {
            request.setBizContent(ProtoJsonUtil.toJson(proto));
            response = alipayClient.execute(request);
            if (response.isSuccess()) {
                log.info("交易关闭成功");
            }
            log.info("response:{}", response.getBody());
        } catch (InvalidProtocolBufferException | AlipayApiException e) {
            e.printStackTrace();
        }
        return response.getBody();
    }

    /**
     * @apiNote 支付宝统一收单交易退款接口
     */
    @GetMapping("/trade/refund")
    public String tradeRefund(@RequestParam("outTradeNo") String outTradeNo) {
//        AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.URL, AliPayConfig.APPID, AliPayConfig.RSA_PRIVATE_KEY, AliPayConfig.FORMAT, AliPayConfig.CHARSET, AliPayConfig.ALIPAY_PUBLIC_KEY, AliPayConfig.SIGNTYPE);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundRequestProto proto = AlipayTradeRefundRequestProto.newBuilder()
                .setOutTradeNo(outTradeNo)
                .build();
        AlipayTradeRefundResponse response = null;
        try {
            request.setBizContent(ProtoJsonUtil.toJson(proto));
            response = alipayClient.execute(request);
            if (response.isSuccess() && "Y".equals(response.getFundChange())) {
                log.info("交易退款成功");
            }
            log.info("response:{}", response.getBody());
        } catch (InvalidProtocolBufferException | AlipayApiException e) {
            e.printStackTrace();
        }
        return response.getBody();
    }

    /**
     * @apiNote 支付宝查询对账单下载地址接口
     */
    @GetMapping("/data/dataservice/bill/downloadurl/query")
    public String data(@RequestParam("billType") String billType) {
//        AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.URL, AliPayConfig.APPID, AliPayConfig.RSA_PRIVATE_KEY, AliPayConfig.FORMAT, AliPayConfig.CHARSET, AliPayConfig.ALIPAY_PUBLIC_KEY, AliPayConfig.SIGNTYPE);
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
        AlipayDataDataserviceBillDownloadurlQueryRequestProto proto = AlipayDataDataserviceBillDownloadurlQueryRequestProto.newBuilder()
                .setBillType(billType)
                .setBillDate("2023-02-21")
                .build();
        AlipayDataDataserviceBillDownloadurlQueryResponse response = null;
        try {
            request.setBizContent(ProtoJsonUtil.toJson(proto));
            response = alipayClient.execute(request);
            log.info("response:{}", response);
        } catch (InvalidProtocolBufferException | AlipayApiException e) {
            e.printStackTrace();
        }
        return response.getBody();
    }

    private void copy(AlipayNotificationStr alipayNotificationStr, AlipayNotification alipayNotification) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        alipayNotification.setAppId(alipayNotificationStr.getApp_id());
        alipayNotification.setCharset(alipayNotificationStr.getCharset());
        alipayNotification.setSellerEmail(alipayNotificationStr.getSeller_email());
        alipayNotification.setSubject(alipayNotificationStr.getSubject());
        alipayNotification.setSign(alipayNotificationStr.getSign());
        alipayNotification.setBuyerId(alipayNotificationStr.getBuyer_id());
        alipayNotification.setInvoiceAmount(new BigDecimal(alipayNotificationStr.getInvoice_amount()));
        alipayNotification.setNotifyId(alipayNotificationStr.getNotify_id());
        alipayNotification.setFundBillList(alipayNotificationStr.getFund_bill_list());
        alipayNotification.setNotifyType(alipayNotificationStr.getNotify_type());
        alipayNotification.setTradeStatus(alipayNotificationStr.getTrade_status());
        alipayNotification.setReceiptAmount(new BigDecimal(alipayNotificationStr.getReceipt_amount()));
        alipayNotification.setBuyerPayAmount(new BigDecimal(alipayNotificationStr.getBuyer_pay_amount()));
        alipayNotification.setSignType(alipayNotificationStr.getSign_type());
        alipayNotification.setSellerId(alipayNotificationStr.getSeller_id());
        alipayNotification.setVersion(alipayNotificationStr.getVersion());
        alipayNotification.setOutTradeNo(alipayNotificationStr.getOut_trade_no());
        alipayNotification.setTotalAmount(new BigDecimal(alipayNotificationStr.getTotal_amount()));
        alipayNotification.setTradeNo(alipayNotificationStr.getTrade_no());
        alipayNotification.setAuthAppId(alipayNotificationStr.getAuth_app_id());
        alipayNotification.setBuyerLogonId(alipayNotificationStr.getBuyer_logon_id());
        alipayNotification.setPointAmount(new BigDecimal(alipayNotificationStr.getPoint_amount()));
        if (StringUtils.isNotEmpty(alipayNotificationStr.getGmt_create())) {
            alipayNotification.setGmtCreate(LocalDateTime.parse(alipayNotificationStr.getGmt_create(), df));
        }
        if (StringUtils.isNotEmpty(alipayNotificationStr.getGmt_payment())) {
            alipayNotification.setGmtPayment(LocalDateTime.parse(alipayNotificationStr.getGmt_payment(), df));
        }
        if (StringUtils.isNotEmpty(alipayNotificationStr.getNotify_time())) {
            alipayNotification.setNotifyTime(LocalDateTime.parse(alipayNotificationStr.getNotify_time(), df));
        }
        if (StringUtils.isNotEmpty(alipayNotificationStr.getGmt_refund())) {
            alipayNotification.setNotifyTime(LocalDateTime.parse(alipayNotificationStr.getGmt_refund(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
        }
    }

    /**
     * @param alipayNotificationStr 支付宝通知回调string
     * @return boolean
     * @apiNote 支付宝异步通知验签
     * @author 琴声何来
     * @since 2023/3/2 14:08
     */
    private boolean verify(AlipayNotificationStr alipayNotificationStr) {
        // 验签
        Map<String, String> params = new HashMap<>();
        params.put("app_id", alipayNotificationStr.getApp_id());
        params.put("gmt_create", alipayNotificationStr.getGmt_create());
        params.put("charset", alipayNotificationStr.getCharset());
        params.put("seller_email", alipayNotificationStr.getSeller_email());
        params.put("subject", alipayNotificationStr.getSubject());
        params.put("sign", alipayNotificationStr.getSign());
        params.put("buyer_id", alipayNotificationStr.getBuyer_id());
        params.put("invoice_amount", alipayNotificationStr.getInvoice_amount());
        params.put("notify_id", alipayNotificationStr.getNotify_id());
        params.put("fund_bill_list", alipayNotificationStr.getFund_bill_list());
        params.put("notify_type", alipayNotificationStr.getNotify_type());
        params.put("trade_status", alipayNotificationStr.getTrade_status());
        params.put("receipt_amount", alipayNotificationStr.getReceipt_amount());
        params.put("buyer_pay_amount", alipayNotificationStr.getBuyer_pay_amount());
        params.put("sign_type", alipayNotificationStr.getSign_type());
        params.put("seller_id", alipayNotificationStr.getSeller_id());
        params.put("gmt_payment", alipayNotificationStr.getGmt_payment());
        params.put("notify_time", alipayNotificationStr.getNotify_time());
        params.put("version", alipayNotificationStr.getVersion());
        params.put("out_trade_no", alipayNotificationStr.getOut_trade_no());
        params.put("total_amount", alipayNotificationStr.getTotal_amount());
        params.put("trade_no", alipayNotificationStr.getTrade_no());
        params.put("auth_app_id", alipayNotificationStr.getAuth_app_id());
        params.put("buyer_logon_id", alipayNotificationStr.getBuyer_logon_id());
        params.put("point_amount", alipayNotificationStr.getPoint_amount());
        try {
            boolean signCheck = AlipaySignature.rsaCheckV1(params, MyAlipayConfig.ALIPAY_PUBLIC_KEY, MyAlipayConfig.CHARSET, MyAlipayConfig.SIGN_TYPE);
            if (!signCheck) {
                log.error("sign验签失败");
                return false;
            }
        } catch (AlipayApiException e) {
            log.error("sign验签异常", e);
            return false;
        }
        //验签通过后，验证out_trade_no是否存在
        ParkingPaymentGrpcRecord record = parkingPaymentGrpcRecordService.getAlipayRecordByOutTradeNo(alipayNotificationStr.getOut_trade_no());
        if (record == null) {
            log.error("订单{}不存在", alipayNotificationStr.getOut_trade_no());
            return false;
        }
        //验证total_amount是否一致
        if (!record.getTotalAmount().equals(new BigDecimal(params.get("total_amount")))) {
            log.error("金额不一致，支付宝：{}，内部：{}", params.get("total_amount"), record.getTotalAmount());
            return false;
        }
        //验证seller_id是否为商家号
        if (!params.get("seller_id").equals(MyAlipayConfig.SELLER_ID)) {
            log.error("商家号不一致，支付宝：{}，内部：{}", params.get("seller_id"), MyAlipayConfig.SELLER_ID);
            return false;
        }
        //验证app_id是否为商家本身应用
        if (!params.get("app_id").equals(MyAlipayConfig.APPID)) {
            log.error("应用号不一致，支付宝：{}，内部：{}", params.get("app_id"), MyAlipayConfig.APPID);
            return false;
        }
        return true;
    }
}
