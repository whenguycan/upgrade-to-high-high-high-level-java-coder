package com.ruoyi.project.parking.domain.bo;

import com.ruoyi.project.parking.domain.TEntryRecords;
import com.ruoyi.project.parking.domain.TExitRecords;
import com.ruoyi.project.parking.domain.vo.ParkLiveRecordsVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.VehicleParkOrderVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 在场记录
 * ParkLiveRecordsBO = {@link ParkLiveRecordsVO} + 业务关联信息（订单、进出场记录）
 */
@Data
public class ParkLiveRecordsBO {

    // region 基础信息
    /**
     * 在场记录 id
     */
    private Integer id;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 车辆状态【1-在场；2-离场；3-异常】
     */
    private String carStatus;

    /**
     * 停车场编号
     */
    private String parkNo;

    /**
     * 车型【字典】
     */
    private String carType;

    /**
     * 入场时间
     */
    private LocalDateTime entryTime;

    /**
     * 出场时间
     */
    private LocalDateTime exitTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;
    // endregion

    // region 业务可视化信息
    /**
     * 车型【字典】名
     */
    private String carTypeName;


    /**
     * 通道id （进口）
     */
    private Integer passageId;

    /**
     * 通道名称 （进口）
     */
    private String passageName;

    /**
     * 场地名称（进口从）
     */
    private String fieldNameFrom;

    /**
     * 场地名称（出口去）
     */
    private String fieldNameTo;

    /**
     * 进场图片存储路径
     */
    private String carImgUrlFrom;

    /**
     * 出场图片存储路径
     */
    private String carImgUrlTo;

    // endregion

    // region 业务关联信息
    /**
     * 进场记录
     */
    private List<TEntryRecords> entryRecordList;

    /**
     * 出场记录
     */
    private List<TExitRecords> exitRecordList;

    /**
     * 停车订单信息 列表
     */
    private List<VehicleParkOrderVO> orderList;
    // endregion

}
