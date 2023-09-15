package com.ruoyi.project.parking.domain.bo;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class NotificationRecordBO {
    /**
     * 消息类型
     */
    @Excel(name = "消息类型")
    private String sendTime;

    /**
     * 推送时间
     */
    @Excel(name = "推送时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date notifyTime;

    /**
     * 接收人
     */
    @Excel(name = "接收人")
    private String userPhone;

    /**
     * 消息内容
     */
    @Excel(name = "消息内容")
    private String comment;

    /**
     * 推送状态
     */
    @Excel(name = "推送状态")
    private String status;
}
