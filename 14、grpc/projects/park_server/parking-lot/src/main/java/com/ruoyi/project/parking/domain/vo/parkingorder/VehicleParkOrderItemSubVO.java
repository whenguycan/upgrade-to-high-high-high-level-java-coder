package com.ruoyi.project.parking.domain.vo.parkingorder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 停车订单 明细项
 * 订单系统 返回值对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleParkOrderItemSubVO extends VehicleParkOrderItemVO {

    /**
     * 场区名称
     */
    private String fieldName;

    /**
     * 停车入场时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryTimeDate;

    /**
     * 停车出场时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime exitTimeDate;


    @Override
    public void setEntryTime(String entryTime) {
        this.entryTimeDate = DateUtils.parseMilliStringToLocalDateTime(entryTime);
        super.setEntryTime(entryTime);
    }

    @Override
    public void setExitTime(String exitTime) {
        this.exitTimeDate = DateUtils.parseMilliStringToLocalDateTime(exitTime);
        super.setExitTime(exitTime);
    }
}
