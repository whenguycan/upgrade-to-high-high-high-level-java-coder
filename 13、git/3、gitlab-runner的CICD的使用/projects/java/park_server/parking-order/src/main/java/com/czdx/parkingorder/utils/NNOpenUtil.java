package com.czdx.parkingorder.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.czdx.parkingorder.domain.nuonuo.BillingNewRequest;
import com.czdx.parkingorder.domain.nuonuo.InvoiceDetail;
import com.czdx.parkingorder.domain.nuonuo.NNResponse;
import com.czdx.parkingorder.domain.nuonuo.QueryInvoiceResultRequest;
import com.czdx.parkingorder.enums.NNEnums;
import lombok.extern.slf4j.Slf4j;
import nuonuo.open.sdk.NNOpenSDK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 诺诺开票工具类
 * </p>
 *
 * @author 琴声何来
 * @since 2023/4/6 17:21
 */
@Slf4j
@Component
public class NNOpenUtil {

    @Autowired
    Environment environment;

    //诺诺发票开放平台appKey
    private static String NN_APP_KEY;

    //诺诺发票开放平台appSecret
    private static String NN_APP_SECRET;

    //诺诺发票开放平台token
    private static String NN_TOKEN;

    //销方税号
    private static String NN_SALER_TAX_NUM;

    //销方电话
    private static String NN_SALER_TEL;

    //销方地址
    private static String NN_SALER_ADDRESS;

    //税率
    private static BigDecimal NN_TAX_RATE;

    //收款人
    private static String NN_PAYEE;

    //复核人
    private static String NN_CHECKER;

    //开票员
    private static String NN_CLERK;

    //发票类型
    private static String NN_INVOICE_TYPE;

    //回调通知地址
    private static String NN_CALLBACK_URL;

    //诺诺发票开放平台url
    private static String NN_OPEN_URL;

    //诺诺发票开放平台openSDK
    private static NNOpenSDK openSDK;

    /**
     * @apiNote 初始化诺诺发票开放平台配置
     * @author 琴声何来
     * @since 2023/4/7 14:13
     */
    @PostConstruct
    void initConfig() {
        log.info("初始化诺诺发票开放平台配置");
        NN_APP_KEY = environment.getProperty("nuonuo.appKey");
        NN_APP_SECRET = environment.getProperty("nuonuo.appSecret");
//        NN_TOKEN = "fa35aa5f2feb821e9c02c00vlilygsvs";
        NN_TOKEN = environment.getProperty("nuonuo.accessToken");
        NN_SALER_TAX_NUM = environment.getProperty("nuonuo.salerTaxNum");
        NN_SALER_TEL = environment.getProperty("nuonuo.salerTel");
        NN_SALER_ADDRESS = environment.getProperty("nuonuo.salerAddress");
        NN_TAX_RATE = new BigDecimal(Objects.requireNonNull(environment.getProperty("nuonuo.taxRate")));
        NN_PAYEE = environment.getProperty("nuonuo.payee");
        NN_CHECKER = environment.getProperty("nuonuo.checker");
        NN_CLERK = environment.getProperty("nuonuo.clerk");
        NN_INVOICE_TYPE = environment.getProperty("nuonuo.invoiceType");
        NN_CALLBACK_URL = environment.getProperty("nuonuo.callbackUrl");
        NN_OPEN_URL = environment.getProperty("nuonuo.openUrl");
        openSDK = NNOpenSDK.getIntance();
        //fa35aa5f2feb821e9c02c00vlilygsvs
//        NN_TOKEN = openSDK.getMerchantToken(NN_APP_KEY, NN_APP_SECRET);
        log.info("初始化诺诺发票开放平台配置完成");
    }

    /**
     * @param request 请求开票参数
     * @return java.lang.String
     * @apiNote 请求开票接口2.0
     * @author 琴声何来
     * @since 2023/4/10 8:53
     */
    public static String requestBillingNew(BillingNewRequest request) {
        //销方税号
        request.setSalerTaxNum(NN_SALER_TAX_NUM);
        //销方电话
        request.setSalerTel(NN_SALER_TEL);
        //销方地址
        request.setSalerAddress(NN_SALER_ADDRESS);
        //开票时间
        request.setInvoiceDate(LocalDateTime.now());
        //收款人
        request.setPayee(NN_PAYEE);
        //复核人
        request.setChecker(NN_CHECKER);
        //开票员
        request.setClerk(NN_CLERK);
        //开票类型。默认蓝票
        request.setInvoiceType(NN_INVOICE_TYPE);
        //设置不通知
        request.setPushMode(NNEnums.PushMode.NON.getValue());
        //通知回调地址
        request.setCallBackUrl(NN_CALLBACK_URL);
        //计算税额和不含税金额
        //含税金额=不含税金额+不含税金额*税率=不含税金额（1+税率）
        InvoiceDetail detail = request.getInvoiceDetail().get(0);
        BigDecimal taxIncludedAmountValue = new BigDecimal(detail.getTaxIncludedAmount());
        BigDecimal taxRate = NN_TAX_RATE.divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
        detail.setTaxExcludedAmount(taxIncludedAmountValue.divide(BigDecimal.ONE.add(taxRate),2,RoundingMode.CEILING).toString());
        detail.setTax(taxIncludedAmountValue.subtract(new BigDecimal(detail.getTaxExcludedAmount())).toString());
        detail.setWithTaxFlag("1");
        detail.setTaxRate(taxRate.toString());
        //构建请求
        String content = "{\"order\":" + JSON.toJSONString(request) + "}";
        String method = "nuonuo.ElectronInvoice.requestBillingNew"; // API方法名
        String senid = UUID.randomUUID().toString().replace("-", "");
        log.info("请求开票接口2.0，请求参数如下：");
        log.info("senid：{}，appKey：{}，appSecret：{}，token：{}，request：{}", senid, NN_APP_KEY, NN_APP_SECRET, NN_TOKEN, request);
        String result = openSDK.sendPostSyncRequest(NN_OPEN_URL, senid, NN_APP_KEY, NN_APP_SECRET, NN_TOKEN, NN_SALER_TAX_NUM, method, content);
        NNResponse<JSONObject> nnResponse = JSON.parseObject(result, NNResponse.class);
        if (!nnResponse.getCode().equals(NNEnums.ReturnCode.SUCCESS.getValue())) {
            log.error("诺诺发票返回异常，异常码：{}，异常描述：{}，异常信息：{}", nnResponse.getCode(), nnResponse.getDescribe(), nnResponse.getResult());
            throw new RuntimeException("请求开票接口2.0异常");
        }
        return nnResponse.getResult().getString("invoiceSerialNum");
    }

    /**
     * @param request 开票结果查询参数
     * @return com.alibaba.fastjson2.JSONArray 根据多条流水号返回多条发票结果信息
     * @apiNote 开票结果查询接口
     * @author 琴声何来
     * @since 2023/4/10 8:53
     */
    public static JSONArray queryInvoiceResult(QueryInvoiceResultRequest request) {
        String content = JSON.toJSONString(request);
        String method = "nuonuo.ElectronInvoice.queryInvoiceResult"; // API方法名
        String senid = UUID.randomUUID().toString().replace("-", "");
        log.info("开票结果查询接口，请求参数如下：");
        log.info("senid：{}，appKey：{}，appSecret：{}，token：{}，request：{}", senid, NN_APP_KEY, NN_APP_SECRET, NN_TOKEN, request);
        String result = openSDK.sendPostSyncRequest(NN_OPEN_URL, senid, NN_APP_KEY, NN_APP_SECRET, NN_TOKEN, NN_SALER_TAX_NUM, method, content);
        NNResponse<JSONArray> nnResponse = JSON.parseObject(result, NNResponse.class);
        if (!nnResponse.getCode().equals(NNEnums.ReturnCode.SUCCESS.getValue())) {
            log.error("诺诺发票返回异常，异常码：{}，异常描述：{}，异常信息：{}", nnResponse.getCode(), nnResponse.getDescribe(), nnResponse.getResult());
            throw new RuntimeException("开票结果查询接口异常");
        }
        return nnResponse.getResult();
    }

    public static void main(String[] args) {
        //测试请求开票
        NN_APP_KEY = "SD63236305";
        NN_APP_SECRET = "SDDED2523BED4643";
        NN_TOKEN = "fa35aa5f2feb821e9c02c00vlilygsvs";
        NN_SALER_TAX_NUM = "339902999999789106";
        NN_SALER_TEL = "12345678912";
        NN_SALER_ADDRESS = "测试销方地址";
        NN_TAX_RATE = BigDecimal.valueOf(6);
        NN_CLERK = "测试开票员甲";
        NN_INVOICE_TYPE = "1";
        NN_CALLBACK_URL = "https://ryt.czctown.com/bill/callback/";
        NN_OPEN_URL = "https://sandbox.nuonuocs.cn/open/v1/services";
        openSDK = NNOpenSDK.getIntance();
        BillingNewRequest billingNewRequest = new BillingNewRequest(
                "购方名称",
                "123456",
                "购方电话",
                "购方地址",
                "开户行123456789",
                "",
                "17615956780",
                "752252081@qq.com",
                null,
                null,
                null,
                "115",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                List.of(new InvoiceDetail("停车服务费", null, null, "10", null, null))
        );
        String invoiceSerialNum = requestBillingNew(billingNewRequest);
        System.out.println(invoiceSerialNum);
        //测试查询开票结果
        QueryInvoiceResultRequest queryInvoiceResultRequest = new QueryInvoiceResultRequest();
        queryInvoiceResultRequest.setSerialNos(List.of("23042715233002714585"));
//        JSONArray jsonObject = queryInvoiceResult(queryInvoiceResultRequest);
//        System.out.println(JSON.toJSONString(jsonObject));
    }
}
