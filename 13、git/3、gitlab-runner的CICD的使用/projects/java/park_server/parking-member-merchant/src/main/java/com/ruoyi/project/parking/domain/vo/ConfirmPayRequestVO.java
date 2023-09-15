package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 拉取支付 请求参数
 */
@Data
public class ConfirmPayRequestVO {
    /**
     * 订单号
     */
    private String orderNo;
    //支付类型 1为支付宝 2为微信
    private Integer payType;

    // region 微信支付参数
    // 微信支付的支付方式 1为JSAPI支付 2为H5支付
    private Integer weChatPayMethod;

    // 如果是微信的jsapi支付，这个参数必传
    private String openid;

    //下面如果是微信h5支付的情况下，才要传参
    // 用户的客户端IP，支持IPv4和IPv6两种格式的IP地址。
    private String payerClientIp;
    // 场景类型 示例值：iOS, Android, Wap
    private String h5Type;
    // endregion

    /**
     * 选择的优惠券
     */
    private List<ParkingOrderCouponVO> couponList;

    /**
     * 优惠原因
     */
    private String discountReason;
}
