package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.parking.domain.BSettingRegularCarCategoryPrice;
import com.ruoyi.project.parking.domain.param.BSettingRegularCarCategoryParam;
import com.ruoyi.project.parking.domain.param.BSettingRegularCarCategoryPriceParam;
import com.ruoyi.project.parking.domain.vo.BSettingRegularCarCategoryPriceVO;
import com.ruoyi.project.parking.service.IBSettingRegularCarCategoryPriceService;
import com.ruoyi.project.parking.mapper.BSettingRegularCarCategoryPriceMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author 琴声何来
 * @description 针对表【b_setting_regular_car_category_price(固定车收费标准表)】的数据库操作Service实现
 * @since 2023-02-24 16:26:52
 */
@Service
public class BSettingRegularCarCategoryPriceServiceImpl extends ServiceImpl<BSettingRegularCarCategoryPriceMapper, BSettingRegularCarCategoryPrice>
        implements IBSettingRegularCarCategoryPriceService {

    @Override
    public boolean add(BSettingRegularCarCategoryPriceVO settingRegularCarCategoryPriceVO) {
        BSettingRegularCarCategoryPrice settingRegularCarCategoryPrice = new BSettingRegularCarCategoryPrice();
        BeanUtils.copyBeanProp(settingRegularCarCategoryPrice, settingRegularCarCategoryPriceVO);
        settingRegularCarCategoryPrice.setParkNo(SecurityUtils.getParkNo());
        settingRegularCarCategoryPrice.setCreateBy(SecurityUtils.getUsername());
        settingRegularCarCategoryPrice.setCreateTime(LocalDateTime.now());
        return save(settingRegularCarCategoryPrice);
    }

    @Override
    public boolean editById(BSettingRegularCarCategoryPriceParam settingRegularCarCategoryPriceParam) {
        BSettingRegularCarCategoryPrice settingRegularCarCategoryPrice = new BSettingRegularCarCategoryPrice();
        BeanUtils.copyBeanProp(settingRegularCarCategoryPrice, settingRegularCarCategoryPriceParam);
        settingRegularCarCategoryPrice.setUpdateBy(SecurityUtils.getUsername());
        settingRegularCarCategoryPrice.setUpdateTime(LocalDateTime.now());
        return updateById(settingRegularCarCategoryPrice);
    }
}




