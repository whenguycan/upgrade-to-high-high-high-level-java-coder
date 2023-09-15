package com.ruoyi.project.parking.controller;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.bo.CarVolumeSheetBO;
import com.ruoyi.project.parking.domain.vo.CarVolumeSheetVO;
import com.ruoyi.project.parking.domain.vo.StatisticsSheetVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.SearchOrderRequestVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.SearchOrderResponseVO;
import com.ruoyi.project.parking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 报表 前端控制器
 * </p>
 *
 * @author 琴声何来
 * @since 2023/4/18 9:42
 */
@RestController
@RequestMapping("/parking/sheet")
public class SheetController extends BaseController {

    @Autowired
    ISheetService sheetService;

    @Autowired
    private ParkingOrderGrpcServiceImpl parkingOrderGrpcService;


    /**
     * 支付类型报表
     */
    @GetMapping("/payTypeStatisticsSheet")
    public AjaxResult payTypeStatisticsSheet(StatisticsSheetVO statisticsSheetVO) {
        statisticsSheetVO.setParkNo(SecurityUtils.getParkNo());
        return success(sheetService.payTypeStatisticsSheet(statisticsSheetVO));
    }

    /**
     * 支付类型报表明细
     */
    @GetMapping("/payTypeStatisticsSheetDetail")
    public TableDataInfo payTypeStatisticsSheetDetail(SearchOrderRequestVO searchOrderRequestVO) {
        searchOrderRequestVO.setParkNo(SecurityUtils.getParkNo());
        searchOrderRequestVO.setOrderStatus("03");
        SearchOrderResponseVO searchOrderResponseVO = parkingOrderGrpcService.queryOrderListWithPage(searchOrderRequestVO);
        return getDataTable(searchOrderResponseVO.getOrderDetail(),searchOrderResponseVO.getPageTotal().longValue());
    }

    /**
     * 车流量报表
     */
    @GetMapping("/carVolumeSheet")
    public AjaxResult carVolumeSheet(CarVolumeSheetVO carVolumeSheetVO) {
        carVolumeSheetVO.setParkNo(SecurityUtils.getParkNo());
        List<CarVolumeSheetBO> list = sheetService.carVolumeSheet(carVolumeSheetVO);
        list.forEach(item -> {
            //差异量=进场车辆-出场车辆
            item.setDisparity(item.getEntryCount() - item.getExitCount());
            BigDecimal total = BigDecimal.valueOf(item.getEntryCount()).add(BigDecimal.valueOf(item.getExitCount()));
            if (total.equals(BigDecimal.ZERO)) {
                item.setDisparityRate(BigDecimal.ZERO + "%");
            } else {
                //差异率=差异量/进出车总数
                item.setDisparityRate(BigDecimal.valueOf(item.getDisparity())
                        .multiply(BigDecimal.valueOf(100))
                        .divide(total, 2, RoundingMode.CEILING)
                        + "%");
            }
        });
        return success(list);
    }

    /**
     * @apiNote 车流量报表导出
     */
    @PostMapping("/exportCarVolumeSheet")
    public void export(HttpServletResponse response, CarVolumeSheetVO carVolumeSheetVO) {
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("车流量报表" + new Date().getTime(), StandardCharsets.UTF_8) + ".xlsx");
        carVolumeSheetVO.setParkNo(SecurityUtils.getParkNo());
        List<CarVolumeSheetBO> list = sheetService.carVolumeSheet(carVolumeSheetVO);
        list.forEach(item -> {
            //差异量=进场车辆-出场车辆
            item.setDisparity(item.getEntryCount() - item.getExitCount());
            BigDecimal total = BigDecimal.valueOf(item.getEntryCount()).add(BigDecimal.valueOf(item.getExitCount()));
            if (total.equals(BigDecimal.ZERO)) {
                item.setDisparityRate(BigDecimal.ZERO + "%");
            } else {
                //差异率=差异量/进出车总数
                item.setDisparityRate(BigDecimal.valueOf(item.getDisparity())
                        .multiply(BigDecimal.valueOf(100))
                        .divide(total, 2, RoundingMode.CEILING)
                        + "%");
            }
        });
        ExcelUtil<CarVolumeSheetBO> util = new ExcelUtil<>(CarVolumeSheetBO.class);
        util.exportExcel(response, list, "车流量报表");
    }

    /**
     * 访客统计报表
     */
    @GetMapping("/visitorApplyStatisticsSheet")
    public AjaxResult visitorApplyStatisticsSheet(StatisticsSheetVO statisticsSheetVO) {
        statisticsSheetVO.setParkNo(SecurityUtils.getParkNo());
        return success(sheetService.visitorApplyStatisticsSheet(statisticsSheetVO));
    }
}
