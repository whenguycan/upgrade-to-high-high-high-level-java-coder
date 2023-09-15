package com.ruoyi.project.system.service;

public interface WxOpenComponentService {

    String miniappJscode2Session(String appId,String secret,String jsCode);

    String getPhoneNumber(String code);

}
