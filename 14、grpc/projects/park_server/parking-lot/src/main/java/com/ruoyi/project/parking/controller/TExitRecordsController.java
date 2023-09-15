package com.ruoyi.project.parking.controller;

import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.TExitRecords;
import com.ruoyi.project.parking.entity.ParkLiveRecords;
import com.ruoyi.project.parking.service.IParkLiveRecordsService;
import com.ruoyi.project.parking.service.ITExitRecordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车辆离场记录Controller
 *
 * @author mzl
 * @date 2023-02-22
 */
@Api(tags = "车辆离场记录")
@RestController
@RequestMapping("/exitrecords")
public class TExitRecordsController extends BaseController
{
    @Autowired
    private ITExitRecordsService tExitRecordsService;

    /**
     * 查询车辆离场记录列表
     */
        @ApiOperation("查询车辆离场记录列表")
    @GetMapping("/list")
    public TableDataInfo list(TExitRecords tExitRecords)
    {
        startPage();
        List<TExitRecords> list = tExitRecordsService.selectTExitRecordsList(tExitRecords);
        return getDataTable(list);
    }

    /**
     * 导出车辆离场记录列表
     */

    /**
     * 获取车辆离场记录详细信息
     */
       @GetMapping(value = "/{id}")
    @ApiOperation("获取车辆离场记录详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tExitRecordsService.selectTExitRecordsById(id));
    }

    /**
     * 新增车辆离场记录
     */
        @Log(title = "车辆离场记录", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增车辆离场记录")
    public AjaxResult add(@RequestBody TExitRecords tExitRecords)
    {
        return toAjax(tExitRecordsService.insertTExitRecords(tExitRecords));
    }

    /**
     * 修改车辆离场记录
     */
        @Log(title = "车辆离场记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改车辆离场记录")
    public AjaxResult edit(@RequestBody TExitRecords tExitRecords)
    {
        return toAjax(tExitRecordsService.updateTExitRecords(tExitRecords));
    }

    /**
     * 删除车辆离场记录
     */
        @Log(title = "车辆离场记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除车辆离场记录")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tExitRecordsService.deleteTExitRecordsByIds(ids));
    }
}
