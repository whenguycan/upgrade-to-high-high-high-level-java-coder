package com.czdx.parkingnotification.controller;

import com.alibaba.fastjson2.JSONObject;
import com.czdx.parkingnotification.domain.NotificationRecord;
import com.czdx.parkingnotification.enums.NotificationRecordEnums;
import com.czdx.parkingnotification.service.INotificationRecordService;
import com.czdx.parkingnotification.utils.CZZHTCUtils;
import com.czdx.parkingnotification.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/wechat/message")
public class WechatMessageTemplateController {

    @Autowired
    INotificationRecordService notificationRecordService;

    @PostMapping("/verify")
    public String verify(@RequestBody String data) {
        log.info("收到微信公众号推送消息结果，data：{}", data);
        try {
            //xml数据
            JSONObject xml = JsonUtil.xmlToJson(data).getJSONObject("xml");
            log.info("转json格式数据：{}", xml);
            //更新微信公众号推送状态
            NotificationRecord notificationRecord = notificationRecordService.getByMessageId(xml.getLong("MsgID"));
            if (notificationRecord != null) {
                if ("success".equals(xml.getString("Status"))) {
                    notificationRecord.setStatus(NotificationRecordEnums.Status.SUCCESS.getValue());
                } else if ("failed:user block".equals(xml.getString("Status"))) {
                    notificationRecord.setStatus(NotificationRecordEnums.Status.FAIL_USER_BLOCK.getValue());
                } else if ("failed: system failed".equals(xml.getString("Status"))) {
                    notificationRecord.setStatus(NotificationRecordEnums.Status.FAIL_SYSTEM_FAILED.getValue());
                }
                notificationRecord.setUpdateBy(notificationRecord.getCreateBy());
                notificationRecord.setUpdateTime(LocalDateTime.now());
                notificationRecordService.updateById(notificationRecord);
            } else {
                log.error("找不到MsgId为{}的消息，更新消息推送状态失败。", xml.getLong("MsgID"));
            }
        } catch (DocumentException e) {
            log.error("xml转换失败，请检查公众号推送数据", e);
        }
        return "success";
    }

}
