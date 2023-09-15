package com.ruoyi.project.merchant.service;

import com.ruoyi.project.merchant.domain.TCouponCarnoRelation;
import com.ruoyi.project.merchant.domain.vo.AssignedCoupon;
import com.ruoyi.project.merchant.domain.vo.CarUserCoupon;
import com.ruoyi.project.merchant.domain.vo.TCouponCarnoRelationVo;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 优惠券车牌关联Service接口
 *
 * @author ruoyi
 * @date 2023-03-02
 */
public interface ITCouponCarnoRelationService {
    /**
     * 查询优惠券车牌关联
     *
     * @param id 优惠券车牌关联主键
     * @return 优惠券车牌关联
     */
    public TCouponCarnoRelation selectTCouponCarnoRelationById(Long id);

    /**
     * 查询优惠券车牌关联列表
     *
     * @param tCouponCarnoRelation 优惠券车牌关联
     * @return 优惠券车牌关联集合
     */
    public List<TCouponCarnoRelation> selectTCouponCarnoRelationList(TCouponCarnoRelation tCouponCarnoRelation);

    /**
     * 新增优惠券车牌关联
     *
     * @param tCouponCarnoRelation 优惠券车牌关联
     * @return 结果
     */
    public int insertTCouponCarnoRelation(TCouponCarnoRelationVo tCouponCarnoRelation);
    /**
     * 新增优惠券车牌关联
     *
     * @param carUserCoupon 优惠券车牌关联
     * @return 结果
     */
    public int addCarUserCouponInfo(CarUserCoupon carUserCoupon);

    /**
     * 修改优惠券车牌关联
     *
     * @param tCouponCarnoRelation 优惠券车牌关联
     * @return 结果
     */
    public int updateTCouponCarnoRelation(TCouponCarnoRelation tCouponCarnoRelation);

    /**
     * 批量删除优惠券车牌关联
     *
     * @param ids 需要删除的优惠券车牌关联主键集合
     * @return 结果
     */
    public int deleteTCouponCarnoRelationByIds(Long[] ids);

    /**
     * 删除优惠券车牌关联信息
     *
     * @param id 优惠券车牌关联主键
     * @return 结果
     */
    public int deleteTCouponCarnoRelationById(Long id);

    /**
     *
     */
    public List<AssignedCoupon> selectAssignedCouponList(String parkNo, String carNo, Long couponDetailId);

    /**
     *
     */
    int updateCouponStatus(Long couponDetailId, Date usedTime) ;

}
