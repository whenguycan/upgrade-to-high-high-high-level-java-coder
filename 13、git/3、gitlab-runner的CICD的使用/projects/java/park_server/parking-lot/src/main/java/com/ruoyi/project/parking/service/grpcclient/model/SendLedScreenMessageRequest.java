package com.ruoyi.project.parking.service.grpcclient.model;

import lombok.Data;

@Data
public class SendLedScreenMessageRequest {
    private String carNumber;
    String parkingTime;
    String masterofCar;
    String userType;
    String RemainDay;
    Integer passEnable;
    String stuInTime;
    String stuOutTime;
    String parkCharges;
    Integer remainSpace;
    String carStatus;
    String subUserTypes;
    String remark;
    String deviceIp;
    Integer port;
    String parkNo;
}
