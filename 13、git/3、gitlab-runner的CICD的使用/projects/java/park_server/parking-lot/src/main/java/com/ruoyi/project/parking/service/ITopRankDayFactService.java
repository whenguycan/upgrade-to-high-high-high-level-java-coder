package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.TopRankDayFact;

import java.util.List;
import java.util.Map;

/**
 * 首页车场热门排行事实Service接口
 * 
 * @author fangch
 * @date 2023-03-28
 */
public interface ITopRankDayFactService 
{
    /**
     * 查询首页车场热门排行事实
     * 
     * @param id 首页车场热门排行事实主键
     * @return 首页车场热门排行事实
     */
    public TopRankDayFact selectTopRankDayFactById(Integer id);

    /**
     * 查询首页车场热门排行事实列表
     * 
     * @param topRankDayFact 首页车场热门排行事实
     * @return 首页车场热门排行事实集合
     */
    public List<TopRankDayFact> selectTopRankDayFactList(TopRankDayFact topRankDayFact);

    /**
     * 新增首页车场热门排行事实
     * 
     * @param topRankDayFact 首页车场热门排行事实
     * @return 结果
     */
    public int insertTopRankDayFact(TopRankDayFact topRankDayFact);

    /**
     * 修改首页车场热门排行事实
     * 
     * @param topRankDayFact 首页车场热门排行事实
     * @return 结果
     */
    public int updateTopRankDayFact(TopRankDayFact topRankDayFact);

    /**
     * 批量删除首页车场热门排行事实
     * 
     * @param ids 需要删除的首页车场热门排行事实主键集合
     * @return 结果
     */
    public int deleteTopRankDayFactByIds(Integer[] ids);

    /**
     * 删除首页车场热门排行事实信息
     * 
     * @param id 首页车场热门排行事实主键
     * @return 结果
     */
    public int deleteTopRankDayFactById(Integer id);

    /**
     * 分析首页车场热门排行事实
     */
    void analyseTopRankDayFact(String userId);

    /**
     * 查询大屏车场热门排行
     *
     * @param parkNo 车场编号
     * @return 大屏车场热门排行
     */
    Map<String, Object> getTopRank();
}
