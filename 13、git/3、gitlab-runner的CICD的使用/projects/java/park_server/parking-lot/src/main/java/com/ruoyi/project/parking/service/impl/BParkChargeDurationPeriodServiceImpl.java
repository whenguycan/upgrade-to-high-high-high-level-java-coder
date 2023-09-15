package com.ruoyi.project.parking.service.impl;

import com.ruoyi.project.parking.domain.BParkChargeDurationPeriod;
import com.ruoyi.project.parking.mapper.BParkChargeDurationPeriodMapper;
import com.ruoyi.project.parking.service.IBParkChargeDurationPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 车场计费规则期间时段Service业务层处理
 * 
 * @author fangch
 * @date 2023-02-21
 */
@Service
public class BParkChargeDurationPeriodServiceImpl implements IBParkChargeDurationPeriodService 
{
    @Autowired
    private BParkChargeDurationPeriodMapper bParkChargeDurationPeriodMapper;

    /**
     * 查询车场计费规则期间时段
     * 
     * @param id 车场计费规则期间时段主键
     * @return 车场计费规则期间时段
     */
    @Override
    public BParkChargeDurationPeriod selectBParkChargeDurationPeriodById(Integer id)
    {
        return bParkChargeDurationPeriodMapper.selectBParkChargeDurationPeriodById(id);
    }

    /**
     * 查询车场计费规则期间时段列表
     * 
     * @param bParkChargeDurationPeriod 车场计费规则期间时段
     * @return 车场计费规则期间时段
     */
    @Override
    public List<BParkChargeDurationPeriod> selectBParkChargeDurationPeriodList(BParkChargeDurationPeriod bParkChargeDurationPeriod)
    {
        return bParkChargeDurationPeriodMapper.selectBParkChargeDurationPeriodList(bParkChargeDurationPeriod);
    }

    /**
     * 新增车场计费规则期间时段
     * 
     * @param bParkChargeDurationPeriod 车场计费规则期间时段
     * @return 结果
     */
    @Override
    public int insertBParkChargeDurationPeriod(BParkChargeDurationPeriod bParkChargeDurationPeriod)
    {
        bParkChargeDurationPeriod.setCreateTime(LocalDateTime.now());
        return bParkChargeDurationPeriodMapper.insertBParkChargeDurationPeriod(bParkChargeDurationPeriod);
    }

    /**
     * 修改车场计费规则期间时段
     * 
     * @param bParkChargeDurationPeriod 车场计费规则期间时段
     * @return 结果
     */
    @Override
    public int updateBParkChargeDurationPeriod(BParkChargeDurationPeriod bParkChargeDurationPeriod)
    {
        bParkChargeDurationPeriod.setUpdateTime(LocalDateTime.now());
        return bParkChargeDurationPeriodMapper.updateBParkChargeDurationPeriod(bParkChargeDurationPeriod);
    }

    /**
     * 批量删除车场计费规则期间时段
     * 
     * @param ids 需要删除的车场计费规则期间时段主键
     * @return 结果
     */
    @Override
    public int deleteBParkChargeDurationPeriodByIds(Integer[] ids)
    {
        return bParkChargeDurationPeriodMapper.deleteBParkChargeDurationPeriodByIds(ids);
    }

    /**
     * 删除车场计费规则期间时段信息
     * 
     * @param id 车场计费规则期间时段主键
     * @return 结果
     */
    @Override
    public int deleteBParkChargeDurationPeriodById(Integer id)
    {
        return bParkChargeDurationPeriodMapper.deleteBParkChargeDurationPeriodById(id);
    }
}
