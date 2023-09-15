package com.ruoyi.project.parking.domain.dahuavo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CarCaptureData implements Serializable {
    private Long id;
    //抓拍时间 yyyy-MM-dd HH:mm:ss
    private String capTime;
    //车牌号码
    private String carNum;
    //行车方向 8进 9出
    private int carDirect;
    //设备通道id
    private String devChnId;
    //设备id  --无用
    private String devId;
    //设备名称 --无用
    private String devName;
    //抓拍图片链接
    private String carImgUrl;
    private int carColor;
    //车牌图片url
    private String carNumPic;
    //设备通道名称
    private String devChnName;
    private int carBrand;
    //车辆颜色，见颜色枚举
    private int carNumColor;
    //车辆进出类型（0：外部车，1：内部车）
    private String carInnerCategory;
    //车道号
    private String carWayCode;
    //相机关联的车场编码，相机未绑定车场时无此字段
    private String parkingLotCode;
    //相机关联的车场名称，相机未绑定车场时无此字段
    private String parkingLot;
    //相机关联的道闸的组织编码， 未关联道闸时无此字段
    private String sluiceOrgCode;
    //行车方向记录 驶入 和驶出
    private String carDirectStr;
    //业务系统用到的有效数据
    private ValidChannelVo validChannelVo;



}
