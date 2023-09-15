package com.ruoyi.project.merchant.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RechargeResVo extends AccountBase {

    private String orderNo;
    private String payDate;
    private BigDecimal actualAcquireAmount;
}
