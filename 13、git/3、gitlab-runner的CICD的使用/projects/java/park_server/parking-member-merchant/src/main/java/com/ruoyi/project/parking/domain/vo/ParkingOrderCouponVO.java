package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

/**
 * 停车订单优惠券
 */
@Data
public class ParkingOrderCouponVO {
    /**
     * 优惠券种类;1-面值型；2-小时型
     */
    private Integer couponType;

    /**
     * 优惠券类型；1-平台券；2-商户券
     */
    private Integer couponMold;

    /**
     * 优惠券值（面值型则为金额，小时型则为小时）
     */
    private Integer couponValue;

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
}

