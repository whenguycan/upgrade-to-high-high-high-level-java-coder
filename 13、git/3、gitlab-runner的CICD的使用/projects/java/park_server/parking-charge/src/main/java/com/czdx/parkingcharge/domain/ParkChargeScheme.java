package com.czdx.parkingcharge.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 停车场收费方案表
 * @TableName b_park_charge_scheme
 */
@TableName(value ="b_park_charge_scheme")
@Data
public class ParkChargeScheme implements Serializable {
    /**
     * 逻辑ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 车场编号
     */
    @TableField(value = "park_no")
    private String parkNo;

    /**
     * 舍入方式（1=全入；2=全舍；3=四舍五入）
     */
    @TableField(value = "round_way")
    private String roundWay;

    /**
     * 最高收费金额
     */
    @TableField(value = "maximum_charge")
    private BigDecimal maximumCharge;

    /**
     * 最小收费精度
     */
    @TableField(value = "minimum_charge_accurary")
    private BigDecimal minimumChargeAccurary;

    /**
     * 时间优惠劵包含免费时段（N=不包含；Y=包含）
     */
    @TableField(value = "tc_free_time_flag")
    private String tcFreeTimeFlag;

    /**
     * 时间优惠券是使用方式（1=入场时间前移；2=出场时间后移；3=减掉优惠时间）
     */
    @TableField(value = "tc_use_way")
    private String tcUseWay;

    /**
     * 秒进位方式（1=截秒；2=秒进位）
     */
    @TableField(value = "second_carry")
    private String secondCarry;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
