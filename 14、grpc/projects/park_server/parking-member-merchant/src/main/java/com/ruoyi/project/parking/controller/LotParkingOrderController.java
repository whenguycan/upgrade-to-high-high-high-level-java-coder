package com.ruoyi.project.parking.controller;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.PageDomain;
import com.ruoyi.framework.web.page.TableSupport;
import com.ruoyi.project.parking.domain.param.VehiclePaymentInfoPavilionCodeParam;
import com.ruoyi.project.parking.domain.param.VehiclePaymentInfoPrepayCodeParam;
import com.ruoyi.project.parking.domain.vo.HistoryParkingOrderVO;
import com.ruoyi.project.parking.domain.vo.ParkingOrderCouponVO;
import com.ruoyi.project.parking.domain.vo.VehicleParkOrderVO;
import com.ruoyi.project.parking.service.LotParkingGrpcServiceImpl;
import com.ruoyi.project.parking.service.ParkingOrderGrpcServiceImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户 停车订单 控制器
 */
@RestController
@RequestMapping("/parking/parkorder")
public class LotParkingOrderController extends BaseController {

    @Autowired
    private LotParkingGrpcServiceImpl lotParkingGrpcService;

    @Autowired
    private ParkingOrderGrpcServiceImpl parkingOrderGrpcService;

    /**
     * 通过 预支付码 创建 订单
     *
     * @param param 预支付订单参数
     */
    @PostMapping("/createbyprepaycode")
    public AjaxResult createByPrepayCode(@RequestBody @Validated VehiclePaymentInfoPrepayCodeParam param) {
        Pair<VehicleParkOrderVO,List<ParkingOrderCouponVO>> pair = lotParkingGrpcService.createLotParkingOrder(getUserId(), param.getParkNo(), param.getCarNumber(), StringUtils.SPACE, param.getCouponList());
        Map<String,Object> map = new HashMap<>();
        map.put("orderData",pair.getLeft());
        map.put("couponList",pair.getRight());
        return success(map);
    }

    /**
     * 通过 岗亭码 创建 订单
     *
     * @param param 岗亭码订单参数
     */
    @PostMapping("/createbypavilioncode")
    public AjaxResult createByPavilionCode(@RequestBody @Validated VehiclePaymentInfoPavilionCodeParam param) {
        Pair<VehicleParkOrderVO,List<ParkingOrderCouponVO>> pair= lotParkingGrpcService.createLotParkingOrder(getUserId(), param.getParkNo(), StringUtils.SPACE, param.getPassageNo(), param.getCouponList());
        Map<String,Object> map = new HashMap<>();
        map.put("orderData",pair.getLeft());
        map.put("couponList",pair.getRight());
        return success(map);
    }

    /**
     * 查询订单信息
     *
     * @param orderNo 订单编号
     */
    @GetMapping("/info")
    public AjaxResult info(@RequestParam String orderNo) {
        return success(lotParkingGrpcService.queryParkingOrderByOrderNo(orderNo));
    }

    /**
     * 拉取订单支付
     *
     * @param orderNo 订单号
     * @param payType 支付类型
     */
    @GetMapping("/pullorderpay")
    public AjaxResult pullOrderPay(@RequestParam String orderNo, @RequestParam Integer payType) {
        // 为了设置 url 至 data
        return success((Object) parkingOrderGrpcService.pullOrderToReadyPay(orderNo, payType));
    }

    /**
     * 查询车牌历史停车记录
     *
     * @param carNumber 车牌号
     */
    @GetMapping("/history")
    public AjaxResult history(@RequestParam String carNumber) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        List<HistoryParkingOrderVO> list = lotParkingGrpcService.queryHistoryParkingOrder(getUserId().toString(), carNumber, pageDomain.getPageNum(), pageDomain.getPageSize());
        if (list == null) {
            return error();
        } else {
            return success(list);
        }
    }

}
