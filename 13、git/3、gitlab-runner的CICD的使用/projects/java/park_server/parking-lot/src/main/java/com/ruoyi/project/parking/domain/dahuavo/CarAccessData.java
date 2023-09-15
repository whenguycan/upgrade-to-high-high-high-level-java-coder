package com.ruoyi.project.parking.domain.dahuavo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CarAccessData implements Serializable {
    private int ownerType;
    private String parkingLotCode;
    private String enterSluiceDevChnid;
    private String realCapturePicPathEnter;
    private String parkingLot;
    private int enterMode;
    private int carStatus;
    private String originalPicPathEnter;
    private String carNum;
    private String enterSluiceDevChnname;
    private String enterWayCode;
    private String payedMoney;
    private String enterItcDevChnname;
    private int carType;
    private String enterItcDevChnid;
    private String carTypeStr;
    private String enterTime;
}
