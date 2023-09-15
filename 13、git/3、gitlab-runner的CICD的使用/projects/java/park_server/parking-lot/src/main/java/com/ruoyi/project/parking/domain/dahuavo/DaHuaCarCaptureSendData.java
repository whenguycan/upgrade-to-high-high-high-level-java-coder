package com.ruoyi.project.parking.domain.dahuavo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DaHuaCarCaptureSendData implements Serializable {
    private String id;
    private String category;
    private String method;
    private CarCaptureData info;
    private String subsystem;
    private String userIds;
    private String sid;
    private String domainId;
    private String infoArray;
    private String protocol;
    private String supplementFlag;
    private String uuid;
}


