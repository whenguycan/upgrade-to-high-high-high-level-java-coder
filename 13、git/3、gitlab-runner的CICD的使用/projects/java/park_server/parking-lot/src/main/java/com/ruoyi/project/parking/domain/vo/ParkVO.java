package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

@Data
public class ParkVO {
    //停车场ID
    private Long parkId;
    //停车场编号
    private String parkNo;
    //停车场名称
    private String parkName;
    //停车场纬度
    private String latitude;
    //停车场经度
    private String longitude;
    //停车场状态 1-空闲 2-较少 3-已满
    private String status;
}
