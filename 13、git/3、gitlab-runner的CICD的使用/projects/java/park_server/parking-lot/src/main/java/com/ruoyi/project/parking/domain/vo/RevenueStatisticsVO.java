package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RevenueStatisticsVO {

    /** 今日收入 */
    private BigDecimal todayIncome;

    /** 本月收入 */
    private BigDecimal monthIncome;

    /** 昨日收入 */
    private BigDecimal yesterdayIncome;
}
