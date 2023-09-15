package com.czdx.parkingcharge.domain.pf;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * description: 停车费用明细
 * @author mingchenxu
 * @date 2023/2/25 09:23
 */
@Data
public class ParkingFeeItem {

    /**
     * 车场场地ID
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
     * 停车时长（分钟）
     */
    private Integer parkingTime;

    /**
     * 应付金额
     */
    private BigDecimal payableAmount;

}
