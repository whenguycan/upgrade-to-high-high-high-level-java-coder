package com.ruoyi.project.merchant.controller;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.merchant.domain.TCouponType;
import com.ruoyi.project.merchant.domain.param.CouponReportParam;
import com.ruoyi.project.merchant.service.ITCouponDetailService;
import com.ruoyi.project.merchant.service.ITOperRecordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商户报表Controller
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@Api(tags = "商户报表")
@RestController
@RequestMapping("/merchant/report")
public class ReportController extends BaseController {

    @Autowired
    private ITCouponDetailService tCouponDetailService;

    @Autowired
    private ITOperRecordsService tOperRecordsService;

    /**
     * 优惠券报表接口
     */
    @ApiOperation(value = "商户优惠券报表接口", notes = "null")
    @PostMapping("/couponReportList")
    public TableDataInfo couponReportList(@RequestBody CouponReportParam param) {
        startPage();
        return getDataTable(tCouponDetailService.selectCouponReportList(param));
    }

    /**
     * 商户流水报表接口
     */
    @ApiOperation(value = "商户流水报表接口", notes = "null")
    @PostMapping("/recordReportList")
    public TableDataInfo recordReportList(@RequestBody CouponReportParam param) {
        startPage();
        return getDataTable(tOperRecordsService.selectRecordReportList(param));
    }
}
