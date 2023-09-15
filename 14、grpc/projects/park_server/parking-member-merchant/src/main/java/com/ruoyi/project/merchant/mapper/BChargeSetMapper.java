package com.ruoyi.project.merchant.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.merchant.domain.BChargeSet;
import com.ruoyi.project.merchant.domain.vo.RechargeVoParam;

/**
 * 充值优惠配置Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-02
 */
public interface BChargeSetMapper extends BaseMapper<BChargeSet>
{
    /**
     * 查询充值优惠配置
     *
     * @param id 充值优惠配置主键
     * @return 充值优惠配置
     */
    public BChargeSet selectBChargeSetById(Long id);
    /**
     * 根据金额查找赠送金额
     *
     */
    public BChargeSet selectChargeSetByRechargeAmount(RechargeVoParam rechargeVoParam);


    /**
     * 查询充值优惠配置列表
     *
     * @param bChargeSet 充值优惠配置
     * @return 充值优惠配置集合
     */
    public List<BChargeSet> selectBChargeSetList(BChargeSet bChargeSet);

    /**
     * 新增充值优惠配置
     *
     * @param bChargeSet 充值优惠配置
     * @return 结果
     */
    public int insertBChargeSet(BChargeSet bChargeSet);

    /**
     * 修改充值优惠配置
     *
     * @param bChargeSet 充值优惠配置
     * @return 结果
     */
    public int updateBChargeSet(BChargeSet bChargeSet);

    /**
     * 删除充值优惠配置
     *
     * @param id 充值优惠配置主键
     * @return 结果
     */
    public int deleteBChargeSetById(Long id);

    /**
     * 批量删除充值优惠配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBChargeSetByIds(Long[] ids);
}
