package com.czdx.parkingnotification.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提醒数据
 */
@Data
public class CarExitNotificationData {

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

    /**
     * 离场时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime exitTime;

    /**
     * 停车费用
     */
    private BigDecimal amount;

}
