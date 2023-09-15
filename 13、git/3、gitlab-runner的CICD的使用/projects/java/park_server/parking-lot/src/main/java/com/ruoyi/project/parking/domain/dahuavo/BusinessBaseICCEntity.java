package com.ruoyi.project.parking.domain.dahuavo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BusinessBaseICCEntity implements Serializable {
    //车牌号
    private String carNum;
    //车牌颜色
    private String carNumColor;
    //图片存储路径
    private String carImgUrl;
    //车颜色
    private String parkingCarColor;
    //车辆种类  桥车，卡车
    private String parkingCarType;
    //操作时间
    private String actTime;
    //道闸编号,设备编号
    private String sluiceDevChnId;
    //道闸名称
    private String sluiceDevChnName;
    //车辆类型：
    private String carType;


}
