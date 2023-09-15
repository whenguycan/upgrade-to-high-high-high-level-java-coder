package com.czdx.parkingpayment.config;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

//@Configuration
public class MyWechatpayConfig {

    @Autowired
    private Environment environment;

    /**
     * 自动更新平台证书的配置类
     */
    private RSAAutoCertificateConfig config;

    public RSAAutoCertificateConfig getInstance() {
        if (config == null) {
            return initConfig();
        } else {
            return config;
        }
    }

    public RSAAutoCertificateConfig initConfig() {
        // 商户号
        MCH_ID = environment.getProperty("wechatpay.mchId");
        // 商户API私钥
        PRIVATE_KEY = environment.getProperty("wechatpay.privateKey");
        // 商户API证书的证书序列号
        MCH_SERIAL_NO = environment.getProperty("wechatpay.merchantSerialNumber");
        // 商户APIV3密钥
        API_V3_KEY = environment.getProperty("wechatpay.apiV3key");
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(MCH_ID)
                .privateKey(PRIVATE_KEY)
                .merchantSerialNumber(MCH_SERIAL_NO)
                .apiV3Key(API_V3_KEY)
                .build();
    }

    @Bean("wechatpayJsapiService")
    public JsapiService jsapiService() {
        return new JsapiService.Builder().config(getInstance()).build();
    }

    public static String PRIVATE_KEY = "";
    public static String MCH_ID = "";
    public static String MCH_SERIAL_NO = "";
    public static String API_V3_KEY = "";
}
