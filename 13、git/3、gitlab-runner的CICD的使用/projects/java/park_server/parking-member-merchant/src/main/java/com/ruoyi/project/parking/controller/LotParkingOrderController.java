package com.ruoyi.project.parking.controller;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.PageDomain;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.page.TableSupport;
import com.ruoyi.project.parking.domain.param.VehiclePaymentInfoPavilionCodeParam;
import com.ruoyi.project.parking.domain.param.VehiclePaymentInfoPrepayCodeParam;
import com.ruoyi.project.parking.domain.vo.*;
import com.ruoyi.project.parking.service.LotParkingGrpcServiceImpl;
import com.ruoyi.project.parking.service.ParkingOrderGrpcServiceImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
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
        Triple<Boolean, VehicleParkOrderVO, List<ParkingOrderCouponShowVO>> triple = lotParkingGrpcService.createOrReCalcLotParkingOrder(param.getParkNo(), param.getCarNumber(), StringUtils.EMPTY, param.getCouponList(), param.getOrderNo(), param.isNotUseCoupon());
        Map<String, Object> map = new HashMap<>();
        map.put("orderData", triple.getMiddle());
        map.put("couponList", triple.getRight());
        return success(map);
    }

    /**
     * 手动 取消 预支付订单
     *
     * @param orderNo 预支付订单编号
     */
    @GetMapping("/cancleorder")
    public AjaxResult cancleOrderByOrderNo(@RequestParam String orderNo) {
        return toAjax(parkingOrderGrpcService.cancleOrderByOrderNo(orderNo));
    }

    /**
     * 通过 岗亭码 创建 订单
     *
     * @param param 岗亭码订单参数
     */
    @PostMapping("/createbypavilioncode")
    public AjaxResult createByPavilionCode(@RequestBody @Validated VehiclePaymentInfoPavilionCodeParam param) {
        Triple<Boolean, VehicleParkOrderVO, List<ParkingOrderCouponShowVO>> triple = lotParkingGrpcService.createOrReCalcLotParkingOrder(param.getParkNo(), StringUtils.EMPTY, param.getPassageNo(), param.getCouponList(), param.getOrderNo(), param.isNotUseCoupon());
        Map<String, Object> map = new HashMap<>();
        map.put("orderData", triple.getMiddle());
        map.put("couponList", triple.getRight());
        return success(map);
    }

    /**
     * 手动切换优惠券
     *
     * @param param 切换优惠券参数
     */
    @PostMapping("/switchordercoupon")
    public AjaxResult switchOrderCoupon(@RequestBody ChangeOrderCouponRquestVO param) {
        Pair<VehicleParkOrderVO, List<ParkingOrderCouponShowVO>> pair = parkingOrderGrpcService.switchOrderCoupon(param);
        Map<String, Object> map = new HashMap<>();
        map.put("orderData", pair.getLeft());
        map.put("couponList", pair.getRight());
        return success(map);
    }

    /**
     * 查询订单信息
     *
     * @param orderNo 订单编号
     */
    @GetMapping("/info")
    public AjaxResult info(@RequestParam @Validated @NotBlank String orderNo) {
        Pair<VehicleParkOrderVO, List<ParkingOrderCouponShowVO>> pair = lotParkingGrpcService.queryParkingOrderByOrderNo(orderNo);
        Map<String, Object> map = new HashMap<>();
        map.put("orderData", pair.getLeft());
        map.put("couponList", pair.getRight());
        return success(map);
    }

    /**
     * 拉取订单支付
     *
     * @param param 拉起支付参数
     */
    @PostMapping("/pullorderpay")
    public AjaxResult pullOrderPay(@RequestBody ConfirmPayRequestVO param) {
        return success(parkingOrderGrpcService.pullOrderToReadyPay(param));
    }

    /**
     * 查询车牌历史停车记录
     *
     * @param carNumber 车牌号 不传查询所有
     */
    @GetMapping("/history")
    public TableDataInfo history(@RequestParam(required = false) String carNumber) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        // null => ""
        carNumber = StringUtils.isEmpty(carNumber) ? StringUtils.EMPTY : carNumber;
        HistoryParkingOrderReponseVO historyParkingOrderReponseVO = lotParkingGrpcService.queryHistoryParkingOrder(getUserId().toString(), carNumber, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(historyParkingOrderReponseVO.getRows(), historyParkingOrderReponseVO.getTotal().longValue());
    }

}
