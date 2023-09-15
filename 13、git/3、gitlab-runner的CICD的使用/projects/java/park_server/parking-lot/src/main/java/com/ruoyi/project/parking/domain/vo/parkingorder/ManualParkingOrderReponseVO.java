package com.ruoyi.project.parking.domain.vo.parkingorder;

import lombok.Data;

/**
 * 手动结算 请求响应类
 */
@Data
public class ManualParkingOrderReponseVO {
    // 返回状态码
    private String status;
    // 返回消息
    private String mess;
    // 返回订单详情
    private String orderNo;
}
