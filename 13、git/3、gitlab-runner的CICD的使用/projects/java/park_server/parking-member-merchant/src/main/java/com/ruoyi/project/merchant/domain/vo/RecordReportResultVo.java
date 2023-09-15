package com.ruoyi.project.merchant.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecordReportResultVo {

    private String operatorType;

    private String parkNo;

    private int num;

    private BigDecimal amount;
}
