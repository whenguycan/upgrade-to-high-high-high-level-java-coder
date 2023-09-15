package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.DurationStatisticDayFact;
import com.ruoyi.project.parking.domain.vo.DurationStatisticFactVO;
import com.ruoyi.project.parking.domain.vo.ParkingLotNumStatisticsVO;

import java.util.List;
import java.util.Map;

/**
 * 首页时长统计事实Service接口
 * 
 * @author fangch
 * @date 2023-03-20
 */
public interface IDurationStatisticDayFactService 
{
    /**
     * 查询首页时长统计事实
     * 
     * @param id 首页时长统计事实主键
     * @return 首页时长统计事实
     */
    public DurationStatisticDayFact selectDurationStatisticDayFactById(Integer id);

    /**
     * 查询首页时长统计事实列表
     * 
     * @param durationStatisticDayFact 首页时长统计事实
     * @return 首页时长统计事实集合
     */
    public List<DurationStatisticDayFact> selectDurationStatisticDayFactList(DurationStatisticDayFact durationStatisticDayFact);

    /**
     * 新增首页时长统计事实
     * 
     * @param durationStatisticDayFact 首页时长统计事实
     * @return 结果
     */
    public int insertDurationStatisticDayFact(DurationStatisticDayFact durationStatisticDayFact);

    /**
     * 修改首页时长统计事实
     * 
     * @param durationStatisticDayFact 首页时长统计事实
     * @return 结果
     */
    public int updateDurationStatisticDayFact(DurationStatisticDayFact durationStatisticDayFact);

    /**
     * 批量删除首页时长统计事实
     * 
     * @param ids 需要删除的首页时长统计事实主键集合
     * @return 结果
     */
    public int deleteDurationStatisticDayFactByIds(Integer[] ids);

    /**
     * 删除首页时长统计事实信息
     * 
     * @param id 首页时长统计事实主键
     * @return 结果
     */
    public int deleteDurationStatisticDayFactById(Integer id);

    /**
     * 分析首页时长统计事实
     */
    void analyseDurationStatisticDayFact(String userId);

    /**
     * 查询首页时长统计事实
     *
     * @param durationStatisticDayFact 首页时长统计事实
     * @return 首页时长统计事实集合
     */
    List<DurationStatisticFactVO> getDurationStatisticDayFact(DurationStatisticDayFact durationStatisticDayFact);

    /**
     * 查询大屏停车时长统计
     *
     * @param parkNo 车场编号
     * @return 首页时长统计事实集合
     */
    Map<String, Object> getDurationStatistic(String parkNo);

    /**
     * 查询车场数据统计
     *
     * @param parkNo 车场编号
     * @return 车场数据统计
     */
    ParkingLotNumStatisticsVO getParkingLotNumStatistics(String parkNo);
}
