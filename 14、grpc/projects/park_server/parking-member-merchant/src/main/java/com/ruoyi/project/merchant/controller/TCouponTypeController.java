package com.ruoyi.project.merchant.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.project.merchant.domain.vo.CouponTypeVo;
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
import com.ruoyi.project.merchant.domain.TCouponType;
import com.ruoyi.project.merchant.service.ITCouponTypeService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 优惠券种类Controller
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@Api(tags = "优惠券种类")
@RestController
@RequestMapping("/merchant/type")
public class TCouponTypeController extends BaseController {
    @Autowired
    private ITCouponTypeService tCouponTypeService;

    /**
     * 查询优惠券种类列表
     */
    @ApiOperation(value = "查询优惠券种类列表", notes = "null")
    @GetMapping("/list")
    public TableDataInfo list(TCouponType tCouponType) {
        startPage();
        List<TCouponType> list = tCouponTypeService.selectTCouponTypeList(tCouponType);
        return getDataTable(list);
    }

    /**
     * 导出优惠券种类列表
     */
    @PreAuthorize("@ss.hasPermi('merchant:type:export')")
    @Log(title = "优惠券种类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCouponType tCouponType) {
        List<TCouponType> list = tCouponTypeService.selectTCouponTypeList(tCouponType);
        ExcelUtil<TCouponType> util = new ExcelUtil<TCouponType>(TCouponType.class);
        util.exportExcel(response, list, "优惠券种类数据");
    }

    /**
     * 获取优惠券种类详细信息
     */
    @ApiOperation(value = "获取优惠券种类详细信息", notes = "null")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tCouponTypeService.selectTCouponTypeById(id));
    }

    /**
     * 新增优惠券种类
     */
    @ApiOperation(value = "新增优惠券种类", notes = "null")
    @Log(title = "优惠券种类", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody TCouponType tCouponType) {
        return toAjax(tCouponTypeService.insertTCouponType(tCouponType));
    }

    /**
     * 修改优惠券种类
     */
    @ApiOperation(value = "修改优惠券种类", notes = "null")
    @Log(title = "优惠券种类", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody TCouponType tCouponType) {
        int result = tCouponTypeService.updateTCouponType(tCouponType);
        if (result == -1) {
            new AjaxResult(500, "此类优惠券已被购买");
        }
        return toAjax(1);
    }

    /**
     * 删除优惠券种类
     */
    @ApiOperation(value = "删除优惠券种类", notes = "null")
    @Log(title = "优惠券种类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        int result = tCouponTypeService.deleteTCouponTypeByIds(ids);
        if (result == -1) {
            return new AjaxResult(500, "此类优惠券已被购买");
        }
        return toAjax(1);
    }


}
