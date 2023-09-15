package com.ruoyi.project.parking.domain.vo;

import com.ruoyi.project.parking.domain.vo.parkingorder.VehicleParkOrderVO;
import lombok.Data;

import java.util.List;

/**
 * 离场车辆 详情记录
 */
@Data
public class ParkSettlementRecordsDetailVO {

    /**
     * 进场图片存储路径
     */
    private String carImgUrlFrom;

    /**
     * 出场图片存储路径
     */
    private String carImgUrlTo;

    /**
     * 场地信息
     */
    private List<ParkSettlementRecordsFieldVO> fieldInfo;

    /**
     * 停车订单信息
     */
    private List<VehicleParkOrderVO> ParkingOrderInfo;
}
