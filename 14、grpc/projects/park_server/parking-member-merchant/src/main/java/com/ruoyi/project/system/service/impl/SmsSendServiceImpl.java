package com.ruoyi.project.system.service.impl;

import com.ruoyi.common.utils.sms.DigestUtil;
import com.ruoyi.common.utils.sms.SendMessage;
import com.ruoyi.common.utils.sms.SendMessageParam;
import com.ruoyi.project.system.service.ISmsSendApplyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 短信发送
 * @author chenlin
 */
@Service
public class SmsSendServiceImpl implements ISmsSendApplyService {

    @Value("${sms.appId}")
    private String appId;

    @Value("${sms.appKey}")
    private String appKey;

    @Override
    public void smsSend(String mobile, String templateCode, Map<String, String> content, String signName) {
        SendMessageParam sendMessageParam =new SendMessageParam();
        sendMessageParam.setAppId(appId);
        sendMessageParam.setTemplateCode(templateCode);
        sendMessageParam.setTimestamp(System.currentTimeMillis());
        sendMessageParam.setContent(content);
        sendMessageParam.setSignName(signName);
        sendMessageParam.setPhones(mobile);
        sendMessageParam.setSign(DigestUtil.enc(sendMessageParam,appKey));
        //发送短信验证码
        SendMessage.send(sendMessageParam);
    }
}
