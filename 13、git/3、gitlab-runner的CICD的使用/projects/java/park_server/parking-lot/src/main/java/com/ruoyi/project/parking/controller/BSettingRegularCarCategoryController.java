package com.ruoyi.project.parking.controller;


import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.BSettingRegularCarCategory;
import com.ruoyi.project.parking.domain.bo.BSettingRegularCarCategoryBO;
import com.ruoyi.project.parking.domain.param.BSettingRegularCarCategoryParam;
import com.ruoyi.project.parking.domain.vo.BSettingRegularCarCategoryVO;
import com.ruoyi.project.parking.service.IBSettingRegularCarCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 固定车类型表 前端控制器
 * </p>
 *
 * @author 琴声何来
 * @since 2023-02-21
 */
@RestController
@RequestMapping("/parking/settingRegularCarCategory")
public class BSettingRegularCarCategoryController extends BaseController {


    @Autowired
    private IBSettingRegularCarCategoryService settingRegularCarCategoryService;

    /**
     * @apiNote 获取固定车类型详细信息
     */
    @GetMapping("/list")
    public TableDataInfo list(BSettingRegularCarCategory settingRegularCarCategory) {
        startPage();
        List<BSettingRegularCarCategoryBO> list = settingRegularCarCategoryService.listByParkNoAndCodeAndGroupIdAndOnlineFlag(settingRegularCarCategory);
        return getDataTable(list);
    }

    /**
     * @apiNote 获取单个固定车类型
     */
    @GetMapping(value = "/detail/{id}")
    public AjaxResult getInfo(@PathVariable("id") @NotNull Integer id) {
        return success(settingRegularCarCategoryService.getBSettingRegularCarCategoryById(id));
    }

    /**
     * @apiNote 新增固定车类型
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BSettingRegularCarCategoryVO settingRegularCarCategoryVO) {
        return toAjax(settingRegularCarCategoryService.add(settingRegularCarCategoryVO));
    }

    /**
     * @apiNote 编辑固定车类型
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody BSettingRegularCarCategoryParam settingRegularCarCategoryParam) {
        return toAjax(settingRegularCarCategoryService.editById(settingRegularCarCategoryParam));
    }

    /**
     * @apiNote 逻辑删除固定车类型
     */
    @GetMapping("/remove/{id}")
    public AjaxResult remove(@PathVariable @NotNull Integer id) {
        return toAjax(settingRegularCarCategoryService.removeById(id));
    }

    /**
     * @apiNote 启用固定车类型
     */
    @GetMapping("/enable/{id}")
    public AjaxResult enable(@PathVariable @NotNull Integer id) {
        return toAjax(settingRegularCarCategoryService.switchStatusById(id, "1"));
    }

    /**
     * @apiNote 停用固定车类型
     */
    @GetMapping("/disable/{id}")
    public AjaxResult disable(@PathVariable @NotNull Integer id) {
        return toAjax(settingRegularCarCategoryService.switchStatusById(id, "0"));
    }
}
