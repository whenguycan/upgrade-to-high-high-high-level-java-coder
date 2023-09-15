package com.czdx.parkingcharge.domain.pf;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * description: 停车费用
 * @author mingchenxu
 * @date 2023/2/25 09:23
 */
@Data
public class ParkingFee {

    /**
     * 车场编号
     */
    private String parkNo;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 应付金额
     */
    private BigDecimal payableAmount;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 实付金额
     */
    private BigDecimal payAmount;

    /**
     * 停车费用明细
     */
    private List<ParkingFeeItem> feeItems;

    /**
     * 优惠券信息
     */
    private List<ChargeCouponInfo> couponItems;

    /**
     * 车类型（临时车还是固定车）
     */
    private String carCategory;

}
