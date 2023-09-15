package com.ruoyi.project.merchant.domain.vo;

import com.ruoyi.project.merchant.domain.TCouponDetail;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TCouponDetailVo extends TCouponDetail {
    private Long userId;
    private String couponName;
    private String couponType;
    private String couponMold;
    private BigDecimal couponValue;
    private BigDecimal couponNum;
    private String parkNo;
}
