package com.ruoyi.project.parking.controller;


import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.param.SettingLiftGateReasonParam;
import com.ruoyi.project.parking.domain.vo.SettingLiftGateReasonVO;
import com.ruoyi.project.parking.entity.SettingLiftGateReason;
import com.ruoyi.project.parking.service.ISettingLiftGateReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 闸道抬杆原因表 前端控制器
 * </p>
 *
 * @author yinwen
 * @since 2023-02-21
 */
@RestController
@RequestMapping("/parking/settingliftgatereason")
public class SettingLiftGateReasonController  extends BaseController {

    @Autowired
    private ISettingLiftGateReasonService settingLiftGateReasonService;

    @GetMapping("/list")
    public TableDataInfo list(SettingLiftGateReason settingLiftGateReason) {
        settingLiftGateReason.setParkNo(getParkNO());
        startPage();
        List<SettingLiftGateReason> list = settingLiftGateReasonService.listCondition(settingLiftGateReason);
        return getDataTable(list);
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody SettingLiftGateReasonVO settingLiftGateReasonVO) {
        settingLiftGateReasonVO.setParkNo(getParkNO());
        return toAjax(settingLiftGateReasonService.add(settingLiftGateReasonVO));
    }

    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody SettingLiftGateReasonParam settingLiftGateReasonParam) {
        settingLiftGateReasonParam.setParkNo(getParkNO());
        return toAjax(settingLiftGateReasonService.editById(settingLiftGateReasonParam));
    }

    @GetMapping("/remove/{id}")
    public AjaxResult remove(@PathVariable Integer id) {
        return toAjax(settingLiftGateReasonService.removeById(id));
    }

}
