package com.ruoyi.project.parking.controller;


import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.notification.CarEntryNotificationData;
import com.ruoyi.project.parking.domain.notification.CarExitNotificationData;
import com.ruoyi.project.parking.domain.notification.CarPayNotificationData;
import com.ruoyi.project.parking.domain.param.BNotifyTemplateParam;
import com.ruoyi.project.parking.service.IBNotifyTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 消息推送模板表 前端控制器
 * </p>
 *
 * @author 琴声何来
 * @since 2023/3/10 14:01
 */
@RestController
@RequestMapping("/parking/notifyTemplate")
public class BNotifyTemplateController extends BaseController {


    @Autowired
    private IBNotifyTemplateService notifyTemplateService;

    /**
     * @apiNote 根据场库编号获取消息推送模板列表
     */
    @GetMapping(value = "/list")
    public TableDataInfo list() {
        return getDataTable(notifyTemplateService.getBNotifyTemplateByParkNo());
    }

    /**
     * @apiNote 编辑消息推送模板
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody BNotifyTemplateParam notifyTemplateParam) {
        return toAjax(notifyTemplateService.editById(notifyTemplateParam));
    }

    /**
     * @apiNote 测试入场消息推送
     */
    @PostMapping("/testCarEntry")
    public AjaxResult testCarEntry(@RequestBody CarEntryNotificationData carEntryNotificationData) {
        return toAjax(notifyTemplateService.testCarEntry(carEntryNotificationData));
    }

    /**
     * @apiNote 测试出场消息推送
     */
    @PostMapping("/testCarExit")
    public AjaxResult testCarExit(@RequestBody CarExitNotificationData carExitNotificationData) {
        return toAjax(notifyTemplateService.testCarExit(carExitNotificationData));
    }

    /**
     * @apiNote 测试支付消息推送
     */
    @PostMapping("/testCarPay")
    public AjaxResult testCarEntry(@RequestBody CarPayNotificationData carPayNotificationData) {
        return toAjax(notifyTemplateService.testCarPay(carPayNotificationData));
    }
}
