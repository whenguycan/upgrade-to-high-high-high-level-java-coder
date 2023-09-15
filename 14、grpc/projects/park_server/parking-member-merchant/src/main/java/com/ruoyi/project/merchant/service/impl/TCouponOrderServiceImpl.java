package com.ruoyi.project.merchant.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.common.CouponConstants;
import com.ruoyi.project.common.OrderNoGenerator;
import com.ruoyi.project.merchant.domain.vo.BatchCouponParam;
import com.ruoyi.project.merchant.service.AccountService;
import com.ruoyi.project.merchant.service.ITCouponDetailService;
import com.ruoyi.project.system.service.ISysUserService;
import org.springframework.stereotype.Service;
import com.ruoyi.project.merchant.mapper.TCouponOrderMapper;
import com.ruoyi.project.merchant.domain.TCouponOrder;
import com.ruoyi.project.merchant.service.ITCouponOrderService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * H5商户优惠券购买订单Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-07
 */
@Service
public class TCouponOrderServiceImpl implements ITCouponOrderService {
    @Resource
    private TCouponOrderMapper tCouponOrderMapper;

    @Resource
    private AccountService accountService;


    /**
     * 查询H5商户优惠券购买订单
     *
     * @param id H5商户优惠券购买订单主键
     * @return H5商户优惠券购买订单
     */
    @Override
    public TCouponOrder selectTCouponOrderById(Long id) {
        return tCouponOrderMapper.selectTCouponOrderById(id);
    }

    /**
     * 查询H5商户优惠券购买订单列表
     *
     * @param tCouponOrder H5商户优惠券购买订单
     * @return H5商户优惠券购买订单
     */
    @Override
    public List<TCouponOrder> selectTCouponOrderList(TCouponOrder tCouponOrder) {
        return tCouponOrderMapper.selectTCouponOrderList(tCouponOrder);
    }

    /**
     * 新增H5商户优惠券购买订单
     *
     * @param tCouponOrder H5商户优惠券购买订单
     * @return 结果
     */
    @Override
    public int insertTCouponOrder(TCouponOrder tCouponOrder) {
        return tCouponOrderMapper.insertTCouponOrder(tCouponOrder);
    }

    /**
     * 修改H5商户优惠券购买订单
     *
     * @param tCouponOrder H5商户优惠券购买订单
     * @return 结果
     */
    @Override
    public int updateTCouponOrder(TCouponOrder tCouponOrder) {
        return tCouponOrderMapper.updateTCouponOrder(tCouponOrder);
    }

    /**
     * 批量删除H5商户优惠券购买订单
     *
     * @param ids 需要删除的H5商户优惠券购买订单主键
     * @return 结果
     */
    @Override
    public int deleteTCouponOrderByIds(Long[] ids) {
        return tCouponOrderMapper.deleteTCouponOrderByIds(ids);
    }

    /**
     * 删除H5商户优惠券购买订单信息
     *
     * @param id H5商户优惠券购买订单主键
     * @return 结果
     */
    @Override
    public int deleteTCouponOrderById(Long id) {
        return tCouponOrderMapper.deleteTCouponOrderById(id);
    }

    @Override
    public int generateCouponOrder(TCouponOrder tCouponOrder, Integer action) {
        return 0;
    }

}
