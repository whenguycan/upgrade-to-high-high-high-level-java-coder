package com.ruoyi.project.parking.service.grpcclient.model;

import lombok.Data;

@Data
public class OpenCloseChannelRequest {
    private String deviceIp;
    private String carNum ;
    private String status;
    private String parkNo;
    private String entryOrExitId ;
}
