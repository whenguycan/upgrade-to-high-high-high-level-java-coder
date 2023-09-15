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
 * 车场计费规则期间
 * @TableName b_park_charge_duration
 */
@TableName(value ="b_park_charge_duration")
@Data
public class ParkChargeDuration implements Serializable {
    /**
     * 逻辑ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 计费规则ID
     */
    @TableField(value = "rule_id")
    private Integer ruleId;

    /**
     * 开始时刻（分钟）-时刻计费使用
     */
    @TableField(value = "start_time")
    private String startTime;

    /**
     * 时长（分钟）-时长计费使用
     */
    @TableField(value = "length_of_time")
    private Integer lengthOfTime;

    /**
     * 免费时长（分钟）
     */
    @TableField(value = "free_minute")
    private Integer freeMinute;

    /**
     * 期间最高收费（元）
     */
    @TableField(value = "maximum_charge")
    private BigDecimal maximumCharge;

    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;

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
