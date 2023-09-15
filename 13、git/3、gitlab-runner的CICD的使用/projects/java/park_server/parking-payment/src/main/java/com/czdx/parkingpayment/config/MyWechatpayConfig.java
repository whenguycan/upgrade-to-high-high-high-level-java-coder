package com.czdx.parkingpayment.config;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.h5.H5Service;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.refund.RefundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
public class MyWechatpayConfig {

    @Autowired
    private Environment environment;

    /**
     * 自动更新平台证书的配置类
     */
    private RSAAutoCertificateConfig config;

    public RSAAutoCertificateConfig getInstance() {
        if (config == null) {
            config = initConfig();
        }
        return config;
    }

    public RSAAutoCertificateConfig initConfig() {
        // 商户号
        MCH_ID = environment.getProperty("wechatpay.mchId");
        // 应用ID
        APP_ID = environment.getProperty("wechatpay.appId");
        // 商户API私钥
        PRIVATE_KEY = environment.getProperty("wechatpay.privateKey");
        // 商户API证书的证书序列号
        MCH_SERIAL_NO = environment.getProperty("wechatpay.merchantSerialNumber");
        // 商户APIV3密钥
        API_V3_KEY = environment.getProperty("wechatpay.apiV3key");
        // 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
        // 公网域名必须为https，如果是走专线接入，使用专线NAT IP或者私有回调域名可使用http
        NOTIFY_URL = environment.getProperty("wechatpay.notifyUrl");
        log.info("初始化wechatpay配置成功");
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(MCH_ID)
                .privateKey(PRIVATE_KEY)
                .merchantSerialNumber(MCH_SERIAL_NO)
                .apiV3Key(API_V3_KEY)
                .build();
    }

    /**
     * @apiNote 初始化jsapi请求service
     */
    @Bean
    public JsapiServiceExtension jsapiServiceExtension() {
        return new JsapiServiceExtension.Builder().config(getInstance()).build();
    }

    /**
     * @apiNote 初始化H5请求service
     */
    @Bean
    public H5Service h5Service() {
        return new H5Service.Builder().config(getInstance()).build();
    }

    /**
     * @apiNote 初始化退款请求service
     */
    @Bean
    public RefundService refundService() {
        return new RefundService.Builder().config(getInstance()).build();
    }

    public static String PRIVATE_KEY = "";

    public static String MCH_ID = "";

    public static String APP_ID = "";

    public static String MCH_SERIAL_NO = "";

    public static String API_V3_KEY = "";

    public static String NOTIFY_URL = "";
}
