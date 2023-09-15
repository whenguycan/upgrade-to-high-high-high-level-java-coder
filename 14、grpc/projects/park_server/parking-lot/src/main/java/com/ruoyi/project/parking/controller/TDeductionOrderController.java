package com.ruoyi.project.parking.controller;


import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.param.TDeductionOrderParam;
import com.ruoyi.project.parking.domain.vo.TDeductionOrderVO;
import com.ruoyi.project.parking.domain.vo.UnusualOrderVO;
import com.ruoyi.project.parking.entity.TDeductionOrder;
import com.ruoyi.project.parking.service.ITDeductionOrderService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 抵扣订单记录 前端控制器
 * </p>
 *
 * @author yinwen
 * @since 2023-03-10
 */
@RestController
@RequestMapping("/parking/deductionorder")
public class TDeductionOrderController extends BaseController {
    @Autowired
    ITDeductionOrderService tDeductionOrderService;


    @GetMapping("/list")
    public TableDataInfo list(TDeductionOrder tDeductionOrder) {
        tDeductionOrder.setParkNo(getParkNO());
        startPage();
        Pair<List<UnusualOrderVO>,Long> pair = tDeductionOrderService.queryUnusualOrderVOCondition(tDeductionOrder);
        return getDataTable(pair.getLeft(),pair.getRight());
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody TDeductionOrderVO tDeductionOrderVO) {
        tDeductionOrderVO.setParkNo(getParkNO());
        return toAjax(tDeductionOrderService.add(tDeductionOrderVO));
    }

    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody TDeductionOrderParam tDeductionOrderParam) {
        tDeductionOrderParam.setParkNo(getParkNO());
        return toAjax(tDeductionOrderService.editById(tDeductionOrderParam));
    }

    @GetMapping("/remove/{id}")
    public AjaxResult remove(@PathVariable @NotNull Integer id) {
        return toAjax(tDeductionOrderService.removeById(id));
    }


    /**
     * 导出
     *
     * @param response       响应消息
     * @param tDeductionOrder 查询条件
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, TDeductionOrder tDeductionOrder) {
        tDeductionOrder.setParkNo(getParkNO());
        List<UnusualOrderVO> list = tDeductionOrderService.queryUnusualOrderVOCondition(tDeductionOrder).getLeft();
        ExcelUtil<UnusualOrderVO> util = new ExcelUtil<>(UnusualOrderVO.class);
        util.exportExcel(response, list, "抵扣订单记录");
    }
}
