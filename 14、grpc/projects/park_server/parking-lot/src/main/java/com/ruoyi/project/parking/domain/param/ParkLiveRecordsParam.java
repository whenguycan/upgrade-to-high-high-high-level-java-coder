package com.ruoyi.project.parking.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 在场车辆 查询参数
 */
@Data
public class ParkLiveRecordsParam {

    /**
     * 场库编号
     */
    private String parkNo;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 车辆类型码
     */
    private String carType;

    /**
     * 通道id
     */
    private String passageId;

    /**
     * 场地id
     */
    private String fieldId;

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
     * 车辆状态
     * 1-在场；2-离场；3-异常
     */
    private String carStatus;

    /**
     * 会员ID
     */
    private String memberId;

    /** 当前记录起始索引 */
    private Integer pageNum;

    /** 每页显示记录数 */
    private Integer pageSize;
}
