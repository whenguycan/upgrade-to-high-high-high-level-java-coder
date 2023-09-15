package com.ruoyi.project.merchant.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券明细对象 t_coupon_detail
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@TableName("t_coupon_detail")
@Data
public class TCouponDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @ApiModelProperty(value = "优惠券主表id", notes = "null")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 优惠券主表id
     */
    @ApiModelProperty(value = "优惠券主表id", notes = "null")
    @Excel(name = "优惠券主表id")
    private Long couponId;

    /**
     * 优惠券码
     */
    @ApiModelProperty(value = "优惠券码", notes = "null")
    @Excel(name = "优惠券码")
    private String couponCode;

    /**
     * 优惠券状态；0-未分配;1-已分配;2-已使用;3-失效
     */
    @ApiModelProperty(value = "优惠券状态；0-未分配;1-已分配;2-已使用;3-失效", notes = "null")
    @Excel(name = "优惠券状态；0-未分配;1-已分配;2-已使用;3-失效")
    private String couponStatus;

    /**
     * 分配时间
     */
    @ApiModelProperty(value = "分配时间", notes = "null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "分配时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date allocatedTime;

    /**
     * 使用时间
     */
    @ApiModelProperty(value = "使用时间", notes = "null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "使用时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date usedTime;

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
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validStartTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validEndTime;

    @ApiModelProperty(value = "订单号", notes = "null")
    private String orderNo;

    @ApiModelProperty(value = "车牌号", notes = "null")
    private String carNumber;

    @ApiModelProperty(value = "优惠券金额")
    private BigDecimal couponPrice;


}
