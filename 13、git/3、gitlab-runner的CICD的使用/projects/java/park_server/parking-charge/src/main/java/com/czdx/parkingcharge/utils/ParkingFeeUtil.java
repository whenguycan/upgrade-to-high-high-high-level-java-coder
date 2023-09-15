package com.czdx.parkingcharge.utils;

import com.czdx.parkingcharge.common.enums.YesOrNo;
import com.czdx.parkingcharge.common.utils.RomeUtil;
import com.czdx.parkingcharge.domain.pr.ParkingRecord;
import com.czdx.parkingcharge.domain.pr.ParkingRuleEnums;
import com.czdx.parkingcharge.service.DroolsService;
import com.czdx.parkingcharge.utils.date.DateRange;
import com.czdx.parkingcharge.utils.date.DateSplitUtil;
import com.czdx.parkingcharge.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 停车费计算工具
 */
@Slf4j
public class ParkingFeeUtil {

    private static final DateTimeFormatter TIME_DTF = DateTimeFormatter.ofPattern("HH:mm");

    private ParkingFeeUtil() {}

    /**
     *
     * description: 处理秒进位
     * @author mingchenxu
     * @date 2023/3/9 15:22
     * @param dt 时间
     * @param secondCarry 秒配置
     * @return java.time.LocalDateTime
     */
    public static LocalDateTime handleSecondCarry(LocalDateTime dt, String secondCarry) {
        int second = dt.getSecond();
        if (second > 0) {
            // 秒进位
            if (ParkingRuleEnums.SecondCarry.UP.getValue().equals(secondCarry)) {
                dt = dt.plusMinutes(1);
            }
            dt = dt.withSecond(0);
        }
        return dt;
    }

    /**
     *
     * description: 计算时段停车费
     * @author mingchenxu
     * @date 2023/3/8 22:52
     * @param durations 计时分钟数
     * @param minUnit 最小单位时长
     * @param rate 费率
     * @param roundMode 计时舍入方式
     * @param limitPrice 限制价格
     * @return java.math.BigDecimal
     */
    public static BigDecimal chargeParkingFee(long durations, long minUnit, BigDecimal rate, String roundMode,
                                              BigDecimal limitPrice) {
        BigDecimal fee = chargeParkingFee(durations, minUnit, rate, roundMode);
        // 不为空，且大于0
        if (limitPrice != null && limitPrice.compareTo(BigDecimal.ZERO) > 0) {
            // 两者取最小的一个
            fee = fee.compareTo(limitPrice) > 0 ? limitPrice : fee;
        }
        return fee;
    }

    /**
     *
     * description: 计算时段停车费
     * @author mingchenxu
     * @date 2023/3/6 13:32
     * @param durations 计时分钟数
     * @param minUnit 最小单位时长
     * @param rate 费率
     * @param roundMode 计时舍入方式
     * @return java.math.BigDecimal
     */
    public static BigDecimal chargeParkingFee(long durations, long minUnit, BigDecimal rate, String roundMode) {
        RoundingMode way = ParkingRuleEnums.TimeRoundWay.getByValue(roundMode).getWay();
        // 根据舍入方式，取商
        BigDecimal quotient = BigDecimal.valueOf(durations).divide(BigDecimal.valueOf(minUnit), 0, way);
        // 乘以费率就是这期间的停车费
        return quotient.multiply(rate);
    }

    /**
     *
     * description: 计算时段停车费
     * @author mingchenxu
     * @date 2023/3/8 22:52
     * @param durations 计时分钟数
     * @param minUnit 最小单位时长
     * @param rate 费率
     * @param roundMode 计时舍入方式
     * @param limitPrice 限价
     * @param existFee 已有价格
     * @return java.math.BigDecimal
     */
    public static BigDecimal chargeParkingFee(long durations, long minUnit, BigDecimal rate, String roundMode,
                                              BigDecimal limitPrice,
                                              BigDecimal existFee) {
        // 存在区间限价
        boolean existLP = limitPrice.compareTo(BigDecimal.ZERO) > 0;
        // 已有价格已经大于等于期间限价，直接返回已有价格
        if (existLP && existFee.compareTo(limitPrice) >= 0) {
            return existFee;
        }
        // 计算停车费
        BigDecimal fee = chargeParkingFee(durations, minUnit, rate, roundMode, limitPrice);
        // 添加已存在的金额
        fee = fee.add(existFee);
        // 不为空，且大于0
        if (existLP) {
            // 两者取最小的一个
            fee = fee.compareTo(limitPrice) > 0 ? limitPrice : fee;
        }
        return fee;
    }

    /**
     *
     * description: 扣减免费时间后计费
     * @author mingchenxu
     * @date 2023/3/20 13:58
     * @param pr 停车记录
     * @param freeTime 免费时间
     * @return java.math.BigDecimal
     */
    public static BigDecimal substractFreeTime(ParkingRecord pr, long freeTime, BigDecimal ceilingPrice) {
        log.info("计费不包含免费时间，扣减免费停车时长：{}", freeTime);
        pr.setEntryTime(pr.getEntryTime().plusMinutes(freeTime));
        pr.setUseFreeTime(false);
        return divisionChargeFee(List.of(pr), ceilingPrice);
    }

    /**
     *
     * description: 分割最高限价计费
     * 最高限价默认分割，需要免费、首时段配置根据分割情况调整
     * @author mingchenxu
     * @date 2023/3/10 10:42
     * @param pr 停车记录
     * @param cpMinute 最高限价时间
     * @param cp 最高限价金额
     * @param fMNumber 免费时长次数
     * @param fDChargeWay 首时段计费方式
     * @return java.math.BigDecimal
     */
    public static BigDecimal divideCP2ChargeFee(ParkingRecord pr, long cpMinute, BigDecimal cp,
                                                String fMNumber, String fDChargeWay) {
        // 分割能否使用免费时长，首时段
        boolean dUseFreeTime = ParkingRuleEnums.FreeMinuteNumber.EVERY_DIVIDE_ONT_TIME.getValue().equals(fMNumber);
        boolean dUseFirstDuration = ParkingRuleEnums.FirstDurationChargeWay.EVERY_DIVIDE_ONT_TIME.getValue().equals(fDChargeWay);

        // 取商、余，计算出有几个限价时间段
        BigDecimal[] divAndRem = BigDecimal.valueOf(pr.getParkingDuration()).divideAndRemainder(BigDecimal.valueOf(cpMinute));
        BigDecimal overTimes = divAndRem[0];
        int remainder = divAndRem[1].intValue();
        log.info("最高限价隐藏分割，超过次数：{}，超出后剩余计费时间：{}", overTimes, remainder);
        // 计算超出次数的最高限价金额
        BigDecimal overTimesFee = cp.multiply(overTimes);

        // 剩余时间重新计费
        // 设置新的入场时间、停车时长，离场时间减去剩余分钟数，就是新的入场时间
        LocalDateTime newEnTime = pr.getExitTime().plusMinutes(-remainder);
        pr.setEntryTime(newEnTime);
        pr.setParkingDuration((long) remainder);
        // 免费时长、首时段根据配置情况设置
        pr.setUseFreeTime(dUseFreeTime);
        pr.setUseFirstPeriod(dUseFirstDuration);
        // 计算剩余计费时间金额
        BigDecimal remainderFee = divisionChargeFee(List.of(pr), cp);
        // 不能超过最高限价
        remainderFee = remainderFee.compareTo(cp) > 0 ? cp : remainderFee;
        return overTimesFee.add(remainderFee);
    }

    /**
     *
     * description: 停车记录拆分24小时，有最高限价
     * 分割为多个期间去算费，不存在总的期间限价，只有最高限价
     * @author mingchenxu
     * @date 2023/3/9 13:24
     * @param parkingRecord 停车记录
     * @param fMNumber 免费时长次数
     * @param fDChargeWay 首时段计费方式
     * @param ceilingPrice 最高限价
     * @return java.math.BigDecimal
     */
    public static BigDecimal divide24Hours2ChargeFee(ParkingRecord parkingRecord, String fMNumber,
                                                     String fDChargeWay, BigDecimal ceilingPrice) {
        return divideDuration2ChargeFee(parkingRecord, 1440, fMNumber, fDChargeWay, ceilingPrice);
    }

    /**
     *
     * description:
     * @author mingchenxu
     * @date 2023/3/9 13:35
     * @param parkingRecord 停车记录
     * @param duration 期间时长
     * @param fMNumber 免费时长次数
     * @param fDChargeWay 首时段计费方式
     * @param ceilingPrice 期间最高限价
     * @return java.math.BigDecimal
     */
    public static BigDecimal divideDuration2ChargeFee(ParkingRecord parkingRecord, long duration, String fMNumber,
                                                      String fDChargeWay, BigDecimal ceilingPrice) {
        List<ParkingRecord> dRecords = divideDuration(parkingRecord, duration, fMNumber, fDChargeWay);
        return divisionChargeFee(dRecords, ceilingPrice);
    }

    /**
     *
     * description: 停车记录隐藏拆分期间 - 有最高限价
     * 超过时长后，隐藏的分割，不会考虑免费时长次数、首时段计费方式
     * @author mingchenxu
     * @date 2023/3/15 10:42
     * @param pr 停车记录
     * @param duration 期间时长
     * @param ceilingPrice 期间最高限价
     * @return java.math.BigDecimal
     */
    public static BigDecimal divideHideDuration2ChargeFee(ParkingRecord pr, long duration, BigDecimal ceilingPrice) {
        // 隐藏的分割，不会考虑免费时长次数、首时段计费方式
        List<ParkingRecord> dRecords = divideDuration(pr, duration,
                ParkingRuleEnums.FreeMinuteNumber.ONE_CHARGE_ONE_TIME.getValue(),
                ParkingRuleEnums.FirstDurationChargeWay.ONE_CHARGE_ONE_TIME.getValue());
        return divisionChargeFee(dRecords, ceilingPrice);
    }

    /**
     *
     * description: 停车记录隐藏拆分期间(一次) - 有最高限价
     * 超过时长后，隐藏的分割，不会考虑免费时长次数、首时段计费方式
     * @author mingchenxu
     * @date 2023/3/15 10:42
     * @param parkingRecord 停车记录
     * @param duration 期间时长
     * @param ceilingPrice 期间最高限价（取时段限价与最高限价中较小的）
     * @return java.math.BigDecimal
     */
    public static BigDecimal divideHideDuration2ChargeFeeOnce(ParkingRecord parkingRecord, long duration, BigDecimal ceilingPrice) {
        // 隐藏的分割，不会考虑免费时长次数、首时段计费方式
        List<ParkingRecord> dRecords = divideDurationOnce(parkingRecord, duration,
                ParkingRuleEnums.FreeMinuteNumber.ONE_CHARGE_ONE_TIME.getValue(),
                ParkingRuleEnums.FirstDurationChargeWay.ONE_CHARGE_ONE_TIME.getValue());
        return divisionChargeFee(dRecords, ceilingPrice);
    }

    /**
     *
     * description: 分割块计费
     * @author mingchenxu
     * @date 2023/3/9 14:58
     * @param ceilingPrice 最高限价
     * @param dRecords 分割块
     * @return java.math.BigDecimal
     */
    private static BigDecimal divisionChargeFee(List<ParkingRecord> dRecords, BigDecimal ceilingPrice) {
        // 不为空，且大于0元，表示最高限价
        boolean hasCP = ceilingPrice != null && ceilingPrice.compareTo(BigDecimal.ZERO) > 0;
        // 每个拆分块都单独去计算费用
        DroolsService droolsService = SpringUtils.getBean(DroolsService.class);
        BigDecimal totalFee = BigDecimal.ZERO;
        for(ParkingRecord pr : dRecords) {
            BigDecimal fee = droolsService.chargeParkingFee(pr);
            totalFee = totalFee.add(fee);
            // 若已计算的模块得出的费用已经大于等于最高限价，则后续无需再计算
            if (hasCP && totalFee.compareTo(ceilingPrice) >= 0) {
                totalFee = ceilingPrice;
                break;
            }
        }
        return totalFee;
    }

    /**
     *
     * description: 停车记录拆分期间
     * @author mingchenxu
     * @date 2023/3/9 13:35
     * @param parkingRecord 停车记录
     * @param fMNumber 免费时长次数
     * @param fDChargeWay 首时段计费方式
     * @param ceilingPrice 最高限价
     * @return java.math.BigDecimal
     */
    public static BigDecimal divideZero2ChargeFee(ParkingRecord parkingRecord, String fMNumber,
                                                  String fDChargeWay, BigDecimal ceilingPrice) {
        List<ParkingRecord> dRecords = divideZeroDuration(parkingRecord, fMNumber, fDChargeWay);
        // 每个拆分块都单独去计算费用
        return divisionChargeFee(dRecords, ceilingPrice);
    }

    /**
     *
     * description: 零点分割时长期间
     * @author mingchenxu
     * @date 2023/3/9 14:52
     * @param parkingRecord 停车记录
     * @param fMNumber 免费时长次数
     * @param fDChargeWay 首时段计费方式
     * @return java.util.List<com.iwither.droolsspringboot.entity.ParkingRecord>
     */
    private static List<ParkingRecord> divideZeroDuration(ParkingRecord parkingRecord, String fMNumber, String fDChargeWay) {
        List<ParkingRecord> dRecords = new ArrayList<>();
        // 分割能否使用免费时长，首时段
        boolean dUseFreeTime = ParkingRuleEnums.FreeMinuteNumber.EVERY_DIVIDE_ONT_TIME.getValue().equals(fMNumber);
        boolean dUseFirstDuration = ParkingRuleEnums.FirstDurationChargeWay.EVERY_DIVIDE_ONT_TIME.getValue().equals(fDChargeWay);

        // 按天拆分时间段
        List<DateRange> dateRanges = DateSplitUtil.splitAndGetByType(4, parkingRecord.getEntryTime(), parkingRecord.getExitTime());
        for (int i = 0; i < dateRanges.size(); i++) {
            DateRange dr = dateRanges.get(i);
            ParkingRecord pr = new ParkingRecord();
            BeanUtils.copyProperties(parkingRecord, pr);
            // 重新设置开始时间、起始时间，重置停车时长
            pr.setEntryTime(dr.getBegin());
            pr.setExitTime(dr.getEnd());
            pr.setParkingDuration(null);
            pr.setParkingFee(BigDecimal.ZERO);
            // [第一个记录不管 - 第一个记录默认可以使用，可能存在最高时段分割，那边分割时会考虑该问题]
            if (i > 0) {
                pr.setUseFreeTime(dUseFreeTime);
                pr.setUseFirstPeriod(dUseFirstDuration);
            }
            dRecords.add(pr);
        }
        log.info("零点强制分割停车记录数：{}", dRecords.size());
        return dRecords;
    }

    /**
     *
     * description: 划分时刻计费
     * @author mingchenxu
     * @date 2023/3/23 17:21
     * @param parkingRecord 停车记录
     * @param cp 最高限价
     * @param times 时刻
     * @return java.math.BigDecimal
     */
    public static BigDecimal divideTime2ChargeFee(ParkingRecord parkingRecord, BigDecimal cp, String fMNumber,
                                                  String fDChargeWay, String ...times) {
        // 不为空，且大于0元，表示最高限价
        boolean hasCP = cp != null && cp.compareTo(BigDecimal.ZERO) > 0;
        // 分割能否使用免费时长，首时段
        boolean dUseFreeTime = ParkingRuleEnums.FreeMinuteNumber.EVERY_DIVIDE_ONT_TIME.getValue().equals(fMNumber);
        boolean dUseFirstDuration = ParkingRuleEnums.FirstDurationChargeWay.EVERY_DIVIDE_ONT_TIME.getValue().equals(fDChargeWay);

        // 提取出入场时间与分割时间段
        LocalDateTime enTime = parkingRecord.getEntryTime();
        LocalDateTime exTime = parkingRecord.getExitTime();
        LocalTime[] splitTimes = Arrays.stream(times).map(e -> LocalTime.parse(e, TIME_DTF)).toArray(LocalTime[]::new);
        List<DateRange> dateRanges = DateSplitUtil.splitByTimeRange(enTime, exTime, splitTimes);
        log.info("多时刻分割停车记录数：{}", dateRanges.size());

        // 遍历分割的时间段，构造停车记录，时间段都单独去计算费用
        BigDecimal totalFee = BigDecimal.ZERO;
        DroolsService droolsService = SpringUtils.getBean(DroolsService.class);
        // 下一个能否使用首时段，因为分割后，可能在上一个期间内没有使用首时段，会顺延到下一个期间
        boolean nextUseFP = true;
        for (int i = 0; i < dateRanges.size(); i++) {
            DateRange dateRange = dateRanges.get(i);
            // 新建多个划分出时间段的停车记录
            ParkingRecord pr = new ParkingRecord();
            BeanUtils.copyProperties(parkingRecord, pr);
            pr.setNeedMTDivide(false);
            pr.setEntryTime(dateRange.getBegin());
            pr.setExitTime(dateRange.getEnd());
            // 设置匹配规则前缀
            pr.setRulePrefix(pr.getRulePrefix() + "_" + getBelongTime(splitTimes, pr));
            // 设置能否使用免费时长，首时段
            if (i > 0) {
                pr.setUseFreeTime(dUseFreeTime);
                pr.setUseFirstPeriod(dUseFirstDuration);
                if (!dUseFirstDuration) {
                    // 如果该期间不能使用首时段，则使用顺延的首时段配置
                    pr.setUseFirstPeriod(nextUseFP);
                }
            }
            BigDecimal fee = droolsService.chargeParkingFee(pr);
            totalFee = totalFee.add(fee);
            // 判断是否超过最高限价
            if (hasCP && totalFee.compareTo(cp) >= 0) {
                totalFee = cp;
                break;
            }
            nextUseFP = pr.isUseFirstPeriod();
        }
        // 计费
        return totalFee;
    }

    /**
     *
     * description: 获取停车记录所属时刻区间
     * @author mingchenxu
     * @date 2023/3/27 16:13
     * @param splitTimes 分割的时刻
     * @param pr 停车记录
     * @return java.lang.String 区间罗马数字
     */
    private static String getBelongTime(LocalTime[] splitTimes, ParkingRecord pr) {
        // 默认在最后一个区间
        String rome = RomeUtil.intToRoman(1);
        LocalTime enTime = pr.getEntryTime().toLocalTime();
        for (int i = 0; i < splitTimes.length; i++) {
            int enSecond = enTime.toSecondOfDay();
            int startSecond = splitTimes[i].toSecondOfDay();
            int endSecond = splitTimes[(i+1) % splitTimes.length].toSecondOfDay();
            // 需要判断是否跨天
            boolean interDay = startSecond > endSecond;
            if (interDay) {
                // 在第二天，加一个一天的秒数
                if (enSecond >=0 && enSecond < endSecond) {
                    enSecond += 86400;
                }
                endSecond += 86400;
            }
            if (enSecond >= startSecond
                    && enSecond < endSecond) {
                rome = RomeUtil.intToRoman(i % splitTimes.length + 1);
                break;
            }
        }
        return rome;
    }



    /**
     * 拆分期间
     * @param parkingRecord 原停车记录
     * @param fMNumber 免费时长次数
     * @param fDChargeWay 首时段计费方式
     * @return
     */
    private static List<ParkingRecord> divideDuration(ParkingRecord parkingRecord, long duration, String fMNumber, String fDChargeWay) {
        List<ParkingRecord> dRecords = new ArrayList<>();
        // 分割能否使用免费时长，首时段
        boolean dUseFreeTime = ParkingRuleEnums.FreeMinuteNumber.EVERY_DIVIDE_ONT_TIME.getValue().equals(fMNumber);
        boolean dUseFirstDuration = ParkingRuleEnums.FirstDurationChargeWay.EVERY_DIVIDE_ONT_TIME.getValue().equals(fDChargeWay);

        // 拆分期间
        long parkingDuration = parkingRecord.getParkingDuration();
        BigDecimal[] division = BigDecimal.valueOf(parkingDuration).divideAndRemainder(BigDecimal.valueOf(duration));
        // 根据商，生成多个满区间的停车记录
        LocalDateTime tempTime = parkingRecord.getEntryTime();
        for (int i = 0; i < division[0].intValue(); i++) {
            ParkingRecord pr = new ParkingRecord();
            BeanUtils.copyProperties(parkingRecord, pr);
            // 设置停车时长、费用
            tempTime = setPREntryAndExitTime(pr, duration, tempTime);
            pr.setParkingFee(BigDecimal.ZERO);
            // 设置能否使用免费时长、首时段计费
            // [第一个记录不管 - 第一个记录默认可以使用，可能存在最高时段分割，那边分割时会考虑该问题]
            if (i > 0) {
                pr.setUseFreeTime(dUseFreeTime);
                pr.setUseFirstPeriod(dUseFirstDuration);
            }
            dRecords.add(pr);
        }
        // 最后一条为余数停车记录
        if (division[1].compareTo(BigDecimal.ZERO) > 0) {
            ParkingRecord pr = new ParkingRecord();
            BeanUtils.copyProperties(parkingRecord, pr);
            // 设置停车入场、离场时间，停车时长为余数
            long pd = division[1].longValue();
            setPREntryAndExitTime(pr, pd, tempTime);
            pr.setUseFreeTime(dUseFreeTime);
            pr.setUseFirstPeriod(dUseFirstDuration);
            dRecords.add(pr);
        }
        log.info("期间<{}>强制分割停车记录数：{}", duration, dRecords.size());
        return dRecords;
    }

    /**
     * 拆分期间，只拆一次
     * @param parkingRecord 原停车记录
     * @param fMNumber 免费时长次数
     * @param fDChargeWay 首时段计费方式
     * @return
     */
    private static List<ParkingRecord> divideDurationOnce(ParkingRecord parkingRecord, long duration, String fMNumber, String fDChargeWay) {
        List<ParkingRecord> dRecords = new ArrayList<>();
        // 分割能否使用免费时长，首时段
        boolean dUseFreeTime = ParkingRuleEnums.FreeMinuteNumber.EVERY_DIVIDE_ONT_TIME.getValue().equals(fMNumber);
        boolean dUseFirstDuration = ParkingRuleEnums.FirstDurationChargeWay.EVERY_DIVIDE_ONT_TIME.getValue().equals(fDChargeWay);

        // 拆分期间
        long parkingDuration = parkingRecord.getParkingDuration();
        long remainderDuration = parkingDuration - duration;
        // 临时时间，用于记录停车开始时间
        LocalDateTime tempTime = parkingRecord.getEntryTime();
        // 第一个期间
        ParkingRecord fpr = new ParkingRecord();
        BeanUtils.copyProperties(parkingRecord, fpr);
        // 设置停车时长、费用
        tempTime = setPREntryAndExitTime(fpr, duration, tempTime);
        fpr.setParkingFee(BigDecimal.ZERO);
        dRecords.add(fpr);
        // 剩余时间作为第二个期间
        if (remainderDuration > 0) {
            ParkingRecord pr = new ParkingRecord();
            BeanUtils.copyProperties(parkingRecord, pr);
            // 设置停车入场、离场时间，停车时长为余数
            setPREntryAndExitTime(pr, remainderDuration, tempTime);
            pr.setUseFreeTime(dUseFreeTime);
            pr.setUseFirstPeriod(dUseFirstDuration);
            dRecords.add(pr);
        }
        log.info("期间<{}>分割停车一次记录数：{}", duration, dRecords.size());
        return dRecords;
    }

    /**
     *
     * description: 设置停车记录的入场离场时间
     * @author mingchenxu
     * @date 2023/3/13 13:20
     * @param pr 停车记录
     * @param duration 期间
     * @param let 上一个记录的离场时间
     * @return java.time.LocalDateTime
     */
    private static LocalDateTime setPREntryAndExitTime(ParkingRecord pr, long duration, LocalDateTime let) {
        pr.setEntryTime(let);
        let = let.plusMinutes(duration);
        pr.setExitTime(let);
        pr.setParkingDuration(duration);
        return let;
    }

}
