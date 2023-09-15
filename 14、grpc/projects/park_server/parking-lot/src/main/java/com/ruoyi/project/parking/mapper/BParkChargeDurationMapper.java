package com.ruoyi.project.parking.mapper;

import java.util.List;
import com.ruoyi.project.parking.domain.BParkChargeDuration;
import org.apache.ibatis.annotations.Mapper;

/**
 * 车场计费规则期间Mapper接口
 * 
 * @author fangch
 * @date 2023-02-21
 */
@Mapper
public interface BParkChargeDurationMapper 
{
    /**
     * 查询车场计费规则期间
     * 
     * @param id 车场计费规则期间主键
     * @return 车场计费规则期间
     */
    BParkChargeDuration selectBParkChargeDurationById(Integer id);

    /**
     * 查询车场计费规则期间列表
     * 
     * @param bParkChargeDuration 车场计费规则期间
     * @return 车场计费规则期间集合
     */
    List<BParkChargeDuration> selectBParkChargeDurationList(BParkChargeDuration bParkChargeDuration);

    /**
     * 新增车场计费规则期间
     * 
     * @param bParkChargeDuration 车场计费规则期间
     * @return 结果
     */
    int insertBParkChargeDuration(BParkChargeDuration bParkChargeDuration);

    /**
     * 修改车场计费规则期间
     * 
     * @param bParkChargeDuration 车场计费规则期间
     * @return 结果
     */
    int updateBParkChargeDuration(BParkChargeDuration bParkChargeDuration);

    /**
     * 删除车场计费规则期间
     * 
     * @param id 车场计费规则期间主键
     * @return 结果
     */
    int deleteBParkChargeDurationById(Integer id);

    /**
     * 批量删除车场计费规则期间
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBParkChargeDurationByIds(Integer[] ids);

    /**
     * 清空规则的规则期间
     *
     * @param ruleId 计费规则ID
     * @return 结果
     */
    int deleteByRuleId(Integer ruleId);

    /**
     * 清空规则的规则期间
     *
     * @param ids 需要删除的计费规则ID集合
     * @return 结果
     */
    int deleteByRuleIds(Integer[] ids);
}
