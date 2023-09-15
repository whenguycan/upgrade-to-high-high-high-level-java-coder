package com.ruoyi.project.merchant.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.merchant.domain.MonthlyCarRentalOrder;
import com.ruoyi.project.merchant.domain.param.MonthlyCarRentalOrderParam;
import com.ruoyi.project.merchant.service.IMonthlyCarRentalOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/merchant/monthlyCarRentalOrder")
public class MonthlyCarRentalOrderController extends BaseController {

    @Autowired
    IMonthlyCarRentalOrderService monthlyCarRentalOrderService;

    /**
     * @apiNote 获取我的订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MonthlyCarRentalOrder monthlyCarRentalOrder){
        return getDataTable(monthlyCarRentalOrderService.listOrder(monthlyCarRentalOrder));
    }

    /**
     * @apiNote 获取单个订单详情
     */
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Integer id){
        return AjaxResult.success(monthlyCarRentalOrderService.getMonthlyCarRentalOrderById(id));
    }

    /**
     * @apiNote 下单接口
     */
    @PostMapping("/createOrder")
    public AjaxResult createOrder(@RequestBody MonthlyCarRentalOrder monthlyCarRentalOrder) {
        if (!monthlyCarRentalOrderService.verifyOrderQualification(monthlyCarRentalOrder)) {
            log.error("不具备下单条件");
            return error("不具备下单条件");
        }
        //调用订单系统接口生成订单，返回订单号
        return AjaxResult.success("操作成功", monthlyCarRentalOrderService.createOrder(monthlyCarRentalOrder));
    }

    /**
     * @apiNote 确认支付接口
     */
    @PostMapping("/confirmPay")
    public AjaxResult confirmPay(@RequestBody MonthlyCarRentalOrderParam monthlyCarRentalOrderParam) {
        //调用订单系统拉起支付
        return AjaxResult.success(monthlyCarRentalOrderService.confirmPay(monthlyCarRentalOrderParam));
    }

}



