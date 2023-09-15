package com.ruoyi.project.parking.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.ParkUsageDayFact;
import com.ruoyi.project.parking.domain.RevenueStatisticsDayFact;
import com.ruoyi.project.parking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 大屏Controller
 * 
 * @author fangch
 * @date 2023-03-24
 */
@RestController
@RequestMapping("/parking/BigScreen")
public class BigScreenController extends BaseController {

    @Autowired
    private IDurationStatisticDayFactService durationStatisticDayFactService;

    @Autowired
    private ITopRankDayFactService topRankDayFactService;

    @Autowired
    private IParkUsageDayFactService parkUsageDayFactService;

    @Autowired
    private IRevenueStatisticsDayFactService revenueStatisticsDayFactService;

    @Autowired
    private ICouponDetailDayFactService couponDetailDayFactService;

    @Autowired
    private IBPassageDeviceService passageDeviceService;

    @Autowired
    private IBigScreenService bigScreenService;

    /**
     * 停车时长统计
     */
    @GetMapping("/getDurationStatistic")
    public AjaxResult getDurationStatistic(@RequestParam String parkNo) {
        return success(durationStatisticDayFactService.getDurationStatistic(parkNo));
    }

    /**
     * 车场数据统计
     */
    @GetMapping("/getParkingLotNumStatistics")
    public AjaxResult getParkingLotNumStatistics(@RequestParam String parkNo) {
        return success(durationStatisticDayFactService.getParkingLotNumStatistics(parkNo));
    }

    /**
     * 车场热门排行
     */
    @GetMapping("/getTopRank")
    public AjaxResult getTopRank() {
        return success(topRankDayFactService.getTopRank());
    }

    /**
     * 今日泊车使用情况
     */
    @GetMapping("/getParkUsage")
    public AjaxResult getParkUsage(@RequestParam String parkNo) {
        ParkUsageDayFact parkUsageDayFact = new ParkUsageDayFact();
        parkUsageDayFact.setParkNo(parkNo);
        parkUsageDayFact.setDay(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return success(parkUsageDayFactService.selectParkUsageDayFact(parkUsageDayFact));
    }

    /**
     * 收入统计
     */
    @GetMapping("/getRevenueStatistics")
    public AjaxResult getRevenueStatistics(@RequestParam String parkNo) {
        RevenueStatisticsDayFact revenueStatisticsDayFact = new RevenueStatisticsDayFact();
        revenueStatisticsDayFact.setParkNo(parkNo);
        return success(revenueStatisticsDayFactService.getRevenueStatistics(revenueStatisticsDayFact));
    }

    /**
     * 月发放优惠券统计
     */
    @GetMapping("/getCouponDetailStatistics")
    public AjaxResult getCouponDetailStatistics(@RequestParam String parkNo) {
        // 获取当前的用户
        return success(couponDetailDayFactService.getCouponDetailStatistics(parkNo));
    }

    /**
     * 设备统计
     */
    @GetMapping("/getDeviceStatistics")
    public AjaxResult getDeviceStatistics(@RequestParam String parkNo) {
        return success(passageDeviceService.getDeviceStatistics(parkNo));
    }

    /**
     * GIS数据统计
     */
    @GetMapping("/getGISStatistics")
    public AjaxResult getGISStatistics(@RequestParam String parkNo) {
        return success(bigScreenService.getGISStatistics(parkNo));
    }

    /**
     * 车场状态数据统计
     */
    @GetMapping("/getParkInfo")
    public AjaxResult getParkInfo(@RequestParam String parkNo) {
        return success(bigScreenService.getParkInfo(parkNo));
    }
}
