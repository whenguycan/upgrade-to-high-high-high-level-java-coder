package com.czdx.parkingnotification.domain;

import lombok.Data;

@Data
public class WechatAccessToken {

    private String access_token;

    private int expires_in;

    private String errcode;

    private String errmsg;
}
