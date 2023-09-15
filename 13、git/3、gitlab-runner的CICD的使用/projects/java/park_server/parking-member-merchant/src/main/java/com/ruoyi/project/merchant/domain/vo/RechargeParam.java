package com.ruoyi.project.merchant.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RechargeParam implements Serializable {

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
    // 微信支付的支付方式 1为JSAPI支付 2为H5支付
    private Integer weChatPayMethod;

    // 如果是微信的jsapi支付，这个参数必传
    private String openId;

    //下面如果是微信h5支付的情况下，才要传参
    // 用户的客户端IP，支持IPv4和IPv6两种格式的IP地址。
    private String payerClientIp;
    // 场景类型 示例值：iOS, Android, Wap
    private String h5Type;
}
