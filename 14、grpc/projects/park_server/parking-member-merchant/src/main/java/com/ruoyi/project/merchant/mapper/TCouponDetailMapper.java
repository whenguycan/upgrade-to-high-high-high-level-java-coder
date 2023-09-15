package com.ruoyi.project.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.merchant.domain.TCouponDetail;
import com.ruoyi.project.merchant.domain.vo.TCouponDetailVo;
import com.ruoyi.project.parking.domain.vo.MemberCouponVO;
import com.ruoyi.project.parking.enums.CouponStatusEnum;

import java.util.List;

/**
 * 优惠券明细Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-02
 */
public interface TCouponDetailMapper extends BaseMapper<TCouponDetail> {
    /**
     * 查询优惠券明细
     *
     * @param id 优惠券明细主键
     * @return 优惠券明细
     */
    public TCouponDetail selectTCouponDetailById(Long id);

    /**
     * 查询优惠券明细列表
     *
     * @param tCouponDetail 优惠券明细
     * @return 优惠券明细集合
     */
    public List<TCouponDetail> selectTCouponDetailList(TCouponDetailVo tCouponDetail);

    public List<TCouponDetail> selectAppPlatformCouponDetailList(TCouponDetailVo tCouponDetail);


    /**
     * 新增优惠券明细
     *
     * @param tCouponDetail 优惠券明细
     * @return 结果
     */
    public int insertTCouponDetail(TCouponDetail tCouponDetail);

    /**
     * 修改优惠券明细
     *
     * @param tCouponDetail 优惠券明细
     * @return 结果
     */
    public int updateTCouponDetail(TCouponDetail tCouponDetail);

    /**
     * 作废优惠券
     *
     * @param tCouponDetail 作废优惠券
     * @return 结果
     */
    public int updateCancelStatus(TCouponDetail tCouponDetail);

    /**
     * 删除优惠券明细
     *
     * @param id 优惠券明细主键
     * @return 结果
     */
    public int deleteTCouponDetailById(Long id);

    /**
     * 批量删除优惠券明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTCouponDetailByIds(Long[] ids);


    List<MemberCouponVO> selectMemberCouponVOList(Long memberId, CouponStatusEnum couponStatus);
}
