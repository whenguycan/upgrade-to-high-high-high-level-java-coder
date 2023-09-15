package com.ruoyi.project.merchant.domain.vo;

import com.ruoyi.project.merchant.domain.TCouponOrder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TCouponOrderVo extends TCouponOrder {

    //账户余额
    private BigDecimal balance;
}
