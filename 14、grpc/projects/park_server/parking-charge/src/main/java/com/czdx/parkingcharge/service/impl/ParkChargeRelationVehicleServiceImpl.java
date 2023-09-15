package com.czdx.parkingcharge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingcharge.domain.ParkChargeRelationVehicle;
import com.czdx.parkingcharge.service.ParkChargeRelationVehicleService;
import com.czdx.parkingcharge.mapper.ParkChargeRelationVehicleMapper;
import org.springframework.stereotype.Service;

/**
* @author mingchenxu
* @description 针对表【b_park_charge_relation_vehicle(区域-车类型-车型-收费规则关联表)】的数据库操作Service实现
* @createDate 2023-03-14 09:46:13
*/
@Service
public class ParkChargeRelationVehicleServiceImpl extends ServiceImpl<ParkChargeRelationVehicleMapper, ParkChargeRelationVehicle>
    implements ParkChargeRelationVehicleService{

}




