package com.ruoyi.project.merchant.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;


/**
 * 优惠券车牌关联对象 t_coupon_carno_relation
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@TableName("t_coupon_carno_relation")
@Data
public class TCouponCarnoRelation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @ApiModelProperty(value = "主键" , notes = "null")
    @TableId
    private Long id;

    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号" , notes = "null")
    @Excel(name = "车牌号")
    private String carNumber;

    /**
     * 优惠券码
     */


    @ApiModelProperty(value = "优惠券码" , notes = "null")
    @Excel(name = "优惠券码")
    private Long couponDetailId;

    /**
     * 有效开始时间
     */


    @ApiModelProperty(value = "有效开始时间" , notes = "null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "有效开始时间" , width = 30, dateFormat = "yyyy-MM-dd")
    private Date validStartTime;

    /**
     * 有效结束时间
     */


    @ApiModelProperty(value = "有效结束时间" , notes = "null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "有效结束时间" , width = 30, dateFormat = "yyyy-MM-dd")
    private Date validEndTime;


}
