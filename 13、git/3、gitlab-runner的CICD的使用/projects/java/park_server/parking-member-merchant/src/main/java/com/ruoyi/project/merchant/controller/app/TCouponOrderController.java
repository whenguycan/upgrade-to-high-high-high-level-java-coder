package com.ruoyi.project.merchant.controller.app;

import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.ruoyi.common.utils.SecurityUtils;
import io.jsonwebtoken.lang.Collections;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.merchant.domain.TCouponOrder;
import com.ruoyi.project.merchant.service.ITCouponOrderService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 商户余额自定义：商户优惠券购订单支付，处理余额；
 *
 * @author mzl
 * @date 2023-03-07
 */
@Api(tags = "H5商户优惠券订单列表")
@RestController
@RequestMapping("/merchant/app/order")
public class TCouponOrderController extends BaseController {
    @Resource
    private ITCouponOrderService tCouponOrderService;

    /**
     * 查询H5商户优惠券购买订单列表
     */
    @ApiOperation(value = "查询H5商户优惠券购买订单列表", notes = "null")
    @GetMapping("/list")
    public TableDataInfo list(@Valid TCouponOrder tCouponOrder) {
        startPage();
        tCouponOrder.setUserId(tCouponOrder.getUserId());
        List<TCouponOrder> list = tCouponOrderService.selectTCouponOrderList(tCouponOrder);
        return getDataTable(list);
    }

    /**
     * 优惠券订单详情
     *
     * @param orderNo
     * @return
     */
    @ApiOperation(value = "根据订单号查优惠券订单信息", notes = "null")
    @GetMapping(value = "/getCouponOrderInfoByOrderNo")
    public AjaxResult getCouponOrderInfoByOrderNo(String orderNo) {
        TCouponOrder tCouponOrder = new TCouponOrder();
        tCouponOrder.setOrderNo(orderNo);
        List<TCouponOrder> list = tCouponOrderService.selectTCouponOrderList(tCouponOrder);
        if (Collections.isEmpty(list)) {
            return AjaxResult.warn("该订单号异常");
        }
        return success(list.get(0));
    }

    /**
     * 获取H5商户优惠券购买订单详细信息
     */
    @ApiOperation(value = "获取H5商户优惠券购买订单详细信息", notes = "null")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tCouponOrderService.selectTCouponOrderById(id));
    }

//    /**
//     * 修改H5商户优惠券购买订单 目前 不让修改
//     */
//    @ApiOperation(value = "修改H5商户优惠券购买订单", notes = "null")
//    @Log(title = "H5商户优惠券购买订单", businessType = BusinessType.UPDATE)
//    @PutMapping("/edit")
//    public AjaxResult edit(@RequestBody TCouponOrder tCouponOrder) {
//        return toAjax(tCouponOrderService.updateTCouponOrder(tCouponOrder));
//    }

//    /**
//     * 删除H5商户优惠券购买订单
//     */
//    @ApiOperation(value = "删除H5商户优惠券购买订单", notes = "null")
//    @Log(title = "H5商户优惠券购买订单", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids) {
//        return toAjax(tCouponOrderService.deleteTCouponOrderByIds(ids));
//    }
}
