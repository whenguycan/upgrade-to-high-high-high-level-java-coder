package com.ruoyi.project.parking.domain.dahuavo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DaHuaCarAccessSendData implements Serializable {
    private String id;
    private CarAccessData info;
    private String method;
    private String subsystem;
}
