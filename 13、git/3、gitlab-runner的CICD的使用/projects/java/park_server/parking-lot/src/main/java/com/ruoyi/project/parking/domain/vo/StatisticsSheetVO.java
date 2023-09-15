package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class StatisticsSheetVO implements Serializable {
    private String parkNo;

    private String startDate;

    private String endDate;

    private String startTime;

    private String endTime;
}
