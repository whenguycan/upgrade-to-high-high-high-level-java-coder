package com.ruoyi.project.parking.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 首页收入统计事实对象 revenue_statistics_day_fact
 * 
 * @author fangch
 * @date 2023-03-28
 */
@Data
@TableName("revenue_statistics_day_fact")
public class RevenueStatisticsDayFact {

    /** 逻辑ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 车场编号 */
    @Excel(name = "车场编号")
    private String parkNo;

    /** 统计日期 */
    @Excel(name = "统计日期")
    private String day;

    /** 线上应收金额（元） */
    @Excel(name = "线上应收金额")
    private BigDecimal onlinePayableAmount;

    /** 线上实收金额（元） */
    @Excel(name = "线上实收金额")
    private BigDecimal onlinePayAmount;

    /** 现金应收金额（元） */
    @Excel(name = "现金应收金额")
    private BigDecimal cashPayableAmount;

    /** 现金实收金额（元） */
    @Excel(name = "现金实收金额")
    private BigDecimal cashPayAmount;

    /** 免费应收金额（元） */
    @Excel(name = "免费应收金额")
    private BigDecimal freePayableAmount;

    /** 免费实收金额（元） */
    @Excel(name = "免费实收金额")
    private BigDecimal freePayAmount;

    /** 车场优惠（笔） */
    @Excel(name = "车场优惠")
    private Integer concession;

    /** 商家券 */
    @Excel(name = "商家券")
    private Integer merchantVoucher;

    /** 交易（笔） */
    @Excel(name = "交易")
    private Integer transactionNumber;

    /**
     * 线上收入
     */
    @TableField(value = "online_income")
    private BigDecimal onlineIncome;

    /**
     * 线上支出
     */
    @TableField(value = "online_outgo")
    private BigDecimal onlineOutgo;

    /**
     * 线上结算
     */
    @TableField(value = "online_amount")
    private BigDecimal onlineAmount;

    /**
     * 现金收入
     */
    @TableField(value = "cash_income")
    private BigDecimal cashIncome;

    /**
     * 减免金额
     */
    @TableField(value = "deduction_amount")
    private BigDecimal deductionAmount;

    /**
     * 本日结算
     */
    @TableField(value = "day_amount")
    private BigDecimal dayAmount;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
