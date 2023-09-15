package com.ruoyi.project.parking.domain.vo.parkingorder;

import lombok.Data;

@Data
public class SearchRefundOrderRecordsRequestVO {
    /**
     * 订单号
     */
    private String orderNo;
    private String parkNo;
    private Integer pageNum;
    private Integer pageSize;
    /**
     * 车牌号
     */
    private String carNumber;
}
