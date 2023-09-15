package com.ruoyi.project.parking.domain.vo.invoice;

import lombok.Data;

/**
 * 开票订单详情
 */
@Data
public class ParkingOrderInvoiceInfoVO {
    private String billOutTradeNo;

    private String billOrderNos;

    private String billcreateTime;

    private String type;

    private double billAmount;

    private String billStatus;

    private String billPdfUrl;
}
