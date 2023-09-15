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
 * 节假日设置对象 b_holiday
 * 
 * @author fangch
 * @date 2023-02-25
 */
@Data
@TableName("b_holiday")
public class BHoliday {

    /** 逻辑ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 车场编号 */
    @Excel(name = "车场编号")
    private String parkNo;

    /** 年份 */
    @Excel(name = "年份")
    private String year;

    /** 节日 */
    @Excel(name = "节日")
    private String name;

    /** 类别(1-假期,2-调休) */
    @Excel(name = "类别")
    private String type;

    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

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
