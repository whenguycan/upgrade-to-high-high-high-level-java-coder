package com.czdx.parkingcharge.mapper;

import com.czdx.parkingcharge.domain.ParkChargeDuration;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author mingchenxu
* @description 针对表【b_park_charge_duration(车场计费规则期间)】的数据库操作Mapper
* @createDate 2023-03-14 09:45:44
* @Entity com.czdx.parkingcharge.domain.ParkChargeDuration
*/
@Mapper
public interface ParkChargeDurationMapper extends BaseMapper<ParkChargeDuration> {

}




