package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CarVolumeSheetVO implements Serializable {

    private String parkNo;

    private String startDate;

    private String endDate;

    //统计方式 1-按日 2-按小时
    private String type;
}
