package com.czdx.parkingorder.project.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatisticIncomeVo {

    // 今日收入（元）
    private BigDecimal todayIncome;

    // 总收入（元）
    private BigDecimal totalIncome;
}
