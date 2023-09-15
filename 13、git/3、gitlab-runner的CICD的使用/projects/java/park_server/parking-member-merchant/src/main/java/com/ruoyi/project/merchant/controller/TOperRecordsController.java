package com.ruoyi.project.merchant.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.common.CommonConstants;
import com.ruoyi.project.merchant.domain.vo.TOperRecordsVo;
import io.swagger.annotations.Api;
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
import com.ruoyi.project.merchant.domain.TOperRecords;
import com.ruoyi.project.merchant.service.ITOperRecordsService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 流水信息Controller
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@Api(tags = "流水信息")
@RestController
@RequestMapping("/merchant/records")
public class TOperRecordsController extends BaseController {
    @Autowired
    private ITOperRecordsService tOperRecordsService;

    /**
     * 查询流水信息列表
     */
    @ApiOperation(value = "查询流水信息列表", notes = "null")
    @GetMapping("/list")
    public TableDataInfo list(TOperRecordsVo tOperRecordsVo) {
        startPage();
        tOperRecordsVo.setStatus(CommonConstants.SUCCESS_STATUS);
        List<TOperRecords> list = tOperRecordsService.selectTOperRecordsList(tOperRecordsVo);
        return getDataTable(list);
    }


    /**
     * 查询流水信息列表
     */
    @ApiOperation(value = "H5平台查询流水信息列表", notes = "null")
    @GetMapping("/app/list")
    public TableDataInfo appList(TOperRecordsVo tOperRecordsVo) {
        startPage();
        tOperRecordsVo.setOperId(SecurityUtils.getUserId());
        List<TOperRecords> list = tOperRecordsService.selectTOperRecordsList(tOperRecordsVo);
        return getDataTable(list);
    }

    /**
     * 获取流水信息详细信息
     */
    @ApiOperation(value = "获取流水信息详细信息", notes = "null")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tOperRecordsService.selectTOperRecordsById(id));
    }

    /**
     * 新增流水信息
     */
    @ApiOperation(value = "新增流水信息", notes = "null")
    @Log(title = "流水信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody TOperRecords tOperRecords) {
        return toAjax(tOperRecordsService.insertTOperRecords(tOperRecords));
    }

    /**
     * 修改流水信息
     */
    @ApiOperation(value = "修改流水信息", notes = "null")
    @Log(title = "流水信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updateRecordSuccess")
    public AjaxResult updateRecordSuccess(@RequestBody TOperRecords tOperRecords) {
        tOperRecords.setStatus(1);
        return toAjax(tOperRecordsService.updateTOperRecords(tOperRecords));
    }

    /**
     * 删除流水信息
     */
    @ApiOperation(value = "删除流水信息", notes = "null")
    @Log(title = "流水信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tOperRecordsService.deleteTOperRecordsByIds(ids));
    }
}
