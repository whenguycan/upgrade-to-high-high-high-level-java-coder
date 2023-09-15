package com.czdx.parkingnotification.domain;

import lombok.Data;

@Data
public class WechatJsapiTicket {

    private String ticket;

    private int expires_in;

    private String errcode;

    private String errmsg;
}
