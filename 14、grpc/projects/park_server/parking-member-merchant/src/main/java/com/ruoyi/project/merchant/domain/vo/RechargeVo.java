package com.ruoyi.project.merchant.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class RechargeVo extends RechargeVoParam {

    //商户id
    @ApiModelProperty(value = "商户id", notes = "null")
    private Long erchantId;
    //赠送金额
    @ApiModelProperty(value = "赠送金额", notes = "null")
    private BigDecimal discountAmount;
}
