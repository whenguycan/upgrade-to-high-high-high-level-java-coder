package com.ruoyi.project.parking.controller;


import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.vo.RefundOrderVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.RefundOrderResponseVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.SearchOrderRequestVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.SearchOrderResponseVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.VehicleParkOrderVO;
import com.ruoyi.project.parking.service.ParkingOrderGrpcServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 交易记录 前端控制器
 * </p>
 *
 * @author yinwen
 * @since 2023-03-02
 */
@RestController
@RequestMapping("/parking/parkorderrecords")
public class ParkOrderRecordsController extends BaseController {


    @Autowired
    private ParkingOrderGrpcServiceImpl parkingOrderGrpcService;


    /**
     * 查询 交易记录 通过 条件
     *
     * @param param 查询条件
     */
    @GetMapping("/list")
    public TableDataInfo list(SearchOrderRequestVO param) {
        SearchOrderResponseVO searchOrderResponseVO = parkingOrderGrpcService.queryOrderListWithPage(param);
        return getDataTable(searchOrderResponseVO.getOrderDetail(), searchOrderResponseVO.getPageTotal().longValue());
    }

    @PostMapping("/export")
    public void export(HttpServletResponse response, SearchOrderRequestVO param) {
        SearchOrderResponseVO searchOrderResponseVO = parkingOrderGrpcService.queryOrderListWithPage(param);
        ExcelUtil<VehicleParkOrderVO> util = new ExcelUtil<>(VehicleParkOrderVO.class);
        util.exportExcel(response, searchOrderResponseVO.getOrderDetail(), "停车交易记录");
    }

    /**
     * @apiNote 退款
     */
    @PostMapping("/refund")
    public AjaxResult refund(@RequestBody RefundOrderVO refundOrderVO) {
        refundOrderVO.setCreateBy(SecurityUtils.getUsername());
        RefundOrderResponseVO refund = parkingOrderGrpcService.refund(refundOrderVO);
        return new AjaxResult(Integer.parseInt(refund.getStatus()), refund.getMess());
    }
}
