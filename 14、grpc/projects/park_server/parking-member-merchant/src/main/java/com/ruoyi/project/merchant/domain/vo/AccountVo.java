package com.ruoyi.project.merchant.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountVo {

    private BigDecimal givenAmount;
    private BigDecimal rechargeAmount;
    private BigDecimal balance ;
    private BigDecimal disposableAmount;
    private String freezeFlag;
}
