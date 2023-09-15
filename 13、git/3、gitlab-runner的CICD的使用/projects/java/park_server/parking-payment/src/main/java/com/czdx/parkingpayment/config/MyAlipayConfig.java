package com.czdx.parkingpayment.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.FileWriter;
import java.io.IOException;

/**
 * <p>
 * 支付宝相关配置初始化
 * </p>
 *
 * @author 琴声何来
 * @since 2023/3/2 17:42
 */
@Slf4j
@Configuration
public class MyAlipayConfig {

    @Autowired
    Environment environment;

    @Bean
    public AlipayClient alipayClient() {
        APPID = environment.getProperty("alipay.appId");
        RSA_PRIVATE_KEY = environment.getProperty("alipay.rsaPrivateKey");
        ALIPAY_PUBLIC_KEY = environment.getProperty("alipay.alipayPublicKey");
        NOTIFY_URL = environment.getProperty("alipay.notifyUrl");
        RETURN_URL = environment.getProperty("alipay.returnUrl");
        SELLER_ID = environment.getProperty("alipay.sellerId");
        URL = environment.getProperty("alipay.gatewayUrl");
        log.info("初始化alipay配置成功");
        return new DefaultAlipayClient(URL,
                APPID,
                RSA_PRIVATE_KEY,
                FORMAT,
                CHARSET,
                ALIPAY_PUBLIC_KEY,
                SIGN_TYPE);
    }

    /**
     * @apiNote 支付宝手机网站支付接口
     */
    @Bean
    public AlipayTradeWapPayRequest alipayTradeWapPayRequest() {
        AlipayTradeWapPayRequest alipayTradeWapPayRequest = new AlipayTradeWapPayRequest();
        alipayTradeWapPayRequest.setNotifyUrl(NOTIFY_URL);
//        alipayTradeWapPayRequest.setReturnUrl(RETURN_URL);
        return alipayTradeWapPayRequest;
    }

    /**
     * @apiNote 支付宝统一收单交易查询接口
     */
    @Bean
    public AlipayTradeQueryRequest alipayTradeQueryRequest() {
        AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();
        return alipayTradeQueryRequest;
    }

    /**
     * @apiNote 支付宝统一收单交易关闭接口
     */
    @Bean
    public AlipayTradeCloseRequest alipayTradeCloseRequest() {
        AlipayTradeCloseRequest alipayTradeCloseRequest = new AlipayTradeCloseRequest();
        return alipayTradeCloseRequest;
    }

    /**
     * @apiNote 支付宝统一收单交易退款接口
     */
    @Bean
    public AlipayTradeRefundRequest alipayTradeRefundRequest() {
        AlipayTradeRefundRequest alipayTradeRefundRequest = new AlipayTradeRefundRequest();
        alipayTradeRefundRequest.setNotifyUrl(NOTIFY_URL);
        return alipayTradeRefundRequest;
    }

    /**
     * @apiNote 支付宝统一收单交易退款查询接口
     */
    @Bean
    public AlipayTradeFastpayRefundQueryRequest alipayTradeFastpayRefundQueryRequest() {
        AlipayTradeFastpayRefundQueryRequest alipayTradeFastpayRefundQueryRequest = new AlipayTradeFastpayRefundQueryRequest();
        return alipayTradeFastpayRefundQueryRequest;
    }

    /**
     * @apiNote 支付宝查询对账单下载地址接口
     */
    @Bean
    public AlipayDataDataserviceBillDownloadurlQueryRequest alipayDataDataserviceBillDownloadurlQueryRequest() {
        AlipayDataDataserviceBillDownloadurlQueryRequest alipayDataDataserviceBillDownloadurlQueryRequest = new AlipayDataDataserviceBillDownloadurlQueryRequest();
        return alipayDataDataserviceBillDownloadurlQueryRequest;
    }

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String APPID = "";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String RSA_PRIVATE_KEY = "";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String ALIPAY_PUBLIC_KEY = "";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String NOTIFY_URL = "";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String RETURN_URL = "";

    //商户号，用于验证异步通知消息是否由支付宝发送
    public static String SELLER_ID = "";

    // 签名方式
    public static String SIGN_TYPE = "RSA2";

    // 字符编码格式
    public static String CHARSET = "UTF-8";

    // 支付宝网关
    public static String URL = "";

    // 支付宝网关
    public static String LOG_PATH = "/log";

    // 返回格式
    public static String FORMAT = "json";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     *
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(LOG_PATH + "alipay_log_" + System.currentTimeMillis() + ".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
