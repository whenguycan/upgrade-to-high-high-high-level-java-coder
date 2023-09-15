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
 * 车场计费规则期间时段
 * @TableName b_park_charge_duration_period
 */
@TableName(value ="b_park_charge_duration_period")
@Data
public class ParkChargeDurationPeriod implements Serializable {
    /**
     * 逻辑ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 期间ID
     */
    @TableField(value = "duration_id")
    private Integer durationId;

    /**
     * 时长（分钟）
     */
    @TableField(value = "length_of_time")
    private Integer lengthOfTime;

    /**
     * 最小单位时长（分钟）
     */
    @TableField(value = "min_lenght_of_time")
    private Integer minLenghtOfTime;

    /**
     * 费率（元）
     */
    @TableField(value = "rate")
    private BigDecimal rate;

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
