package com.ruoyi.project.parking.domain.vo.invoice;

import lombok.Data;

/**
 * 查询 停车订单开票详情信息 - 响应参数
 */
@Data
public class BillDetailResponseVO {

    private String status;
    private String mess;

    //开票人姓名
    private String billUsername;

    //开票人税号
    private String billTaxNum;

    //开票人手机号
    private String billPhone;

    //开票人邮箱
    private String billEmail;

    //需要开票的订单号
    private String orderNo;

    Integer userId;

    private String billCreateTime;

    private String billNo;

    private String reasonCode;

    private String type;

    private double billAmount;

    private String billStatus;

    private String billPdfUrl;

    private String billAddress;

    private String billDepositBank;

    private String billDepositAccount;
}

