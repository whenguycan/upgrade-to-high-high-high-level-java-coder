package com.ruoyi.project.merchant.service;

import java.util.List;
import com.ruoyi.project.merchant.domain.BDurationAmount;
import com.ruoyi.project.merchant.domain.vo.BDurationAmountVo;

/**
 * 优惠抵扣时长金额配置Service接口
 *
 * @author ruoyi
 * @date 2023-03-02
 */
public interface IBDurationAmountService
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
    public BDurationAmountVo selectBDurationAmountList(BDurationAmount bDurationAmount);

    /**
     * 新增优惠抵扣时长金额配置
     *
     * @param bDurationAmountVo 优惠抵扣时长金额配置
     * @return 结果
     */
    public int insertBDurationAmount(BDurationAmountVo bDurationAmountVo);

    /**
     * 修改优惠抵扣时长金额配置
     *
     * @param bDurationAmount 优惠抵扣时长金额配置
     * @return 结果
     */
    public int updateBDurationAmount(BDurationAmount bDurationAmount);

    /**
     * 批量删除优惠抵扣时长金额配置
     *
     * @param ids 需要删除的优惠抵扣时长金额配置主键集合
     * @return 结果
     */
    public int deleteBDurationAmountByIds(Long[] ids);

    /**
     * 删除优惠抵扣时长金额配置信息
     *
     * @param id 优惠抵扣时长金额配置主键
     * @return 结果
     */
    public int deleteBDurationAmountById(Long id);




    /**
     * 查询优惠抵扣时长金额配置列表
     *
     * @param bDurationAmount 优惠抵扣时长金额配置
     * @return 优惠抵扣时长金额配置集合
     */
    public BDurationAmount selectOneBDurationAmount(BDurationAmount bDurationAmount);
}
