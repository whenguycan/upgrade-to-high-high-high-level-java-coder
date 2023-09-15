package com.ruoyi.project.merchant.service;

import com.ruoyi.project.merchant.domain.MonthlyCarRentalOrder;
import com.ruoyi.project.merchant.domain.bo.BSettingRegularCarCategoryBO;
import com.ruoyi.project.merchant.domain.bo.RegularCarBO;
import com.ruoyi.project.merchant.domain.vo.BSettingRegularCarCategoryVO;

import java.util.List;

/**
 * <p>
 * 月租车管理Service接口
 * </p>
 *
 * @author 琴声何来
 * @since 2023/3/3 15:57
 */
public interface IMonthlyCarRentalService {

    /**
     * @apiNote 通过用户信息获取月租车信息
     */
    List<RegularCarBO> selectRegularCarByUserId();

    /**
     * @apiNote 获取当前线上可续费的固定车类型
     */
    List<BSettingRegularCarCategoryBO> listOnlineCategory(BSettingRegularCarCategoryVO settingRegularCarCategoryVO);

}
