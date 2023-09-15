package com.ruoyi.project.common;

import io.jsonwebtoken.Claims;

/**
 * 优惠券通用的常量名
 *
 * @author ruoyi
 */
public class CouponConstants {
    //商户作废优惠券订单
    public static final String CANCEL_ORDER_TYPE = "4";
    //商户购买优惠券订单
    public static final String MERCHANT_PURCHASE_ORDER_TYPE = "5";
    //商户充值订单类型
    public static final String RECHARGE_ORDER_TYPE = "3";
    //订单状态
    public static final String PAYED_STATUS = "03";
    //充值状态
    public static final Integer RECHARGE_STATUS = 1;
    //消费状态
    public static final int CONSUME_STATUS = 3;
    //回收状态
    public static final Integer RETRIEVE_STATUS = 4;
    //退款状态
    public static final int REFUND_STATUS = 5;

    //作废状态
    public static final int CANCEL_STATUS = 6;

    //面值型
    public static final String COUPON_TYPE_FACE_VALUE = "1";
    //小时型
    public static final String COUPON_TYPE_HOUR = "2";

    //冻结状态
    public static final String FREEZE_STATUS = "1";
    //未冻结状态
    public static final String NO_FREEZE_STATUS = "0";


}
