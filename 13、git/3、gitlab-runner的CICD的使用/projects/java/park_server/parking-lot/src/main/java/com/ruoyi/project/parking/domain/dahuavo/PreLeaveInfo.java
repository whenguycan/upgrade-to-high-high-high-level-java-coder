package com.ruoyi.project.parking.domain.dahuavo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString(callSuper=true)
public class PreLeaveInfo extends BusinessBaseICCEntity {
//    //操作时间格式
//    private String actTime;
    //操作类型（0代表月租长包车辆， 1代表时租访客车辆， 2代表免费车辆， 3代表异常未知车辆）
//    //车牌号
//    private String carNum;
//    //车辆类型
//    private String carType;
    //    //道闸编号
//    private String sluiceDevChnId;
//    //道闸名称
//    private String sluiceDevChnName;

    //图片存储路径
//    private String carImgUrl;
    //车型
//    private String parkingCarType;
    //车身颜色
//    private String parkingCarColor;
    //车牌颜色
    //  private String carNumColor;
    private String actType;
    //进入时间
    private String arriveTime;

    //进场时间
    private String leaveTime;
    //停车时长
    private String parkingTimeLength;
    //停车场编号
    private String parkingLot;

    //车标
    private String parkingCarLogo;

    //总余位数
    private String totRemainNum;
    private CommonParam commonParam;
    //岗亭编号
    private String watchHouseCode;


}
