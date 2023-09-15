package com.ruoyi.project.parking.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.project.parking.domain.vo.TPassageLeverRecordsVo;
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
import com.ruoyi.project.parking.domain.TPassageLeverRecords;
import com.ruoyi.project.parking.service.ITPassageLeverRecordsService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 起落杆记录Controller
 *
 * @author mzl
 * @date 2023-02-23
 */
@Api(tags = "起落杆记录")
@RestController
@RequestMapping("/parking/records")
public class TPassageLeverRecordsController extends BaseController
{
    @Autowired
    private ITPassageLeverRecordsService tPassageLeverRecordsService;

    /**
     * 查询起落杆记录列表
     */
        @ApiOperation("查询起落杆记录列表")
    @GetMapping("/list")
    public TableDataInfo list(TPassageLeverRecords tPassageLeverRecords)
    {
        startPage();
        List<TPassageLeverRecordsVo> list = tPassageLeverRecordsService.selectTPassageLeverRecordsList(tPassageLeverRecords);
        return getDataTable(list);
    }

    /**
     * 导出起落杆记录列表
     */

    /**
     * 获取起落杆记录详细信息
     */
       @GetMapping(value = "/{id}")
    @ApiOperation("获取起落杆记录详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tPassageLeverRecordsService.selectTPassageLeverRecordsById(id));
    }

    /**
     * 新增起落杆记录
     */
        @Log(title = "起落杆记录", businessType = BusinessType.INSERT)
    @PostMapping("/addLever")
    @ApiOperation("新增起落杆记录")
    public AjaxResult add(@RequestBody TPassageLeverRecords tPassageLeverRecords)
    {
        return toAjax(tPassageLeverRecordsService.insertTPassageLeverRecords(tPassageLeverRecords));
    }

    /**
     * 修改起落杆记录
     */
        @Log(title = "起落杆记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改起落杆记录")
    public AjaxResult edit(@RequestBody TPassageLeverRecords tPassageLeverRecords)
    {
        return toAjax(tPassageLeverRecordsService.updateTPassageLeverRecords(tPassageLeverRecords));
    }

    /**
     * 删除起落杆记录
     */
        @Log(title = "起落杆记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除起落杆记录")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tPassageLeverRecordsService.deleteTPassageLeverRecordsByIds(ids));
    }
}
