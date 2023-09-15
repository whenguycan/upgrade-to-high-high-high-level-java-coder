package com.ruoyi.common.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间工具类
 *
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }


    /*
     * 字符串拼接转日期
     * @date: 2020年08月20日 0020 15:51
     * @param: date
     * @param: format
     * @return: java.time.LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(String dateTime, String format) {
        if (StringUtils.isEmpty(dateTime)) {
            return null;
        }
        if (StringUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        LocalDateTime ldt = LocalDateTime.parse(dateTime, df);
        return ldt;
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 计算两个时间差
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     */
    public static String getDatePoor(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return "";
        }
        Duration duration = Duration.between(startDate, endDate);
        long day = duration.toDays();
        long hour = duration.toHours() - day * 24;
        long min = duration.toMinutes() - (day * 24 + hour) * 60;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    // region 时间类型格式 转 时间戳字符串

    /**
     * 日期转换 Date => 时间戳字符串
     *
     * @param date 日期
     */
    public static String toMilliString(Date date) {
        return String.valueOf(date.getTime());
    }

    /**
     * 日期转换 LocalDateTime => 时间戳字符串
     *
     * @param date 日期
     */
    public static String toMilliString(LocalDateTime date) {
        return String.valueOf(date.toInstant(ZoneOffset.of("+8")).toEpochMilli());
    }

    /**
     * 标准时间字符串  => 时间戳字符串
     *
     * @param dateString 标准时间字符串 yyyy-MM-dd HH:mm:ss
     */
    public static String toMilliString(String dateString) {
        Date date = parseDate(dateString);
        if (date == null) {
            return null;
        }
        return toMilliString(date);
    }
    // endregion

    // region 其他 转 LocalDateTime

    /**
     * 日期转换 Date => LocalDateTime
     *
     * @param date Date 类型时间
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return date.toInstant().atZone(zoneId).toLocalDateTime();
    }

    /**
     * 日期转换 标准时间字符串 => LocalDateTime
     *
     * @param dateString 标准时间字符串 yyyy-MM-dd HH:mm:ss
     */
    public static LocalDateTime toLocalDateTime(String dateString) {
        return toLocalDateTime(dateString, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 时间戳字符串 => 时间类型 LocalDateTime
     *
     * @param dateString 时间戳字符串
     */
    public static LocalDateTime parseMilliStringToLocalDateTime(String dateString) {
        if (StringUtils.isEmpty(dateString)) {
            return null;
        }
        if (13 != dateString.length()) {
            return null;
        }
        Instant instant;
        try {
            instant = Instant.ofEpochMilli(Long.parseLong(dateString));
        } catch (NumberFormatException e) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    // endregion

    // region LocalDateTime 格式化字符串

    /**
     * LocalDateTime 格式化输出
     *
     * @param date 日期
     */
    public static String format(LocalDateTime date, String pattern) {
        if (date == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * LocalDateTime 格式化输出
     *
     * @param date 日期
     */
    public static String format(LocalDateTime date) {
        if (date == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
    }

    // endregion
}
