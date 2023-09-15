package com.ruoyi.project.parking.domain.vo.parkingorder;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 历史停车订单
 */
@Data
public class HistoryParkingOrderVO {

    // 订单编号
    private String orderNo;

    // 车牌号
    private String carNumber;

    // 停车场编号
    private String parkNo;

    // 停车场名称
    private String parkName;

    // 车型码
    private String carType;

    // 车型名
    private String carTypeName;

    // 入场时间 2023-03-01 12:00:00
    private LocalDateTime entryTime;

    //出场时间 2023-03-01 12:00:00
    private LocalDateTime exitTime;

    // 停车时长 1天2小时3分钟
    private String durationTime;

    // 支付金额
    private Double payAmount;

}
