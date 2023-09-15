package com.ruoyi.project.merchant.domain.bo;



import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MonthlyCarRentalOrderBO {
    /**
     * 逻辑ID
     */
    private Integer id;

    /**
     * 场库编号
     */
    private String parkNo;

    /**
     * 场库名称
     */
    private String parkName;

    /**
     * 固定车类型id
     */
    private Integer regularCarCategoryId;

    /**
     * 固定车类型
     */
    private BSettingRegularCarCategoryBO regularCarCategory;

    /**
     * 固定车价格id
     */
    private Integer regularCarCategoryPriceId;

    /**
     * 续费天数
     */
    private Integer rentalDays;

    /**
     * 续费价格
     */
    private BigDecimal rentalPrice;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单状态（字典表）
     */
    private String orderStatus;

    /**
     * 应付金额（元）
     */
    private BigDecimal payableAmount;

    /**
     * 优惠金额（元）
     */
    private BigDecimal discountAmount;

    /**
     * 已付金额（元）
     */
    private BigDecimal paidAmount;

    /**
     * 实付金额（元）
     */
    private BigDecimal payAmount;

    /**
     * 支付方式（字典表）
     */
    private String payMethod;

    /**
     * 下单时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    /**
     * 支付状态（字典表）
     */
    private String payStatus;

    /**
     * 发票流水号
     */
    private String billOutTradeNo;
}
