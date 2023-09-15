package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 闸道抬杆原因
 */
@Data
public class SettingLiftGateReasonVO {
    /**
     * 场库编号
     */
    private String parkNo;

    /**
     * 抬杆理由
     */
    private String reason;

    /**
     * 备注
     */
    private String remark;

}
