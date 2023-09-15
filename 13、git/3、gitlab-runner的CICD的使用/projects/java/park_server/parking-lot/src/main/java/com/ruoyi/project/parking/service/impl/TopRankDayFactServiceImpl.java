package com.ruoyi.project.parking.service.impl;

import com.ruoyi.project.parking.domain.TopRankDayFact;
import com.ruoyi.project.parking.domain.vo.TopRankVO;
import com.ruoyi.project.parking.mapper.HomePageMapper;
import com.ruoyi.project.parking.mapper.TopRankDayFactMapper;
import com.ruoyi.project.parking.service.ITopRankDayFactService;
import com.ruoyi.project.system.domain.SysDept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * 首页车场热门排行事实Service业务层处理
 * 
 * @author fangch
 * @date 2023-03-28
 */
@Service
public class TopRankDayFactServiceImpl implements ITopRankDayFactService 
{
    @Autowired
    private TopRankDayFactMapper topRankDayFactMapper;

    @Autowired
    private HomePageMapper homePageMapper;

    /**
     * 查询首页车场热门排行事实
     * 
     * @param id 首页车场热门排行事实主键
     * @return 首页车场热门排行事实
     */
    @Override
    public TopRankDayFact selectTopRankDayFactById(Integer id)
    {
        return topRankDayFactMapper.selectTopRankDayFactById(id);
    }

    /**
     * 查询首页车场热门排行事实列表
     * 
     * @param topRankDayFact 首页车场热门排行事实
     * @return 首页车场热门排行事实
     */
    @Override
    public List<TopRankDayFact> selectTopRankDayFactList(TopRankDayFact topRankDayFact)
    {
        return topRankDayFactMapper.selectTopRankDayFactList(topRankDayFact);
    }

    /**
     * 新增首页车场热门排行事实
     * 
     * @param topRankDayFact 首页车场热门排行事实
     * @return 结果
     */
    @Override
    public int insertTopRankDayFact(TopRankDayFact topRankDayFact)
    {
        topRankDayFact.setCreateTime(LocalDateTime.now());
        return topRankDayFactMapper.insertTopRankDayFact(topRankDayFact);
    }

    /**
     * 修改首页车场热门排行事实
     * 
     * @param topRankDayFact 首页车场热门排行事实
     * @return 结果
     */
    @Override
    public int updateTopRankDayFact(TopRankDayFact topRankDayFact)
    {
        topRankDayFact.setUpdateTime(LocalDateTime.now());
        return topRankDayFactMapper.updateTopRankDayFact(topRankDayFact);
    }

    /**
     * 批量删除首页车场热门排行事实
     * 
     * @param ids 需要删除的首页车场热门排行事实主键
     * @return 结果
     */
    @Override
    public int deleteTopRankDayFactByIds(Integer[] ids)
    {
        return topRankDayFactMapper.deleteTopRankDayFactByIds(ids);
    }

    /**
     * 删除首页车场热门排行事实信息
     * 
     * @param id 首页车场热门排行事实主键
     * @return 结果
     */
    @Override
    public int deleteTopRankDayFactById(Integer id)
    {
        return topRankDayFactMapper.deleteTopRankDayFactById(id);
    }

    /**
     * 分析首页车场热门排行事实
     */
    @Override
    public void analyseTopRankDayFact(String userId) {
        // 查询所有车场id和编号
        List<SysDept> parks = homePageMapper.getAllParkIds();
        for (SysDept park : parks) {
            // 查询所有归属子车场编号
            List<String> parkNos = homePageMapper.getChildParkNos(park.getDeptId());
            // 分析首页车场热门排行事实
            String day = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            TopRankDayFact topRankDayFact = topRankDayFactMapper.analyseTopRankDayFact(parkNos, day);
            if (null != topRankDayFact) {
                // 设置车场编号、日期
                topRankDayFact.setParkNo(park.getParkNo());
                topRankDayFact.setDay(day);
                // 检查同维度的分析数据是否已存在，存在更新，不存在新增
                TopRankDayFact existFact = topRankDayFactMapper.selectTopRankDayFact(topRankDayFact);
                if (null == existFact) {
                    topRankDayFact.setCreateBy(userId);
                    topRankDayFact.setCreateTime(LocalDateTime.now());
                    topRankDayFactMapper.insertTopRankDayFact(topRankDayFact);
                    // 更新一下昨天的统计，防止丢数据
                    String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    TopRankDayFact yesterdayFact = topRankDayFactMapper.analyseTopRankDayFact(parkNos, yesterday);
                    if (null != yesterdayFact) {
                        // 设置车场编号、日期
                        yesterdayFact.setParkNo(park.getParkNo());
                        yesterdayFact.setDay(yesterday);
                        // 检查同维度的分析数据是否已存在，存在更新，不存在新增
                        TopRankDayFact existYesterdayFact = topRankDayFactMapper.selectTopRankDayFact(yesterdayFact);
                        if (null != existYesterdayFact) {
                            existYesterdayFact.setCount(yesterdayFact.getCount());
                            existYesterdayFact.setUpdateBy(userId);
                            existYesterdayFact.setUpdateTime(LocalDateTime.now());
                            topRankDayFactMapper.updateTopRankDayFact(existYesterdayFact);
                        }
                    }
                } else {
                    existFact.setCount(topRankDayFact.getCount());
                    existFact.setUpdateBy(userId);
                    existFact.setUpdateTime(LocalDateTime.now());
                    topRankDayFactMapper.updateTopRankDayFact(existFact);
                }
            }
        }
    }

    /**
     * 查询大屏车场热门排行
     *
     * @param parkNo 车场编号
     * @return 大屏车场热门排行
     */
    @Override
    public Map<String, Object> getTopRank() {
        Map<String, Object> result = new HashMap<>();
        LocalDate now = LocalDate.now();
        String day = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<TopRankVO> todayTopRankVOS = topRankDayFactMapper.getTopRank(null, null, null, day);
        List<TopRankVO> todayTopRankVOList = new ArrayList<>();
        todayTopRankVOS.forEach(item -> todayTopRankVOList.add(topRankDayFactMapper.getSpaceInfo(item)));
        result.put("日", todayTopRankVOList);

        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        List<TopRankVO> weekTopRankVOS = topRankDayFactMapper.getTopRank(now.getYear(), null, now.get(weekFields.weekOfWeekBasedYear()), null);
        List<TopRankVO> weekTopRankVOList = new ArrayList<>();
        weekTopRankVOS.forEach(item -> weekTopRankVOList.add(topRankDayFactMapper.getSpaceInfo(item)));
        result.put("周", weekTopRankVOList);

        List<TopRankVO> monthTopRankVOS = topRankDayFactMapper.getTopRank(now.getYear(), now.getMonthValue(), null, null);
        List<TopRankVO> monthTopRankVOList = new ArrayList<>();
        monthTopRankVOS.forEach(item -> monthTopRankVOList.add(topRankDayFactMapper.getSpaceInfo(item)));
        result.put("月", monthTopRankVOList);
        return result;
    }
}
