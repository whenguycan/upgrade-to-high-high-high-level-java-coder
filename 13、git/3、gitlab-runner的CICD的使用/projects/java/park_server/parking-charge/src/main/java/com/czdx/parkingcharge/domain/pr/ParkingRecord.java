package com.czdx.parkingcharge.domain.pr;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * description: 停车费
 * @author mingchenxu
 * @date 2023/3/6 11:13
 */
@Data
public class ParkingRecord {

    /**
     * 停车时长
     */
    private Long parkingDuration;

    /**
     * 原开始停车时间
     */
    private LocalDateTime originalEntryTime;

    /**
     * 开始停车时间
     */
    private LocalDateTime entryTime;

    /**
     * 原结束停车时间
     */
    private LocalDateTime originalExitTime;

    /**
     * 结束停车时间
     */
    private LocalDateTime exitTime;

    /**
     * 是否跨天
     */
    private boolean hasInterDay;

    /**
     * 停车费
     */
    private BigDecimal parkingFee = BigDecimal.ZERO;

    /**
     * 可用首时段
     */
    private boolean useFirstPeriod = true;

    /**
     * 可用免费时长
     */
    private boolean useFreeTime = true;

    /**
     * 需要时刻分割
     * 处理多时刻期间场景
     */
    private boolean needMTDivide = true;

    /**
     * 车场编码
     */
    private String parkNo;

    /**
     * 规则前缀
     */
    private String rulePrefix;

    public ParkingRecord() {
    }

    public ParkingRecord(String parkNo, String rulePrefix, boolean useFreeTime, LocalDateTime entryTime, LocalDateTime exitTime) {
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.parkNo = parkNo;
        this.rulePrefix = rulePrefix;
        this.useFreeTime = useFreeTime;
    }
}
