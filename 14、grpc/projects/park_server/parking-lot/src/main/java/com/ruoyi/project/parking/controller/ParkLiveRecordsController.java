package com.ruoyi.project.parking.controller;


import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.param.ParkLiveRecordsManualComputationParam;
import com.ruoyi.project.parking.domain.param.ParkLiveRecordsParam;
import com.ruoyi.project.parking.domain.vo.ParkLiveRecordsVO;
import com.ruoyi.project.parking.enums.SettleTypeEnum;
import com.ruoyi.project.parking.service.IParkLiveRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 在场记录表 前端控制器
 * </p>
 *
 * @author yinwen
 * @since 2023-02-22
 */
@RestController
@RequestMapping("/parking/parkliverecords")
public class ParkLiveRecordsController extends BaseController {

    @Autowired
    private IParkLiveRecordsService parkLiveRecordsService;

    /**
     * 查询 在场订单-带支付订单 通过  车场编号 车牌号
     *
     * @param carNumber 车牌号
     */
    @GetMapping("/info")
    public AjaxResult infoWithOrders(@RequestParam String carNumber) {
        String parkNo = getParkNO();
        return AjaxResult.success(parkLiveRecordsService.queryWithOrdersByParkNoCarNumber(parkNo, carNumber));
    }

    /**
     * 查询 在场订单记录
     *
     * @param parkLiveRecordsParam 在场订单记录 查询条件
     */
    @GetMapping("/list")
    public TableDataInfo info(ParkLiveRecordsParam parkLiveRecordsParam) {
        parkLiveRecordsParam.setParkNo(getParkNO());
        startPage();
        List<ParkLiveRecordsVO> list = parkLiveRecordsService.selectParkLiveRecordsVOList(parkLiveRecordsParam);
        return getDataTable(list);
    }

    /**
     * 批量手动结算
     *
     * @param param 手动结算参数
     */
    @PostMapping("/manual")
    public AjaxResult manualComputation(@RequestBody ParkLiveRecordsManualComputationParam param) {
        return toAjax(parkLiveRecordsService.manualComputationBatch(param.getLiveIdList(), param.getPayAmount(), param.getLiftGateReason()));
    }


    /**
     * 删除 在场记录订单
     *
     * @param id 在场记录id
     */
    @GetMapping("/remove/{id}")
    public AjaxResult remove(@PathVariable Integer id) {
        return toAjax(parkLiveRecordsService.removeParkLiveRecordsById(id));
    }


    // region 测试

    /**
     * 创建订单
     * 测试接口
     */
    @GetMapping("/test/create")
    public AjaxResult create(@RequestParam String parkNo, @RequestParam String carNumber) {
        return AjaxResult.success(parkLiveRecordsService.createParkingOrder(SettleTypeEnum.MANUAL_PAY, parkNo, carNumber, null));
    }

    /**
     * 查询订单 通过订单号
     * 测试接口
     *
     * @param orderNo 订单号
     */
    @GetMapping("/test/queryorder")
    public AjaxResult queryOrder(@RequestParam String orderNo) {
        return AjaxResult.success(parkLiveRecordsService.queryParkingOrderByOrderNo(orderNo));
    }

    // endregion
}
