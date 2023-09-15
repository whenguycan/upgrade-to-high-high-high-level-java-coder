package com.ruoyi.project.system.service;

public interface WxOpenComponentService {

    String miniappJscode2Session(String appId,String secret,String jsCode);

    String getPhoneNumber(String code);

    //拉取微信用户信息
    String getUserInfo(String accessToken,String openid);

}
