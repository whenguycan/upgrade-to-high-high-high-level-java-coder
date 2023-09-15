package com.ruoyi.project.grpclient.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class MerchantOrderDetail implements Serializable {


    // 订单号
    private  String orderNo;
    // 车场编号
    private  String parkNo;
    // 所属商家ID
    private Long erchantId;
    // 车牌号
    private String carNumber;
    // 应付金额（元）
    private Double payableAmount;
    // 优惠金额（元）
    private Double discountAmount;
    // 已付金额（元）
    private Double paidAmount;
    // 实付金额（元）
    private Double payAmount;
    //  支付方式
    private String payMethod;
    // 支付时间
    private String payTime;
    // 支付状态
    private String payStatus;
    // 支付流水号
    private String payNumber;
    // 过期时间
    private String expireTime;
    // 备注
    private String remark;
    // 创建时间
    private String createTime;
    // 更新时间
    private String updateTime;
}
