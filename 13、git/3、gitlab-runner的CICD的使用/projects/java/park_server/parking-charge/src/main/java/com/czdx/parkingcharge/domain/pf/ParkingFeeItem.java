package com.czdx.parkingcharge.domain.pf;

import com.czdx.parkingcharge.system.gson.LocalDateTimeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
     * 入场时间
     */
    private transient LocalDateTime entryTimeLDT;

    /**
     * 出场时间
     */
    private transient LocalDateTime exitTimeLDT;

    /**
     * 停车时长（分钟）
     */
    private Integer parkingTime;

    /**
     * 应付金额
     */
    private BigDecimal payableAmount;

}
