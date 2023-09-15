package com.ruoyi.project.parking.controller;


import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.vo.parkingorder.*;
import com.ruoyi.project.parking.service.ParkingOrderGrpcServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 支出（退款）记录 前端控制器
 * </p>
 *
 * @author 琴声何来
 * @since 2023/4/21 14:05
 */
@RestController
@RequestMapping("/parking/refundOrderRecords")
public class RefundOrderRecordsController extends BaseController {


    @Autowired
    private ParkingOrderGrpcServiceImpl parkingOrderGrpcService;


    /**
     * 查询 支出（退款）记录 通过 条件
     *
     * @param param 查询条件
     */
    @GetMapping("/list")
    public TableDataInfo list(SearchRefundOrderRecordsRequestVO param) {
        param.setParkNo(SecurityUtils.getParkNo());
        SearchRefundOrderRecordsResponseVO searchRefundOrderRecordsResponseVO = parkingOrderGrpcService.queryRefundOrderListWithPage(param);
        return getDataTable(searchRefundOrderRecordsResponseVO.getOrderDetail(), searchRefundOrderRecordsResponseVO.getPageTotal().longValue());
    }

    /**
     * @apiNote 导出 支出（退款）记录
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, SearchRefundOrderRecordsRequestVO param) {
        SearchRefundOrderRecordsResponseVO searchRefundOrderRecordsResponseVO = parkingOrderGrpcService.queryRefundOrderListWithPage(param);
        ExcelUtil<RefundOrderRecordsVO> util = new ExcelUtil<>(RefundOrderRecordsVO.class);
        util.exportExcel(response, searchRefundOrderRecordsResponseVO.getOrderDetail(), "停车交易记录");
    }

}
