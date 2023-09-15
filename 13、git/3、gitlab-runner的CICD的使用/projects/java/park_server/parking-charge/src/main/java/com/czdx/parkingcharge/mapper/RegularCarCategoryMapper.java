package com.czdx.parkingcharge.mapper;

import com.czdx.parkingcharge.domain.RegularCarCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author mingchenxu
* @description 针对表【b_setting_regular_car_category(固定车类型表)】的数据库操作Mapper
* @createDate 2023-03-30 14:22:31
* @Entity com.czdx.parkingcharge.domain.RegularCarCategory
*/
@Mapper
public interface RegularCarCategoryMapper extends BaseMapper<RegularCarCategory> {

}




