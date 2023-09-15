package com.ruoyi.project.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.project.merchant.domain.TCouponDetail;
import com.ruoyi.project.merchant.domain.TCouponType;
import com.ruoyi.project.merchant.domain.vo.MerchantVo;
import com.ruoyi.project.merchant.mapper.TCouponDetailMapper;
import com.ruoyi.project.merchant.mapper.TCouponTypeMapper;
import com.ruoyi.project.merchant.service.IMerchantService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.mapper.SysUserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商户管理
 */
@Service
public class MerchantServiceImpl implements IMerchantService {
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    TCouponTypeMapper tCouponTypeMapper;
    @Autowired
    TCouponDetailMapper tCouponDetailMapper;

    @Override
    public List<SysUser> selectMerchantList(SysUser sysUser) {
        return sysUserMapper.selectMerchantList(sysUser);
    }

    @Override
    public MerchantVo selectMerchantById(Long merchantId) {
        SysUser sysUser = sysUserMapper.selectUserById(merchantId);
        MerchantVo merchantVo = new MerchantVo();
        BeanUtils.copyProperties(sysUser, merchantVo);
        QueryWrapper<TCouponType> tCouponTypeQueryWrapper = new QueryWrapper();
        tCouponTypeQueryWrapper.eq("user_id", merchantId);
        List<TCouponType> couponTypeList = tCouponTypeMapper.selectList(tCouponTypeQueryWrapper);
        List<Long> ids = couponTypeList.stream().map(TCouponType::getId).collect(Collectors.toList());

        QueryWrapper<TCouponDetail> tCouponDetailQueryWrapper = new QueryWrapper<>();
        tCouponDetailQueryWrapper.eq("coupon_status", "0").in("coupon_id", ids);
        merchantVo.setCouponCount(tCouponDetailMapper.selectCount(tCouponDetailQueryWrapper));
        return merchantVo;
    }

    @Override
    public SysUser selectOneMerchantById(Long merchantId) {
        return sysUserMapper.selectUserById(merchantId);
    }
}
