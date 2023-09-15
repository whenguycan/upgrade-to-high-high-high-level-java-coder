package com.ruoyi.project.merchant.domain.vo;

import com.ruoyi.project.system.domain.SysUser;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("优惠券订购，作废")
@Data
public class OrderCouponVo extends OrderCouponVoParam {

    //总金额
    private BigDecimal calculateAmount;
    //账户余额
    private BigDecimal balance;
    //优惠券类型
    private String couponType;
    //优惠券购买的金额
    private BigDecimal unitValue;
    //优惠券名称
    private String couponName;
    //优惠券的状态
    private String couponStatus;
    //特殊情况的标注，针对小时券
    private String hourCouponMark;
}
