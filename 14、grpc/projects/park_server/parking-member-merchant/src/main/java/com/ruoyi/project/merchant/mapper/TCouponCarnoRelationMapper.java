package com.ruoyi.project.merchant.mapper;

import com.ruoyi.project.merchant.domain.TCouponCarnoRelation;
import com.ruoyi.project.merchant.domain.vo.AssignedCoupon;
import org.apache.ibatis.annotations.Param;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * 优惠券车牌关联Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-02
 */
public interface TCouponCarnoRelationMapper {
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
    public int insertTCouponCarnoRelation(TCouponCarnoRelation tCouponCarnoRelation);

    /**
     * 修改优惠券车牌关联
     *
     * @param tCouponCarnoRelation 优惠券车牌关联
     * @return 结果
     */
    public int updateTCouponCarnoRelation(TCouponCarnoRelation tCouponCarnoRelation);

    /**
     * 删除优惠券车牌关联
     *
     * @param id 优惠券车牌关联主键
     * @return 结果
     */
    public int deleteTCouponCarnoRelationById(Long id);

    /**
     * 批量删除优惠券车牌关联
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTCouponCarnoRelationByIds(Long[] ids);

    /**
     * 查询已分配的优惠券
     *
     * @param parkNo
     * @param carNumber
     * @return
     */

    public List<AssignedCoupon> selectAssignedCouponList(@Param("parkNo") String parkNo, @Param("carNumber") String carNumber, @Param("couponDetailId") Long couponDetailId);

}
