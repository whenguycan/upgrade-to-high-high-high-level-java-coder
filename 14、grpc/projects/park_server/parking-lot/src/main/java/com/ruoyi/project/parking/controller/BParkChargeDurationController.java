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
import com.ruoyi.project.parking.domain.BParkChargeDuration;
import com.ruoyi.project.parking.service.IBParkChargeDurationService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 车场计费规则期间Controller
 * 
 * @author fangch
 * @date 2023-02-21
 */
//@RestController
//@RequestMapping("/parking/BParkChargeDuration")
public class BParkChargeDurationController extends BaseController
{
    @Autowired
    private IBParkChargeDurationService bParkChargeDurationService;

    /**
     * 查询车场计费规则期间列表
     */
    @PreAuthorize("@ss.hasPermi('parking:BParkChargeDuration:list')")
    @GetMapping("/list")
    public TableDataInfo list(BParkChargeDuration bParkChargeDuration)
    {
        startPage();
        List<BParkChargeDuration> list = bParkChargeDurationService.selectBParkChargeDurationList(bParkChargeDuration);
        return getDataTable(list);
    }

    /**
     * 导出车场计费规则期间列表
     */
    @PreAuthorize("@ss.hasPermi('parking:BParkChargeDuration:export')")
    @Log(title = "车场计费规则期间", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BParkChargeDuration bParkChargeDuration)
    {
        List<BParkChargeDuration> list = bParkChargeDurationService.selectBParkChargeDurationList(bParkChargeDuration);
        ExcelUtil<BParkChargeDuration> util = new ExcelUtil<BParkChargeDuration>(BParkChargeDuration.class);
        util.exportExcel(response, list, "车场计费规则期间数据");
    }

    /**
     * 获取车场计费规则期间详细信息
     */
    @PreAuthorize("@ss.hasPermi('parking:BParkChargeDuration:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(bParkChargeDurationService.selectBParkChargeDurationById(id));
    }

    /**
     * 新增车场计费规则期间
     */
    @PreAuthorize("@ss.hasPermi('parking:BParkChargeDuration:add')")
    @Log(title = "车场计费规则期间", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BParkChargeDuration bParkChargeDuration)
    {
        return toAjax(bParkChargeDurationService.insertBParkChargeDuration(bParkChargeDuration));
    }

    /**
     * 修改车场计费规则期间
     */
    @PreAuthorize("@ss.hasPermi('parking:BParkChargeDuration:edit')")
    @Log(title = "车场计费规则期间", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BParkChargeDuration bParkChargeDuration)
    {
        return toAjax(bParkChargeDurationService.updateBParkChargeDuration(bParkChargeDuration));
    }

    /**
     * 删除车场计费规则期间
     */
    @PreAuthorize("@ss.hasPermi('parking:BParkChargeDuration:remove')")
    @Log(title = "车场计费规则期间", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(bParkChargeDurationService.deleteBParkChargeDurationByIds(ids));
    }
}
