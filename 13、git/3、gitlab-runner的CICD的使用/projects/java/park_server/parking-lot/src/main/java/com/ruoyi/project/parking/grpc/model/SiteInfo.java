package com.ruoyi.project.parking.grpc.model;

import lombok.Data;

import java.io.Serializable;
@Data
public class SiteInfo implements Serializable {
    private String carNum;
    private String deviceIp;
    private String  openFlag;
}
