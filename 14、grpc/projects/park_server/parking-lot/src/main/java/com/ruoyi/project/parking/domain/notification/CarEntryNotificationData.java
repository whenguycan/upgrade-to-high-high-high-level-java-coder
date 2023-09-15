package com.ruoyi.project.parking.domain.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 入场消息提醒数据
 */
@Data
public class CarEntryNotificationData {

    /**
     * 场库编号
     */
    private String parkNo;

    /**
     * 场库名称
     */
    private String parkName;

    /**
     * 用户车牌号
     */
    private String carNumber;

    /**
     * 入场时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryTime;

}
