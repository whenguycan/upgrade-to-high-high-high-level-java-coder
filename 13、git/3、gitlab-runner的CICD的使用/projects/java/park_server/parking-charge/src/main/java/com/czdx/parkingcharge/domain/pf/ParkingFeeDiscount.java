package com.czdx.parkingcharge.domain.pf;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * description: 停车费折扣
 * @author mingchenxu
 * @date 2023/4/3 11:28
 */
@Data
public class ParkingFeeDiscount {

    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;

    /**
     * 优惠券信息
     */
    private List<ChargeCouponInfo> couponInfos;

    public ParkingFeeDiscount(BigDecimal discountAmount, List<ChargeCouponInfo> couponInfos) {
        this.discountAmount = discountAmount;
        this.couponInfos = couponInfos;
    }
}
