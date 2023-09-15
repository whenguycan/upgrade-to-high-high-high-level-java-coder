package com.ruoyi.project.parking.controller;


import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.param.ParkLiveRecordsEditEntryTimeBatchParam;
import com.ruoyi.project.parking.domain.param.ParkLiveRecordsManualComputationParam;
import com.ruoyi.project.parking.domain.param.ParkLiveRecordsParam;
import com.ruoyi.project.parking.domain.vo.ParkLiveRecordsVO;
import com.ruoyi.project.parking.service.IParkLiveRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
     * 批量 修改入场时间
     *
     * @param param 修改入场时间参数
     */
    @PostMapping("/editentrytime")
    public AjaxResult editEntryTimeBatch(@RequestBody ParkLiveRecordsEditEntryTimeBatchParam param) {
        return toAjax(parkLiveRecordsService.editEntryTimeBatch(param.getLiveIdList(), param.getEntryTime()));
    }


    /**
     * 批量手动结算
     * 发送通知 - 更新在场记录的 在场状态 离场时间,(更新 离场记录数据)
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

    /**
     * 导出 在场订单记录
     *
     * @param response             响应消息
     * @param parkLiveRecordsParam 查询条件
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, ParkLiveRecordsParam parkLiveRecordsParam) {
        parkLiveRecordsParam.setParkNo(getParkNO());
        List<ParkLiveRecordsVO> list = parkLiveRecordsService.selectParkLiveRecordsVOList(parkLiveRecordsParam);
        ExcelUtil<ParkLiveRecordsVO> util = new ExcelUtil<>(ParkLiveRecordsVO.class);
        util.exportExcel(response, list, "在场记录");
    }


}
