package com.ruoyi.project.merchant.domain;

import lombok.Data;

@Data
public class WechatJsapiSignature {
    //随机字符串
    private String noncestr;
    //公众号用于调用微信 JS 接口的临时票据，两小时有效期
    private String jsapi_ticket;
    //时间戳
    private String timestamp;
    //当前网页的URL，不包含#及其后面部分
    private String url;

    //加密后的结果signature
    private String signature;
}
