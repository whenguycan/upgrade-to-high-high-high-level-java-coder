package com.ruoyi.project.merchant.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.project.merchant.domain.vo.TCouponOperLogsVo;
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
import com.ruoyi.project.merchant.domain.TCouponOperLogs;
import com.ruoyi.project.merchant.service.ITCouponOperLogsService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 优惠券日志Controller
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@Api(tags = "优惠券日志")
@RestController
@RequestMapping("/merchant/logs")
public class TCouponOperLogsController extends BaseController
{
    @Autowired
    private ITCouponOperLogsService tCouponOperLogsService;

    /**
     * 查询优惠券日志列表
     */
    @ApiOperation(value = "查询优惠券日志列表",notes = "null")
    @GetMapping("/list")
    public TableDataInfo list(TCouponOperLogs tCouponOperLogs)
    {
        startPage();
        List<TCouponOperLogsVo> list = tCouponOperLogsService.selectTCouponOperLogsList(tCouponOperLogs);
        return getDataTable(list);
    }

    /**
     * 导出优惠券日志列表
     */
    @PreAuthorize("@ss.hasPermi('merchant:logs:export')")
    @Log(title = "优惠券日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCouponOperLogs tCouponOperLogs)
    {
        List<TCouponOperLogsVo> list = tCouponOperLogsService.selectTCouponOperLogsList(tCouponOperLogs);
        ExcelUtil<TCouponOperLogsVo> util = new ExcelUtil<TCouponOperLogsVo>(TCouponOperLogsVo.class);
        util.exportExcel(response, list, "优惠券日志数据");
    }

    /**
     * 获取优惠券日志详细信息
     */
    @ApiOperation(value = "获取优惠券日志详细信息",notes = "null")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tCouponOperLogsService.selectTCouponOperLogsById(id));
    }

    /**
     * 新增优惠券日志
     */
    @ApiOperation(value = "新增优惠券日志",notes = "null")
    @Log(title = "优惠券日志", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody TCouponOperLogs tCouponOperLogs)
    {
        return toAjax(tCouponOperLogsService.insertTCouponOperLogs(tCouponOperLogs));
    }

    /**
     * 修改优惠券日志
     */
    @ApiOperation(value = "修改优惠券日志",notes = "null")
    @Log(title = "优惠券日志", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody TCouponOperLogs tCouponOperLogs)
    {
        return toAjax(tCouponOperLogsService.updateTCouponOperLogs(tCouponOperLogs));
    }

    /**
     * 删除优惠券日志
     */
    @ApiOperation(value = "删除优惠券日志",notes = "null")
    @Log(title = "优惠券日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tCouponOperLogsService.deleteTCouponOperLogsByIds(ids));
    }
}
