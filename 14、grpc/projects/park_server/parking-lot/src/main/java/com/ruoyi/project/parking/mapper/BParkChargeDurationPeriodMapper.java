package com.ruoyi.project.parking.mapper;

import java.util.List;
import com.ruoyi.project.parking.domain.BParkChargeDurationPeriod;
import org.apache.ibatis.annotations.Mapper;

/**
 * 车场计费规则期间时段Mapper接口
 * 
 * @author fangch
 * @date 2023-02-21
 */
@Mapper
public interface BParkChargeDurationPeriodMapper 
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
     * 删除车场计费规则期间时段
     * 
     * @param id 车场计费规则期间时段主键
     * @return 结果
     */
    int deleteBParkChargeDurationPeriodById(Integer id);

    /**
     * 批量删除车场计费规则期间时段
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBParkChargeDurationPeriodByIds(Integer[] ids);

    /**
     * 清空规则的规则期间时段
     *
     * @param ruleId 计费规则ID
     * @return 结果
     */
    int deleteByRuleId(Integer ruleId);

    /**
     * 批量清空规则的规则期间时段
     *
     * @param ids 需要删除的计费规则ID集合
     * @return 结果
     */
    int deleteByRuleIds(Integer[] ids);
}
