package com.czdx.parkingcharge.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 区域-车类型-车型-收费规则关联表
 * @TableName b_park_charge_relation_vehicle
 */
@TableName(value ="b_park_charge_relation_vehicle")
@Data
public class ParkChargeRelationVehicle implements Serializable {
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
     * 关联的收费规则ID
     */
    @TableField(value = "rule_id")
    private Integer ruleId;

    /**
     * 关联的停车场区域标识(ALL-全部(除专门设定的停车场外),其他-停车场区域ID)
     */
    @TableField(value = "park_lot_sign")
    private String parkLotSign;

    /**
     * 关联的车类型标识(LS-临时车,GD-全部固定车(除专门设定的固定车外),其他-固定车类型ID)
     */
    @TableField(value = "vehicle_category_sign")
    private String vehicleCategorySign;

    /**
     * 关联的车型标识(ALL-全部(除专门设定的车型外),其他-车型ID)
     */
    @TableField(value = "vehicle_type_sign")
    private String vehicleTypeSign;

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


    /**
     * 关系ID
     */
    @TableField(exist = false)
    private String relId;

    /**
     * 假期类型
     */
    @TableField(exist = false)
    private String holidayType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
