package com.czdx.parkingcharge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingcharge.domain.ParkChargeRelationHoliday;
import com.czdx.parkingcharge.service.ParkChargeRelationHolidayService;
import com.czdx.parkingcharge.mapper.ParkChargeRelationHolidayMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
* @author mingchenxu
* @description 针对表【b_park_charge_relation_holiday(节假日-区域-车类型-车型-收费规则关联表)】的数据库操作Service实现
* @createDate 2023-03-14 09:46:07
*/
@Service
public class ParkChargeRelationHolidayServiceImpl extends ServiceImpl<ParkChargeRelationHolidayMapper, ParkChargeRelationHoliday>
    implements ParkChargeRelationHolidayService{

    /**
     *
     * description: 获取当前时间的节假日类型
     * @author mingchenxu
     * @date 2023/4/11 17:02
     * @param curDay 当前时间
     * @return java.lang.String
     */
    @Override
    public String getDayHolidayType(String parkNo, LocalDateTime curDay) {
        return baseMapper.selectHolidayType(parkNo, curDay);
    }
}




