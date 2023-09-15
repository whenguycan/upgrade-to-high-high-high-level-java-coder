package com.czdx.parkingcharge.utils.date;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DateSplitUtilTest {

    @Test
    void splitByTimeRange() {
        LocalDateTime sTime;
        LocalDateTime eTime;
        List<DateRange> dateRanges;
        sTime = LocalDateTime.of(2023, 4, 3, 9, 0, 0);
        eTime = LocalDateTime.of(2023, 4, 4, 0, 0, 0);
        LocalTime[] timeRanges = new LocalTime[]{LocalTime.of(9, 0), LocalTime.of(18, 0)};

        dateRanges = DateSplitUtil.splitByTimeRange(sTime, eTime, timeRanges);
        Assert.isTrue(dateRanges.size() == 2);

        sTime = LocalDateTime.of(2023, 3, 29, 0, 0, 0);
        eTime = LocalDateTime.of(2023, 3, 29, 0, 1, 0);
        dateRanges = DateSplitUtil.splitByTimeRange(sTime, eTime, timeRanges);
        Assert.isTrue(dateRanges.size() == 1);


        sTime = LocalDateTime.of(2023, 3, 29, 0, 0, 0);
        eTime = LocalDateTime.of(2023, 3, 29, 9, 0, 0);
        dateRanges = DateSplitUtil.splitByTimeRange(sTime, eTime, timeRanges);
        Assert.isTrue(dateRanges.size() == 1);

        sTime = LocalDateTime.of(2023, 3, 29, 0, 0, 0);
        eTime = LocalDateTime.of(2023, 3, 29, 9, 1, 0);
        dateRanges = DateSplitUtil.splitByTimeRange(sTime, eTime, timeRanges);
        Assert.isTrue(dateRanges.size() == 2);

        sTime = LocalDateTime.of(2023, 3, 29, 0, 0, 0);
        eTime = LocalDateTime.of(2023, 3, 29, 18, 0, 0);
        dateRanges = DateSplitUtil.splitByTimeRange(sTime, eTime, timeRanges);
        Assert.isTrue(dateRanges.size() == 2);

        sTime = LocalDateTime.of(2023, 3, 29, 0, 0, 0);
        eTime = LocalDateTime.of(2023, 3, 29, 18, 1, 0);
        dateRanges = DateSplitUtil.splitByTimeRange(sTime, eTime, timeRanges);
        Assert.isTrue(dateRanges.size() == 3);

        sTime = LocalDateTime.of(2023, 3, 29, 0, 0, 0);
        eTime = LocalDateTime.of(2023, 3, 29, 23, 59, 0);
        dateRanges = DateSplitUtil.splitByTimeRange(sTime, eTime, timeRanges);
        Assert.isTrue(dateRanges.size() == 3);

        sTime = LocalDateTime.of(2023, 3, 29, 0, 0, 0);
        eTime = LocalDateTime.of(2023, 3, 30, 0, 0, 0);
        dateRanges = DateSplitUtil.splitByTimeRange(sTime, eTime, timeRanges);
        Assert.isTrue(dateRanges.size() == 3);

        sTime = LocalDateTime.of(2023, 3, 29, 0, 0, 0);
        eTime = LocalDateTime.of(2023, 3, 30, 0, 1, 0);
        dateRanges = DateSplitUtil.splitByTimeRange(sTime, eTime, timeRanges);
        Assert.isTrue(dateRanges.size() == 3);

        sTime = LocalDateTime.of(2023, 3, 29, 0, 0, 0);
        eTime = LocalDateTime.of(2023, 3, 30, 23, 59, 0);
        dateRanges = DateSplitUtil.splitByTimeRange(sTime, eTime, timeRanges);
        Assert.isTrue(dateRanges.size() == 5);

        sTime = LocalDateTime.of(2023, 3, 29, 9, 0, 0);
        eTime = LocalDateTime.of(2023, 3, 29, 18, 0, 0);
        dateRanges = DateSplitUtil.splitByTimeRange(sTime, eTime, timeRanges);
        Assert.isTrue(dateRanges.size() == 1);

        sTime = LocalDateTime.of(2023, 3, 29, 9, 0, 0);
        eTime = LocalDateTime.of(2023, 3, 29, 18, 1, 0);
        dateRanges = DateSplitUtil.splitByTimeRange(sTime, eTime, timeRanges);
        Assert.isTrue(dateRanges.size() == 2);

        sTime = LocalDateTime.of(2023, 3, 29, 18, 0, 0);
        eTime = LocalDateTime.of(2023, 3, 29, 18, 1, 0);
        dateRanges = DateSplitUtil.splitByTimeRange(sTime, eTime, timeRanges);
        Assert.isTrue(dateRanges.size() == 1);

        sTime = LocalDateTime.of(2023, 3, 29, 18, 0, 0);
        eTime = LocalDateTime.of(2023, 3, 29, 18, 0, 0);
        dateRanges = DateSplitUtil.splitByTimeRange(sTime, eTime, timeRanges);
        Assert.isTrue(dateRanges.size() == 0);


        sTime = LocalDateTime.of(2023, 3, 29, 18, 10, 0);
        eTime = LocalDateTime.of(2023, 3, 29, 18, 11, 0);
        dateRanges = DateSplitUtil.splitByTimeRange(sTime, eTime, timeRanges);
        Assert.isTrue(dateRanges.size() == 1);


        sTime = LocalDateTime.of(2023, 3, 29, 18, 10, 0);
        eTime = LocalDateTime.of(2023, 3, 30, 8, 11, 0);
        dateRanges = DateSplitUtil.splitByTimeRange(sTime, eTime, timeRanges);
        Assert.isTrue(dateRanges.size() == 1);

        sTime = LocalDateTime.of(2023, 3, 29, 18, 10, 0);
        eTime = LocalDateTime.of(2023, 3, 30, 9, 1, 0);
        dateRanges = DateSplitUtil.splitByTimeRange(sTime, eTime, timeRanges);
        Assert.isTrue(dateRanges.size() == 2);

    }
}
