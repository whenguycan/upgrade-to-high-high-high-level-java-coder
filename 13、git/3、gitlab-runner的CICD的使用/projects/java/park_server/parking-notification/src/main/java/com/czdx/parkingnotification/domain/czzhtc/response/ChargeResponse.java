package com.czdx.parkingnotification.domain.czzhtc.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChargeResponse {
    //车牌号
    private String plate;
    //应收金额 (扣除已收费、减免后的金额)
    private double should;
    //减免金额
    private double freeMoney;
    //已收费金额
    private double advPaid;
    //账单标识（billID必填）
    private int billID;
    //入场时间
    private LocalDateTime recordTime;
    //计费时间
    private LocalDateTime billTime;
}
