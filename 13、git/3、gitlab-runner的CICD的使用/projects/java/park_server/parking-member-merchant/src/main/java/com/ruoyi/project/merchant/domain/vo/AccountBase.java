package com.ruoyi.project.merchant.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AccountBase implements Serializable {
    private BigDecimal givenAmount;
    private BigDecimal rechargeAmount;
    private BigDecimal balance;
}
