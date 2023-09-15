package com.ruoyi.project.parking.service;

import java.util.List;
import com.ruoyi.project.parking.domain.BParkChargeDurationPeriod;

/**
 * 车场计费规则期间时段Service接口
 * 
 * @author fangch
 * @date 2023-02-21
 */
public interface IBParkChargeDurationPeriodService 
{
    /**
     * 查询车场计费规则期间时段
     * 
     * @param id 车场计费规则期间时段主键
     * @return 车场计费规则期间时段
     */
    BParkChargeDurationPeriod selectBParkChargeDurationPeriodById(Integer id);

    /**
     * 查询车场计费规则期间时段列表
     * 
     * @param bParkChargeDurationPeriod 车场计费规则期间时段
     * @return 车场计费规则期间时段集合
     */
    List<BParkChargeDurationPeriod> selectBParkChargeDurationPeriodList(BParkChargeDurationPeriod bParkChargeDurationPeriod);

    /**
     * 新增车场计费规则期间时段
     * 
     * @param bParkChargeDurationPeriod 车场计费规则期间时段
     * @return 结果
     */
    int insertBParkChargeDurationPeriod(BParkChargeDurationPeriod bParkChargeDurationPeriod);

    /**
     * 修改车场计费规则期间时段
     * 
     * @param bParkChargeDurationPeriod 车场计费规则期间时段
     * @return 结果
     */
    int updateBParkChargeDurationPeriod(BParkChargeDurationPeriod bParkChargeDurationPeriod);

    /**
     * 批量删除车场计费规则期间时段
     * 
     * @param ids 需要删除的车场计费规则期间时段主键集合
     * @return 结果
     */
    int deleteBParkChargeDurationPeriodByIds(Integer[] ids);

    /**
     * 删除车场计费规则期间时段信息
     * 
     * @param id 车场计费规则期间时段主键
     * @return 结果
     */
    int deleteBParkChargeDurationPeriodById(Integer id);
}
