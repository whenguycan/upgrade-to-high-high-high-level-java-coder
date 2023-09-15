package com.ruoyi.project.parking.domain.param;

import lombok.Data;

import java.util.List;

/**
 * 手动结算参数
 */
@Data
public class ParkLiveRecordsManualComputationParam {

    /**
     * 在场记录id 列表
     */
    private List<Integer> liveIdList;
    /**
     * 抬杆原因
     */
    private String liftGateReason;
    /**
     * 结算金额
     */
    private Double payAmount;
}
