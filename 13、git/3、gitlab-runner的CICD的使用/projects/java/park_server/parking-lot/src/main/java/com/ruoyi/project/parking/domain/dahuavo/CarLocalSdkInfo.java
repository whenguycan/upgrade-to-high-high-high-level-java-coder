package com.ruoyi.project.parking.domain.dahuavo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CarLocalSdkInfo implements Serializable {
    //车场编号
    private String parkNo;
    //设备本地ip
    private String deviceLocalIp;
    //抓拍时间 yyyy-MM-dd HH:mm:ss
    private String capTime;
    //车牌号码
    private String carNum;
    //行车方向 8进 9出
    private int carDirect;
    //设备通道id
    private String devChnId;
    //抓拍图片链接
    private String carImgUrl;
    //车身颜色（Red）
    private int carColor;
    //车牌图片url
    private String carNumPic;
    //车辆颜色，见颜色枚举（Blue）
    private int carNumColor;
    //车辆种类  桥车，卡车（SaloonCar）
    private String parkingCarType;
    //车辆种类  桥车，卡车（小型车）-暂不用
    private String parkingCarSize;
    //车辆颜色，见颜色枚举（Normal）-暂不用
    private int carNumType;
    //业务系统用到的有效数据
    private ValidChannelVo validChannelVo;
}
