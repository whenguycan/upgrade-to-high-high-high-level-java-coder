package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 车辆类型
 */
@Data
public class SettingCarTypeVO {
    /**
     * 场库编号
     */
    private String parkNo;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 车辆类型码
     */
    private String code;

    /**
     * 备注
     */
    private String remark;
}
