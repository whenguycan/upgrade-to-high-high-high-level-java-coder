package com.ruoyi.project.parking.mapper;

import com.ruoyi.project.parking.domain.DurationStatisticDayFact;
import com.ruoyi.project.parking.domain.vo.DurationStatisticFactVO;
import com.ruoyi.project.parking.domain.vo.DurationStatisticVO;
import com.ruoyi.project.parking.domain.vo.ParkingLotNumStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 首页时长统计事实Mapper接口
 * 
 * @author fangch
 * @date 2023-03-20
 */
@Mapper
public interface DurationStatisticDayFactMapper 
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
     * 删除首页时长统计事实
     * 
     * @param id 首页时长统计事实主键
     * @return 结果
     */
    public int deleteDurationStatisticDayFactById(Integer id);

    /**
     * 批量删除首页时长统计事实
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDurationStatisticDayFactByIds(Integer[] ids);

    /**
     * 分析首页时长统计事实
     *
     * @return 首页时长统计事实集合
     */
    List<DurationStatisticDayFact> analyseDurationStatisticDayFact(@Param("parkNos") List<String> parkNos, @Param("day") String day);

    /**
     * 查询同维度的首页时长统计事实
     *
     * @param durationStatisticDayFact
     * @return
     */
    DurationStatisticDayFact selectDurationStatisticDayFact(DurationStatisticDayFact durationStatisticDayFact);

    /**
     * 查询首页时长统计事实
     *
     * @param durationStatisticDayFact 首页时长统计事实
     * @return 首页时长统计事实集合
     */
    List<DurationStatisticFactVO> getDurationStatisticDayFact(DurationStatisticDayFact durationStatisticDayFact);

    /**
     * 查询大屏时长统计
     *
     * @return 首页时长统计事实集合
     */
    List<DurationStatisticVO> getScreenDurationStatisticDayFact(@Param("parkNo") String parkNo, @Param("year") Integer year, @Param("month") Integer month
            , @Param("week") Integer week, @Param("day") String day);

    /**
     * 查询车场数据统计
     *
     * @param parkNos 子节点车场编号
     * @return 车场数据统计
     */
    ParkingLotNumStatisticsVO getParkingLotNumStatistics(List<String> parkNos);
}
