package com.ruoyi.project.parking.service.impl;

import com.ruoyi.project.parking.domain.ParkUsageDayFact;
import com.ruoyi.project.parking.mapper.HomePageMapper;
import com.ruoyi.project.parking.mapper.ParkUsageDayFactMapper;
import com.ruoyi.project.parking.service.IParkUsageDayFactService;
import com.ruoyi.project.system.domain.SysDept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 首页今日泊车使用情况事实Service业务层处理
 * 
 * @author fangch
 * @date 2023-03-28
 */
@Service
public class ParkUsageDayFactServiceImpl implements IParkUsageDayFactService 
{
    @Autowired
    private ParkUsageDayFactMapper parkUsageDayFactMapper;

    @Autowired
    private HomePageMapper homePageMapper;

    /**
     * 查询首页今日泊车使用情况事实
     * 
     * @param id 首页今日泊车使用情况事实主键
     * @return 首页今日泊车使用情况事实
     */
    @Override
    public ParkUsageDayFact selectParkUsageDayFactById(Integer id)
    {
        return parkUsageDayFactMapper.selectParkUsageDayFactById(id);
    }

    /**
     * 查询首页今日泊车使用情况事实列表
     * 
     * @param parkUsageDayFact 首页今日泊车使用情况事实
     * @return 首页今日泊车使用情况事实
     */
    @Override
    public List<ParkUsageDayFact> selectParkUsageDayFactList(ParkUsageDayFact parkUsageDayFact)
    {
        return parkUsageDayFactMapper.selectParkUsageDayFactList(parkUsageDayFact);
    }

    /**
     * 新增首页今日泊车使用情况事实
     * 
     * @param parkUsageDayFact 首页今日泊车使用情况事实
     * @return 结果
     */
    @Override
    public int insertParkUsageDayFact(ParkUsageDayFact parkUsageDayFact)
    {
        parkUsageDayFact.setCreateTime(LocalDateTime.now());
        return parkUsageDayFactMapper.insertParkUsageDayFact(parkUsageDayFact);
    }

    /**
     * 修改首页今日泊车使用情况事实
     * 
     * @param parkUsageDayFact 首页今日泊车使用情况事实
     * @return 结果
     */
    @Override
    public int updateParkUsageDayFact(ParkUsageDayFact parkUsageDayFact)
    {
        parkUsageDayFact.setUpdateTime(LocalDateTime.now());
        return parkUsageDayFactMapper.updateParkUsageDayFact(parkUsageDayFact);
    }

    /**
     * 批量删除首页今日泊车使用情况事实
     * 
     * @param ids 需要删除的首页今日泊车使用情况事实主键
     * @return 结果
     */
    @Override
    public int deleteParkUsageDayFactByIds(Integer[] ids)
    {
        return parkUsageDayFactMapper.deleteParkUsageDayFactByIds(ids);
    }

    /**
     * 删除首页今日泊车使用情况事实信息
     * 
     * @param id 首页今日泊车使用情况事实主键
     * @return 结果
     */
    @Override
    public int deleteParkUsageDayFactById(Integer id)
    {
        return parkUsageDayFactMapper.deleteParkUsageDayFactById(id);
    }



    /**
     * 分析首页今日泊车使用情况事实
     *
     */
    @Override
    public void analyseParkUsageDayFact(String userId) {
        // 查询所有车场id和编号
        List<SysDept> parks = homePageMapper.getAllParkIds();
        for (SysDept park : parks) {
            // 查询所有归属子车场编号
            List<String> parkNos = homePageMapper.getChildParkNos(park.getDeptId());
            // 分析首页今日泊车使用情况事实
            String day = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            ParkUsageDayFact parkUsageDayFact = parkUsageDayFactMapper.analyseParkUsageDayFact(parkNos, day);
            if (null != parkUsageDayFact) {
                // 设置车场编号、日期
                parkUsageDayFact.setParkNo(park.getParkNo());
                parkUsageDayFact.setDay(day);
                // 检查同维度的分析数据是否已存在，存在更新，不存在新增
                ParkUsageDayFact existFact = parkUsageDayFactMapper.selectParkUsageDayFact(parkUsageDayFact);
                if (null == existFact) {
                    parkUsageDayFact.setCreateBy(userId);
                    parkUsageDayFact.setCreateTime(LocalDateTime.now());
                    parkUsageDayFactMapper.insertParkUsageDayFact(parkUsageDayFact);
                    // 更新一下昨天的统计，防止丢数据
                    String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    ParkUsageDayFact yesterdayFact = parkUsageDayFactMapper.analyseParkUsageDayFact(parkNos, yesterday);
                    if (null != yesterdayFact) {
                        // 设置车场编号、日期
                        yesterdayFact.setParkNo(park.getParkNo());
                        yesterdayFact.setDay(yesterday);
                        // 检查同维度的分析数据是否已存在，存在更新，不存在新增
                        ParkUsageDayFact existYesterdayFact = parkUsageDayFactMapper.selectParkUsageDayFact(yesterdayFact);
                        if (null != existYesterdayFact) {
                            existYesterdayFact.setEntryCount(yesterdayFact.getEntryCount());
                            existYesterdayFact.setExitCount(yesterdayFact.getExitCount());
                            existYesterdayFact.setLeaveCount(yesterdayFact.getLeaveCount());
                            existYesterdayFact.setUseRatio(yesterdayFact.getUseRatio());
                            existYesterdayFact.setAbnormalRate(yesterdayFact.getAbnormalRate());
                            existYesterdayFact.setUpdateBy(userId);
                            existYesterdayFact.setUpdateTime(LocalDateTime.now());
                            parkUsageDayFactMapper.updateParkUsageDayFact(existYesterdayFact);
                        }
                    }
                } else {
                    existFact.setEntryCount(parkUsageDayFact.getEntryCount());
                    existFact.setExitCount(parkUsageDayFact.getExitCount());
                    existFact.setLeaveCount(parkUsageDayFact.getLeaveCount());
                    existFact.setUseRatio(parkUsageDayFact.getUseRatio());
                    existFact.setAbnormalRate(parkUsageDayFact.getAbnormalRate());
                    existFact.setUpdateBy(userId);
                    existFact.setUpdateTime(LocalDateTime.now());
                    parkUsageDayFactMapper.updateParkUsageDayFact(existFact);
                }
            }
        }
    }

    /**
     * 查询大屏今日泊车使用情况事实
     *
     * @param parkUsageDayFact
     * @return
     */
    @Override
    public ParkUsageDayFact selectParkUsageDayFact(ParkUsageDayFact parkUsageDayFact) {
        return parkUsageDayFactMapper.selectParkUsageDayFact(parkUsageDayFact);
    }
}
