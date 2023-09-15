package com.ruoyi.project.parking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.parking.domain.RevenueStatisticsDayFact;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 首页收入统计事实Service接口
 *
 * @author fangch
 * @date 2023-03-28
 */
public interface IRevenueStatisticsDayFactService extends IService<RevenueStatisticsDayFact>
{
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
     * 批量删除首页收入统计事实
     *
     * @param ids 需要删除的首页收入统计事实主键集合
     * @return 结果
     */
    public int deleteRevenueStatisticsDayFactByIds(Integer[] ids);

    /**
     * 删除首页收入统计事实信息
     *
     * @param id 首页收入统计事实主键
     * @return 结果
     */
    public int deleteRevenueStatisticsDayFactById(Integer id);

    /**
     * 分析首页收入统计事实
     *
     */
    void analyseRevenueStatisticsDayFact(String userId);

    /**
     * 查询大屏收入统计
     *
     * @param revenueStatisticsDayFact
     * @return
     */
    Map<String, Object> getRevenueStatistics(RevenueStatisticsDayFact revenueStatisticsDayFact);

    /**
     *
     * @param now
     * @param parkNo
     * @return
     */
    RevenueStatisticsDayFact sumDisConsumptionAmountOfDay(LocalDate now, String parkNo);

    /**
     * 查询本年收入
     */
    BigDecimal sumAmountOfYear(LocalDate startOfYear, LocalDate endOfYear, List<String> parkNos);


    /**
     * 查询当日收入
     */
    BigDecimal sumAmountOfDay(LocalDate now, List<String> parkNos);

}
