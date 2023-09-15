package com.ruoyi.project.merchant.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.common.CouponEnums;
import com.ruoyi.project.merchant.domain.vo.TCouponDetailVo;
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
import com.ruoyi.project.merchant.domain.TCouponDetail;
import com.ruoyi.project.merchant.service.ITCouponDetailService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 优惠券明细Controller
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@Api(tags = "优惠券明细")
@RestController
@RequestMapping("/merchant/detail")
public class TCouponDetailController extends BaseController {
    @Autowired
    private ITCouponDetailService tCouponDetailService;

    /**
     * 查询优惠券明细列表
     */
    @ApiOperation(value = "查询优惠券明细列表", notes = "null")
    @GetMapping("/list")
    public TableDataInfo list(TCouponDetailVo tCouponDetail) {
        startPage();
        List<TCouponDetail> list = tCouponDetailService.selectTCouponDetailList(tCouponDetail);
        return getDataTable(list);
    }

    /**
     * H5平台查询优惠券明细列表
     */
    @ApiOperation(value = "H5平台查询优惠券明细列表", notes = "null")
    @GetMapping("/app/list")
    public TableDataInfo platformList(TCouponDetailVo tCouponDetail) {
        startPage();
        List<TCouponDetail> list = tCouponDetailService.selectAppPlatformCouponDetailList(tCouponDetail);
        return getDataTable(list);
    }

    /**
     * H5平台查询已创建的优惠券
     */
    @ApiOperation(value = "H5平台单个优惠券", notes = "null")
    @GetMapping("/app/singleCoupon")
    public AjaxResult SingleCoupon(TCouponDetailVo tCouponDetail) {
        TCouponDetailVo couponDetailVo = new TCouponDetailVo();
        couponDetailVo.setCouponStatus(CouponEnums.COUPON_STATUS.CREATED.getValue());
        couponDetailVo.setUserId(SecurityUtils.getUserId());
        List<TCouponDetail> list = tCouponDetailService.selectAppPlatformCouponDetailList(tCouponDetail);
        return AjaxResult.success(list.get(0));
    }


    /**
     * 导出优惠券明细列表
     */
    @PreAuthorize("@ss.hasPermi('merchant:detail:export')")
    @Log(title = "优惠券明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCouponDetailVo tCouponDetail) {
        List<TCouponDetail> list = tCouponDetailService.selectTCouponDetailList(tCouponDetail);
        ExcelUtil<TCouponDetail> util = new ExcelUtil<TCouponDetail>(TCouponDetail.class);
        util.exportExcel(response, list, "优惠券明细数据");
    }

    /**
     * 获取优惠券明细详细信息
     */
    @ApiOperation(value = "获取优惠券明细详细信息", notes = "null")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tCouponDetailService.selectTCouponDetailById(id));
    }

    /**
     * 新增优惠券明细
     */
    @ApiOperation(value = "新增优惠券明细", notes = "null")
    @Log(title = "优惠券明细", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody TCouponDetail tCouponDetail) {
        return toAjax(tCouponDetailService.insertTCouponDetail(tCouponDetail));
    }

    /**
     * 修改优惠券明细
     */
    @ApiOperation(value = "修改优惠券明细", notes = "null")
    @Log(title = "优惠券明细", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody TCouponDetail tCouponDetail) {
        return toAjax(tCouponDetailService.updateTCouponDetail(tCouponDetail));
    }

    /**
     * 删除优惠券明细
     */
    @ApiOperation(value = "删除优惠券明细", notes = "null")
    @Log(title = "优惠券明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tCouponDetailService.deleteTCouponDetailByIds(ids));
    }
}
