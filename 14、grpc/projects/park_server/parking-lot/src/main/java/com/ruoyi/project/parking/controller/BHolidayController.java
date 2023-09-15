package com.ruoyi.project.parking.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.security.LoginUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.parking.domain.BHoliday;
import com.ruoyi.project.parking.service.IBHolidayService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 节假日设置Controller
 * 
 * @author fangch
 * @date 2023-02-25
 */
@RestController
@RequestMapping("/parking/BHoliday")
public class BHolidayController extends BaseController
{
    @Autowired
    private IBHolidayService bHolidayService;

    /**
     * 查询节假日设置列表
     */
//    @PreAuthorize("@ss.hasPermi('parking:BHoliday:list')")
    @GetMapping("/list")
    public TableDataInfo list(BHoliday bHoliday)
    {
        startPage();
        List<BHoliday> list = bHolidayService.selectBHolidayList(bHoliday);
        return getDataTable(list);
    }

    /**
     * 导出节假日设置列表
     */
//    @PreAuthorize("@ss.hasPermi('parking:BHoliday:export')")
//    @Log(title = "节假日设置", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, BHoliday bHoliday)
//    {
//        List<BHoliday> list = bHolidayService.selectBHolidayList(bHoliday);
//        ExcelUtil<BHoliday> util = new ExcelUtil<BHoliday>(BHoliday.class);
//        util.exportExcel(response, list, "节假日设置数据");
//    }

    /**
     * 获取节假日设置详细信息
     */
//    @PreAuthorize("@ss.hasPermi('parking:BHoliday:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(bHolidayService.selectBHolidayById(id));
    }

    /**
     * 新增节假日设置
     */
//    @PreAuthorize("@ss.hasPermi('parking:BHoliday:add')")
    @Log(title = "节假日设置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BHoliday bHoliday)
    {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bHoliday.setCreateBy(String.valueOf(loginUser.getUserId()));
        return toAjax(bHolidayService.insertBHoliday(bHoliday));
    }

    /**
     * 修改节假日设置
     */
//    @PreAuthorize("@ss.hasPermi('parking:BHoliday:edit')")
    @Log(title = "节假日设置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody BHoliday bHoliday)
    {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bHoliday.setUpdateBy(String.valueOf(loginUser.getUserId()));
        return toAjax(bHolidayService.updateBHoliday(bHoliday));
    }

    /**
     * 删除节假日设置
     */
//    @PreAuthorize("@ss.hasPermi('parking:BHoliday:remove')")
    @Log(title = "节假日设置", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult remove(@RequestParam Integer[] ids)
    {
        return toAjax(bHolidayService.deleteBHolidayByIds(ids));
    }

    /**
     * 获取国家法定节假日
     */
//    @PreAuthorize("@ss.hasPermi('parking:BHoliday:query')")
    @GetMapping(value = "/getThisYearJjr")
    public AjaxResult getThisYearJjr() {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        return success(bHolidayService.getThisYearJjr(String.valueOf(loginUser.getUserId())));
    }
}
