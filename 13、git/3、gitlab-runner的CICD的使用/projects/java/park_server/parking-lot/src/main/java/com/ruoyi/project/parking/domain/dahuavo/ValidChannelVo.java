package com.ruoyi.project.parking.domain.dahuavo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ValidChannelVo  implements Serializable, Cloneable {
    //车牌号
    private String carNum;
    //车辆类型
    private String carType;
    //进场时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime actTime;
    //ip
    private String deviceIp;
    //道闸编号,设备编号
    private String sluiceDevChnId;
    //道闸名称
    private String sluiceDevChnName;
    //图片存储路径
    private String carImgUrl;
    //车牌图片url
    private String carNumPic;
    private String parkingCarColor;
    private String carNumColor;
    //预到达时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime preArriveTime;

    //预离场时间
    private String preLeaveTime;
    //预离场时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime leaveTime;
    /*******************以下是添加的业务字段***/
    private String durationTime;
    //场地编号
    private String parkNo;
    //通道编号 自定义的一个字段
    private String passageNo;
    //通道ID 自定一个字段
    private Integer passageId;
    //通道名称
    private String passageName;
    private String fromFieldName;
    private String toFieldName;
    private String entryOrExitId;
    private String passageFlag;

    /**
     * 应付金额（元）
     */
    private Double payableAmount;
    /**
     * 进场时间 ，预离场的时候才有
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime entryTime;
    //订单编号
    private String orderNo;
}
