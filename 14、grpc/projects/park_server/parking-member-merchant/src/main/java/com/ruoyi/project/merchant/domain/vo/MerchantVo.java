package com.ruoyi.project.merchant.domain.vo;

import com.ruoyi.project.system.domain.SysUser;
import lombok.Data;

@Data
public class MerchantVo extends SysUser {
    /**
     * 优惠券张数
     */
    private Long couponCount;
}
