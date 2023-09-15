package com.ruoyi.project.merchant.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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

/**
 * 流水信息对象 t_oper_records
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@TableName("t_oper_records")
@Data
public class TOperRecords implements Serializable {
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

    /**
     * 操作类别；1-充值；2-续费；3-使用；4-回收；5-退款；6-作废；
     */
    @ApiModelProperty(value = "操作类别；1-充值；2-续费；3-使用；4-回收；5-退款；6-作废；", notes = "null")
    @Excel(name = "操作类别；1-充值；2-续费；3-使用；4-回收；5-退款；6-作废；")
    private Integer operatorType;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号", notes = "null")
    @Excel(name = "订单号")
    private String orderNo;

    /**
     * 涉及金额（元）
     */
    @ApiModelProperty(value = "涉及金额", notes = "null")
    @Excel(name = "涉及金额", readConverterExp = "元=")

    private BigDecimal amount;

    /**
     * 操作人员id
     */
    @ApiModelProperty(value = "操作人员id", notes = "null")
    @Excel(name = "操作人员id")
    private Long operId;

    /**
     * 操作人员
     */
    @ApiModelProperty(value = "操作人员", notes = "null")
    @Excel(name = "操作人员")
    private String operName;

    /**
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数", notes = "null")
    @Excel(name = "请求参数")
    private String operParam;

    /**
     * 返回参数
     */
    @ApiModelProperty(value = "返回参数", notes = "null")
    @Excel(name = "返回参数")
    private String jsonResult;

    /**
     * 操作状态（0正常 1异常）
     */
    @ApiModelProperty(value = "操作状态", notes = "null")
    @Excel(name = "操作状态", readConverterExp = "0=正常,1=异常")
    private Integer status;


    /**
     * 赠送金额
     */
    @ApiModelProperty(value = "赠送金额", notes = "null")
    private BigDecimal giveAmount;

    /**
     * 错误消息
     */
    @ApiModelProperty(value = "错误消息", notes = "null")
    @Excel(name = "错误消息")
    private String errorMsg;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间", notes = "null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operTime;


}
