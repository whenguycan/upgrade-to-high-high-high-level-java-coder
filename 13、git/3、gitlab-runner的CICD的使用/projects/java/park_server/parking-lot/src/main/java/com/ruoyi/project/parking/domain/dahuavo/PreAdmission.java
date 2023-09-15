package com.ruoyi.project.parking.domain.dahuavo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString(callSuper=true)
public class PreAdmission extends BusinessBaseICCEntity {


//    private String carImgUrl;
//    private String carNum;
//    private String carNumColor;
//    private String parkingCarColor;
//    private String parkingCarType;
//    private String sluiceDevChnId;
//    private String sluiceDevChnName;
//    private String actTime;
//    private String carType;

    private String actType;
    private String addBerth;
    private String berthId;
    private String bizSn;
    //1-进场  2-出场
    private String businessType;
    private CommonParam commonParam;
    private String guestRemainNum;
    private String monthlyRemainNum;

    private String parkingCarLogo;
    private String parkingLot;
    private Date preArriveTime;

    private String totRemainNum;
    private String uid;
    private String voucherType;
}
