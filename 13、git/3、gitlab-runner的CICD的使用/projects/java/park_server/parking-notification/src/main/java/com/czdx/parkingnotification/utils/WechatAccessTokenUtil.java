package com.czdx.parkingnotification.utils;

import com.czdx.parkingnotification.config.MyWechatConfig;
import com.czdx.parkingnotification.domain.WechatAccessToken;
import com.czdx.parkingnotification.domain.WechatJsapiTicket;
import com.czdx.parkingnotification.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @apiNote 微信AccessToken工具类
 * @Author 琴声何来
 * @Date 2023/2/23 10:23
 */
@Slf4j
@Component
@DependsOn(value = "myWechatConfig")
public class WechatAccessTokenUtil {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RestTemplate restTemplate;

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public static final String WECHAT_ACCESS_TOKEN_KEY = "wechat_access_token";

    public static final String WECHAT_JSAPI_TICKET_KEY = "wechat_jsapi_ticket";

    public String getToken() {
        //从redis中获取
        WechatAccessToken cacheAccessToken = redisCache.getCacheObject(WECHAT_ACCESS_TOKEN_KEY);
        if (cacheAccessToken == null) {
            //从微信获取AccessToken并存入缓存
            WechatAccessToken tokenFromWechat = getTokenFromWechat();
            redisCache.setCacheObject(WECHAT_ACCESS_TOKEN_KEY, tokenFromWechat);
            //从微信获取JsapiTicket并存入缓存
            WechatJsapiTicket jsapiTicketFromWechat = getJsapiTicketFromWechat();
            redisCache.setCacheObject(WECHAT_JSAPI_TICKET_KEY, jsapiTicketFromWechat);
            assert tokenFromWechat != null;
            return tokenFromWechat.getAccess_token();
        } else {
            return cacheAccessToken.getAccess_token();
        }
    }

    private WechatAccessToken getTokenFromWechat() {
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", MyWechatConfig.APP_ID);
        params.put("secret", MyWechatConfig.SECRET);
        log.info("appid:{}，secret:{}", MyWechatConfig.APP_ID, MyWechatConfig.SECRET);
        ResponseEntity<WechatAccessToken> responseEntity = restTemplate.getForEntity(
                MyWechatConfig.ACCESS_TOKEN_URL +
                        "?grant_type={grant_type}&appid={appid}&secret={secret}", WechatAccessToken.class, params);
        log.info("收到响应：{}", responseEntity.getBody());
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }
        log.error("刷新微信AccessToken失败，code：{}，body：{}", responseEntity.getStatusCodeValue(), responseEntity.getBody());
        return null;
    }

    private WechatJsapiTicket getJsapiTicketFromWechat() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "jsapi");
        params.put("access_token", getToken());
        ResponseEntity<WechatJsapiTicket> responseEntity = restTemplate.getForEntity(
                MyWechatConfig.JSAPI_TICKET_URL +
                        "?access_token={access_token}&type={type}", WechatJsapiTicket.class, params);
        log.info("收到响应：{}", responseEntity.getBody());
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }
        log.error("刷新微信JsapiTicket失败，code：{}，body：{}", responseEntity.getStatusCodeValue(), responseEntity.getBody());
        return null;
    }

    @PostConstruct
    private void scheduleGetWechatAccessToken() {
        log.info("定时任务准备执行");
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("定时任务开始执行");
                    redisCache.setCacheObject(WECHAT_ACCESS_TOKEN_KEY, getTokenFromWechat());
                    log.info("定时任务刷新微信AccessToken成功");
                    redisCache.setCacheObject(WECHAT_JSAPI_TICKET_KEY, getJsapiTicketFromWechat());
                    log.info("定时任务刷新微信JsapiTicket成功");
                } catch (Exception e) {
                    log.error("定时任务执行出现异常", e);
                }
            }
        }, 0, 90, TimeUnit.MINUTES);
    }

}
