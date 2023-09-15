package com.ruoyi.project.parking.controller;


import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.PageDomain;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.page.TableSupport;
import com.ruoyi.project.parking.domain.vo.VehicleParkOrderVO;
import com.ruoyi.project.parking.domain.vo.invoice.ApplyBillRequestVO;
import com.ruoyi.project.parking.domain.vo.invoice.ParkingOrderInvoiceInfoVO;
import com.ruoyi.project.parking.enums.BillEnums;
import com.ruoyi.project.parking.service.ITInvoiceService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 发票记录表 前端控制器
 * </p>
 *
 * @author yinwen
 * @since 2023-04-06
 */
@RestController
@RequestMapping("/parking/invoice")
public class TInvoiceController extends BaseController {

    @Autowired
    private ITInvoiceService tInvoiceService;

    /**
     * 查询当前用户的 开票历史记录-分页
     */
    @GetMapping("/history")
    public TableDataInfo history() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Pair<List<ParkingOrderInvoiceInfoVO>, Long> pair = tInvoiceService.pageHistoryByUseId(pageDomain.getPageNum(), pageDomain.getPageSize(), getUserId());
        return getDataTable(pair.getLeft(), pair.getRight());
    }

    /**
     * 查询当前用户的 可开票的记录
     */
    @GetMapping("/listbillable")
    public TableDataInfo listBillable() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Pair<List<VehicleParkOrderVO>, Long> pair = tInvoiceService.pageParkingOrderCanInvoiceByUserId(pageDomain.getPageNum(), pageDomain.getPageSize(), getUserId());
        return getDataTable(pair.getLeft(), pair.getRight());
    }

    /**
     * 确认开票
     */
    @PostMapping("/confirm")
    public AjaxResult confirm(@RequestBody ApplyBillRequestVO requestVO) {
        requestVO.setType(BillEnums.TYPE.PARKING.getValue());
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

