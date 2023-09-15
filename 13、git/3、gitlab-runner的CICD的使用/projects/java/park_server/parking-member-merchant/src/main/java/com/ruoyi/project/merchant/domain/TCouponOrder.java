package com.ruoyi.project.merchant.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * H5商户优惠券购买订单对象 t_coupon_order
 *
 * @author ruoyi
 * @date 2023-03-07
 */
@TableName("t_coupon_order")
@Data
@ApiModel(value = "H5商户优惠券购买订单对象", description = "")
@Validated
public class TCouponOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @ApiModelProperty(value = "主键", notes = "null")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 订单号
     */

    @ApiModelProperty(value = "订单号", notes = "null")
    @Excel(name = "订单号")
    private String orderNo;
    /**
     * 订单类型（字典表）
     */

    @ApiModelProperty(value = "订单类型", notes = "null")
    @Excel(name = "订单类型", readConverterExp = "字=典表")
    private String orderType;
    /**
     * 订单状态（字典表）
     */

    @ApiModelProperty(value = "订单状态", notes = "null")
    @Excel(name = "订单状态", readConverterExp = "字=典表")
    private String orderStatus;
    /**
     * 场库编号
     */

    @ApiModelProperty(value = "场库编号", notes = "null")
    @Excel(name = "场库编号")
    @NotBlank(message = "场库编号不能为空")
    private String parkNo;
    /**
     * 商户id
     */

    @ApiModelProperty(value = "商户id", notes = "null")
    @Excel(name = "商户id")
    private Long userId;
    /**
     * 购买优惠券张数
     */

    @ApiModelProperty(value = "购买优惠券张数", notes = "null")
    @Excel(name = "购买优惠券张数")
    private Long payNum;
    /**
     * 优惠券主表id
     */

    @ApiModelProperty(value = "优惠券主表id", notes = "null")
    @Excel(name = "优惠券主表id")
    private Long couponId;

    @ApiModelProperty(value = "单张优惠券价格", notes = "null")
    @Excel(name = "单张优惠券价格")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "优惠券名称", notes = "null")
    @Excel(name = "优惠券名称")
    private String couponName;

    /**
     * 优惠券种类;1-面值型；2-小时型
     */
    @ApiModelProperty(value = "优惠券种类;1-面值型；2-小时型", notes = "null")
    @Excel(name = "优惠券种类;1-面值型；2-小时型")
    private String couponType;
    /**
     * 应付金额（元）
     */

    @ApiModelProperty(value = "应付金额", notes = "null")
    @Excel(name = "应付金额", readConverterExp = "元=")

    private BigDecimal payableAmount;
    /**
     * 优惠金额（元）
     */

    @ApiModelProperty(value = "优惠金额", notes = "null")
    @Excel(name = "优惠金额", readConverterExp = "元=")

    private BigDecimal discountAmount;
    /**
     * 已付金额（元）
     */

    @ApiModelProperty(value = "已付金额", notes = "null")
    @Excel(name = "已付金额", readConverterExp = "元=")

    private BigDecimal paidAmount;
    /**
     * 实付金额（元）
     */

    @ApiModelProperty(value = "实付金额", notes = "null")
    @Excel(name = "实付金额", readConverterExp = "元=")

    private BigDecimal payAmount;
    /**
     * 支付时间
     */

    @ApiModelProperty(value = "支付时间", notes = "null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payTime;
    /**
     * 支付状态
     */

    @ApiModelProperty(value = "支付状态", notes = "null")
    @Excel(name = "支付状态")
    private String payStatus;
    /**
     *
     */
    @ApiModelProperty(value = "remark", notes = "null")
    @Excel(name = "remark")
    private String remark;
}
