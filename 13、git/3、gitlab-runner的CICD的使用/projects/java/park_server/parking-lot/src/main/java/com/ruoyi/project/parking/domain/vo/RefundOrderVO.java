package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

@Data
public class RefundOrderVO {

    //订单号
    private String orderNo;

    //退款原因
    private String reason;

    //操作人
    private String createBy;
}
