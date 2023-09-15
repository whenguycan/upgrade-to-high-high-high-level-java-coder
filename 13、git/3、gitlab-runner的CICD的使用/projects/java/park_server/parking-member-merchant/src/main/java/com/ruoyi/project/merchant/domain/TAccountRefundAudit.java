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

import javax.validation.constraints.NotBlank;

/**
 * 商户账户余额退款审核对象 t_account_refund_audit
 *
 * @author ruoyi
 * @date 2023-03-08
 */
@TableName("t_account_refund_audit")
@Data
@ApiModel(value = "商户账户余额退款审核对象", description = "")
public class TAccountRefundAudit implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @ApiModelProperty(value = "主键", notes = "null")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 退款单号
     */
    @ApiModelProperty(value = "退款单号", notes = "null")
    @Excel(name = "退款单号")
    private String orderNo;

    /**
     * 所属车场编号
     */

    @ApiModelProperty(value = "所属车场编号", notes = "null")
    @Excel(name = "所属车场编号")
    private String parkNo;

    /**
     * 商户ID
     */
    @ApiModelProperty(value = "商户ID", notes = "null")
    @Excel(name = "商户ID")
    private Long applyUserId;

    @ApiModelProperty(value = "商户名称", notes = "null")
    @Excel(name = "商户名称")
    private String applyUserName;

    @ApiModelProperty(value = "申请时间", notes = "null")
    @Excel(name = "申请时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;


    /**
     * 退款金额（元）
     */

    @ApiModelProperty(value = "退款金额", notes = "null")
    @Excel(name = "退款金额", readConverterExp = "元=")
    private BigDecimal refundAmount;

    /**
     * 赠送金额（元）
     */

    @ApiModelProperty(value = "赠送金额", notes = "null")
    @Excel(name = "赠送金额", readConverterExp = "元=")
    private BigDecimal giveAmount;

    /**
     * 退款状态；0-已申请；1-审核通过
     */

    @ApiModelProperty(value = "退款状态；0-已申请；1-审核通过 2-审核不通过", notes = "null")
    @Excel(name = "退款状态；0-已申请；1-审核通过")
    @NotBlank(message = "审核状态不能为空")
    private String refundStatus;
    /**
     * 申请说明
     */

    @ApiModelProperty(value = "申请说明", notes = "null")
    @Excel(name = "申请说明")
    private String applyRemark;
    /**
     * 审核人id
     */

    @ApiModelProperty(value = "审核人id", notes = "null")
    @Excel(name = "审核人id")
    private Long auditUserId;
    /**
     * 审核人姓名
     */

    @ApiModelProperty(value = "审核人姓名", notes = "null")
    @Excel(name = "审核人姓名")
    private String auditUserName;
    /**
     * 审核时间
     */

    @ApiModelProperty(value = "审核时间", notes = "null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date auditTime;
    /**
     * 审核说明
     */
    @ApiModelProperty(value = "审核说明", notes = "null")
    @Excel(name = "审核说明")
    @NotBlank(message = "申请说明不能为空")
    private String auditRemark;
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
}
