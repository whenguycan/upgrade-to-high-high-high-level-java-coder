package com.ruoyi.project.merchant.service;

import java.util.List;
import com.ruoyi.project.merchant.domain.BChargeSet;
import com.ruoyi.project.merchant.domain.vo.BChargeSetVo;
import com.ruoyi.project.merchant.domain.vo.RechargeVoParam;

/**
 * 充值优惠配置Service接口
 *
 * @author ruoyi
 * @date 2023-03-02
 */
public interface IBChargeSetService
{


    /**
     * 根据金额查找赠送金额
     *
     */
    public BChargeSet selectBChargeSetByRechargeAmount(RechargeVoParam rechargeVoParam);
    /**
     * 查询充值优惠配置
     *
     * @param id 充值优惠配置主键
     * @return 充值优惠配置
     */
    public BChargeSet selectBChargeSetById(Long id);

    /**
     * 查询充值优惠配置列表
     *
     * @param bChargeSet 充值优惠配置
     * @return 充值优惠配置集合
     */
    public BChargeSetVo selectBChargeSetList(BChargeSet bChargeSet);

    /**
     * 新增充值优惠配置
     *
     * @param bChargeSetVo 充值优惠配置
     * @return 结果
     */
    public int insertBChargeSet(BChargeSetVo bChargeSetVo);

    /**
     * 修改充值优惠配置
     *
     * @param bChargeSet 充值优惠配置
     * @return 结果
     */
    public int updateBChargeSet(BChargeSet bChargeSet);

    /**
     * 批量删除充值优惠配置
     *
     * @param ids 需要删除的充值优惠配置主键集合
     * @return 结果
     */
    public int deleteBChargeSetByIds(Long[] ids);

    /**
     * 删除充值优惠配置信息
     *
     * @param id 充值优惠配置主键
     * @return 结果
     */
    public int deleteBChargeSetById(Long id);


    public BChargeSet selectOneBChargeSet(BChargeSet bChargeSet);
}
