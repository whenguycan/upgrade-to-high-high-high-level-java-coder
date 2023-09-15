package com.ruoyi.project.merchant.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RechargeVoParam implements Serializable {

    //场地编号
    @ApiModelProperty(value = "场地编号", notes = "null")
    @NotBlank(message = "没有指定场地编号")
    private String parkNo;
    @ApiModelProperty(value = "充值金额", notes = "null")
    @NotNull(message = "没有指定充值金额")
    private BigDecimal payAmount;
    @ApiModelProperty(value = "支付方式", notes = "null")
    @NotNull(message = "没有指定支付方式")
    private Integer payType;
}
