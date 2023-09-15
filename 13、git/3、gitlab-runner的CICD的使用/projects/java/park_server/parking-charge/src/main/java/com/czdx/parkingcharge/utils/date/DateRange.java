package com.czdx.parkingcharge.utils.date;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DateRange {

    /**
     * 开始时间
     */
    private LocalDateTime begin;

    /**
     * 结束时间
     */
    private LocalDateTime end;

    /**
     * 当前时间的第几轮（段）
     */
    private Long turnNum;

}
