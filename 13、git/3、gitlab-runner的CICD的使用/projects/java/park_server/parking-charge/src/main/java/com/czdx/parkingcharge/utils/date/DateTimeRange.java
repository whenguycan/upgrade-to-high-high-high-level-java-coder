package com.czdx.parkingcharge.utils.date;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class DateTimeRange {

    /**
     * 开始时刻
     */
    private LocalDateTime begin;

    /**
     * 结束时刻
     */
    private LocalTime end;

}
