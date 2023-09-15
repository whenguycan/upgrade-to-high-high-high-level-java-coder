package com.ruoyi.project.merchant.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券种类对象 t_coupon_type
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@TableName("t_coupon_type")
@Data
public class TCouponType implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键", notes = "null")
    private Long id;

    /**
     * 所属车场编号
     */
    @ApiModelProperty(value = "所属车场编号", notes = "null")
    @Excel(name = "所属车场编号")
    private String parkNo;

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
     * 优惠券类型；1-平台券；2-商户券
     */
    @ApiModelProperty(value = "优惠券类型；1-平台券；2-商户券", notes = "null")
    @Excel(name = "优惠券类型；1-平台券；2-商户券")
    private String couponMold;

    /**
     * 优惠券值（面值型则为金额，小时型则为小时）
     */
    @ApiModelProperty(value = "优惠券值", notes = "null")
    @Excel(name = "优惠券值", readConverterExp = "面=值型则为金额，小时型则为小时")
    @NotNull(message = "优惠券值不能为空")
    private BigDecimal couponValue;

    /**
     * 所属用户ID
     */
    @ApiModelProperty(value = "所属用户ID", notes = "null")
    @Excel(name = "所属用户ID")
    private Long userId;


    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
