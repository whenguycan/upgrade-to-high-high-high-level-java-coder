package com.czdx.parkingcharge.service;

import com.czdx.parkingcharge.domain.RegularCar;
import com.baomidou.mybatisplus.extension.service.IService;
import com.czdx.parkingcharge.domain.custom.RegularCarCustom;

import java.util.Optional;

/**
* @author mingchenxu
* @description 针对表【t_regular_car(固定车记录表)】的数据库操作Service
* @createDate 2023-03-30 14:24:54
*/
public interface RegularCarService extends IService<RegularCar> {

    /**
     *
     * description: 获取固定车信息
     * @author mingchenxu
     * @date 2023/3/30 14:53
     * @param parkNo 车场编号
     * @param carNumber 车牌
     * @return com.czdx.parkingcharge.domain.custom.RegularCarCustom
     */
    Optional<RegularCarCustom> getRegularCarInfo(String parkNo, String carNumber);

}
