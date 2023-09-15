package com.ruoyi.project.parking.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationRecordParam {

    /**
     * 消息类型
     */
    private String sendTime;

    /**
     * 推送时间--开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 推送时间--结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 接收人
     */
    private String userPhone;
}
