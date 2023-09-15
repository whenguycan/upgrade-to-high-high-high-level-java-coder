package com.ruoyi.project.parking.service.impl;

import com.ruoyi.project.parking.domain.DurationStatisticDayFact;
import com.ruoyi.project.parking.domain.vo.DurationStatisticFactVO;
import com.ruoyi.project.parking.domain.vo.ParkingLotNumStatisticsVO;
import com.ruoyi.project.parking.mapper.DurationStatisticDayFactMapper;
import com.ruoyi.project.parking.mapper.HomePageMapper;
import com.ruoyi.project.parking.service.IDurationStatisticDayFactService;
import com.ruoyi.project.system.domain.SysDept;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 首页时长统计事实Service业务层处理
 * 
 * @author fangch
 * @date 2023-03-20
 */
@Service
public class DurationStatisticDayFactServiceImpl implements IDurationStatisticDayFactService 
{
    @Autowired
    private DurationStatisticDayFactMapper durationStatisticDayFactMapper;

    @Autowired
    private HomePageMapper homePageMapper;

    /**
     * 查询首页时长统计事实
     * 
     * @param id 首页时长统计事实主键
     * @return 首页时长统计事实
     */
    @Override
    public DurationStatisticDayFact selectDurationStatisticDayFactById(Integer id)
    {
        return durationStatisticDayFactMapper.selectDurationStatisticDayFactById(id);
    }

    /**
     * 查询首页时长统计事实列表
     * 
     * @param durationStatisticDayFact 首页时长统计事实
     * @return 首页时长统计事实
     */
    @Override
    public List<DurationStatisticDayFact> selectDurationStatisticDayFactList(DurationStatisticDayFact durationStatisticDayFact)
    {
        return durationStatisticDayFactMapper.selectDurationStatisticDayFactList(durationStatisticDayFact);
    }

    /**
     * 新增首页时长统计事实
     * 
     * @param durationStatisticDayFact 首页时长统计事实
     * @return 结果
     */
    @Override
    public int insertDurationStatisticDayFact(DurationStatisticDayFact durationStatisticDayFact)
    {
        durationStatisticDayFact.setCreateTime(LocalDateTime.now());
        return durationStatisticDayFactMapper.insertDurationStatisticDayFact(durationStatisticDayFact);
    }

    /**
     * 修改首页时长统计事实
     * 
     * @param durationStatisticDayFact 首页时长统计事实
     * @return 结果
     */
    @Override
    public int updateDurationStatisticDayFact(DurationStatisticDayFact durationStatisticDayFact)
    {
        durationStatisticDayFact.setUpdateTime(LocalDateTime.now());
        return durationStatisticDayFactMapper.updateDurationStatisticDayFact(durationStatisticDayFact);
    }

    /**
     * 批量删除首页时长统计事实
     * 
     * @param ids 需要删除的首页时长统计事实主键
     * @return 结果
     */
    @Override
    public int deleteDurationStatisticDayFactByIds(Integer[] ids)
    {
        return durationStatisticDayFactMapper.deleteDurationStatisticDayFactByIds(ids);
    }

    /**
     * 删除首页时长统计事实信息
     * 
     * @param id 首页时长统计事实主键
     * @return 结果
     */
    @Override
    public int deleteDurationStatisticDayFactById(Integer id)
    {
        return durationStatisticDayFactMapper.deleteDurationStatisticDayFactById(id);
    }

    /**
     * 分析首页时长统计事实
     */
    @Override
    public void analyseDurationStatisticDayFact(String userId) {
        // 查询所有车场id和编号
        List<SysDept> parks = homePageMapper.getAllParkIds();
        for (SysDept park : parks) {
            // 查询所有归属子车场编号
            List<String> parkNos = homePageMapper.getChildParkNos(park.getDeptId());
            // 分析首页付费类型事实
            String day = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            List<DurationStatisticDayFact> durationStatisticDayFactList = durationStatisticDayFactMapper.analyseDurationStatisticDayFact(parkNos, day);
            if (CollectionUtils.isNotEmpty(durationStatisticDayFactList)) {
                BigDecimal sum = BigDecimal.ZERO;
                for (DurationStatisticDayFact item : durationStatisticDayFactList) {
                    sum = sum.add(BigDecimal.valueOf(item.getCount()));
                }
                BigDecimal finalSum = sum;
                durationStatisticDayFactList.forEach(item -> {
                    // 设置车场编号、日期、占比
                    item.setParkNo(park.getParkNo());
                    item.setDay(day);
                    item.setRatio(finalSum.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(item.getCount()).multiply(new BigDecimal(100)).divide(finalSum, 2, RoundingMode.HALF_UP));
                    // 检查同维度的分析数据是否已存在，存在更新，不存在新增
                    DurationStatisticDayFact existFact = durationStatisticDayFactMapper.selectDurationStatisticDayFact(item);
                    if (null == existFact) {
                        item.setCreateBy(userId);
                        item.setCreateTime(LocalDateTime.now());
                        durationStatisticDayFactMapper.insertDurationStatisticDayFact(item);
                        // 更新一下昨天的统计，防止丢数据
                        String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        List<DurationStatisticDayFact> yesterdayList = durationStatisticDayFactMapper.analyseDurationStatisticDayFact(parkNos, yesterday);
                        if (CollectionUtils.isNotEmpty(yesterdayList)) {
                            BigDecimal yesterdaySum = BigDecimal.ZERO;
                            for (DurationStatisticDayFact yesterdayItem : yesterdayList) {
                                yesterdaySum = yesterdaySum.add(BigDecimal.valueOf(yesterdayItem.getCount()));
                            }
                            BigDecimal finalYesterdaySum = yesterdaySum;
                            yesterdayList.forEach(yesterdayItem -> {
                                // 设置车场编号、日期、占比
                                yesterdayItem.setParkNo(park.getParkNo());
                                yesterdayItem.setDay(yesterday);
                                yesterdayItem.setRatio(finalYesterdaySum.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(yesterdayItem.getCount()).multiply(new BigDecimal(100)).divide(finalYesterdaySum, 2, RoundingMode.HALF_UP));
                                // 检查同维度的分析数据是否已存在，存在更新
                                DurationStatisticDayFact existYesterdayFact = durationStatisticDayFactMapper.selectDurationStatisticDayFact(yesterdayItem);
                                if (null != existYesterdayFact) {
                                    existYesterdayFact.setCount(yesterdayItem.getCount());
                                    existYesterdayFact.setRatio(yesterdayItem.getRatio());
                                    existYesterdayFact.setUpdateBy(userId);
                                    existYesterdayFact.setUpdateTime(LocalDateTime.now());
                                    durationStatisticDayFactMapper.updateDurationStatisticDayFact(existYesterdayFact);
                                }
                            });
                        }
                    } else {
                        existFact.setCount(item.getCount());
                        existFact.setRatio(item.getRatio());
                        existFact.setUpdateBy(userId);
                        existFact.setUpdateTime(LocalDateTime.now());
                        durationStatisticDayFactMapper.updateDurationStatisticDayFact(existFact);
                    }
                });
            }
        }
    }

    /**
     * 查询首页时长统计事实
     *
     * @param durationStatisticDayFact 首页时长统计事实
     * @return 首页时长统计事实集合
     */
    @Override
    public List<DurationStatisticFactVO> getDurationStatisticDayFact(DurationStatisticDayFact durationStatisticDayFact) {
        return durationStatisticDayFactMapper.getDurationStatisticDayFact(durationStatisticDayFact);
    }

    /**
     * 查询大屏停车时长统计
     *
     * @param parkNo 车场编号
     * @return 首页时长统计事实集合
     */
    @Override
    public Map<String, Object> getDurationStatistic(String parkNo) {
        Map<String, Object> result = new HashMap<>();
        LocalDate now = LocalDate.now();
        String day = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        result.put("日", durationStatisticDayFactMapper.getScreenDurationStatisticDayFact(parkNo, null, null, null, day));
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        result.put("周", durationStatisticDayFactMapper.getScreenDurationStatisticDayFact(parkNo, now.getYear(), null, now.get(weekFields.weekOfWeekBasedYear()), null));
        result.put("月", durationStatisticDayFactMapper.getScreenDurationStatisticDayFact(parkNo, now.getYear(), now.getMonthValue(), null, null));
        return result;
    }

    /**
     * 查询车场数据统计
     *
     * @param parkNo 车场编号
     * @return 车场数据统计
     */
    @Override
    public ParkingLotNumStatisticsVO getParkingLotNumStatistics(String parkNo) {
        // 查询所有归属子车场编号
        List<String> parkNos = homePageMapper.getChildParkNosByNo(parkNo);
        if (CollectionUtils.isEmpty(parkNos)) {
            return null;
        }
        ParkingLotNumStatisticsVO result = durationStatisticDayFactMapper.getParkingLotNumStatistics(parkNos);
        result.setParkingLotNum(parkNos.size());
        return result;
    }
}
