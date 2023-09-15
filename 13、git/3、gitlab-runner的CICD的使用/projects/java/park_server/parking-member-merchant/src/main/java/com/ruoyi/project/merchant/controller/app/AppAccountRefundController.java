package com.ruoyi.project.merchant.controller.app;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.merchant.domain.TAccountRefundAudit;
import com.ruoyi.project.merchant.service.ITAccountRefundAuditService;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商户账户余额退款审核Controller
 *
 * @author ruoyi
 * @date 2023-03-08
 */
@Api(tags = "H5平台商户账户余额提取功能")
@RestController
@RequestMapping("/merchant/app/audit")
public class AppAccountRefundController extends BaseController {
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
     * 获取商户账户余额退款审核详细信息
     */
    @ApiOperation(value = "获取商户账户余额退款审核详细信息", notes = "null")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tAccountRefundAuditService.selectTAccountRefundAuditById(id));
    }

    /**
     * 优惠券订单详情
     *
     * @param orderNo
     * @return
     */
    @ApiOperation(value = "根据订单号查寻提现订单信息", notes = "null")
    @GetMapping(value = "/getRefundOrderInfoByOrderNo")
    public AjaxResult getRefundOrderInfoByOrderNo(String orderNo) {
        TAccountRefundAudit accountRefundAudit = new TAccountRefundAudit();
        accountRefundAudit.setOrderNo(orderNo);
        List<TAccountRefundAudit> list = tAccountRefundAuditService.selectTAccountRefundAuditList(accountRefundAudit);
        if (Collections.isEmpty(list)) {
            return AjaxResult.warn("该订单号异常");
        }
        return success(list.get(0));
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
     * 删除商户账户余额退款审核
     */
    @ApiOperation(value = "删除商户账户余额退款审核", notes = "null")
    @Log(title = "商户账户余额退款审核", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tAccountRefundAuditService.deleteTAccountRefundAuditByIds(ids));
    }
}
