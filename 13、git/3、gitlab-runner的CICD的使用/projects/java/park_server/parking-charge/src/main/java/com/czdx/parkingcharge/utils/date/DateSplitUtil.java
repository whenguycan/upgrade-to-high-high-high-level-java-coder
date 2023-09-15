package com.czdx.parkingcharge.utils.date;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 切分时间，并且获取切分后的时间段集合
 */
public class DateSplitUtil {

    private static final DateTimeFormatter TIME_DTF = DateTimeFormatter.ofPattern("HH:mm");

    private DateSplitUtil() {
        throw new IllegalStateException("请通过静态方法调用！");
    }

    /**
     * 切分时间，并且获取切分后的时间段集合
     *
     * @param type      分割类型：按年、按月、按周、按日
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 切分后的时间段集合
     */
    public static List<DateRange> splitAndGetByType(int type, LocalDateTime startTime, LocalDateTime endTime) {
        if (type == 1) {
            // 按年切分
            return splitDateRangeByYear(startTime, endTime);
        } else if (type == 2) {
            // 按月切分
            return splitDateRangeByMonth(startTime, endTime);
        } else if (type == 3) {
            // 按周切分
            return splitDateRangeByWeek(startTime, endTime);
        } else if (type == 4) {
            //按日切分
            return splitDateRangeByDay(startTime, endTime);
        } else if (type == 5) {
            //一次性
            List<DateRange> dateRangeList = new ArrayList<>();
            DateRange dateRange = new DateRange();
            dateRange.setBegin(startTime);
            dateRange.setEnd(endTime);
            dateRangeList.add(dateRange);
            return dateRangeList;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 切分时间，并且获取某一轮的时间对象
     *
     * @param type      分割类型：按年、按月、按周、按日
     * @param turn      时间段（分割后的时间轮数，如将2021-10-01~2022-10-01，按月分割时，就能得到12轮时间段）
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 第turn段的时间对象
     */
    public static DateRange splitAndGetByTurn(int type, long turn, LocalDateTime startTime, LocalDateTime endTime) {
        //按年切分
        if (type == 1) {
            List<DateRange> dateRangeList = splitDateRangeByYear(startTime, endTime);
            return getRangeByTurn(dateRangeList, turn);
        } else if (type == 2) {
            // 按月切分
            List<DateRange> dateRangeList = splitDateRangeByMonth(startTime, endTime);
            return getRangeByTurn(dateRangeList, turn);
        } else if (type == 3) {
            // 按周切分
            List<DateRange> dateRangeList = splitDateRangeByWeek(startTime, endTime);
            return getRangeByTurn(dateRangeList, turn);
        } else if (type == 4) {
            //按日切分
            List<DateRange> dateRangeList = splitDateRangeByDay(startTime, endTime);
            return getRangeByTurn(dateRangeList, turn);
        } else if (type == 5) {
            //一次性
            DateRange dateRange = new DateRange();
            dateRange.setBegin(startTime);
            dateRange.setEnd(endTime);
            return dateRange;
        } else {
            return new DateRange();
        }
    }


    /**
     * 按年分割
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 分割后的时间段集合
     */
    private static List<DateRange> splitDateRangeByYear(LocalDateTime startTime, LocalDateTime endTime) {
        long seconds = startTime.until(endTime, ChronoUnit.SECONDS);
        if (seconds <= 0) {
            return new ArrayList<>();
        }
        //轮数
        long turnNum = 0;
        //分割的时间段集合，使用累加算法
        List<DateRange> dateList = new ArrayList<>();
        DateRange range = new DateRange();
        range.setBegin(startTime);
        while (true) {
            turnNum++;
            //年份相等，不再累加
            if (startTime.getYear() == endTime.getYear()) {
                range.setEnd(endTime);
                range.setTurnNum(turnNum);
                dateList.add(range);
                break;
            }
            //将时间调整为该年的第一天 0时 0分 0秒
            startTime = LocalDateTime.of(LocalDate.from(startTime.with(TemporalAdjusters.firstDayOfYear()).plusYears(1)), LocalTime.MIN);
            LocalDateTime tmpBegin = startTime;
            //计算出上一天的最后一秒
            LocalDateTime endDate = tmpBegin.minusSeconds(1);
            range.setEnd(endDate);
            range.setTurnNum(turnNum);
            dateList.add(range);
            //创建新的时间段
            range = new DateRange();
            range.setBegin(tmpBegin);
        }
        return dateList;
    }

    /**
     * 按月分割
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 分割后的时间段集合
     */
    private static List<DateRange> splitDateRangeByMonth(LocalDateTime startTime, LocalDateTime endTime) {

        long seconds = startTime.until(endTime, ChronoUnit.SECONDS);
        if (seconds <= 0) {
            return new ArrayList<>();
        }
        //轮数
        long turnNum = 0;
        //分割的时间段集合，使用累加算法
        List<DateRange> dateList = new ArrayList<>();
        DateRange range = new DateRange();
        range.setBegin(startTime);
        while (true) {
            turnNum++;
            startTime = startTime.plusMonths(1);
            //大于截止日期时，不再累加
            if (startTime.isAfter(endTime)) {
                range.setEnd(endTime);
                range.setTurnNum(turnNum);
                dateList.add(range);
                break;
            }
            //将时间调整为当前月的第一天的 0时 0分 0秒
            startTime = LocalDateTime.of(LocalDate.from(startTime.with(TemporalAdjusters.firstDayOfMonth())), LocalTime.MIN);
            LocalDateTime tmpBegin = startTime;
            //计算出上一天的最后一秒
            LocalDateTime endDate = tmpBegin.minusSeconds(1);
            range.setEnd(endDate);
            range.setTurnNum(turnNum);
            dateList.add(range);
            //创建新的时间段
            range = new DateRange();
            range.setBegin(tmpBegin);
        }
        return dateList;
    }

    /**
     * 按周分割
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 分割后的时间段集合
     */
    private static List<DateRange> splitDateRangeByWeek(LocalDateTime startTime, LocalDateTime endTime) {
        long seconds = startTime.until(endTime, ChronoUnit.SECONDS);
        if (seconds <= 0) {
            return new ArrayList<>();
        }
        //轮数
        long turnNum = 0;
        //分割的时间段集合，使用累加算法
        List<DateRange> dateList = new ArrayList<>();
        DateRange range = new DateRange();
        range.setBegin(startTime);
        while (true) {
            turnNum++;
            startTime = startTime.plusWeeks(1);
            //大于截止日期时，不再累加
            if (startTime.isAfter(endTime)) {
                range.setEnd(endTime);
                range.setTurnNum(turnNum);
                dateList.add(range);
                break;
            }
            //将时间调整为该周第一天的 0时 0分 0秒
            startTime = LocalDateTime.of(LocalDate.from(startTime.with(DayOfWeek.MONDAY)), LocalTime.MIN);
            LocalDateTime tmpBegin = startTime;
            //计算出上一天的最后一秒
            LocalDateTime endDate = tmpBegin.minusSeconds(1);
            range.setEnd(endDate);
            range.setTurnNum(turnNum);
            dateList.add(range);
            //创建新的时间段
            range = new DateRange();
            range.setBegin(tmpBegin);
        }
        return dateList;
    }

    /**
     * 按日分割
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 分割后的时间段集合
     */
    private static List<DateRange> splitDateRangeByDay(LocalDateTime startTime, LocalDateTime endTime) {
        long seconds = startTime.until(endTime, ChronoUnit.SECONDS);
        if (seconds < 0) {
            return new ArrayList<>();
        }
        //轮数
        long turnNum = 0;
        //分割的时间段集合，使用累加算法
        List<DateRange> dateList = new ArrayList<>();
        DateRange range = new DateRange();
        range.setBegin(startTime);
        while (true) {
            turnNum++;
            //将时间调整为该天的 0时 0分 0秒
            startTime = LocalDateTime.of(startTime.getYear(), startTime.getMonth(), startTime.getDayOfMonth(), 0, 0, 0);
            startTime = startTime.plusDays(1);
            //大于截止日期时，不再累加
            if (startTime.isAfter(endTime)) {
                range.setEnd(endTime);
                range.setTurnNum(turnNum);
                dateList.add(range);
                break;
            }
            LocalDateTime tmpBegin = startTime;
            //计算出上一天的最后一秒
//            LocalDateTime endDate = tmpBegin.minusSeconds(1);
            LocalDateTime endDate = tmpBegin;
            range.setEnd(endDate);
            range.setTurnNum(turnNum);
            dateList.add(range);
            //创建新的时间段
            range = new DateRange();
            range.setBegin(tmpBegin);
        }
        return dateList;
    }

    /**
     * 按时刻集合分割
     * 时刻形如 HH:mm
     * @param start 开始时间
     * @param end   结束时间
     * @return 分割后的时间段集合
     */
    public static List<DateRange> splitByTimeRange(LocalDateTime start, LocalDateTime end, LocalTime[] timeRanges) {
        LinkedHashSet<LocalDateTime> ldtSet = new LinkedHashSet<>();
        int i = 0;
        // 开始时间在第一个时刻之前，加入到返回期间中去
        if (start.isBefore(start.with(timeRanges[i]))) {
            ldtSet.add(start);
            // 如果结束时间也在第一个时刻之前，加入到返回期间中去，且直接结束方法
            if (!end.isAfter(start.with(timeRanges[i]))) {
                ldtSet.add(end);
                return handleTime2DateRange(ldtSet);
            }
        }
        LocalDateTime current = start;
        while (current.isBefore(end)) {
            // 时间段组合
            LocalDateTime startRange = current.with(timeRanges[i]);
            LocalDateTime endRange = current.with(timeRanges[(i+1) % timeRanges.length]);
            if (startRange.isAfter(endRange) || startRange.isEqual(endRange)) {
                // 如果开始时间段超过结束时间段，则说明跨天，需要结束时间段+1天
                endRange = endRange.plusDays(1);
            }
            // 当前时间在本时间段之后，则取开始时刻为当前时间的时刻
            if (current.isBefore(startRange)) {
                current = current.with(startRange.toLocalTime());
            } else if (current.isAfter(endRange)) {
                endRange = current;
            }
            LocalDateTime next = endRange;
            // 如果下一个时间在结束时间之后，则取结束时间为下一个时间
            if (next.isAfter(end)) {
                next = end;
            }
            ldtSet.add(current);
            current = next;
            if (current.isEqual(end)) {
                ldtSet.add(current);
            }
            i = (i+1) % timeRanges.length;
        }
        return handleTime2DateRange(ldtSet);
    }

    public static void main(String[] args) {
        LocalDateTime start = LocalDateTime.of(2023, 3, 23, 3, 0);
        LocalDateTime end = LocalDateTime.of(2023, 3, 25, 7, 0);
        LocalTime[] timeRanges = {
                LocalTime.of(4, 0),
        };
        List<DateRange> result = splitByTimeRange(start, end, timeRanges);
        System.out.println(result);
    }


    /**
     *
     * description: 处理时刻
     * @author mingchenxu
     * @date 2023/3/23 17:59
     * @param ldts 分割后时刻集合
     * @return java.util.List<com.czdx.parkingcharge.utils.date.DateTimeRange>
     */
    private static List<DateRange> handleTime2DateRange(Set<LocalDateTime> ldts) {
        // 根据时刻创建出计算DateRange对象
        DateRange range;
        List<DateRange> dRange = new ArrayList<>();
        LocalDateTime[] ldtArray = ldts.toArray(new LocalDateTime[0]);
        for (int i = 0; i < (ldtArray.length-1); i++) {
            range = new DateRange();
            range.setBegin(ldtArray[i]);
            range.setEnd(ldtArray[i+1]);
            range.setTurnNum(i+1L);
            dRange.add(range);
        }
        return dRange;
    }

    /**
     * 根据时间段（分割后的时间轮数，2021-10-01~2022-10-01，按月分隔时就有12轮时间段）获取时间范围
     *
     * @param dateRangeList 分隔后的时间段集合
     * @param turn          轮次，当前时间处于第几段
     * @return 时间对象
     */
    private static DateRange getRangeByTurn(List<DateRange> dateRangeList, long turn) {
        DateRange dateRange = new DateRange();
        for (DateRange d : dateRangeList) {
            if (d.getTurnNum() == turn) {
                dateRange = d;
            }
        }
        return dateRange;
    }

}
