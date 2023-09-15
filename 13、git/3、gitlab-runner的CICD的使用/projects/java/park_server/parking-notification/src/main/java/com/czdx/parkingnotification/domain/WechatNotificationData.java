package com.czdx.parkingnotification.domain;

import lombok.Data;

import java.util.Map;

@Data
public class WechatNotificationData {
    private Map<String,String> first;
    private Map<String,String> keyword1;
    private Map<String,String> keyword2;
    private Map<String,String> keyword3;
    private Map<String,String> keyword4;
    private Map<String,String> keyword5;
    private Map<String,String> remark;
}