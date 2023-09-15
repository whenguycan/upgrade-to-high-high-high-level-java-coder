package com.ruoyi.project.merchant.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.merchant.domain.TCouponDetail;
import com.ruoyi.project.merchant.domain.vo.CouponTypeVo;
import com.ruoyi.project.merchant.service.ITCouponDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.merchant.mapper.TCouponTypeMapper;
import com.ruoyi.project.merchant.domain.TCouponType;
import com.ruoyi.project.merchant.service.ITCouponTypeService;

import javax.annotation.Resource;

/**
 * 优惠券种类Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@Service
public class TCouponTypeServiceImpl implements ITCouponTypeService {
    @Resource
    private TCouponTypeMapper tCouponTypeMapper;

    @Resource
    private ITCouponDetailService itCouponDetailService;

    /**
     * 查询优惠券种类
     *
     * @param id 优惠券种类主键
     * @return 优惠券种类
     */
    @Override
    public TCouponType selectTCouponTypeById(Long id) {
        return tCouponTypeMapper.selectTCouponTypeById(id);
    }

    /**
     * 查询优惠券种类列表
     *
     * @param tCouponType 优惠券种类
     * @return 优惠券种类
     */
    @Override
    public List<TCouponType> selectTCouponTypeList(TCouponType tCouponType) {
        return tCouponTypeMapper.selectTCouponTypeList(tCouponType);
    }

    /**
     * 新增优惠券种类
     *
     * @param tCouponType 优惠券种类
     * @return 结果
     */
    @Override
    public int insertTCouponType(TCouponType tCouponType) {
        tCouponType.setCreateTime(DateUtils.getNowDate());
        tCouponType.setCreateBy(SecurityUtils.getUsername());
        return tCouponTypeMapper.insertTCouponType(tCouponType);
    }

    /**
     * 修改优惠券种类
     *
     * @param tCouponType 优惠券种类
     * @return 结果
     */
    @Override
    public int updateTCouponType(TCouponType tCouponType) {
        tCouponType.setUpdateTime(DateUtils.getNowDate());
        tCouponType.setUpdateBy(SecurityUtils.getUsername());
        TCouponDetail tCouponDetail = new TCouponDetail();
        tCouponDetail.setCouponId(tCouponType.getId());
        if (itCouponDetailService.countCouponDetail(tCouponDetail) > 0) {
            return -1;
        }
        return tCouponTypeMapper.updateTCouponType(tCouponType);
    }

    /**
     * 批量删除优惠券种类
     *
     * @param ids 需要删除的优惠券种类主键
     * @return 结果
     */
    @Override
    public int deleteTCouponTypeByIds(Long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            TCouponDetail tCouponDetail = new TCouponDetail();
            tCouponDetail.setCouponId(ids[i]);
            if (itCouponDetailService.countCouponDetail(tCouponDetail) > 0) {
                return -1;
            }
        }
        return tCouponTypeMapper.deleteTCouponTypeByIds(ids);
    }

    /**
     * 删除优惠券种类信息
     *
     * @param id 优惠券种类主键
     * @return 结果
     */
    @Override
    public int deleteTCouponTypeById(Long id) {
        return tCouponTypeMapper.deleteTCouponTypeById(id);
    }

    @Override
    public List<CouponTypeVo> countAppCoupon(CouponTypeVo couponType) {
        return tCouponTypeMapper.countCouponForApp(couponType);

    }

    @Override
    public Long countCouponType(Long userId) {
        LambdaQueryWrapper<TCouponType> couponTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        couponTypeLambdaQueryWrapper.eq(TCouponType::getUserId, userId);
        return tCouponTypeMapper.selectCount(couponTypeLambdaQueryWrapper);
    }
}
