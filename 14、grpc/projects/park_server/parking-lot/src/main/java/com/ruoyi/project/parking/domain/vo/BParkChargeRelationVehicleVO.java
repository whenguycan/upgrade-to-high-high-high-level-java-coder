package com.ruoyi.project.parking.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.project.parking.domain.BParkChargeRelationVehicle;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 区域-车类型-车型-收费规则关联对象 b_park_charge_relation_vehicle
 * 
 * @author fangch
 * @date 2023-02-23
 */
@Data
public class BParkChargeRelationVehicleVO extends BParkChargeRelationVehicle {

    /** 收费规则名称 */
    private String ruleName;

    /** 停车场区域名称 */
    private String parkLotName;

    /** 车类型名称 */
    private String vehicleCategoryName;

    /** 车型名称 */
    private String vehicleTypeName;
}
