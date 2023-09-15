package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GISStatisticsVO {
    //本年收入
    private BigDecimal yearAmount;
    //今日收入
    private BigDecimal dayAmount;
    //今日访客
    private int dayVisitor;
    //进出车辆
    private int turnoverNum;
    //入场车辆
    private int entryNum;
    //出场车辆
    private int exitNum;
}
