package com.ruoyi.project.parking.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.parking.domain.BParkChargeDurationPeriod;
import com.ruoyi.project.parking.service.IBParkChargeDurationPeriodService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 车场计费规则期间时段Controller
 * 
 * @author fangch
 * @date 2023-02-21
 */
//@RestController
//@RequestMapping("/parking/BParkChargeDurationPeriod")
public class BParkChargeDurationPeriodController extends BaseController
{
    @Autowired
    private IBParkChargeDurationPeriodService bParkChargeDurationPeriodService;

    /**
     * 查询车场计费规则期间时段列表
     */
    @PreAuthorize("@ss.hasPermi('parking:BParkChargeDurationPeriod:list')")
    @GetMapping("/list")
    public TableDataInfo list(BParkChargeDurationPeriod bParkChargeDurationPeriod)
    {
        startPage();
        List<BParkChargeDurationPeriod> list = bParkChargeDurationPeriodService.selectBParkChargeDurationPeriodList(bParkChargeDurationPeriod);
        return getDataTable(list);
    }

    /**
     * 导出车场计费规则期间时段列表
     */
    @PreAuthorize("@ss.hasPermi('parking:BParkChargeDurationPeriod:export')")
    @Log(title = "车场计费规则期间时段", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BParkChargeDurationPeriod bParkChargeDurationPeriod)
    {
        List<BParkChargeDurationPeriod> list = bParkChargeDurationPeriodService.selectBParkChargeDurationPeriodList(bParkChargeDurationPeriod);
        ExcelUtil<BParkChargeDurationPeriod> util = new ExcelUtil<BParkChargeDurationPeriod>(BParkChargeDurationPeriod.class);
        util.exportExcel(response, list, "车场计费规则期间时段数据");
    }

    /**
     * 获取车场计费规则期间时段详细信息
     */
    @PreAuthorize("@ss.hasPermi('parking:BParkChargeDurationPeriod:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(bParkChargeDurationPeriodService.selectBParkChargeDurationPeriodById(id));
    }

    /**
     * 新增车场计费规则期间时段
     */
    @PreAuthorize("@ss.hasPermi('parking:BParkChargeDurationPeriod:add')")
    @Log(title = "车场计费规则期间时段", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BParkChargeDurationPeriod bParkChargeDurationPeriod)
    {
        return toAjax(bParkChargeDurationPeriodService.insertBParkChargeDurationPeriod(bParkChargeDurationPeriod));
    }

    /**
     * 修改车场计费规则期间时段
     */
    @PreAuthorize("@ss.hasPermi('parking:BParkChargeDurationPeriod:edit')")
    @Log(title = "车场计费规则期间时段", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BParkChargeDurationPeriod bParkChargeDurationPeriod)
    {
        return toAjax(bParkChargeDurationPeriodService.updateBParkChargeDurationPeriod(bParkChargeDurationPeriod));
    }

    /**
     * 删除车场计费规则期间时段
     */
    @PreAuthorize("@ss.hasPermi('parking:BParkChargeDurationPeriod:remove')")
    @Log(title = "车场计费规则期间时段", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(bParkChargeDurationPeriodService.deleteBParkChargeDurationPeriodByIds(ids));
    }
}
