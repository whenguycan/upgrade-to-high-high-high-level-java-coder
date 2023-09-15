package com.ruoyi.project.parking.controller;


import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.BSettingRegularCarCategoryPrice;
import com.ruoyi.project.parking.domain.param.BSettingRegularCarCategoryParam;
import com.ruoyi.project.parking.domain.param.BSettingRegularCarCategoryPriceParam;
import com.ruoyi.project.parking.domain.vo.BSettingRegularCarCategoryPriceVO;
import com.ruoyi.project.parking.service.IBSettingRegularCarCategoryPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 固定车收费标准表 前端控制器
 * </p>
 *
 * @author 琴声何来
 * @since 2023/2/24 15:53
 */
@RestController
@RequestMapping("/parking/settingRegularCarCategoryPrice")
public class BSettingRegularCarCategoryPriceController extends BaseController {


    @Autowired
    private IBSettingRegularCarCategoryPriceService settingRegularCarCategoryPriceService;

    /**
     * @apiNote 新增固定车收费标准
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BSettingRegularCarCategoryPriceVO settingRegularCarCategoryPriceVO) {
        return toAjax(settingRegularCarCategoryPriceService.add(settingRegularCarCategoryPriceVO));
    }

    /**
     * @apiNote 编辑固定车收费标准
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody BSettingRegularCarCategoryPriceParam settingRegularCarCategoryPriceParam) {
        return toAjax(settingRegularCarCategoryPriceService.editById(settingRegularCarCategoryPriceParam));
    }

    /**
     * @apiNote 物理删除固定车收费标准
     */
    @GetMapping("/remove/{id}")
    public AjaxResult remove(@PathVariable @NotNull Integer id) {
        return toAjax(settingRegularCarCategoryPriceService.removeById(id));
    }

}
