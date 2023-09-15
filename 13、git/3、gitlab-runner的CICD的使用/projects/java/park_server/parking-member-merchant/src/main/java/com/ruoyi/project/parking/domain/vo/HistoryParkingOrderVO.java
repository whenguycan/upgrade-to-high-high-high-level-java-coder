package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

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

    // 入场时间 2023-03-01 12:00:00
    private String entryTime;

    //出场时间 2023-03-01 12:00:00
    private String exitTime;

    // 停车时长 1天2小时3分钟
    private String durationTime;

    // 支付金额
    private Double payAmount;

    // 有效状态 有效标记 0 无效(未支付或支付失败) 1 有效 支付成功  状态有延期非实时
    private Integer effectFlag;
}
