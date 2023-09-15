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
     * 支付类型 1为支付宝 2为微信
     */
    private int payType;

}
