package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HomePageHeaderVo {

    // 今日收入（元）
    private BigDecimal todayIncome;

    // 总收入（元）
    private BigDecimal totalIncome;

    // 剩余车位数
    private Integer remainSpaceCount;

    // 访客待审批数
    private Integer notDisposedCount;

    // 固定车辆数
    private Integer regularCarCount;
}
