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
 * 首页付费方式分析事实对象 pay_method_day_fact
 * 
 * @author fangch
 * @date 2023-03-20
 */
@Data
@TableName("pay_method_day_fact")
public class PayMethodDayFact{

    /** 逻辑ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 车场编号 */
    @Excel(name = "车场编号")
    private String parkNo;

    /** 统计日期 */
    @Excel(name = "统计日期")
    private String day;

    /** 付费方式【字典表】 */
    @Excel(name = "付费方式【字典表】")
    private String payMethod;

    /** 金额（元） */
    @Excel(name = "金额（元）")
    private BigDecimal amount;

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
