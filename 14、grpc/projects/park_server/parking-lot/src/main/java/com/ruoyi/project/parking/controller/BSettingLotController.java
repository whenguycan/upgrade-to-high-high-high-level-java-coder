package com.ruoyi.project.parking.controller;

import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.BSettingLot;
import com.ruoyi.project.parking.service.IBSettingLotService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 场库基础配置表;(b_setting_lot)表控制层
 * @author : http://www.chiner.pro
 * @date : 2023-3-14
 */
@Api(tags = "场库基础配置表对象功能接口")
@RestController
@RequestMapping("/parking/bSettingLot")
public class BSettingLotController extends BaseController {
    @Autowired
    private IBSettingLotService bSettingLotService;

    /**
     * 获取场库基础配置详细信息
     */
    @PostMapping("/getInfo")
    public AjaxResult getInfo(BSettingLot bSettingLot) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bSettingLot.setParkNo(loginUser.getUser().getDept().getParkNo());
        return success(bSettingLotService.getBSettingLotByParkNo(bSettingLot));
    }

    /**
     * 新增场库基础配置
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BSettingLot bSettingLot) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bSettingLot.setParkNo(loginUser.getUser().getDept().getParkNo());
        return toAjax(bSettingLotService.insertBSettingLot(bSettingLot));
    }

    /**
     * 修改场库基础配置
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody BSettingLot bSettingLot) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bSettingLot.setParkNo(loginUser.getUser().getDept().getParkNo());
        return toAjax(bSettingLotService.updateBSettingLot(bSettingLot));
    }
}
