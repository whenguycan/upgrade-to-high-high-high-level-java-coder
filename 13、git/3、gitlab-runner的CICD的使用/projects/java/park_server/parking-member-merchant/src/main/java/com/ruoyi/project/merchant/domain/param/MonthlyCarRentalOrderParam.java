package com.ruoyi.project.merchant.domain.param;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyCarRentalOrderParam {

    /**
     * 逻辑ID
     */
    private Integer id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 支付方式 1为支付宝 2为微信
     */
    private int payType;

    /**
     * 支付类型 1：jsapi 2：h5
     */
    private int weChatPayMethod;

    /**
     * 用户的客户端IP，支持IPv4和IPv6两种格式的IP地址。
     */
    private String payerClientIp;

    /**
     * 场景类型
     * 示例值：iOS, Android, Wap
     */
    private String h5Type;
}
