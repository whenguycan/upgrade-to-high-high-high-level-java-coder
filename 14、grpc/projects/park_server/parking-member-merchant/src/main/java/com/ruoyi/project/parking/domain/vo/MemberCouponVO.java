package com.ruoyi.project.parking.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.project.parking.enums.CouponMoldEnum;
import com.ruoyi.project.parking.enums.CouponStatusEnum;
import com.ruoyi.project.parking.enums.CouponTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员优惠券
 */
@Data
public class MemberCouponVO {
    /**
     * 所属车场编号
     */
    private String parkNo;

    /**
     * 优惠券种类;1-面值型；2-小时型
     */
    private CouponTypeEnum couponType;

    /**
     * 优惠券类型；1-平台券；2-商户券
     */
    private CouponMoldEnum couponMold;

    /**
     * 优惠券值（面值型则为金额，小时型则为小时）
     */
    private BigDecimal couponValue;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 优惠券码
     */
    private String couponCode;

    /**
     * 优惠券状态；0-未分配;1-已分配;2-已使用;3-失效
     */
    private CouponStatusEnum couponStatus;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validStartTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validEndTime;

    /**
     * 使用时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime usedTime;

    /**
     * 订单号
     */
    private String orderNo;

}
