package com.ruoyi.project.merchant.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayResponseVo implements Serializable {

    private String payUrl;

    private Long orderRecordId;

    //微信jsapi支付参数--开始
    private String appId;

    private String timestamp;

    private String nonceStr;

    private String packageVal;

    private String signType;

    private String paySign;
    //微信jsapi支付参数--结束
}
