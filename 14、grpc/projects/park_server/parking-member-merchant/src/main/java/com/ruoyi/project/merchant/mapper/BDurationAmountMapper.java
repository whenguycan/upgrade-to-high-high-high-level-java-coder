package com.ruoyi.project.merchant.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.merchant.domain.BDurationAmount;

/**
 * 优惠抵扣时长金额配置Mapper接口
 * 
 * @author ruoyi
 * @date 2023-03-02
 */
public interface BDurationAmountMapper extends BaseMapper<BDurationAmount>
{
    /**
     * 查询优惠抵扣时长金额配置
     * 
     * @param id 优惠抵扣时长金额配置主键
     * @return 优惠抵扣时长金额配置
     */
    public BDurationAmount selectBDurationAmountById(Long id);

    /**
     * 查询优惠抵扣时长金额配置列表
     * 
     * @param bDurationAmount 优惠抵扣时长金额配置
     * @return 优惠抵扣时长金额配置集合
     */
    public List<BDurationAmount> selectBDurationAmountList(BDurationAmount bDurationAmount);

    /**
     * 新增优惠抵扣时长金额配置
     * 
     * @param bDurationAmount 优惠抵扣时长金额配置
     * @return 结果
     */
    public int insertBDurationAmount(BDurationAmount bDurationAmount);

    /**
     * 修改优惠抵扣时长金额配置
     * 
     * @param bDurationAmount 优惠抵扣时长金额配置
     * @return 结果
     */
    public int updateBDurationAmount(BDurationAmount bDurationAmount);

    /**
     * 删除优惠抵扣时长金额配置
     * 
     * @param id 优惠抵扣时长金额配置主键
     * @return 结果
     */
    public int deleteBDurationAmountById(Long id);

    /**
     * 批量删除优惠抵扣时长金额配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBDurationAmountByIds(Long[] ids);

}
