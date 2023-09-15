package com.ruoyi.project.parking.controller;


import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.param.SettingCarTypeParam;
import com.ruoyi.project.parking.domain.vo.SettingCarTypeVO;
import com.ruoyi.project.parking.entity.SettingCarType;
import com.ruoyi.project.parking.service.ISettingCarTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 车辆类型表 前端控制器
 * </p>
 *
 * @author yinwen
 * @since 2023-02-21
 */
@RestController
@RequestMapping("/parking/settingcartype")
public class SettingCarTypeController extends BaseController {


    @Autowired
    private ISettingCarTypeService settingCarTypeService;

    @GetMapping("/list")
    public TableDataInfo list(SettingCarType settingCarType) {
        settingCarType.setParkNo(getParkNO());
        startPage();
        List<SettingCarType> list = settingCarTypeService.listCondition(settingCarType);
        return getDataTable(list);
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody SettingCarTypeVO settingCarTypeVO) {
        settingCarTypeVO.setParkNo(getParkNO());
        return toAjax(settingCarTypeService.add(settingCarTypeVO));
    }

    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody SettingCarTypeParam settingCarTypeParam) {
        settingCarTypeParam.setParkNo(getParkNO());
        return toAjax(settingCarTypeService.editById(settingCarTypeParam));
    }

    @GetMapping("/remove/{id}")
    public AjaxResult remove(@PathVariable @NotNull Integer id) {
        return toAjax(settingCarTypeService.removeById(id));
    }

    @GetMapping("/enable/{id}")
    public AjaxResult enable(@PathVariable @NotNull Integer id) {
        return toAjax(settingCarTypeService.switchStatusById(id, "1"));
    }

    @GetMapping("/disable/{id}")
    public AjaxResult disable(@PathVariable @NotNull Integer id) {
        return toAjax(settingCarTypeService.switchStatusById(id, "0"));
    }
}
