package com.ruoyi.project.parking.controller;


import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.vo.monthlyOrder.MonthlyOrderVO;
import com.ruoyi.project.parking.domain.vo.monthlyOrder.SearchMonthlyOrderRequestVO;
import com.ruoyi.project.parking.domain.vo.monthlyOrder.SearchMonthlyOrderResponseVO;
import com.ruoyi.project.parking.service.MonthlyOrderGrpcServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 包月订单记录 前端控制器
 * </p>
 *
 * @author yinwen
 * @since 2023-03-02
 */
@RestController
@RequestMapping("/parking/monthlyorderrecords")
public class MonthlyOrderRecordsController extends BaseController {


    @Autowired
    private MonthlyOrderGrpcServiceImpl monthlyOrderGrpcService;


    /**
     * 查询 包月订单记录 通过 条件
     *
     * @param param 车牌号
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody SearchMonthlyOrderRequestVO param) {
        // 仅查询 成功的 交易记录
        param.setOrderStatus("03");
        SearchMonthlyOrderResponseVO searchMonthlyOrderResponseVO = monthlyOrderGrpcService.queryOrderListWithPage(param);
        return getDataTable(searchMonthlyOrderResponseVO.getOrderDetail(), searchMonthlyOrderResponseVO.getPageTotal().longValue());
    }

    @PostMapping("/export")
    public void export(HttpServletResponse response, SearchMonthlyOrderRequestVO param) {
        SearchMonthlyOrderResponseVO searchMonthlyOrderResponseVO = monthlyOrderGrpcService.queryOrderListWithPage(param);
        ExcelUtil<MonthlyOrderVO> util = new ExcelUtil<>(MonthlyOrderVO.class);
        util.exportExcel(response, searchMonthlyOrderResponseVO.getOrderDetail(), "包月交易记录");
    }
}
