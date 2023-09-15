package com.ruoyi.project.merchant.service;

import java.util.List;

import com.ruoyi.project.merchant.controller.TCouponTypeController;
import com.ruoyi.project.merchant.domain.TCouponType;
import com.ruoyi.project.merchant.domain.vo.CouponTypeVo;

/**
 * 优惠券种类Service接口
 *
 * @author ruoyi
 * @date 2023-03-02
 */
public interface ITCouponTypeService {
    /**
     * 查询优惠券种类
     *
     * @param id 优惠券种类主键
     * @return 优惠券种类
     */
    public TCouponType selectTCouponTypeById(Long id);

    /**
     * 查询优惠券种类列表
     *
     * @param tCouponType 优惠券种类
     * @return 优惠券种类集合
     */
    public List<TCouponType> selectTCouponTypeList(TCouponType tCouponType);

    /**
     * 新增优惠券种类
     *
     * @param tCouponType 优惠券种类
     * @return 结果
     */
    public int insertTCouponType(TCouponType tCouponType);

    /**
     * 修改优惠券种类
     *
     * @param tCouponType 优惠券种类
     * @return 结果
     */
    public int updateTCouponType(TCouponType tCouponType);

    /**
     * 批量删除优惠券种类
     *
     * @param ids 需要删除的优惠券种类主键集合
     * @return 结果
     */
    public int deleteTCouponTypeByIds(Long[] ids);

    /**
     * 删除优惠券种类信息
     *
     * @param id 优惠券种类主键
     * @return 结果
     */
    public int deleteTCouponTypeById(Long id);

    /**
     * 统计已经创建的优惠券数量
     *
     * @param couponType
     * @return
     */
    public List<CouponTypeVo> countAppCoupon(CouponTypeVo couponType);


    public Long countCouponType(Long userId);
}
