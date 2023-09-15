package com.czdx.parkingcharge.mapper;

import com.czdx.parkingcharge.domain.ParkChargeScheme;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author mingchenxu
* @description 针对表【b_park_charge_scheme(停车场收费方案表)】的数据库操作Mapper
* @createDate 2023-03-14 09:42:04
* @Entity com.czdx.parkingcharge.domain.ParkChargeScheme
*/
@Mapper
public interface ParkChargeSchemeMapper extends BaseMapper<ParkChargeScheme> {

}




