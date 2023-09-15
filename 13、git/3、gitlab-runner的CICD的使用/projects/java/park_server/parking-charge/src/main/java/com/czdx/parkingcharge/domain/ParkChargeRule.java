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
 * 车场收费规则
 * @TableName b_park_charge_rule
 */
@TableName(value ="b_park_charge_rule")
@Data
public class ParkChargeRule implements Serializable {
    /**
     * 逻辑ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 车场编号
     */
    @TableField(value = "park_no")
    private String parkNo;

    /**
     * 规则名称
     */
    @TableField(value = "rule_name")
    private String ruleName;

    /**
     * 期间生成方式（1=按时刻计时；2=按时长计时）
     */
    @TableField(value = "duration_create_way")
    private String durationCreateWay;

    /**
     * 期间个数
     */
    @TableField(value = "duration_num")
    private Integer durationNum;

    /**
     * 是否最高限价（N=否；Y=是）
     */
    @TableField(value = "ceiling_price_flag")
    private String ceilingPriceFlag;

    /**
     * 最高限价分钟内
     */
    @TableField(value = "ceiling_price_minute")
    private Integer ceilingPriceMinute;

    /**
     * 最高限价（元）
     */
    @TableField(value = "ceiling_price")
    private BigDecimal ceilingPrice;

    /**
     * 算费包含免费时间（N=否；Y=是）
     */
    @TableField(value = "charge_contain_free_time")
    private String chargeContainFreeTime;

    /**
     * 免费时长次数（1=一次收费一次；2=每个分割一次）
     */
    @TableField(value = "free_minute_number")
    private String freeMinuteNumber;

    /**
     * 计时分割方式（1=不分割；2=24小时强制分割；3=0点强制分割；4=期间强制分割）
     */
    @TableField(value = "time_division_way")
    private String timeDivisionWay;

    /**
     * 首时段计费方式（1=一次收费只有一次；2=每次分割一次）
     */
    @TableField(value = "first_duration_charge_way")
    private String firstDurationChargeWay;

    /**
     * 计时舍入方式（1=全入；2=全舍；3=四舍五入）
     */
    @TableField(value = "time_round_way")
    private String timeRoundWay;

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
