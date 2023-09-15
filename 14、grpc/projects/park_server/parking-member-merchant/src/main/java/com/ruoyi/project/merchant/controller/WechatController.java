package com.ruoyi.project.merchant.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.sms.DigestUtil;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.merchant.domain.WechatJsapiSignature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * <p>
 * 微信相关 前端控制器
 * </p>
 *
 * @author 琴声何来
 * @since 2023/3/14 14:34
 */
@Slf4j
@RestController
@RequestMapping("/wechat")
public class WechatController extends BaseController {

    @Autowired
    RedisCache redisCache;

    public static final String WECHAT_JSAPI_TICKET_KEY = "wechat_jsapi_ticket";

    /**
     * @apiNote 获取微信js-sdk所需的signature
     */
    @GetMapping("/getSignature")
    public AjaxResult getSignature(@RequestParam String url) {
        JSONObject ticket = redisCache.getCacheObject(WECHAT_JSAPI_TICKET_KEY);
        //拼装加密前字符串
        StringBuilder str = new StringBuilder();
        List<String> keys = new ArrayList<>(List.of("noncestr", "jsapi_ticket", "timestamp", "url"));
        Collections.sort(keys);
        Map<String, String> param = new HashMap<>();
        param.put("noncestr", UUID.randomUUID().toString());
        param.put("jsapi_ticket", ticket.getString("ticket"));
        param.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        param.put("url", url);
        keys.forEach(key -> {
            str.append(key).append("=").append(param.get(key)).append("&");
        });
        String substring = str.substring(0, str.length() - 1);
        log.info("str:{}", substring);
        //对字符串进行sha1加密，得到signature
        WechatJsapiSignature wechatJsapiSignature = new WechatJsapiSignature();
        wechatJsapiSignature.setSignature(DigestUtil.getSha1(substring));
        wechatJsapiSignature.setNoncestr(param.get("noncestr"));
        wechatJsapiSignature.setTimestamp(param.get("timestamp"));
        return AjaxResult.success(wechatJsapiSignature);
    }

}
