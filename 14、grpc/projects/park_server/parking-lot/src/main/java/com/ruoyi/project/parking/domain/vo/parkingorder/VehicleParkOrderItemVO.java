package com.ruoyi.project.parking.domain.vo.parkingorder;

import lombok.Data;

/**
 * 停车订单 明细项
 * 订单系统 输入参数对象
 */
@Data
public class VehicleParkOrderItemVO {

    /**
     * 场区id
     */
    private Integer parkFieldId;

    /**
     * 入场时间
     */
    private String entryTime;

    /**
     * 出场时间
     */
    private String exitTime;

    /**
     * 区域应付金额
     */
    private Double payedAmount;

}
