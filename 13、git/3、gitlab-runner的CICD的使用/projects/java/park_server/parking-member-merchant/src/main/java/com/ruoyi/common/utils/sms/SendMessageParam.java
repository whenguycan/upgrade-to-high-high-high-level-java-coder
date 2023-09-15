package com.ruoyi.common.utils.sms;


import lombok.Data;

import java.util.Map;

/**
 * describe <p>短信发送参数</p>
 *
 * @author LijieZhu
 * @date 2021/8/6
 */
@Data
public class SendMessageParam {

    /**
     * 应用id
     */
    private String appId;


    /**
     * 发送类型 0:三网    1:电信
     */
    private Integer sendType;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 短信内容
     * key-value形式
     * key在短信模板中定义 如：  验证码：${code},有效期5分钟
     * content            如：  {"code":"123321"}
     */
    private Map<String, String> content;

    /**
     * 发送手机号码
     */
    private String phones;


    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 签名----通过 SHA1 加密生成
     */
    private String sign;

}
