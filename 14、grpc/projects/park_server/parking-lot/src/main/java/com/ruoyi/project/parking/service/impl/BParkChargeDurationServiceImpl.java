package com.ruoyi.project.parking.service.impl;

import com.ruoyi.project.parking.domain.BParkChargeDuration;
import com.ruoyi.project.parking.mapper.BParkChargeDurationMapper;
import com.ruoyi.project.parking.service.IBParkChargeDurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 车场计费规则期间Service业务层处理
 * 
 * @author fangch
 * @date 2023-02-21
 */
@Service
public class BParkChargeDurationServiceImpl implements IBParkChargeDurationService 
{
    @Autowired
    private BParkChargeDurationMapper bParkChargeDurationMapper;

    /**
     * 查询车场计费规则期间
     * 
     * @param id 车场计费规则期间主键
     * @return 车场计费规则期间
     */
    @Override
    public BParkChargeDuration selectBParkChargeDurationById(Integer id)
    {
        return bParkChargeDurationMapper.selectBParkChargeDurationById(id);
    }

    /**
     * 查询车场计费规则期间列表
     * 
     * @param bParkChargeDuration 车场计费规则期间
     * @return 车场计费规则期间
     */
    @Override
    public List<BParkChargeDuration> selectBParkChargeDurationList(BParkChargeDuration bParkChargeDuration)
    {
        return bParkChargeDurationMapper.selectBParkChargeDurationList(bParkChargeDuration);
    }

    /**
     * 新增车场计费规则期间
     * 
     * @param bParkChargeDuration 车场计费规则期间
     * @return 结果
     */
    @Override
    public int insertBParkChargeDuration(BParkChargeDuration bParkChargeDuration)
    {
        bParkChargeDuration.setCreateTime(LocalDateTime.now());
        return bParkChargeDurationMapper.insertBParkChargeDuration(bParkChargeDuration);
    }

    /**
     * 修改车场计费规则期间
     * 
     * @param bParkChargeDuration 车场计费规则期间
     * @return 结果
     */
    @Override
    public int updateBParkChargeDuration(BParkChargeDuration bParkChargeDuration)
    {
        bParkChargeDuration.setUpdateTime(LocalDateTime.now());
        return bParkChargeDurationMapper.updateBParkChargeDuration(bParkChargeDuration);
    }

    /**
     * 批量删除车场计费规则期间
     * 
     * @param ids 需要删除的车场计费规则期间主键
     * @return 结果
     */
    @Override
    public int deleteBParkChargeDurationByIds(Integer[] ids)
    {
        return bParkChargeDurationMapper.deleteBParkChargeDurationByIds(ids);
    }

    /**
     * 删除车场计费规则期间信息
     * 
     * @param id 车场计费规则期间主键
     * @return 结果
     */
    @Override
    public int deleteBParkChargeDurationById(Integer id)
    {
        return bParkChargeDurationMapper.deleteBParkChargeDurationById(id);
    }
}
