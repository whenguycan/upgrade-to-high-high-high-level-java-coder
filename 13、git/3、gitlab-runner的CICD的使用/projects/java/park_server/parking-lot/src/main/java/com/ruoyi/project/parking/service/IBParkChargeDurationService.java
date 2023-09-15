package com.ruoyi.project.parking.service;

import java.util.List;
import com.ruoyi.project.parking.domain.BParkChargeDuration;

/**
 * 车场计费规则期间Service接口
 * 
 * @author fangch
 * @date 2023-02-21
 */
public interface IBParkChargeDurationService 
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
     * 批量删除车场计费规则期间
     * 
     * @param ids 需要删除的车场计费规则期间主键集合
     * @return 结果
     */
    int deleteBParkChargeDurationByIds(Integer[] ids);

    /**
     * 删除车场计费规则期间信息
     * 
     * @param id 车场计费规则期间主键
     * @return 结果
     */
    int deleteBParkChargeDurationById(Integer id);
}
