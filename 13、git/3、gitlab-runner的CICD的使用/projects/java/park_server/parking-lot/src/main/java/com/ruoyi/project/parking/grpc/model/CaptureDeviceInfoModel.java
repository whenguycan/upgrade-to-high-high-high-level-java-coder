package com.ruoyi.project.parking.grpc.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CaptureDeviceInfoModel implements Serializable {

    //车牌号码
    private String carNum;
    //车场编号
    private String parkNo;
    //设备本地ip
    private String deviceLocalIp;
    //抓拍图片链接
    private String carImgUrl;
    //车身颜色（Red）
    private String carColor;
    //车牌图片url
    private String carNumPic;
    //车辆颜色，见颜色枚举（Blue）
    private String carNumColor;
    //车辆种类  桥车，卡车（SaloonCar）
    private String parkingCarType;
    //车辆种类  桥车，卡车（小型车）-暂不用
    private String parkingCarSize;
    //车辆颜色，见颜色枚举（Normal）-暂不用
    private String carNumType;
    //抓拍时间 yyyy-MM-dd HH:mm:ss
    private String capTime;
}
