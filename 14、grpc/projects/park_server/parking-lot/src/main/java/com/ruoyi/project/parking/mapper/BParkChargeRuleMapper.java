package com.ruoyi.project.parking.mapper;

import java.util.List;
import com.ruoyi.project.parking.domain.BParkChargeRule;
import org.apache.ibatis.annotations.Mapper;

/**
 * 车场收费规则Mapper接口
 * 
 * @author fangch
 * @date 2023-02-21
 */
@Mapper
public interface BParkChargeRuleMapper
{
    /**
     * 查询车场收费规则
     * 
     * @param id 车场收费规则主键
     * @return 车场收费规则
     */
    BParkChargeRule selectBParkChargeRuleById(Integer id);

    /**
     * 查询车场收费规则列表
     * 
     * @param bParkChargeRule 车场收费规则
     * @return 车场收费规则集合
     */
    List<BParkChargeRule> selectBParkChargeRuleList(BParkChargeRule bParkChargeRule);

    /**
     * 新增车场收费规则
     * 
     * @param bParkChargeRule 车场收费规则
     * @return 结果
     */
    int insertBParkChargeRule(BParkChargeRule bParkChargeRule);

    /**
     * 修改车场收费规则
     * 
     * @param bParkChargeRule 车场收费规则
     * @return 结果
     */
    int updateBParkChargeRule(BParkChargeRule bParkChargeRule);

    /**
     * 删除车场收费规则
     * 
     * @param id 车场收费规则主键
     * @return 结果
     */
    int deleteBParkChargeRuleById(Integer id);

    /**
     * 批量删除车场收费规则
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBParkChargeRuleByIds(Integer[] ids);
}
