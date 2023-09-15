package com.ruoyi.project.parking.controller;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.*;
import com.ruoyi.project.parking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 首页Controller
 * 
 * @author fangch
 * @date 2023-03-20
 */
@RestController
@RequestMapping("/parking/HomePage")
public class HomePageController extends BaseController
{
    @Autowired
    private IHomePageService homePageService;

    @Autowired
    private IPayMethodDayFactService payMethodDayFactService;

    @Autowired
    private IPayTypeDayFactService payTypeDayFactService;

    @Autowired
    private IDurationStatisticDayFactService durationStatisticDayFactService;

    @Autowired
    private IOrderSituationDayFactService orderSituationDayFactService;

    @Autowired
    private IEntryExitAnalysisDayFactService entryExitAnalysisDayFactService;

    @Autowired
    private ITimeShareUsageDayFactService timeShareUsageDayFactService;

    /**
     * 首页顶部
     */
    @GetMapping("/homePageHeader")
    public AjaxResult homePageHeader() {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        return success(homePageService.homePageHeader(loginUser.getDeptId()));
    }

    /**
     * 付费方式
     */
    @GetMapping("/getPayMethodDayFact")
    public AjaxResult getPayMethodDayFact() {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        PayMethodDayFact payMethodDayFact = new PayMethodDayFact();
        payMethodDayFact.setDay(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        payMethodDayFact.setParkNo(loginUser.getUser().getDept().getParkNo());
        return success(payMethodDayFactService.getPayMethodDayFact(payMethodDayFact));
    }

    /**
     * 付费类型
     */
    @GetMapping("/getPayTypeDayFact")
    public AjaxResult getPayTypeDayFact() {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        PayTypeDayFact payTypeDayFact = new PayTypeDayFact();
        payTypeDayFact.setDay(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        payTypeDayFact.setParkNo(loginUser.getUser().getDept().getParkNo());
        return success(payTypeDayFactService.getPayTypeDayFact(payTypeDayFact));
    }

    /**
     * 昨日时长统计
     */
    @GetMapping("/getDurationStatisticDayFact")
    public AjaxResult getDurationStatisticDayFact() {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        DurationStatisticDayFact durationStatisticDayFact = new DurationStatisticDayFact();
        durationStatisticDayFact.setDay(LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        durationStatisticDayFact.setParkNo(loginUser.getUser().getDept().getParkNo());
        return success(durationStatisticDayFactService.getDurationStatisticDayFact(durationStatisticDayFact));
    }

    /**
     * 近七日订单情况
     */
    @GetMapping("/getOrderSituationDayFact")
    public AjaxResult getOrderSituationDayFact() {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        OrderSituationDayFact orderSituationDayFact = new OrderSituationDayFact();
        orderSituationDayFact.setDay(LocalDate.now().minusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        orderSituationDayFact.setParkNo(loginUser.getUser().getDept().getParkNo());
        return success(orderSituationDayFactService.getOrderSituationDayFact(orderSituationDayFact));
    }

    /**
     * 昨日出入场分析
     */
    @GetMapping("/getEntryExitAnalysisDayFact")
    public AjaxResult getEntryExitAnalysisDayFact() {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        EntryExitAnalysisDayFact entryExitAnalysisDayFact = new EntryExitAnalysisDayFact();
        entryExitAnalysisDayFact.setDay(LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        entryExitAnalysisDayFact.setParkNo(loginUser.getUser().getDept().getParkNo());
        return success(entryExitAnalysisDayFactService.getEntryExitAnalysisDayFact(entryExitAnalysisDayFact));
    }

    /**
     * 昨日分时利用
     */
    @GetMapping("/getTimeShareUsageDayFact")
    public AjaxResult getTimeShareUsageDayFact() {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        TimeShareUsageDayFact timeShareUsageDayFact = new TimeShareUsageDayFact();
        timeShareUsageDayFact.setDay(LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeShareUsageDayFact.setParkNo(loginUser.getUser().getDept().getParkNo());
        return success(timeShareUsageDayFactService.getTimeShareUsageDayFact(timeShareUsageDayFact));
    }

}
