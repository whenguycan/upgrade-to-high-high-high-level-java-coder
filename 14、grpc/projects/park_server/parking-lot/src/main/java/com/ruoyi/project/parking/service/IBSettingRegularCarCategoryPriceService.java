package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.BSettingRegularCarCategoryPrice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.parking.domain.param.BSettingRegularCarCategoryParam;
import com.ruoyi.project.parking.domain.param.BSettingRegularCarCategoryPriceParam;
import com.ruoyi.project.parking.domain.vo.BSettingRegularCarCategoryPriceVO;

/**
* @author 琴声何来
* @description 针对表【b_setting_regular_car_category_price(固定车收费标准表)】的数据库操作Service
* @since 2023-02-24 16:26:52
*/
public interface IBSettingRegularCarCategoryPriceService extends IService<BSettingRegularCarCategoryPrice> {
    boolean add(BSettingRegularCarCategoryPriceVO settingRegularCarCategoryPrice);

    boolean editById(BSettingRegularCarCategoryPriceParam settingRegularCarCategoryPriceParam);

}
