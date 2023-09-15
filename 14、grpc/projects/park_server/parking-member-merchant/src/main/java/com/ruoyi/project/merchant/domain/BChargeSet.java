package com.ruoyi.project.merchant.domain;

import java.io.Serializable;
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

/**
 * 充值优惠配置对象 b_charge_set
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@TableName("b_charge_set")
@Data
@ApiModel(value = "充值优惠配置对象", description = "")
public class BChargeSet implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @ApiModelProperty(value = "主键", notes = "null")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 车场编号
     */

    @ApiModelProperty(value = "车场编号", notes = "null")
    @Excel(name = "车场编号")
    private String parkNo;
    /**
     * 充值金额
     */

    @ApiModelProperty(value = "充值金额", notes = "null")
    @Excel(name = "充值金额")
    private BigDecimal chargeAmount;
    /**
     * 赠送金额
     */

    @ApiModelProperty(value = "赠送金额", notes = "null")
    @Excel(name = "赠送金额")
    private BigDecimal giveAmount;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
