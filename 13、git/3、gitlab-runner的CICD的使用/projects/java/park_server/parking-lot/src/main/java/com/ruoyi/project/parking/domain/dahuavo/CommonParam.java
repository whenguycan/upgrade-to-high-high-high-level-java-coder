package com.ruoyi.project.parking.domain.dahuavo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonParam implements Serializable {
    private String appVer;
    private String flag;
    private String psam;
    private String sim;
    private String sysVer;
    private String tsn;
}
