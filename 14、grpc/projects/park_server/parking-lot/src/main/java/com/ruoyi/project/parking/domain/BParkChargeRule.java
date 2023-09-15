package com.ruoyi.project.parking.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 车场收费规则对象 b_park_charge_rule
 *
 * @author fangch
 * @date 2023-02-21
 */
@Data
@TableName("b_park_charge_rule")
public class BParkChargeRule {

    /** 逻辑ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 车场编号 */
    @Excel(name = "车场编号")
    private String parkNo;

    /** 规则名称 */
    @Excel(name = "规则名称")
    private String ruleName;

    /** 期间生成方式（1=按时刻计时；2=按时长计时） */
    @Excel(name = "期间生成方式", readConverterExp = "1=按时刻计时,2=按时长计时")
    private String durationCreateWay;

    /** 期间个数 */
    @Excel(name = "期间个数")
    private Integer durationNum;

    /** 是否最高限价（N=否；Y=是） */
    @Excel(name = "是否最高限价", readConverterExp = "N=否,Y=是")
    private String ceilingPriceFlag;

    /** 最高限价分钟内 */
    @Excel(name = "最高限价分钟内")
    private Integer ceilingPriceMinute;

    /** 最高限价（元） */
    @Excel(name = "最高限价")
    private BigDecimal ceilingPrice;

    /** 算费包含免费时间（N=否；Y=是） */
    @Excel(name = "算费包含免费时间", readConverterExp = "N=否,Y=是")
    private String chargeContainFreeTime;

    /** 免费时长次数（1=一次收费一次；2=每个分割一次） */
    @Excel(name = "免费时长次数", readConverterExp = "1=一次收费一次,2=每个分割一次")
    private String freeMinuteNumber;

    /** 计时分割方式（1=不分割；2=24小时强制分割；3=0点强制分割；4=期间强制分割） */
    @Excel(name = "计时分割方式", readConverterExp = "1=不分割,2=24小时强制分割,3=0点强制分割,4=期间强制分割")
    private String timeDivisionWay;

    /** 首时段计费方式（1=一次收费只有一次；2=每次分割一次） */
    @Excel(name = "首时段计费方式", readConverterExp = "1=一次收费只有一次,2=每次分割一次")
    private String firstDurationChargeWay;

    /** 计时舍入方式（1=全入；2=全舍；3=四舍五入） */
    @Excel(name = "计时舍入方式", readConverterExp = "1=全入,2=全舍,3=四舍五入")
    private String timeRoundWay;

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
