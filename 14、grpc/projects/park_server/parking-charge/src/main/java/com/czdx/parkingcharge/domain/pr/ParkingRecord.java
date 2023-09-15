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
     * 开始停车时间
     */
    private LocalDateTime entryTime;

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
     * 计费计数器
     */
    private int count = 0;

    /**
     * 可用首时段
     */
    private boolean useFirstPeriod = true;

    /**
     * 可用免费时长
     */
    private boolean useFreeTime = true;

    /**
     * 规则前缀
     */
    private String rulePrefix;

}
