package com.ruoyi.project.parking.controller;


import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.NotificationRecord;
import com.ruoyi.project.parking.domain.RegularCar;
import com.ruoyi.project.parking.domain.bo.NotificationRecordBO;
import com.ruoyi.project.parking.domain.bo.RegularCarBO;
import com.ruoyi.project.parking.domain.param.NotificationRecordParam;
import com.ruoyi.project.parking.domain.param.RegularCarParam;
import com.ruoyi.project.parking.domain.vo.RegularCarVO;
import com.ruoyi.project.parking.enums.NotificationRecordEnums;
import com.ruoyi.project.parking.service.INotificationRecordService;
import com.ruoyi.project.parking.service.IRegularCarService;
import com.ruoyi.project.parking.utils.DateLocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 消息推送记录表 前端控制器
 * </p>
 *
 * @author 琴声何来
 * @since 2023/3/10 17:14
 */
@RestController
@RequestMapping("/parking/notificationRecord")
public class NotificationRecordController extends BaseController {


    @Autowired
    private INotificationRecordService notificationRecordService;

    /**
     * @apiNote 获取消息推送记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(NotificationRecordParam notificationRecordParam) {
        startPage();
        List<NotificationRecord> list = notificationRecordService.listNotificationRecords(notificationRecordParam);
        return getDataTable(list);
    }

    /**
     * @apiNote 消息推送记录列表导出
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, NotificationRecordParam notificationRecordParam) {
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("消息推送记录列表" + new Date().getTime(), StandardCharsets.UTF_8) + ".xlsx");
        List<NotificationRecord> list = notificationRecordService.listNotificationRecords(notificationRecordParam);
        List<NotificationRecordBO> exportList = new ArrayList<>();
        list.forEach(record -> {
            NotificationRecordBO notificationRecordBO = new NotificationRecordBO();
            BeanUtils.copyBeanProp(notificationRecordBO, record);
            // excel不识别LocalDate格式，需要转成Date格式返回
            if (null != record.getNotifyTime()) {
                notificationRecordBO.setNotifyTime(DateLocalDateUtils.localDateTimeToDate(record.getNotifyTime()));
            }
            //转换消息类型
            notificationRecordBO.setSendTime(convertSendTime(notificationRecordBO.getSendTime()));
            notificationRecordBO.setStatus(convertStatus(notificationRecordBO.getStatus()));
            exportList.add(notificationRecordBO);
        });
        ExcelUtil<NotificationRecordBO> util = new ExcelUtil<>(NotificationRecordBO.class);
        util.exportExcel(response, exportList, "消息推送记录列表");
    }

    private String convertSendTime(String sendTime) {
        return Objects.requireNonNull(NotificationRecordEnums.SendTime.getByValue(sendTime)).getDesc();
    }

    private String convertStatus(String status) {
        return Objects.requireNonNull(NotificationRecordEnums.Status.getByValue(status)).getDesc();
    }
}
