package com.ruoyi.project.merchant.mapper;

import java.util.List;
import com.ruoyi.project.merchant.domain.TCouponOrder;

/**
 * H5商户优惠券购买订单Mapper接口
 * 
 * @author ruoyi
 * @date 2023-03-07
 */
public interface TCouponOrderMapper 
{
    /**
     * 查询H5商户优惠券购买订单
     * 
     * @param id H5商户优惠券购买订单主键
     * @return H5商户优惠券购买订单
     */
    public TCouponOrder selectTCouponOrderById(Long id);

    /**
     * 查询H5商户优惠券购买订单列表
     * 
     * @param tCouponOrder H5商户优惠券购买订单
     * @return H5商户优惠券购买订单集合
     */
    public List<TCouponOrder> selectTCouponOrderList(TCouponOrder tCouponOrder);

    /**
     * 新增H5商户优惠券购买订单
     * 
     * @param tCouponOrder H5商户优惠券购买订单
     * @return 结果
     */
    public int insertTCouponOrder(TCouponOrder tCouponOrder);

    /**
     * 修改H5商户优惠券购买订单
     * 
     * @param tCouponOrder H5商户优惠券购买订单
     * @return 结果
     */
    public int updateTCouponOrder(TCouponOrder tCouponOrder);

    /**
     * 删除H5商户优惠券购买订单
     * 
     * @param id H5商户优惠券购买订单主键
     * @return 结果
     */
    public int deleteTCouponOrderById(Long id);

    /**
     * 批量删除H5商户优惠券购买订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTCouponOrderByIds(Long[] ids);
}
