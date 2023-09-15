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
 * 车场计费规则期间时段对象 b_park_charge_duration_period
 * 
 * @author fangch
 * @date 2023-02-21
 */
@Data
@TableName("b_park_charge_duration_period")
public class BParkChargeDurationPeriod implements Comparable<BParkChargeDurationPeriod> {

    /** 逻辑ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 期间ID */
    @Excel(name = "期间ID")
    private Integer durationId;

    /** 时长（分钟） */
    @Excel(name = "时长")
    private Integer lengthOfTime;

    /** 最小单位时长（分钟） */
    @Excel(name = "最小单位时长")
    private Integer minLenghtOfTime;

    /** 费率（元） */
    @Excel(name = "费率")
    private BigDecimal rate;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

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

    @Override
    public int compareTo(BParkChargeDurationPeriod o) {
        return this.getSort().compareTo(o.getSort());
    }
}
