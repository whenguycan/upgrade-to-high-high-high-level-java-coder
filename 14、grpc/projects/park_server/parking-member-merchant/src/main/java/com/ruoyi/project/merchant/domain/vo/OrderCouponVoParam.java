package com.ruoyi.project.merchant.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderCouponVoParam implements Serializable {


    @ApiModelProperty(value = "优惠券id", notes = "null")
    private Long couponId;

    @ApiModelProperty(value = "优惠券数量", notes = "null")
    private BigDecimal couponNum;

    @ApiModelProperty(value = "场地编号", notes = "null")
    private String parkNo;
}
