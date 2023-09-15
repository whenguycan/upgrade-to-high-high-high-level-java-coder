package com.ruoyi.project.merchant.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.project.merchant.domain.vo.BDurationAmountVo;
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
import com.ruoyi.project.merchant.domain.BDurationAmount;
import com.ruoyi.project.merchant.service.IBDurationAmountService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 优惠抵扣时长金额配置Controller
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@Api(tags = "优惠抵扣时长金额配置")
@RestController
@RequestMapping("/merchant/amount")
public class BDurationAmountController extends BaseController {
    @Autowired
    private IBDurationAmountService bDurationAmountService;

    /**
     * 查询优惠抵扣时长金额配置列表
     */
    @ApiOperation(value = "查询优惠抵扣时长金额配置列表", notes = "null")
    @GetMapping("/list")
    public BDurationAmountVo list(BDurationAmount bDurationAmount) {
        return bDurationAmountService.selectBDurationAmountList(bDurationAmount);
    }

//    /**
//     * 导出优惠抵扣时长金额配置列表
//     */
//    @PreAuthorize("@ss.hasPermi('merchant:amount:export')")
//    @Log(title = "优惠抵扣时长金额配置", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, BDurationAmount bDurationAmount)
//    {
//        List<BDurationAmount> list = bDurationAmountService.selectBDurationAmountList(bDurationAmount);
//        ExcelUtil<BDurationAmount> util = new ExcelUtil<BDurationAmount>(BDurationAmount.class);
//        util.exportExcel(response, list, "优惠抵扣时长金额配置数据");
//    }

    /**
     * 获取优惠抵扣时长金额配置详细信息
     */
    @ApiOperation(value = "获取优惠抵扣时长金额配置详细信息", notes = "null")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bDurationAmountService.selectBDurationAmountById(id));
    }

    /**
     * 新增优惠抵扣时长金额配置
     */
    @ApiOperation(value = "新增优惠抵扣时长金额配置", notes = "null")
    @Log(title = "优惠抵扣时长金额配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BDurationAmountVo bDurationAmountVo) {
        return toAjax(bDurationAmountService.insertBDurationAmount(bDurationAmountVo));
    }

    /**
     * 修改优惠抵扣时长金额配置
     */
    @ApiOperation(value = "修改优惠抵扣时长金额配置", notes = "null")
    @Log(title = "优惠抵扣时长金额配置", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody BDurationAmount bDurationAmount) {
        return toAjax(bDurationAmountService.updateBDurationAmount(bDurationAmount));
    }

    /**
     * 删除优惠抵扣时长金额配置
     */
    @ApiOperation(value = "删除优惠抵扣时长金额配置", notes = "null")
    @Log(title = "优惠抵扣时长金额配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bDurationAmountService.deleteBDurationAmountByIds(ids));
    }
}
