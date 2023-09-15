package com.czdx.parkingcharge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingcharge.common.enums.ParkEnums;
import com.czdx.parkingcharge.domain.RegularCar;
import com.czdx.parkingcharge.domain.custom.RegularCarCustom;
import com.czdx.parkingcharge.service.RegularCarService;
import com.czdx.parkingcharge.mapper.RegularCarMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
* @author mingchenxu
* @description 针对表【t_regular_car(固定车记录表)】的数据库操作Service实现
* @createDate 2023-03-30 14:24:54
*/
@Service
public class RegularCarServiceImpl extends ServiceImpl<RegularCarMapper, RegularCar>
    implements RegularCarService{

    /**
     *
     * description: 获取固定车信息
     * @author mingchenxu
     * @date 2023/3/30 14:53
     * @param parkNo 车场编号
     * @param carNumber 车牌
     * @return com.czdx.parkingcharge.domain.custom.RegularCarCustom
     */
    @Override
    public Optional<RegularCarCustom> getRegularCarInfo(String parkNo, String carNumber) {
        List<RegularCarCustom> regularCarInfos = baseMapper.getRegularCarInfo(Arrays.asList(parkNo, ParkEnums.ParkNo.WEST_TAI_LAKE_GROUP.getValue()), carNumber);
        RegularCarCustom rcc = null;
        if (CollectionUtils.isNotEmpty(regularCarInfos)) {
            rcc = regularCarInfos.get(0);
        }
        return Optional.ofNullable(rcc);
    }
}




