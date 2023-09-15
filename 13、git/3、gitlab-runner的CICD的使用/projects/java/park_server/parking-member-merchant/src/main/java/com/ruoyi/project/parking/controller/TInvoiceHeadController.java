package com.ruoyi.project.parking.controller;


import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.param.InvoiceHeadParam;
import com.ruoyi.project.parking.domain.vo.InvoiceHeadVO;
import com.ruoyi.project.parking.service.ITInvoiceHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 发票抬头记录表 前端控制器
 * </p>
 *
 * @author yinwen
 * @since 2023-04-06
 */
@RestController
@RequestMapping("/parking/invoicehead")
public class TInvoiceHeadController extends BaseController {

    @Autowired
    private ITInvoiceHeadService tInvoiceHeadService;

    /**
     * 查询当前用户的 发票抬头
     */
    @GetMapping("/list")
    public AjaxResult list(@RequestParam(required = false)String type) {
        return success(tInvoiceHeadService.listByUseId(getUserId(),type));
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody InvoiceHeadVO param) {
        param.setUserId(getUserId().intValue());
        return toAjax(tInvoiceHeadService.add(param));
    }

    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody InvoiceHeadParam param) {
        return toAjax(tInvoiceHeadService.editById(param));
    }

    @GetMapping("/default/{id}")
    public AjaxResult setdefault(@PathVariable @NotNull Integer id) {
        return toAjax(tInvoiceHeadService.setdefault(id, 1));
    }

    @GetMapping("/undefault/{id}")
    public AjaxResult setundefault(@PathVariable @NotNull Integer id) {
        return toAjax(tInvoiceHeadService.setdefault(id, 0));
    }
}
