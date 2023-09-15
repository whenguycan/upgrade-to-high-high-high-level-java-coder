package com.ruoyi.project.parking.domain.vo.invoice;

import lombok.Data;

/**
 * 停车订单 申请开票 响应参数
 */
@Data
public class ApplyBillResponseVO {
    private String status;
    private String mess;
    private String outTradeNo;
}
