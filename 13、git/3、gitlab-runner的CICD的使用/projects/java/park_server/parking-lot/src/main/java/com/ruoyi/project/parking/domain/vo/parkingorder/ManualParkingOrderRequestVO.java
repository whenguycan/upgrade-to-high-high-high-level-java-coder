package com.ruoyi.project.parking.domain.vo.parkingorder;

import lombok.Data;

/**
 * 手动结算 请求类
 */
@Data
public class ManualParkingOrderRequestVO {
    private String parkNo;
    // 车牌号
    private String carNumber;
    // 车型编码
    private String carTypeCode;
    // 入场时间
    private String entryTime;
    //出场时间
    private String exitTime;
    // 手动结算金额
    Double manualAmount;
    // 手动结算原因
    private String manualReason;
}
