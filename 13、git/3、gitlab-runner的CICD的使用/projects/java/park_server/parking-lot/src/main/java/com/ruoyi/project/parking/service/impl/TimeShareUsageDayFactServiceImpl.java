package com.ruoyi.project.parking.service.impl;

import com.ruoyi.project.parking.domain.TimeShareUsageDayFact;
import com.ruoyi.project.parking.domain.vo.TimeShareUsageFactVO;
import com.ruoyi.project.parking.mapper.HomePageMapper;
import com.ruoyi.project.parking.mapper.TimeShareUsageDayFactMapper;
import com.ruoyi.project.parking.service.ITimeShareUsageDayFactService;
import com.ruoyi.project.system.domain.SysDept;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 首页分时利用率事实Service业务层处理
 * 
 * @author fangch
 * @date 2023-03-20
 */
@Service
public class TimeShareUsageDayFactServiceImpl implements ITimeShareUsageDayFactService 
{
    @Autowired
    private TimeShareUsageDayFactMapper timeShareUsageDayFactMapper;

    @Autowired
    private HomePageMapper homePageMapper;

    /**
     * 查询首页分时利用率事实
     * 
     * @param id 首页分时利用率事实主键
     * @return 首页分时利用率事实
     */
    @Override
    public TimeShareUsageDayFact selectTimeShareUsageDayFactById(Integer id)
    {
        return timeShareUsageDayFactMapper.selectTimeShareUsageDayFactById(id);
    }

    /**
     * 查询首页分时利用率事实列表
     * 
     * @param timeShareUsageDayFact 首页分时利用率事实
     * @return 首页分时利用率事实
     */
    @Override
    public List<TimeShareUsageDayFact> selectTimeShareUsageDayFactList(TimeShareUsageDayFact timeShareUsageDayFact)
    {
        return timeShareUsageDayFactMapper.selectTimeShareUsageDayFactList(timeShareUsageDayFact);
    }

    /**
     * 新增首页分时利用率事实
     * 
     * @param timeShareUsageDayFact 首页分时利用率事实
     * @return 结果
     */
    @Override
    public int insertTimeShareUsageDayFact(TimeShareUsageDayFact timeShareUsageDayFact)
    {
        timeShareUsageDayFact.setCreateTime(LocalDateTime.now());
        return timeShareUsageDayFactMapper.insertTimeShareUsageDayFact(timeShareUsageDayFact);
    }

    /**
     * 修改首页分时利用率事实
     * 
     * @param timeShareUsageDayFact 首页分时利用率事实
     * @return 结果
     */
    @Override
    public int updateTimeShareUsageDayFact(TimeShareUsageDayFact timeShareUsageDayFact)
    {
        timeShareUsageDayFact.setUpdateTime(LocalDateTime.now());
        return timeShareUsageDayFactMapper.updateTimeShareUsageDayFact(timeShareUsageDayFact);
    }

    /**
     * 批量删除首页分时利用率事实
     * 
     * @param ids 需要删除的首页分时利用率事实主键
     * @return 结果
     */
    @Override
    public int deleteTimeShareUsageDayFactByIds(Integer[] ids)
    {
        return timeShareUsageDayFactMapper.deleteTimeShareUsageDayFactByIds(ids);
    }

    /**
     * 删除首页分时利用率事实信息
     * 
     * @param id 首页分时利用率事实主键
     * @return 结果
     */
    @Override
    public int deleteTimeShareUsageDayFactById(Integer id)
    {
        return timeShareUsageDayFactMapper.deleteTimeShareUsageDayFactById(id);
    }

    /**
     * 分析首页分时利用率事实
     */
    @Override
    public void analyseTimeShareUsageDayFact(String userId) {
        // 查询所有车场id和编号
        List<SysDept> parks = homePageMapper.getAllParkIds();
        for (SysDept park : parks) {
            // 查询所有归属子车场编号
            List<String> parkNos = homePageMapper.getChildParkNos(park.getDeptId());
            // 分析首页付费类型事实
            String day = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            List<TimeShareUsageDayFact> TimeShareUsageDayFactList = timeShareUsageDayFactMapper.analyseTimeShareUsageDayFact(parkNos, day);
            if (CollectionUtils.isNotEmpty(TimeShareUsageDayFactList)) {
                TimeShareUsageDayFactList.forEach(item -> {
                    // 设置车场编号、日期、利用率
                    item.setParkNo(park.getParkNo());
                    item.setDay(day);
                    BigDecimal fullPercent = new BigDecimal(100);
                    item.setUseRatio(item.getUseRatio().compareTo(fullPercent) > 0 ? fullPercent : item.getUseRatio());
                    // 检查同维度的分析数据是否已存在，存在更新，不存在新增
                    TimeShareUsageDayFact existFact = timeShareUsageDayFactMapper.selectTimeShareUsageDayFact(item);
                    if (null == existFact) {
                        item.setCreateBy(userId);
                        item.setCreateTime(LocalDateTime.now());
                        timeShareUsageDayFactMapper.insertTimeShareUsageDayFact(item);
                    } else {
                        existFact.setUseRatio(item.getUseRatio());
                        existFact.setUpdateBy(userId);
                        existFact.setUpdateTime(LocalDateTime.now());
                        timeShareUsageDayFactMapper.updateTimeShareUsageDayFact(existFact);
                    }
                });
            }
        }
    }

    /**
     * 查询首页分时利用率事实
     *
     * @param timeShareUsageDayFact 首页分时利用率事实
     * @return 首页分时利用率事实集合
     */
    @Override
    public List<TimeShareUsageFactVO> getTimeShareUsageDayFact(TimeShareUsageDayFact timeShareUsageDayFact) {
        return timeShareUsageDayFactMapper.getTimeShareUsageDayFact(timeShareUsageDayFact);
    }
}
