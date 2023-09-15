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
 * 停车场收费方案对象 b_park_charge_scheme
 * 
 * @author fangch
 * @date 2023-02-21
 */
@Data
@TableName("b_park_charge_scheme")
public class BParkChargeScheme {

    /** 逻辑ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 车场编号 */
    @Excel(name = "车场编号")
    @TableField(value = "park_no")
    private String parkNo;

    /** 舍入方式（1=全入；2=全舍；3=四舍五入） */
    @Excel(name = "舍入方式", readConverterExp = "1=全入,2=全舍,3=四舍五入")
    @TableField(value = "round_way")
    private String roundWay;

    /** 最高收费金额 */
    @Excel(name = "最高收费金额")
    @TableField(value = "maximum_charge")
    private BigDecimal maximumCharge;

    /** 最小收费精度 */
    @Excel(name = "最小收费精度")
    @TableField(value = "minimum_charge_accurary")
    private BigDecimal minimumChargeAccurary;

    /** 时间优惠劵包含免费时段（0=不包含；1=包含） */
    @Excel(name = "时间优惠劵包含免费时段", readConverterExp = "0=不包含,1=包含")
    @TableField(value = "tc_free_time_flag")
    private String tcFreeTimeFlag;

    /** 时间优惠券是使用方式（1=入场时间前移；2=出场时间后移；3=减掉优惠时间） */
    @Excel(name = "时间优惠券是使用方式", readConverterExp = "1=入场时间前移,2=出场时间后移,3=减掉优惠时间")
    @TableField(value = "tc_use_way")
    private String tcUseWay;

    /** 秒进位方式（1=截秒；2=秒进位） */
    @Excel(name = "秒进位方式", readConverterExp = "1=截秒,2=秒进位")
    @TableField(value = "second_carry")
    private String secondCarry;

    /** 创建者 */
    @TableField(value = "create_by")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /** 更新者 */
    @TableField(value = "update_by")
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}
