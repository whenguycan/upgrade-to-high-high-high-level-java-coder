package com.ruoyi.project.parking.controller;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.param.ParkLiveRecordsAddParam;
import com.ruoyi.project.parking.domain.param.ParkLiveRecordsParam;
import com.ruoyi.project.parking.domain.param.ParkLiveRecordsUpdateParam;
import com.ruoyi.project.parking.domain.vo.ParkLiveRecordsVO;
import com.ruoyi.project.parking.domain.vo.ParkSettlementRecordsVO;
import com.ruoyi.project.parking.service.IParkLiveRecordsService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 已结算订单记录
 * </p>
 *
 * @author yinwen
 * @since 2023-02-24
 */
@RestController
@RequestMapping("/parking/parksettlementrecords")
public class ParkSettlementRecordsController extends BaseController {

    @Autowired
    private IParkLiveRecordsService parkLiveRecordsService;



    /**
     * 查询 已结算订单记录
     *
     * @param parkLiveRecordsParam 已结算订单记录 查询条件
     */
    @GetMapping("/list")
    public TableDataInfo list(ParkLiveRecordsParam parkLiveRecordsParam) {
        parkLiveRecordsParam.setParkNo(getParkNO());
        startPage();
        Pair<List<ParkSettlementRecordsVO>, Long> pair = parkLiveRecordsService.selectParkSettlementRecordsVOList(parkLiveRecordsParam);
        TableDataInfo dataTable = getDataTable(pair.getLeft());
        dataTable.setTotal(pair.getRight());
        return dataTable;
    }

    /**
     * 查询 已结算订单 详情
     *
     * @param id 在场记录id
     */
    @GetMapping("/info")
    public AjaxResult info(@RequestParam Integer id) {
        return AjaxResult.success(parkLiveRecordsService.queryParkSettlementRecordsVOById(id));
    }


    /**
     * 新增 已结算订单记录
     *
     * @param param 新增离场参数
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody ParkLiveRecordsAddParam param) {
        param.setParkNo(getParkNO());
        return toAjax(parkLiveRecordsService.addSettlementRecords(param));
    }

    /**
     * 导入 已结算订单记录
     *
     * @param file excel文件
     */
    @PostMapping("/import")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<ParkLiveRecordsAddParam> util = new ExcelUtil<>(ParkLiveRecordsAddParam.class);
        List<ParkLiveRecordsAddParam> records = util.importExcel(file.getInputStream());
        records.forEach(m -> m.setParkNo(getParkNO()));
        return toAjax(parkLiveRecordsService.importSettlementRecords(records));
    }

    /**
     * 导出 已结算订单记录
     *
     * @param response             响应消息
     * @param parkLiveRecordsParam 查询条件
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, ParkLiveRecordsParam parkLiveRecordsParam) {
        parkLiveRecordsParam.setParkNo(getParkNO());
        List<ParkLiveRecordsVO> list = parkLiveRecordsService.selectParkLiveRecordsVOList(parkLiveRecordsParam);
        ExcelUtil<ParkLiveRecordsVO> util = new ExcelUtil<>(ParkLiveRecordsVO.class);
        util.exportExcel(response, list, "出场记录");
    }

    /**
     * 编辑 已结算订单 详情
     *
     * @param param 离场记录更新参数
     */
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody ParkLiveRecordsUpdateParam param) {
        return toAjax(parkLiveRecordsService.updateSettlementRecordsCarNumberById(param.getId(), param.getCarNumber()));
    }

    /**
     * 删除 已结算订单 详情
     *
     * @param id 离场记录id
     */
    @GetMapping("/remove/{id}")
    public AjaxResult remove(@PathVariable Integer id) {
        return toAjax(parkLiveRecordsService.removeSettlementRecordsById(id));
    }

}
