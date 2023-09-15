package com.czdx.parkingcharge.service;

import com.czdx.parkingcharge.domain.ParkChargeRelationVehicle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.czdx.parkingcharge.domain.custom.ParkChargeVehicleRelationCustom;

import java.util.List;

/**
* @author mingchenxu
* @description 针对表【b_park_charge_relation_vehicle(区域-车类型-车型-收费规则关联表)】的数据库操作Service
* @createDate 2023-03-14 09:46:13
*/
public interface ParkChargeRelationVehicleService extends IService<ParkChargeRelationVehicle> {

    /**
     *
     * description: 加载计费规则与车型关联
     * @author mingchenxu
     * @date 2023/3/31 13:18
     * @return int
     */
    int loadParkChargeRuleVehicleRelations();

    /**
     *
     * description: 刷新车场计费关系
     * @author mingchenxu
     * @date 2023/3/31 16:35
     * @param parkNo 车场编号
     * @return int
     */
    int refreshParkLotChargeRelation(String parkNo);

    /**
     *
     * description: 获取车场计费车型关联
     * @author mingchenxu
     * @date 2023/3/30 18:26
     * @param parkNo 车场编号
     * @return java.util.List<com.czdx.parkingcharge.domain.ParkChargeRelationVehicle>
     */
    List<ParkChargeVehicleRelationCustom> getParkChargeRelationVehicles(String parkNo);

}
