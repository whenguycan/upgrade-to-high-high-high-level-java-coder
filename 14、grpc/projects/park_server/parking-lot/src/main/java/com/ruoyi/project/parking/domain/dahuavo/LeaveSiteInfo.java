package com.ruoyi.project.parking.domain.dahuavo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LeaveSiteInfo implements Serializable,Cloneable {
    //操作时间格式
    private String actTime;
    //操作类型（0代表月租长包车辆， 1代表时租访客车辆， 2代表免费车辆， 3代表异常未知车辆）
    private String actType;
    //车牌号
    private String carNum;
    //车辆类型
    private String carType;
    //进场时间
    private String leaveTime;
    //停车时长
    private String parkingTimeLength;
    //停车场编号
    private String parkingLot;
    //道闸编号
    private String sluiceDevChnId;
    //道闸名称
    private String sluiceDevChnName;
    //岗亭编号
    private String watchHouseCode;
    //图片存储路径
    private String carImgUrl;
    //车牌颜色
    private String carNumColor;
    //车身颜色
    private String parkingCarColor;
    //车标
    private String parkingCarLogo;
    //车型
    private String parkingCarType;
    //总余位数
    private String totRemainNum;
    //平台code
    private String platFormCode;
    /*******************以下是添加的业务字段***/
    //通道编号 自定义的一个字段
    private String passageNo;
    //通道ID 自定一个字段
    private Integer passageId;
}
