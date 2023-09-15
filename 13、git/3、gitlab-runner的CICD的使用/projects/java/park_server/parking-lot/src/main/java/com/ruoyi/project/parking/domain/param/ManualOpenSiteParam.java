package com.ruoyi.project.parking.domain.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class ManualOpenSiteParam implements Serializable {
    private String freeReason;
    private Double needPayAmount;
    private String orderNo;
    private String passageNo;

    private String parkNo;
    private String carNumber;
}
