package com.ruoyi.project.parking.controller;


import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.param.TExemptOrderParam;
import com.ruoyi.project.parking.domain.vo.TExemptOrderVO;
import com.ruoyi.project.parking.domain.vo.UnusualOrderVO;
import com.ruoyi.project.parking.entity.TExemptOrder;
import com.ruoyi.project.parking.service.ITExemptOrderService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 减免订单记录 前端控制器
 * </p>
 *
 * @author yinwen
 * @since 2023-03-10
 */
@RestController
@RequestMapping("/parking/exemptorder")
public class TExemptOrderController extends BaseController {
    @Autowired
    ITExemptOrderService tExemptOrderService;


    @GetMapping("/list")
    public TableDataInfo list(TExemptOrder tExemptOrder) {
        tExemptOrder.setParkNo(getParkNO());
        startPage();
        Pair<List<UnusualOrderVO>,Long> pair = tExemptOrderService.queryUnusualOrderVOCondition(tExemptOrder);
        return getDataTable(pair.getLeft(),pair.getRight());
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody TExemptOrderVO tExemptOrderVO) {
        tExemptOrderVO.setParkNo(getParkNO());
        return toAjax(tExemptOrderService.add(tExemptOrderVO));
    }

    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody TExemptOrderParam tExemptOrderParam) {
        tExemptOrderParam.setParkNo(getParkNO());
        return toAjax(tExemptOrderService.editById(tExemptOrderParam));
    }

    @GetMapping("/remove/{id}")
    public AjaxResult remove(@PathVariable @NotNull Integer id) {
        return toAjax(tExemptOrderService.removeById(id));
    }

    /**
     * 导出
     *
     * @param response       响应消息
     * @param tExemptOrder 查询条件
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, TExemptOrder tExemptOrder) {
        tExemptOrder.setParkNo(getParkNO());
        List<UnusualOrderVO> list = tExemptOrderService.queryUnusualOrderVOCondition(tExemptOrder).getLeft();
        ExcelUtil<UnusualOrderVO> util = new ExcelUtil<>(UnusualOrderVO.class);
        util.exportExcel(response, list, "减免订单记录");
    }
}
