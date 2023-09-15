package com.ruoyi.project.merchant.service.impl;

import com.dahuatech.hutool.core.collection.CollectionUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.common.CouponEnums;
import com.ruoyi.project.merchant.domain.TCouponCarnoRelation;
import com.ruoyi.project.merchant.domain.TCouponDetail;
import com.ruoyi.project.merchant.domain.vo.AssignedCoupon;
import com.ruoyi.project.merchant.domain.vo.TCouponCarnoRelationVo;
import com.ruoyi.project.merchant.domain.vo.TCouponDetailVo;
import com.ruoyi.project.merchant.mapper.TCouponCarnoRelationMapper;
import com.ruoyi.project.merchant.mapper.TCouponDetailMapper;
import com.ruoyi.project.merchant.service.ITCouponCarnoRelationService;
import com.ruoyi.project.merchant.service.ITCouponDetailService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 优惠券车牌关联Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@Service
public class TCouponCarnoRelationServiceImpl implements ITCouponCarnoRelationService {
    @Resource
    private TCouponCarnoRelationMapper tCouponCarnoRelationMapper;

    @Resource
    private ITCouponDetailService itCouponDetailService;

    /**
     * 查询优惠券车牌关联
     *
     * @param id 优惠券车牌关联主键
     * @return 优惠券车牌关联
     */
    @Override
    public TCouponCarnoRelation selectTCouponCarnoRelationById(Long id) {
        return tCouponCarnoRelationMapper.selectTCouponCarnoRelationById(id);
    }

    /**
     * 查询优惠券车牌关联列表
     *
     * @param tCouponCarnoRelation 优惠券车牌关联
     * @return 优惠券车牌关联
     */
    @Override
    public List<TCouponCarnoRelation> selectTCouponCarnoRelationList(TCouponCarnoRelation tCouponCarnoRelation) {
        return tCouponCarnoRelationMapper.selectTCouponCarnoRelationList(tCouponCarnoRelation);
    }

    /**
     * 新增优惠券车牌关联
     *
     * @param tCouponCarnoRelation 优惠券车牌关联
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTCouponCarnoRelation(TCouponCarnoRelationVo tCouponCarnoRelation) {
        TCouponDetailVo tCouponDetailVo = new TCouponDetailVo();
        tCouponDetailVo.setCouponId(tCouponCarnoRelation.getCouponId());
        tCouponDetailVo.setCouponStatus(CouponEnums.COUPON_STATUS.CREATED.getValue());
        List<TCouponDetail> tCouponDetails = itCouponDetailService.selectAppPlatformCouponDetailList(tCouponDetailVo);
        if (CollectionUtils.isEmpty(tCouponDetails)) {
            return -1;
        }
        TCouponDetail tCouponDetail = tCouponDetails.get(0);
        tCouponDetail.setCouponStatus(CouponEnums.COUPON_STATUS.ALLOCATED.getValue());
        tCouponDetail.setCarNumber(tCouponCarnoRelation.getCarNumber());
        tCouponDetail.setUpdateBy(SecurityUtils.getUsername());
        tCouponDetail.setUpdateTime(DateUtils.getNowDate());
        itCouponDetailService.updateById(tCouponDetail);
        tCouponCarnoRelation.setCreateBy(SecurityUtils.getUsername());
        tCouponCarnoRelation.setCreateTime(DateUtils.getNowDate());
        tCouponCarnoRelation.setCouponDetailId(tCouponDetail.getId());
        return tCouponCarnoRelationMapper.insertTCouponCarnoRelation(tCouponCarnoRelation);
    }

    /**
     * 修改优惠券车牌关联
     *
     * @param tCouponCarnoRelation 优惠券车牌关联
     * @return 结果
     */
    @Override
    public int updateTCouponCarnoRelation(TCouponCarnoRelation tCouponCarnoRelation) {
        return tCouponCarnoRelationMapper.updateTCouponCarnoRelation(tCouponCarnoRelation);
    }

    /**
     * 批量删除优惠券车牌关联
     *
     * @param ids 需要删除的优惠券车牌关联主键
     * @return 结果
     */
    @Override
    public int deleteTCouponCarnoRelationByIds(Long[] ids) {
        return tCouponCarnoRelationMapper.deleteTCouponCarnoRelationByIds(ids);
    }

    /**
     * 删除优惠券车牌关联信息
     *
     * @param id 优惠券车牌关联主键
     * @return 结果
     */
    @Override
    public int deleteTCouponCarnoRelationById(Long id) {
        return tCouponCarnoRelationMapper.deleteTCouponCarnoRelationById(id);
    }

    @Override
    public List<AssignedCoupon> selectAssignedCouponList(String parkNo, String carNo,Long couponDetailId) {
        return tCouponCarnoRelationMapper.selectAssignedCouponList(parkNo, carNo,couponDetailId);
    }

    @Override
    public int updateCouponStatus(Long couponDetailId, Date usedTime) {
        TCouponDetail couponDetail = new TCouponDetail();
        couponDetail.setId(couponDetailId);
        couponDetail.setUsedTime(usedTime);
        couponDetail.setUpdateTime(new Date());
        couponDetail.setUpdateBy("支付系统调用");
        itCouponDetailService.updateById(couponDetail);
        return 0;
    }

}
