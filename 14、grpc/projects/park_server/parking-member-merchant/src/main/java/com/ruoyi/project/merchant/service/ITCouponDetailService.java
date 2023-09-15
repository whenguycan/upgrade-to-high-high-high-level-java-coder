package com.ruoyi.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.merchant.domain.TCouponDetail;
import com.ruoyi.project.merchant.domain.TCouponOrder;
import com.ruoyi.project.merchant.domain.vo.HourCouponVo;
import com.ruoyi.project.merchant.domain.vo.OrderCouponVo;
import com.ruoyi.project.merchant.domain.vo.TCouponDetailVo;
import com.ruoyi.project.parking.domain.vo.MemberCouponVO;
import com.ruoyi.project.parking.enums.CouponStatusEnum;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;


/**
 * 优惠券明细Service接口
 *
 * @author ruoyi
 * @date 2023-03-02
 */
public interface ITCouponDetailService extends IService<TCouponDetail> {

    /**
     * 批量保存
     *
     * @param orderCouponVo
     * @return
     */
    public boolean batchEditCoupon(OrderCouponVo orderCouponVo );

    /**
     * 批量添加优惠券
     *
     * @param orderCouponVo
     * @return
     */
    public boolean batchAddCoupon(OrderCouponVo orderCouponVo);

    /**
     * 作废优惠券
     *
     * @param orderCouponVo 作废优惠券
     * @return 结果
     */
    public  TCouponOrder cancelCoupon(OrderCouponVo orderCouponVo);

    /**
     * 购买优惠券
     */
    public TCouponOrder purchaseCoupon(OrderCouponVo purchaseCouponVo);


    /**
     * 查询优惠券明细
     *
     * @param id 优惠券明细主键
     * @return 优惠券明细
     */
    public TCouponDetail selectTCouponDetailById(Long id);

    public Long countCouponDetail(TCouponDetail tCouponDetail);


    /**
     * 查询优惠券明细列表
     *
     * @param tCouponDetail 优惠券明细
     * @return 优惠券明细集合
     */
    public List<TCouponDetail> selectTCouponDetailList(TCouponDetailVo tCouponDetail);

    /**
     * 查询优惠券明细列表
     *
     * @param tCouponDetail 优惠券明细
     * @return 优惠券明细集合
     */
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
     * 批量删除优惠券明细
     *
     * @param ids 需要删除的优惠券明细主键集合
     * @return 结果
     */
    public int deleteTCouponDetailByIds(Long[] ids);

    /**
     * 统计作废的不同价格的相同的小时券优惠券数量
     * @param tCouponDetailVo
     * @return
     */
    public HourCouponVo countCancelHourCoupon(TCouponDetailVo tCouponDetailVo);

    /**
     * 删除优惠券明细信息
     *
     * @param id 优惠券明细主键
     * @return 结果
     */
    public int deleteTCouponDetailById(Long id);

    // region 会员优惠券

    /**
     * 查询用户的 优惠券
     * 通过用户关联的车牌号查询
     * @param memberId 会员id
     * @param couponStatusEnum 优惠券状态
     */
    Pair<List<MemberCouponVO>, Long> selectMemberCouponVOList(Long memberId, CouponStatusEnum couponStatusEnum);


    /**
     * 查询优惠券是否失效
     * @param couponCodeList 优惠券码 列表
     */
    boolean verifyCouponBatchByCouponCode(List<String> couponCodeList);
    // endregion
}
