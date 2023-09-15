package com.ruoyi.project.merchant.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.project.merchant.domain.vo.TCouponCarnoRelationVo;
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
import com.ruoyi.project.merchant.domain.TCouponCarnoRelation;
import com.ruoyi.project.merchant.service.ITCouponCarnoRelationService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 优惠券车牌关联Controller
 *
 * @author ruoyi
 * @date 2023-03-02
 */
//@Api(tags = "优惠券车牌关联")
@RestController
@RequestMapping("/merchant/relation")
public class TCouponCarnoRelationController extends BaseController
{
    @Autowired
    private ITCouponCarnoRelationService tCouponCarnoRelationService;

    /**
     * 查询优惠券车牌关联列表
     */
    @ApiOperation(value = "查询优惠券车牌关联列表",notes = "null")
    @GetMapping("/list")
    public TableDataInfo list(TCouponCarnoRelation tCouponCarnoRelation)
    {
        startPage();
        List<TCouponCarnoRelation> list = tCouponCarnoRelationService.selectTCouponCarnoRelationList(tCouponCarnoRelation);
        return getDataTable(list);
    }

    /**
     * 导出优惠券车牌关联列表
     */
    @PreAuthorize("@ss.hasPermi('merchant:relation:export')")
    @Log(title = "优惠券车牌关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCouponCarnoRelation tCouponCarnoRelation)
    {
        List<TCouponCarnoRelation> list = tCouponCarnoRelationService.selectTCouponCarnoRelationList(tCouponCarnoRelation);
        ExcelUtil<TCouponCarnoRelation> util = new ExcelUtil<TCouponCarnoRelation>(TCouponCarnoRelation.class);
        util.exportExcel(response, list, "优惠券车牌关联数据");
    }

    /**
     * 获取优惠券车牌关联详细信息
     */
    @ApiOperation(value = "获取优惠券车牌关联详细信息",notes = "null")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tCouponCarnoRelationService.selectTCouponCarnoRelationById(id));
    }

    /**
     * 新增优惠券车牌关联
     */
    @ApiOperation(value = "新增优惠券车牌关联",notes = "null")
    @Log(title = "优惠券车牌关联", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody TCouponCarnoRelationVo tCouponCarnoRelation)
    {
        return toAjax(tCouponCarnoRelationService.insertTCouponCarnoRelation(tCouponCarnoRelation));
    }

    /**
     * 修改优惠券车牌关联
     */
    @ApiOperation(value = "修改优惠券车牌关联",notes = "null")
    @Log(title = "优惠券车牌关联", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody TCouponCarnoRelation tCouponCarnoRelation)
    {
        return toAjax(tCouponCarnoRelationService.updateTCouponCarnoRelation(tCouponCarnoRelation));
    }

    /**
     * 删除优惠券车牌关联
     */
    @ApiOperation(value = "删除优惠券车牌关联",notes = "null")
    @Log(title = "优惠券车牌关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tCouponCarnoRelationService.deleteTCouponCarnoRelationByIds(ids));
    }
}
