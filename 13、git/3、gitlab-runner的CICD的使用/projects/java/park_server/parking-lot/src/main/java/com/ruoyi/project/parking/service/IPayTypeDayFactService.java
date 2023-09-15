package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.PayTypeDayFact;
import com.ruoyi.project.parking.domain.vo.PayStatisticFactVO;
import com.ruoyi.project.parking.domain.vo.StatisticsSheetVO;

import java.util.List;

/**
 * 首页付费类型分析事实Service接口
 * 
 * @author fangch
 * @date 2023-03-20
 */
public interface IPayTypeDayFactService 
{
    /**
     * 查询首页付费类型分析事实
     * 
     * @param id 首页付费类型分析事实主键
     * @return 首页付费类型分析事实
     */
    public PayTypeDayFact selectPayTypeDayFactById(Integer id);

    /**
     * 查询首页付费类型分析事实列表
     * 
     * @param payTypeDayFact 首页付费类型分析事实
     * @return 首页付费类型分析事实集合
     */
    public List<PayTypeDayFact> selectPayTypeDayFactList(PayTypeDayFact payTypeDayFact);

    /**
     * 新增首页付费类型分析事实
     * 
     * @param payTypeDayFact 首页付费类型分析事实
     * @return 结果
     */
    public int insertPayTypeDayFact(PayTypeDayFact payTypeDayFact);

    /**
     * 修改首页付费类型分析事实
     * 
     * @param payTypeDayFact 首页付费类型分析事实
     * @return 结果
     */
    public int updatePayTypeDayFact(PayTypeDayFact payTypeDayFact);

    /**
     * 批量删除首页付费类型分析事实
     * 
     * @param ids 需要删除的首页付费类型分析事实主键集合
     * @return 结果
     */
    public int deletePayTypeDayFactByIds(Integer[] ids);

    /**
     * 删除首页付费类型分析事实信息
     * 
     * @param id 首页付费类型分析事实主键
     * @return 结果
     */
    public int deletePayTypeDayFactById(Integer id);

    /**
     * 分析首页付费类型事实
     */
    void analysePayTypeDayFact(String userId);

    /**
     * 查询首页付费类型分析事实
     *
     * @param payTypeDayFact 首页付费类型分析事实
     * @return 首页付费类型分析事实集合
     */
    List<PayStatisticFactVO> getPayTypeDayFact(PayTypeDayFact payTypeDayFact);

    List<PayTypeDayFact> getPayTypeStatisticsSheet(StatisticsSheetVO statisticsSheetVO);
}
