package com.ruoyi.project.merchant.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountVo extends AccountBase {
    private BigDecimal disposableAmount;
    private String freezeFlag;
}
