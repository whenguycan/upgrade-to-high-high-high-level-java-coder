package com.ruoyi.project.merchant.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.PageDomain;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.page.TableSupport;
import com.ruoyi.project.merchant.domain.bo.MonthlyCarRentalOrderBO;
import com.ruoyi.project.parking.domain.vo.VehicleParkOrderVO;
import com.ruoyi.project.parking.domain.vo.invoice.ApplyBillRequestVO;
import com.ruoyi.project.parking.domain.vo.invoice.ParkingOrderInvoiceInfoVO;
import com.ruoyi.project.parking.enums.BillEnums;
import com.ruoyi.project.parking.service.ITInvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 月租车管理 前端控制器
 * </p>
 *
 * @author 琴声何来
 * @since 2023/3/3 15:27
 */
@Slf4j
@RestController
@RequestMapping("/monthly/invoice")
public class MonthlyInvoiceController extends BaseController {

    @Autowired
    private ITInvoiceService tInvoiceService;

    /**
     * 查询当前用户的 可开票的记录
     */
    @GetMapping("/listbillable")
    public TableDataInfo listBillable() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Pair<List<MonthlyCarRentalOrderBO>, Long> pair = tInvoiceService.pageMonthlyOrderCanInvoiceByUserId(pageDomain.getPageNum(), pageDomain.getPageSize(), getUserId());
        return getDataTable(pair.getLeft(), pair.getRight());
    }

    /**
     * 确认开票
     */
    @PostMapping("/confirm")
    public AjaxResult confirm(@RequestBody ApplyBillRequestVO requestVO) {
        requestVO.setType(BillEnums.TYPE.MONTHLY.getValue());
        requestVO.setUserId(getUserId().intValue());
        return success((Object) tInvoiceService.confirm(requestVO));
    }

    /**
     * 查看开票详情
     *
     * @param tradeNo 开票id
     */
    @GetMapping("/info/{tradeNo}")
    public AjaxResult info(@PathVariable String tradeNo) {
        return success(tInvoiceService.info(tradeNo));
    }
}
