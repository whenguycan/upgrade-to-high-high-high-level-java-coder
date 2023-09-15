package com.ruoyi.project.parking.domain.vo.parkingorder;

import lombok.Data;

@Data
public class SearchOrderRequestVO {
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 支付方式 1 支付宝
     */
    private Integer paymetnod;
    private Integer pageNum;
    private Integer pageSize;

    /**
     * 车牌号
     */
    private String carNumber;
}
