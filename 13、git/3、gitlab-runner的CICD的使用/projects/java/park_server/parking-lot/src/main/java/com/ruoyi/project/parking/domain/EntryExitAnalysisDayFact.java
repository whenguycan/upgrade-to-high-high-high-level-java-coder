package com.ruoyi.project.parking.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 首页出入场分析事实对象 entry_exit_analysis_day_fact
 * 
 * @author fangch
 * @date 2023-03-20
 */
@Data
@TableName("entry_exit_analysis_day_fact")
public class EntryExitAnalysisDayFact {

    /** 逻辑ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 车场编号 */
    @Excel(name = "车场编号")
    private String parkNo;

    /** 统计日期 */
    @Excel(name = "统计日期")
    private String day;

    /** 时段 */
    @Excel(name = "时段")
    private String timeInterval;

    /** 入场车辆数 */
    @Excel(name = "入场车辆数")
    private Integer entryCount;

    /** 出场车辆数 */
    @Excel(name = "出场车辆数")
    private Integer exitCount;

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
