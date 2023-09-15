package com.czdx.parkingcharge.mapper;

import com.czdx.parkingcharge.domain.ParkChargeRelationVehicle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author mingchenxu
* @description 针对表【b_park_charge_relation_vehicle(区域-车类型-车型-收费规则关联表)】的数据库操作Mapper
* @createDate 2023-03-14 09:46:13
* @Entity com.czdx.parkingcharge.domain.ParkChargeRelationVehicle
*/
@Mapper
public interface ParkChargeRelationVehicleMapper extends BaseMapper<ParkChargeRelationVehicle> {

}




