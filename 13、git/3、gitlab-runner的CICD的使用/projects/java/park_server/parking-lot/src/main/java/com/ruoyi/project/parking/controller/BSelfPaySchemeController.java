package com.ruoyi.project.parking.controller;


import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.BSelfPayScheme;
import com.ruoyi.project.parking.domain.param.BSelfPaySchemeParam;
import com.ruoyi.project.parking.service.IBSelfPaySchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 自主缴费 前端控制器
 * </p>
 *
 * @author 琴声何来
 * @since 2023/3/1 9:34
 */
@RestController
@RequestMapping("/parking/selfPayScheme")
public class BSelfPaySchemeController extends BaseController {


    @Autowired
    private IBSelfPaySchemeService selfPaySchemeService;

    /**
     * @apiNote 根据场库编号获取单个自主缴费方案
     */
    @GetMapping(value = "/detail")
    public AjaxResult getInfo() {
        return success(selfPaySchemeService.getBSelfPaySchemeByParkNo());
    }

    /**
     * @apiNote 编辑自主缴费方案
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody BSelfPaySchemeParam selfPaySchemeParam) {
        return toAjax(selfPaySchemeService.editById(selfPaySchemeParam));
    }
}
