package com.ruoyi.project.parking.dahua.icc;

import com.dahuatech.hutool.http.Method;
import com.dahuatech.hutool.json.JSONArray;
import com.dahuatech.hutool.json.JSONObject;
import com.dahuatech.hutool.json.JSONUtil;
import com.dahuatech.icc.event.model.v202011.EventSubscribeDelRequest;
import com.dahuatech.icc.event.model.v202011.EventSubscribeDelResponse;
import com.dahuatech.icc.event.model.v202011.EventSubscribeRequest;
import com.dahuatech.icc.exception.ClientException;
import com.dahuatech.icc.oauth.http.DefaultClient;
import com.dahuatech.icc.oauth.http.IClient;
import com.dahuatech.icc.oauth.http.IccHttpHttpRequest;
import com.dahuatech.icc.oauth.model.v202010.GeneralRequest;
import com.dahuatech.icc.oauth.model.v202010.GeneralResponse;
import com.google.gson.JsonObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class IccEventSubscribe implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(IccEventSubscribe.class);

    @Autowired
    private Environment environment;

    //    @Value("${icc.types}")
//    private String types;
    private String types = "car.capture,car.access";
    //    @Value("${icc.sdk.host}")
//    private String HOST;
    private String HOST = "121.229.5.12";
    //    @Value("${icc.sdk.port}")
//    private String PORT;
    private String PORT = "9443";
    //    @Value("${icc.sdk.version}")
//    private String VERSION;
    private String VERSION = "1.0.0";
    //    @Value("${local.ip}")
//    private String ip;
//    private String ip="mzl-park-web.cz19.tk";
    /**
     * 生产环境
     */
//    private String ip="https://ryt.czctown.com/prod-api";
//    private String magicIp="121.229.5.12";
    //    private String _port = "9443";
    /**
     * 张老板环境
     */
    private String ip = "http://zh-web.cz19.tk:8080";
    private String magicIp = "104.225.232.72";
    private String _port = "8080";
    //    @Value("${local.port}")
//    private String _port;


    /**
     * 事件订阅示例
     *
     * @throws ClientException
     */
    @Test
    public void eventSubscribe() throws ClientException {
        String SUBSCRIE_URL = "https://" + HOST + ":" + PORT + "/evo-apigw/evo-event/" + VERSION + "/subscribe/mqinfo";
//        String monitorEntry = "https://"+ip +":"+ _port + "/SiteInfo/capture";
//        String monitorLeave = "https://"+ip +":"+ _port + "/SiteInfo/capture";
        String monitorEntry = ip + "/SiteInfo/capture";
        String monitorLeave = ip + "/SiteInfo/capture";
        log.info("----开始执行----{}------请求地址:{}", "事件订阅", SUBSCRIE_URL);
        String[] codes = types.split(",");//查询订阅告警编号-大华
        /*if(codes.length == 0){
            return;
        }*/
        // 接收事件地址
        String mqinfo = "{" +
                "    \"param\": {" +
                "        \"monitors\": [" +
                "            {" +
                "                \"monitor\":" + "\"" + monitorEntry + "\"" + "," +
                "                \"monitorType\": \"url\"," +
                "                \"events\": [" +
                "                    {" +
                "                        \"category\": \"business\"," +
                "                        \"subscribeAll\": 1," +
                "                        \"domainSubscribe\": 2," +
                "                        \"authorities\": [" +
                "                            {" +
                "                                \"types\": " + "[\n" +
                "                     \"car.capture\"\n" +
//                "                     \"car.access\"\n" +
                "                   ]" +
                "                            }" +
                "                        ]" +
                "                    }" +
                "                ]" +
                "            }," +
                "            {" +
                "                \"monitor\": " + "\"" + monitorLeave + "\"" + "," +
                "                \"monitorType\": \"url\"," +
                "                \"events\": [" +
                "                    {" +
                "                        \"category\": \"business\"," +
                "                        \"subscribeAll\": 1," +
                "                        \"domainSubscribe\": 2," +
                "                        \"authorities\": [" +
                "                            {" +
                "                                \"types\": " + "[\n" +
                "                     \"car.capture\"\n" +
//                "                     \"car.access\"\n" +
                "                   ]" +
                "                            }" +
                "                        ]" +
                "                    }" +
                "                ]" +
                "            }" +
                "        ]," +
                "        \"subsystem\": {" +
                "            \"subsystemType\": 0," +                //固定值：0
                "            \"name\": " + "\"" + magicIp + "_" + _port + "\"" + "," +
                "            \"magic\": " + "\"" + magicIp + "_" + _port + "\"" +
                "        }" +
                "    }" +
                "}";
        IClient iClient = new DefaultClient();
        IccHttpHttpRequest pr = new IccHttpHttpRequest(SUBSCRIE_URL, Method.POST, mqinfo);
        pr.getHttpRequest().header("Authorization", iClient.getAccessToken().getToken_type() + " " + iClient.getAccessToken().getAccess_token());
        String prBody = pr.execute();
//        GeneralResponse generalResponse=iClient.doAction(pr,pr.get());
        log.info("----结束执行----{}------返回报文:{}", "事件订阅", prBody);
//         - ----结束执行----事件订阅------返回报文:{"success":true,"data":{},"code":"0","errMsg":""}
    }

    @Test
    public void eventUnSubscribe() throws ClientException {
        log.info("----开始执行----{}------", "事件取消订阅");
//        String subscribeName = ip + "_" + _port;
        String subscribeName = magicIp + "_" + _port;
        IClient iClient = new DefaultClient();
        // 事件订阅按`name`字段取消
        EventSubscribeDelRequest subscribeRequest = new EventSubscribeDelRequest(subscribeName);
        EventSubscribeDelResponse subscribeResponse =
                iClient.doAction(subscribeRequest, subscribeRequest.getResponseClass());
        log.info("----结束执行----{}------返回报文:{}", "事件取消订阅", JSONUtil.toJsonStr(subscribeResponse));
        // - ----结束执行----事件取消订阅------返回报文:{"result":"{\"success\":true,\"data\":{},\"code\":\"0\",\"errMsg\":\"\"}","code":"0","success":true,"errMsg":""}
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        eventUnSubscribe();
    }
}
