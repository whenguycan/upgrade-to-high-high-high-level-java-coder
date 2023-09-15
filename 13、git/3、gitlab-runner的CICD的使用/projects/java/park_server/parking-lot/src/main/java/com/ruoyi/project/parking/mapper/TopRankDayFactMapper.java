package com.ruoyi.project.parking.mapper;

import com.ruoyi.project.parking.domain.TopRankDayFact;
import com.ruoyi.project.parking.domain.vo.TopRankVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 首页车场热门排行事实Mapper接口
 * 
 * @author fangch
 * @date 2023-03-28
 */
@Mapper
public interface TopRankDayFactMapper 
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
     * 删除首页车场热门排行事实
     * 
     * @param id 首页车场热门排行事实主键
     * @return 结果
     */
    public int deleteTopRankDayFactById(Integer id);

    /**
     * 批量删除首页车场热门排行事实
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTopRankDayFactByIds(Integer[] ids);

    /**
     * 分析首页车场热门排行事实
     *
     * @return 首页车场热门排行事实
     */
    TopRankDayFact analyseTopRankDayFact(@Param("parkNos") List<String> parkNos, @Param("day") String day);

    /**
     * 查询同维度的首页车场热门排行事实
     *
     * @param topRankDayFact
     * @return
     */
    TopRankDayFact selectTopRankDayFact(TopRankDayFact topRankDayFact);

    /**
     * 查询大屏车场热门排行
     *
     * @return 车场热门排行
     */
    List<TopRankVO> getTopRank(@Param("year") Integer year, @Param("month") Integer month, @Param("week") Integer week, @Param("day") String day);

    /**
     * 查询车位信息
     *
     * @param item
     * @return
     */
    TopRankVO getSpaceInfo(TopRankVO item);

}
