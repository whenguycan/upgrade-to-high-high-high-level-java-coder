package com.ruoyi.project.parking.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 离场车辆 场地信息
 * 停车记录 按照 场地信息 展示
 * 计算出每个区域的 进出通道信息
 */
@Data
public class ParkSettlementRecordsFieldVO {

    // region 含 VehicleParkOrderItemVO

    /**
     * 场区id
     */
    private Integer parkFieldId;

    /**
     * 入场时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryTime;

    /**
     * 出场时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime exitTime;

    /**
     * 区域应付金额
     */
    private Double payedAmount;

    // endregion

    /**
     * 区域名称
     */
    private String parkFieldName;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 车型码
     */
    private String carType;

    /**
     * 车型名
     */
    private String carTypeName;

    /**
     * 入场通道名
     */
    private String entryPassageName;

    /**
     * 出场通道名
     */
    private String exitPassageName;

    /**
     * 停留时长
     */
    private String durationTime;

    /**
     * 进出场图片
     */
    private String imgUrl;

}
