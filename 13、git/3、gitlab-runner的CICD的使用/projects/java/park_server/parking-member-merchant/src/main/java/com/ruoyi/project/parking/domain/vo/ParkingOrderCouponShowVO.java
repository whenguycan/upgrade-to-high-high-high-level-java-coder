package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

/**
 * 停车订单优惠券
 * 带优惠券信息
 */
@Data
public class ParkingOrderCouponShowVO {

    /**
     * 优惠券码
     */
    private String couponCode;

    /**
     * 是否选中
     */
    private boolean choosed;

    /**
     * 是否能被使用
     */
    private boolean canUse;

    /**
     * 优惠券信息
     */
    private MemberCouponVO coupon;
}
