package com.ruoyi.project.parking.mapper;

import com.ruoyi.project.parking.domain.TimeShareUsageDayFact;
import com.ruoyi.project.parking.domain.vo.TimeShareUsageFactVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 首页分时利用率事实Mapper接口
 * 
 * @author fangch
 * @date 2023-03-20
 */
@Mapper
public interface TimeShareUsageDayFactMapper 
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
     * 删除首页分时利用率事实
     * 
     * @param id 首页分时利用率事实主键
     * @return 结果
     */
    public int deleteTimeShareUsageDayFactById(Integer id);

    /**
     * 批量删除首页分时利用率事实
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTimeShareUsageDayFactByIds(Integer[] ids);

    /**
     * 分析首页分时利用率事实
     *
     * @return 首页分时利用率事实
     */
    List<TimeShareUsageDayFact> analyseTimeShareUsageDayFact(@Param("parkNos") List<String> parkNos, @Param("day") String day);

    /**
     * 查询同维度的首页分时利用率事实
     *
     * @param timeShareUsageDayFact
     * @return
     */
    TimeShareUsageDayFact selectTimeShareUsageDayFact(TimeShareUsageDayFact timeShareUsageDayFact);

    /**
     * 查询首页分时利用率事实
     *
     * @param timeShareUsageDayFact 首页分时利用率事实
     * @return 首页分时利用率事实集合
     */
    List<TimeShareUsageFactVO> getTimeShareUsageDayFact(TimeShareUsageDayFact timeShareUsageDayFact);
}
