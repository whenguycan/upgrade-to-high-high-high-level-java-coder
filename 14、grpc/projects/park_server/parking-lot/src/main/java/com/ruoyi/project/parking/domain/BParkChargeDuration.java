package com.ruoyi.project.parking.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 车场计费规则期间对象 b_park_charge_duration
 * 
 * @author fangch
 * @date 2023-02-21
 */
@Data
@TableName("b_park_charge_duration")
public class BParkChargeDuration {

    /** 逻辑ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 计费规则ID */
    @Excel(name = "计费规则ID")
    private Integer ruleId;

    /** 开始时刻（分钟）-时刻计费使用 */
    @Excel(name = "开始时刻")
    private String startTime;

    /** 时长（分钟）-时长计费使用 */
    @Excel(name = "时长")
    private Integer lengthOfTime;

    /** 免费时长（分钟） */
    @Excel(name = "免费时长")
    private Integer freeMinute;

    /** 期间最高收费（元） */
    @Excel(name = "期间最高收费")
    private Integer maximumCharge;

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

}
