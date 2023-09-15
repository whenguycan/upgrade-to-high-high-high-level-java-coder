package com.czdx.parkingnotification.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@Configuration
public class MyWechatConfig {
    @Autowired
    private Environment environment;

    public static String APP_ID = "";

    public static String SECRET = "";

    public static String ACCESS_TOKEN_URL = "";

    public static String JSAPI_TICKET_URL = "";

    public static String PAGE_ACCESS_TOKEN_URL = "";

    public static String MESSAGE_TEMPLATE_SEND_URL = "";

    public static String ENTRY_TEMPLATE_ID = "";

    public static String EXIT_TEMPLATE_ID = "";

    public static String PAY_TEMPLATE_ID = "";

    @PostConstruct
    public void initConfig() {
        APP_ID = environment.getProperty("wechat.appid");
        SECRET = environment.getProperty("wechat.secret");
        ACCESS_TOKEN_URL = environment.getProperty("wechat.accessTokenUrl");
        JSAPI_TICKET_URL = environment.getProperty("wechat.jsapiTicketUrl");
        PAGE_ACCESS_TOKEN_URL = environment.getProperty("wechat.pageAccessTokenUrl");
        MESSAGE_TEMPLATE_SEND_URL = environment.getProperty("wechat.messageTemplateSendUrl");
        ENTRY_TEMPLATE_ID = environment.getProperty("wechat.entryTemplateId");
        EXIT_TEMPLATE_ID = environment.getProperty("wechat.exitTemplateId");
        PAY_TEMPLATE_ID = environment.getProperty("wechat.payTemplateId");
    }
}
