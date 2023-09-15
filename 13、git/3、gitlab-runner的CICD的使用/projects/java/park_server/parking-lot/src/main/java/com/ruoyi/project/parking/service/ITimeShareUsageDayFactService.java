package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.TimeShareUsageDayFact;
import com.ruoyi.project.parking.domain.vo.TimeShareUsageFactVO;

import java.util.List;

/**
 * 首页分时利用率事实Service接口
 * 
 * @author fangch
 * @date 2023-03-20
 */
public interface ITimeShareUsageDayFactService 
{
    /**
     * 查询首页分时利用率事实
     * 
     * @param id 首页分时利用率事实主键
     * @return 首页分时利用率事实
     */
    public TimeShareUsageDayFact selectTimeShareUsageDayFactById(Integer id);

    /**
     * 查询首页分时利用率事实列表
     * 
     * @param timeShareUsageDayFact 首页分时利用率事实
     * @return 首页分时利用率事实集合
     */
    public List<TimeShareUsageDayFact> selectTimeShareUsageDayFactList(TimeShareUsageDayFact timeShareUsageDayFact);

    /**
     * 新增首页分时利用率事实
     * 
     * @param timeShareUsageDayFact 首页分时利用率事实
     * @return 结果
     */
    public int insertTimeShareUsageDayFact(TimeShareUsageDayFact timeShareUsageDayFact);

    /**
     * 修改首页分时利用率事实
     * 
     * @param timeShareUsageDayFact 首页分时利用率事实
     * @return 结果
     */
    public int updateTimeShareUsageDayFact(TimeShareUsageDayFact timeShareUsageDayFact);

    /**
     * 批量删除首页分时利用率事实
     * 
     * @param ids 需要删除的首页分时利用率事实主键集合
     * @return 结果
     */
    public int deleteTimeShareUsageDayFactByIds(Integer[] ids);

    /**
     * 删除首页分时利用率事实信息
     * 
     * @param id 首页分时利用率事实主键
     * @return 结果
     */
    public int deleteTimeShareUsageDayFactById(Integer id);

    /**
     * 分析首页分时利用率事实
     */
    void analyseTimeShareUsageDayFact(String userId);

    /**
     * 查询首页分时利用率事实
     *
     * @param timeShareUsageDayFact 首页分时利用率事实
     * @return 首页分时利用率事实集合
     */
    List<TimeShareUsageFactVO> getTimeShareUsageDayFact(TimeShareUsageDayFact timeShareUsageDayFact);
}
