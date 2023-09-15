package com.czdx.parkingcharge.mapper;

import com.czdx.parkingcharge.domain.ParkChargeDurationPeriod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author mingchenxu
* @description 针对表【b_park_charge_duration_period(车场计费规则期间时段)】的数据库操作Mapper
* @createDate 2023-03-14 09:45:54
* @Entity com.czdx.parkingcharge.domain.ParkChargeDurationPeriod
*/
@Mapper
public interface ParkChargeDurationPeriodMapper extends BaseMapper<ParkChargeDurationPeriod> {

}




