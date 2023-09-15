package com.ruoyi.project.parking.controller;


import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.param.TAbnormalOrderParam;
import com.ruoyi.project.parking.domain.vo.TAbnormalOrderVO;
import com.ruoyi.project.parking.domain.vo.UnusualOrderVO;
import com.ruoyi.project.parking.entity.TAbnormalOrder;
import com.ruoyi.project.parking.service.ITAbnormalOrderService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 异常订单记录 前端控制器
 * </p>
 *
 * @author yinwen
 * @since 2023-03-10
 */
@RestController
@RequestMapping("/parking/abnormalorder")
public class TAbnormalOrderController extends BaseController {

    @Autowired
    ITAbnormalOrderService tAbnormalOrderService;


    @GetMapping("/list")
    public TableDataInfo list(TAbnormalOrder tAbnormalOrder) {
        tAbnormalOrder.setParkNo(getParkNO());
        startPage();
        Pair<List<UnusualOrderVO>,Long> pair = tAbnormalOrderService.queryUnusualOrderVOCondition(tAbnormalOrder);
        return getDataTable(pair.getLeft(),pair.getRight());
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody TAbnormalOrderVO tAbnormalOrderVO) {
        tAbnormalOrderVO.setParkNo(getParkNO());
        return toAjax(tAbnormalOrderService.add(tAbnormalOrderVO));
    }

    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody TAbnormalOrderParam tAbnormalOrderParam) {
        tAbnormalOrderParam.setParkNo(getParkNO());
        return toAjax(tAbnormalOrderService.editById(tAbnormalOrderParam));
    }

    @GetMapping("/remove/{id}")
    public AjaxResult remove(@PathVariable @NotNull Integer id) {
        return toAjax(tAbnormalOrderService.removeById(id));
    }


    /**
     * 导出
     *
     * @param response       响应消息
     * @param tAbnormalOrder 查询条件
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, TAbnormalOrder tAbnormalOrder) {
        tAbnormalOrder.setParkNo(getParkNO());
        List<UnusualOrderVO> list = tAbnormalOrderService.queryUnusualOrderVOCondition(tAbnormalOrder).getLeft();
        ExcelUtil<UnusualOrderVO> util = new ExcelUtil<>(UnusualOrderVO.class);
        util.exportExcel(response, list, "异常订单记录");
    }
}
