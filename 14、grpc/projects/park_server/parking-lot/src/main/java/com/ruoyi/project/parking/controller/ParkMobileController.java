package com.ruoyi.project.parking.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.param.VehiclePaymentInfoPavilionCodeParam;
import com.ruoyi.project.parking.domain.param.VehiclePaymentInfoPrepayCodeParam;
import com.ruoyi.project.parking.service.IParkMobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 移动端 前端控制器
 * </p>
 *
 * @author yinwen
 * @since 2023-02-22
 */
@RestController
@RequestMapping("/parking/mobile")
public class ParkMobileController extends BaseController {

    @Autowired
    private IParkMobileService parkMobileService;

    /**
     * 移动端 预支付码 查询在场应付信息
     *
     * @param vehiclePaymentInfoPrepayCodeParam 预支付请求参数
     */
    @PostMapping("/prepaycodepayinfo")
    public AjaxResult queryVehiclePrepayCodePaymentInfo(@Validated @RequestBody VehiclePaymentInfoPrepayCodeParam vehiclePaymentInfoPrepayCodeParam) {
        return success(parkMobileService.queryVehiclePrepayCodePaymentInfo(vehiclePaymentInfoPrepayCodeParam));
    }

    /**
     * 移动端 岗亭码 查询在场应付信息
     *
     * @param vehiclePaymentInfoPavilionCodeParam 岗亭支付请求参数
     */
    @PostMapping("/pavilioncodepayinfo")
    public AjaxResult queryVehiclePavilionCodePaymentInfo(@Validated @RequestBody VehiclePaymentInfoPavilionCodeParam vehiclePaymentInfoPavilionCodeParam) {
        return success(parkMobileService.queryVehiclePavilionCodePaymentInfo(vehiclePaymentInfoPavilionCodeParam));
    }

}