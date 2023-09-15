package com.ruoyi.project.parking.controller;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.BParkChargeScheme;
import com.ruoyi.project.parking.service.IBParkChargeSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 停车场收费方案Controller
 * 
 * @author fangch
 * @date 2023-02-21
 */
@RestController
@RequestMapping("/parking/BParkChargeScheme")
public class BParkChargeSchemeController extends BaseController
{
    @Autowired
    private IBParkChargeSchemeService bParkChargeSchemeService;

    /**
     * 查询停车场收费方案列表
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeScheme:list')")
//    @GetMapping("/list")
//    public TableDataInfo list(BParkChargeScheme bParkChargeScheme)
//    {
//        startPage();
//        List<BParkChargeScheme> list = bParkChargeSchemeService.selectBParkChargeSchemeList(bParkChargeScheme);
//        return getDataTable(list);
//    }

    /**
     * 导出停车场收费方案列表
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeScheme:export')")
//    @Log(title = "停车场收费方案", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, BParkChargeScheme bParkChargeScheme)
//    {
//        List<BParkChargeScheme> list = bParkChargeSchemeService.selectBParkChargeSchemeList(bParkChargeScheme);
//        ExcelUtil<BParkChargeScheme> util = new ExcelUtil<BParkChargeScheme>(BParkChargeScheme.class);
//        util.exportExcel(response, list, "停车场收费方案数据");
//    }

    /**
     * 根据车场编号获取停车场收费方案详细信息
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeScheme:query')")
    @GetMapping(value = "/getInfo")
    public AjaxResult getInfo()
    {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String parkNo = loginUser.getUser().getDept().getParkNo();
        return success(bParkChargeSchemeService.selectBParkChargeSchemeById(parkNo));
    }

    /**
     * 新增停车场收费方案
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeScheme:add')")
    @Log(title = "停车场收费方案", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BParkChargeScheme bParkChargeScheme)
    {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bParkChargeScheme.setCreateBy(String.valueOf(loginUser.getUserId()));
        bParkChargeScheme.setParkNo(loginUser.getUser().getDept().getParkNo());
        return toAjax(bParkChargeSchemeService.insertBParkChargeScheme(bParkChargeScheme));
    }

    /**
     * 修改停车场收费方案
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeScheme:edit')")
    @Log(title = "停车场收费方案", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody BParkChargeScheme bParkChargeScheme)
    {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bParkChargeScheme.setUpdateBy(String.valueOf(loginUser.getUserId()));
        bParkChargeScheme.setParkNo(loginUser.getUser().getDept().getParkNo());
        return toAjax(bParkChargeSchemeService.updateBParkChargeScheme(bParkChargeScheme));
    }

    /**
     * 删除停车场收费方案
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeScheme:remove')")
//    @Log(title = "停车场收费方案", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Integer[] ids)
//    {
//        return toAjax(bParkChargeSchemeService.deleteBParkChargeSchemeByIds(ids));
//    }
}
