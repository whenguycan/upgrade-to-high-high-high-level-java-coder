package com.ruoyi.common.constant;

public class WxInterfaceConstants {

    /**
     * 微信获取AccessToken接口地址
     */
    public static final String GET_ACCESSTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    /**
     * 微信获取电话号码接口地址
     */
    public static final String GET_PHONENUMBER_URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=";

    /**
     *
     */
    public static final String MINIAPP_JSCODE_2_SESSION_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

}
