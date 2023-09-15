package com.ruoyi.project.merchant.service;

import com.ruoyi.project.merchant.domain.vo.MerchantVo;
import com.ruoyi.project.system.domain.SysUser;

import java.util.List;

/**
 * 商户管理Service
 */
public interface IMerchantService {
    public List<SysUser> selectMerchantList(SysUser sysUser);

    public MerchantVo selectMerchantById(Long merchantId);


    public SysUser selectOneMerchantById(Long merchantId);
}
