package com.czdx.parkingnotification.domain;

import lombok.Data;

@Data
public class WechatNotificationRequest {
    private String touser;
    private String template_id;
    private String appid;
    private WechatNotificationData data;
}
