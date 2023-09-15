package com.ruoyi.project.parking.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.time.LocalDateTime;

/**
 * 节假日-区域-车类型-车型-收费规则关联对象 b_park_charge_relation_holiday
 * 
 * @author fangch
 * @date 2023-02-23
 */
@Data
@TableName("b_park_charge_relation_holiday")
public class BParkChargeRelationHoliday {

    /** 逻辑ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 车场编号 */
    @Excel(name = "车场编号")
    private String parkNo;

    /** 关联的收费规则ID */
    @Excel(name = "关联的收费规则ID")
    private Integer ruleId;

    /** 关联的节假日类型(1-假期,2-调休) */
    @Excel(name = "关联的节假日类型")
    private String holidayType;

    /** 关联的停车场区域标识(ALL-全部(除专门设定的停车场外),其他-停车场区域ID) */
    @Excel(name = "关联的停车场区域标识")
    private String parkLotSign;

    /** 关联的车类型标识(LS-临时车,GD-全部固定车(除专门设定的固定车外),其他-固定车类型ID) */
    @Excel(name = "关联的车类型标识")
    private String vehicleCategorySign;

    /** 关联的车型标识(ALL-全部(除专门设定的车型外),其他-车型ID) */
    @Excel(name = "关联的车型标识")
    private String vehicleTypeSign;

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
