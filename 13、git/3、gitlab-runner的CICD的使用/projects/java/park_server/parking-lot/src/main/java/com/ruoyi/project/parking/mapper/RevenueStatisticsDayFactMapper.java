package com.ruoyi.project.parking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.parking.domain.RevenueStatisticsDayFact;
import com.ruoyi.project.parking.domain.vo.RevenueStatisticsDayFactVO;
import com.ruoyi.project.parking.domain.vo.RevenueStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 首页收入统计事实Mapper接口
 *
 * @author fangch
 * @date 2023-03-28
 */
@Mapper
public interface RevenueStatisticsDayFactMapper extends BaseMapper<RevenueStatisticsDayFact> {
    /**
     * 查询首页收入统计事实
     *
     * @param id 首页收入统计事实主键
     * @return 首页收入统计事实
     */
    public RevenueStatisticsDayFact selectRevenueStatisticsDayFactById(Integer id);

    /**
     * 查询首页收入统计事实列表
     *
     * @param revenueStatisticsDayFact 首页收入统计事实
     * @return 首页收入统计事实集合
     */
    public List<RevenueStatisticsDayFact> selectRevenueStatisticsDayFactList(RevenueStatisticsDayFact revenueStatisticsDayFact);

    /**
     * 新增首页收入统计事实
     *
     * @param revenueStatisticsDayFact 首页收入统计事实
     * @return 结果
     */
    public int insertRevenueStatisticsDayFact(RevenueStatisticsDayFact revenueStatisticsDayFact);

    /**
     * 修改首页收入统计事实
     *
     * @param revenueStatisticsDayFact 首页收入统计事实
     * @return 结果
     */
    public int updateRevenueStatisticsDayFact(RevenueStatisticsDayFact revenueStatisticsDayFact);

    /**
     * 删除首页收入统计事实
     *
     * @param id 首页收入统计事实主键
     * @return 结果
     */
    public int deleteRevenueStatisticsDayFactById(Integer id);

    /**
     * 批量删除首页收入统计事实
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRevenueStatisticsDayFactByIds(Integer[] ids);

    /**
     * 查询同维度的首页收入统计事实
     *
     * @param revenueStatisticsDayFact
     * @return
     */
    RevenueStatisticsDayFact selectRevenueStatisticsDayFact(RevenueStatisticsDayFact revenueStatisticsDayFact);

    /**
     * 查询大屏收入统计第一行
     *
     * @return 收入统计第一行
     */
    RevenueStatisticsVO getRevenueStatisticsFirst(@Param("parkNo") String parkNo, @Param("year") Integer year, @Param("month") Integer month, @Param("yesterday") String yesterday, @Param("today") String today);

    /**
     * 查询大屏收入统计第二行
     *
     * @return 收入统计第二行
     */
    RevenueStatisticsDayFactVO getRevenueStatisticsSecond(@Param("parkNo") String parkNo, @Param("year") Integer year, @Param("month") Integer month
            , @Param("week") Integer week, @Param("day") String day);

    /**
     * 查询本年收入
     */
    BigDecimal sumAmountOfYear(LocalDate startOfYear, LocalDate endOfYear, List<String> parkNos);

    /**
     * 查询当日收入
     */
    BigDecimal sumAmountOfDay(LocalDate now, List<String> parkNos);

    /**
     * 岗亭数据展示
     *
     * @param now
     * @param parkNo
     * @return
     */
    RevenueStatisticsDayFact sumDisConsumptionAmountOfDay(LocalDate now,  String parkNo);

}
