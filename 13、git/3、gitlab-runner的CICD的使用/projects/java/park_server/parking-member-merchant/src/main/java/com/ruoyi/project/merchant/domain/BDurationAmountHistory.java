package com.ruoyi.project.merchant.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BDurationAmountHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @ApiModelProperty(value = "主键", notes = "null")
    @TableId(value = "{id}", type = IdType.AUTO)
    private Long id;

    /**
     * $column.columnComment
     */
    @ApiModelProperty(value = "优惠抵扣时长金额配置表id", notes = "null")
    @Excel(name = "优惠抵扣时长金额配置表id")
    private Long durationAmountId;

    /**
     * 车场编号
     */

    @ApiModelProperty(value = "车场编号", notes = "null")
    @Excel(name = "车场编号")
    private String parkNo;
    /**
     * 优惠券抵扣时长
     */

    @ApiModelProperty(value = "优惠券抵扣时长", notes = "null")
    @Excel(name = "优惠券抵扣时长")
    private Long duration;
    /**
     * 售价金额
     */

    @ApiModelProperty(value = "售价金额", notes = "null")
    @Excel(name = "售价金额")
    private BigDecimal amount;

    /** 入库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enterTime;

    @ApiModelProperty(value = "来源；D-删除；M-修改", notes = "null")
    @Excel(name = "来源；D-删除；M-修改")
    private String source;
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
