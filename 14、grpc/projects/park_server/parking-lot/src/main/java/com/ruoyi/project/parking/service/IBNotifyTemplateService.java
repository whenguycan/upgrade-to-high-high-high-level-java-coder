package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.BNotifyTemplate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.parking.domain.notification.CarEntryNotificationData;
import com.ruoyi.project.parking.domain.notification.CarExitNotificationData;
import com.ruoyi.project.parking.domain.notification.CarPayNotificationData;
import com.ruoyi.project.parking.domain.param.BNotifyTemplateParam;

import java.util.List;

/**
* @author 琴声何来
* @description 针对表【b_notify_template(消息推送模板表)】的数据库操作Service
* @since 2023-03-10 13:59:57
*/
public interface IBNotifyTemplateService extends IService<BNotifyTemplate> {

    /**
     * @apiNote 根据场库编号获取消息推送模板列表
     */
    List<BNotifyTemplate> getBNotifyTemplateByParkNo();

    /**
     * @apiNote 编辑消息推送模板
     */
    boolean editById(BNotifyTemplateParam notifyTemplateParam);

    boolean testCarEntry(CarEntryNotificationData carEntryNotificationData);

    boolean testCarExit(CarExitNotificationData carExitNotificationData);

    boolean testCarPay(CarPayNotificationData carPayNotificationData);
}
