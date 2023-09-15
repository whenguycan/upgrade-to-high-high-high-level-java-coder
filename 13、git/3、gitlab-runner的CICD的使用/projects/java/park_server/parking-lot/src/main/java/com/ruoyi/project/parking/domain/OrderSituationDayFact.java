package com.ruoyi.project.parking.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 首页订单情况事实对象 order_situation_day_fact
 * 
 * @author fangch
 * @date 2023-03-20
 */
@Data
@TableName("order_situation_day_fact")
public class OrderSituationDayFact{

    /** 逻辑ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 车场编号 */
    @Excel(name = "车场编号")
    private String parkNo;

    /** 统计日期 */
    @Excel(name = "统计日期")
    private String day;

    /** 订单数 */
    @Excel(name = "订单数")
    private Integer count;

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
