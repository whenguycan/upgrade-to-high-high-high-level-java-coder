package com.ruoyi.project.parking.domain.vo.invoice;

import lombok.Data;

import java.util.List;

/**
 * 停车订单 申请开票 请求参数
 */
@Data
public class ApplyBillRequestVO {
    // 开票人姓名
    private String billUsername;

    // 开票人税号
    private String billTaxNum;

    // 开票人地址
    private String billAddress;

    // 开票人手机号
    private String billPhone;

    // 开票人邮箱
    private String billEmail;

    // 开户行地址
    private String billDepositBank;

    // 银行账号
    private String billDepositAccount;

    // 需要开票的订单号
    List<String> orderNo;

    // 用户id
    Integer userId;

    //类型 1-停车 2-月租
    private String type;
}
