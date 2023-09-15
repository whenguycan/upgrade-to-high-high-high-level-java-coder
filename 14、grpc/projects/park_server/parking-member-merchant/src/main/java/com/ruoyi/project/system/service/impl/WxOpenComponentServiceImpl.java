package com.ruoyi.project.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.constant.WxInterfaceConstants;
import com.ruoyi.common.utils.http.HttpClientUtil;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.security.GetPhoneNumberBody;
import com.ruoyi.project.system.service.WxOpenComponentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class WxOpenComponentServiceImpl implements WxOpenComponentService {

    @Autowired
    private RedisCache redisCache;

    @Override
    public String miniappJscode2Session(String appId,String secret, String jsCode) {
        String url = String.format(WxInterfaceConstants.MINIAPP_JSCODE_2_SESSION_URL, appId, secret, jsCode);
        String responseContent = HttpUtils.sendGet(url);
        log.info("小程序登录返回数据===>"+responseContent);
        return responseContent;
    }

    @Override
    public String getPhoneNumber(String code) {
        String url = WxInterfaceConstants.GET_PHONENUMBER_URL+redisCache.getCacheObject("accessToken");
        GetPhoneNumberBody phoneNumberBody = new GetPhoneNumberBody();
        phoneNumberBody.setCode(code);
        String responseContent = HttpClientUtil.doPostOfJson(url,JSON.toJSONString(phoneNumberBody));
        log.info("phoneRes====>{}"+responseContent);
        System.out.println("phoneRes====>"+responseContent);
        Map<String,Object> resMap = (Map<String, Object>) JSON.parse(responseContent);
        if(resMap.containsKey("phone_info")){
            Map<String,Object> phoneInfoMap = (Map<String, Object>) JSON.parse(JSON.toJSONString(resMap.get("phone_info")));
            if(phoneInfoMap.containsKey("phoneNumber")){
                return phoneInfoMap.get("phoneNumber").toString();
            }else{
                return null;
            }
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(String.format("sdad%s","d"));
    }

}
