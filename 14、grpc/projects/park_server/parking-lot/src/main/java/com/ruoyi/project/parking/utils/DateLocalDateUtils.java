package com.ruoyi.project.parking.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateLocalDateUtils {

    /**
     * @param localDate 入参
     * @return java.util.Date 出参
     * @apiNote LocalDate转Date格式
     * @author 琴声何来
     * @since 2023/2/28 15:05
     */
    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * @param date 入参
     * @return java.time.LocalDate 出参
     * @apiNote Date转LocalDate格式
     * @author 琴声何来
     * @since 2023/2/28 15:07
     */
    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * @param localDateTime 入参
     * @return java.util.Date 出参
     * @apiNote LocalDateTime转Date格式
     * @author 琴声何来
     * @since 2023/2/28 16:05
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * @param date 入参
     * @return java.time.LocalDateTime 出参
     * @apiNote Date转LocalDateTime格式
     * @author 琴声何来
     * @since 2023/2/28 16:05
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
