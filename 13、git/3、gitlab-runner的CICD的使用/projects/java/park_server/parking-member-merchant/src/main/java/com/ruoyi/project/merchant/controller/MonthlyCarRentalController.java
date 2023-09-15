package com.ruoyi.project.merchant.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.merchant.domain.MonthlyCarRentalOrder;
import com.ruoyi.project.merchant.domain.bo.BSettingRegularCarCategoryBO;
import com.ruoyi.project.merchant.domain.bo.RegularCarBO;
import com.ruoyi.project.merchant.domain.vo.BSettingRegularCarCategoryVO;
import com.ruoyi.project.merchant.service.IMonthlyCarRentalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 月租车管理 前端控制器
 * </p>
 *
 * @author 琴声何来
 * @since 2023/3/3 15:27
 */
@Slf4j
@RestController
@RequestMapping("/merchant/monthlyCarRental")
public class MonthlyCarRentalController extends BaseController {

    @Autowired
    private IMonthlyCarRentalService monthlyCarRentalService;

    /**
     * @apiNote 通过用户信息获取月租车信息
     */
    @GetMapping("/list")
    public TableDataInfo list() {
        List<RegularCarBO> list = monthlyCarRentalService.selectRegularCarByUserId();
        return getDataTable(list);
    }


    /**
     * @apiNote 获取当前线上可续费的固定车类型
     */
    @GetMapping("/listOnlineCategory")
    public TableDataInfo listOnlineCategory(BSettingRegularCarCategoryVO settingRegularCarCategoryVO) {
        List<BSettingRegularCarCategoryBO> list = monthlyCarRentalService.listOnlineCategory(settingRegularCarCategoryVO);
        return getDataTable(list);
    }
}
