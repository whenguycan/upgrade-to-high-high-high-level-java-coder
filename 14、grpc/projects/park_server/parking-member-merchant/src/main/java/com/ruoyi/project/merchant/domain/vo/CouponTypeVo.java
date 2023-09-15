package com.ruoyi.project.merchant.domain.vo;

import com.ruoyi.project.merchant.domain.TCouponType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CouponTypeVo extends TCouponType {

    @ApiModelProperty(value = "数量", notes = "null")
    private Integer amount = 0;
    @ApiModelProperty(value = "优惠券状态", notes = "null")
    private String couponStatus;
}
