package com.ruoyi.project.parking.domain.vo.parkingorder;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 车辆 场区停车记录
 * 进场记录 + 出场记录
 */
@Data
public class VehicleParkFieldRecordVO {

    /**
     * 场区名称 from
     */
    private String fieldNameFrom;

    /**
     * 场区id from
     */
    private Integer fieldIdFrom;

    /**
     * 场区名称 to
     */
    private String fieldNameTo;

    /**
     * 场区id to
     */
    private Integer fieldIdTo;

    /**
     * 记录时间
     * 进场/出场 时间
     */
    private LocalDateTime recordTime;

    /**
     * 通道编号
     */
    private String passageNo;

    /**
     * 通道名称
     */
    private String passageName;

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
     * 记录截图
     * 进场/出场 截图
     */
    private String carImgUrl;
}
