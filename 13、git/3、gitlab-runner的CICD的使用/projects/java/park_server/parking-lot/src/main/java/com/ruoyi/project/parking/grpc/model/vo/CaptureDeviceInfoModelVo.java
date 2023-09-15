package com.ruoyi.project.parking.grpc.model.vo;

import com.ruoyi.project.parking.domain.dahuavo.ValidChannelVo;
import com.ruoyi.project.parking.grpc.model.CaptureDeviceInfoModel;
import lombok.Data;

@Data
public class CaptureDeviceInfoModelVo extends CaptureDeviceInfoModel {

    //设备通道id
    private String devChnId;
    //业务系统用到的有效数据
    private ValidChannelVo validChannelVo;

}
