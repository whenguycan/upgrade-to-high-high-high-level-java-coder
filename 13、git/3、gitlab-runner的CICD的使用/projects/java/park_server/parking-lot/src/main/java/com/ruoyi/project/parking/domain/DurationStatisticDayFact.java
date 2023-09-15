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
 * 首页时长统计事实对象 duration_statistic_day_fact
 * 
 * @author fangch
 * @date 2023-03-20
 */
@Data
@TableName("duration_statistic_day_fact")
public class DurationStatisticDayFact{

    /** 逻辑ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 车场编号 */
    @Excel(name = "车场编号")
    private String parkNo;

    /** 统计日期 */
    @Excel(name = "统计日期")
    private String day;

    /** 时长类型【字典表】 */
    @Excel(name = "时长类型【字典表】")
    private String durationType;

    /** 车辆数 */
    @Excel(name = "车辆数")
    private Integer count;

    /** 占比 */
    @Excel(name = "占比")
    private BigDecimal ratio;

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
