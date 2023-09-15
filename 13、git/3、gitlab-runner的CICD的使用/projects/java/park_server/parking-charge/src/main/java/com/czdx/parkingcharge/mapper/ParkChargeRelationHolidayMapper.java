package com.czdx.parkingcharge.mapper;

import com.czdx.parkingcharge.domain.ParkChargeRelationHoliday;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
* @author mingchenxu
* @description 针对表【b_park_charge_relation_holiday(节假日-区域-车类型-车型-收费规则关联表)】的数据库操作Mapper
* @createDate 2023-03-14 09:46:07
* @Entity com.czdx.parkingcharge.domain.ParkChargeRelationHoliday
*/
@Mapper
public interface ParkChargeRelationHolidayMapper extends BaseMapper<ParkChargeRelationHoliday> {

    /**
     *
     * description: 查询节假日类型
     * @author mingchenxu
     * @date 2023/4/11 17:08
     * @param curDay 当前日期
     * @return java.lang.String
     */
    String selectHolidayType(@Param("parkNo") String parkNo, @Param("curDay") LocalDateTime curDay);
}




