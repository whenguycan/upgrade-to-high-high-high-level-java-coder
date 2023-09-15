package com.ruoyi.project.merchant.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class HourCouponVo implements Serializable {

    private String hourCouponRemark;
    private BigDecimal totalAmount;
}
