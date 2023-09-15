package com.czdx.parkingcharge.service;

import com.czdx.parkingcharge.domain.ParkChargeScheme;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Optional;

/**
* @author mingchenxu
* @description 针对表【b_park_charge_scheme(停车场收费方案表)】的数据库操作Service
* @createDate 2023-03-14 09:42:04
*/
public interface ParkChargeSchemeService extends IService<ParkChargeScheme> {

    /**
     *
     * description: 缓存车场计费约束
     * @author mingchenxu
     * @date 2023/3/21 10:45
     */
    void cacheParkLotChargeScheme(String parkNo);

    /**
     *
     * description: 刷新车场计费约束
     * @author mingchenxu
     * @date 2023/3/21 11:06
     * @param parkNo 车场编号
     * @return int
     */
    int refreshParkLotChargeScheme(String parkNo);

    /**
     *
     * description: 获取计费约束
     * @author mingchenxu
     * @date 2023/3/21 10:51
     * @param parkNo 车场编号
     * @return com.czdx.parkingcharge.domain.ParkChargeScheme
     */
    Optional<ParkChargeScheme> getChargeScheme(String parkNo);
}
