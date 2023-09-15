package com.ruoyi.project.parking.mapper;

import com.ruoyi.project.parking.domain.PayTypeDayFact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.parking.domain.vo.PayStatisticFactVO;
import com.ruoyi.project.parking.domain.vo.StatisticsSheetVO;

import java.util.List;

/**
 * @author 琴声何来
 * @description 针对表【pay_type_day_fact(首页付费类型分析事实表)】的数据库操作Mapper
 * @Entity com.ruoyi.project.parking.domain.PayTypeDayFact
 * @since 2023-04-18 11:03:11
 */
public interface PayTypeDayFactMapper extends BaseMapper<PayTypeDayFact> {

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
     * 删除首页付费类型分析事实
     *
     * @param id 首页付费类型分析事实主键
     * @return 结果
     */
    public int deletePayTypeDayFactById(Integer id);

    /**
     * 批量删除首页付费类型分析事实
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePayTypeDayFactByIds(Integer[] ids);

    /**
     * 查询首页付费类型分析事实
     *
     * @param payTypeDayFact 首页付费类型分析事实
     * @return 首页付费类型分析事实
     */
    PayTypeDayFact selectPayTypeDayFact(PayTypeDayFact payTypeDayFact);

    /**
     * 查询首页付费类型分析事实
     *
     * @param payTypeDayFact 首页付费类型分析事实
     * @return 首页付费类型分析事实集合
     */
    List<PayStatisticFactVO> getPayTypeDayFact(PayTypeDayFact payTypeDayFact);

    List<PayTypeDayFact> getPayTypeStatisticsSheet(StatisticsSheetVO statisticsSheetVO);
}
