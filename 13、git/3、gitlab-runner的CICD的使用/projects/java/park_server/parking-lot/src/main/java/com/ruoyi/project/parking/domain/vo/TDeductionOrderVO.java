package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

@Data
public class TDeductionOrderVO {
    /**
     * 在场记录id
     */
    private Integer parkLiveId;

    /**
     * 停车场编号
     */
    private String parkNo;

    /**
     * 订单类型 1停车订单
     */
    private Integer orderType;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 抵扣类型
     */
    private Integer deductionType;

    /**
     * 抵扣原因
     */
    private String deductionReason;

    /**
     * 备注
     */
    private String remark;
}
