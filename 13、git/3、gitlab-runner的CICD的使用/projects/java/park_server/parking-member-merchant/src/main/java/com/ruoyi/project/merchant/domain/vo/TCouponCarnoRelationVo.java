package com.ruoyi.project.merchant.domain.vo;

import com.ruoyi.project.merchant.domain.TCouponCarnoRelation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "优惠券车牌关联对象", description = "")
public class TCouponCarnoRelationVo extends TCouponCarnoRelation {
    /**
     * 优惠券主表id
     */
    @ApiModelProperty(value = "couponId", notes = "null")
    private Long couponId;

    /**
     * 优惠券主表id
     */
    @ApiModelProperty(value = "优惠券码", notes = "null")
    private String couponCode;
}
