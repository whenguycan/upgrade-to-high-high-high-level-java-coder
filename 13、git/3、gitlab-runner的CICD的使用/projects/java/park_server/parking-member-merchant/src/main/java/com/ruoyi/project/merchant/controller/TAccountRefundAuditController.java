package com.ruoyi.project.merchant.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.merchant.domain.TCouponOrder;
import com.ruoyi.project.merchant.enums.RefundAuditing;
import io.jsonwebtoken.lang.Collections;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
import com.ruoyi.project.merchant.domain.TAccountRefundAudit;
import com.ruoyi.project.merchant.service.ITAccountRefundAuditService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 商户账户余额退款审核Controller
 *
 * @author ruoyi
 * @date 2023-03-08
 */
@Api(tags = "后端商户账户提现审核功能")
@RestController
@RequestMapping("/merchant/audit")
public class TAccountRefundAuditController extends BaseController {
    @Autowired
    private ITAccountRefundAuditService tAccountRefundAuditService;

    /**
     * 查询商户账户余额退款审核列表
     */
    @ApiOperation(value = "查询商户账户余额退款审核列表", notes = "null")
    @GetMapping("/list")
    public TableDataInfo list(TAccountRefundAudit tAccountRefundAudit) {
        startPage();
        List<TAccountRefundAudit> list = tAccountRefundAuditService.selectTAccountRefundAuditList(tAccountRefundAudit);
        return getDataTable(list);
    }

    /**
     * 导出商户账户余额退款审核列表
     */
    @Log(title = "商户账户余额退款审核", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TAccountRefundAudit tAccountRefundAudit) {
        List<TAccountRefundAudit> list = tAccountRefundAuditService.selectTAccountRefundAuditList(tAccountRefundAudit);
        ExcelUtil<TAccountRefundAudit> util = new ExcelUtil<TAccountRefundAudit>(TAccountRefundAudit.class);
        util.exportExcel(response, list, "商户账户余额退款审核数据");
    }

    /**
     * 获取商户账户余额退款审核详细信息
     */
    @ApiOperation(value = "获取商户账户余额退款审核详细信息", notes = "null")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tAccountRefundAuditService.selectTAccountRefundAuditById(id));
    }

    /**
     * 新增商户账户余额退款审核
     */
    @ApiOperation(value = "新增商户账户余额退款审核", notes = "null")
    @Log(title = "商户账户余额退款审核", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody TAccountRefundAudit tAccountRefundAudit) {

        Long result = tAccountRefundAuditService.insertTAccountRefundAudit(tAccountRefundAudit);
        if (result.equals(-1L)) {
            return AjaxResult.warn("余额提现出错");
        }
        Map resultMap = new HashMap();
        resultMap.put("id", result);
        return AjaxResult.success(resultMap);
    }

    /**
     * 修改商户账户余额退款审核
     */
    @ApiOperation(value = "修改商户账户余额退款审核", notes = "null")
    @Log(title = "商户账户余额退款审核", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody TAccountRefundAudit tAccountRefundAudit) {
        return toAjax(tAccountRefundAuditService.updateTAccountRefundAudit(tAccountRefundAudit));
    }

    /**
     * 删除商户账户余额退款审核
     */
    @ApiOperation(value = "删除商户账户余额退款审核", notes = "null")
    @Log(title = "商户账户余额退款审核", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tAccountRefundAuditService.deleteTAccountRefundAuditByIds(ids));
    }


    /**
     * 商户账户余额退款审核
     */
    @ApiOperation(value = "商户账户余额退款审核", notes = "")
    @Log(title = "商户账户余额退款审核", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody @Validated  TAccountRefundAudit tAccountRefundAudit) {
        tAccountRefundAuditService.auditRefund(tAccountRefundAudit);
        return AjaxResult.success("审核成功！");
    }

}
