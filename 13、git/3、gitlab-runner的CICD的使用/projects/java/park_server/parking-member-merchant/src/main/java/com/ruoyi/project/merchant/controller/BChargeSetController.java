package com.ruoyi.project.merchant.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.project.merchant.domain.vo.BChargeSetVo;
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
import com.ruoyi.project.merchant.domain.BChargeSet;
import com.ruoyi.project.merchant.service.IBChargeSetService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 充值优惠配置Controller
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@Api(tags = "充值优惠配置")
@RestController
@RequestMapping("/merchant/set")
public class BChargeSetController extends BaseController {
    @Autowired
    private IBChargeSetService bChargeSetService;

    /**
     * 查询充值优惠配置列表
     */
    @ApiOperation(value = "查询充值优惠配置列表", notes = "null")
    @GetMapping("/list")
    public BChargeSetVo list(BChargeSet bChargeSet) {
        return bChargeSetService.selectBChargeSetList(bChargeSet);
    }

//    /**
//     * 导出充值优惠配置列表
//     */
//    @PreAuthorize("@ss.hasPermi('merchant:set:export')")
//    @Log(title = "充值优惠配置", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, BChargeSet bChargeSet)
//    {
//        List<BChargeSet> list = bChargeSetService.selectBChargeSetList(bChargeSet);
//        ExcelUtil<BChargeSet> util = new ExcelUtil<BChargeSet>(BChargeSet.class);
//        util.exportExcel(response, list, "充值优惠配置数据");
//    }

    /**
     * 获取充值优惠配置详细信息
     */
    @ApiOperation(value = "获取充值优惠配置详细信息", notes = "null")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bChargeSetService.selectBChargeSetById(id));
    }

    /**
     * 新增充值优惠配置
     */
    @ApiOperation(value = "新增充值优惠配置", notes = "null")
    @Log(title = "充值优惠配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BChargeSetVo bChargeSetVo) {
        return toAjax(bChargeSetService.insertBChargeSet(bChargeSetVo));
    }

    /**
     * 修改充值优惠配置
     */
    @ApiOperation(value = "修改充值优惠配置", notes = "null")
    @Log(title = "充值优惠配置", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody BChargeSet bChargeSet) {
        return toAjax(bChargeSetService.updateBChargeSet(bChargeSet));
    }

    /**
     * 删除充值优惠配置
     */
    @ApiOperation(value = "删除充值优惠配置", notes = "null")
    @Log(title = "充值优惠配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bChargeSetService.deleteBChargeSetByIds(ids));
    }
}
