package com.ruoyi.project.parking.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 首页今日泊车使用情况事实对象 park_usage_day_fact
 * 
 * @author fangch
 * @date 2023-03-28
 */
@Data
@TableName("park_usage_day_fact")
public class ParkUsageDayFact {

    /** 逻辑ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 车场编号 */
    @Excel(name = "车场编号")
    private String parkNo;

    /** 统计日期 */
    @Excel(name = "统计日期")
    private String day;

    /** 入场数 */
    @Excel(name = "入场数")
    private Integer entryCount;

    /** 出场数 */
    @Excel(name = "出场数")
    private Integer exitCount;

    /** 进出数 */
    @Excel(name = "进出数")
    private Integer leaveCount;

    /** 泊车使用率 */
    @Excel(name = "泊车使用率")
    private BigDecimal useRatio;

    /** 异常抬杆率 */
    @Excel(name = "异常抬杆率")
    private BigDecimal abnormalRate;

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
