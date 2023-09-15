package com.ruoyi.project.system.service;

import java.util.Map;

/**
 * 短信发送
 * @author chenlin
 */
public interface ISmsSendApplyService {

    /**
     * 公共发送短信方法
     * @param mobile 手机号
     * @param templateCode 模板编号
     * @param content 短信内容
     * @param signName 签名类型（验证码 , 通知）
     */
    void smsSend(String mobile, String templateCode, Map<String,String> content, String signName);
}
