package com.ruoyi.project.parking.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.project.parking.domain.TParkSettingIssueRecords;
import com.ruoyi.project.parking.service.ITParkSettingIssueRecordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
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
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 车场配置下发记录Controller
 *
 * @author ruoyi
 * @date 2023-03-27
 */
@Api(tags = "车场配置下发记录")
@RestController
@RequestMapping("/parking/issueRecords")
public class TParkSettingIssueRecordsController extends BaseController {
    @Autowired
    private ITParkSettingIssueRecordsService tParkSettingIssueRecordsService;

    /**
     * 查询车场配置下发记录列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询车场配置下发记录列表", notes = "null")


    public TableDataInfo list(TParkSettingIssueRecords tParkSettingIssueRecords) {
        startPage();
        List<TParkSettingIssueRecords> list = tParkSettingIssueRecordsService.selectTParkSettingIssueRecordsList(tParkSettingIssueRecords);
        return getDataTable(list);
    }

    /**
     * 导出车场配置下发记录列表
     */
    @ApiOperation(value = "查询车场配置下发记录列表", notes = "null")
    @Log(title = "车场配置下发记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TParkSettingIssueRecords tParkSettingIssueRecords) {
        List<TParkSettingIssueRecords> list = tParkSettingIssueRecordsService.selectTParkSettingIssueRecordsList(tParkSettingIssueRecords);
        ExcelUtil<TParkSettingIssueRecords> util = new ExcelUtil<TParkSettingIssueRecords>(TParkSettingIssueRecords.class);
        util.exportExcel(response, list, "车场配置下发记录数据");
    }

    /**
     * @apiNote 车场配置下发导入
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<TParkSettingIssueRecords> util = new ExcelUtil<>(TParkSettingIssueRecords.class);
        List<TParkSettingIssueRecords> tParkSettingIssueRecordsList = util.importExcel(file.getInputStream());
        String message = tParkSettingIssueRecordsService.importTParkSettingIssueRecords(tParkSettingIssueRecordsList, updateSupport);
        return success(message);
    }

    /**
     * 获取车场配置下发记录详细信息
     */
    @ApiOperation(value = "获取车场配置下发记录详细信息", notes = "null")

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tParkSettingIssueRecordsService.selectTParkSettingIssueRecordsById(id));
    }

    /**
     * 新增车场配置下发记录
     */
    @ApiOperation(value = "新增车场配置下发记录", notes = "null")
    @Log(title = "车场配置下发记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TParkSettingIssueRecords tParkSettingIssueRecords) {
        tParkSettingIssueRecords.setCreateBy(getUsername());
        return toAjax(tParkSettingIssueRecordsService.insertTParkSettingIssueRecords(tParkSettingIssueRecords));
    }

    /**
     * 修改车场配置下发记录
     */
    @ApiOperation(value = "修改车场配置下发记录", notes = "null")
    @Log(title = "车场配置下发记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TParkSettingIssueRecords tParkSettingIssueRecords) {
        return toAjax(tParkSettingIssueRecordsService.updateTParkSettingIssueRecords(tParkSettingIssueRecords));
    }

    /**
     * 删除车场配置下发记录
     */
    @ApiModelProperty(value = "删除车场配置下发记录", notes = "null")
    @Log(title = "车场配置下发记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tParkSettingIssueRecordsService.deleteTParkSettingIssueRecordsByIds(ids));
    }
}
