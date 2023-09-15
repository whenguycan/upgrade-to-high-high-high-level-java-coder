package com.ruoyi.project.merchant.domain.bo;


import lombok.Data;

import java.time.LocalDate;

@Data
public class RegularCarBO {
    /**
     * 固定车记录id
     */
    private Integer id;

    /**
     * 场库编号
     */
    private String parkNo;

    /**
     * 场库名称
     */
    private String parkName;

    /**
     * 固定车车牌号
     */
    private String carNumber;

    /**
     * 固定车车辆颜色
     */
    private String carColor;

    /**
     * 固定车车牌备注
     */
    private String carRemark;

    /**
     * 固定车车主身份证号
     */
    private String ownerCardId;

    /**
     * 固定车车主姓名
     */
    private String ownerName;

    /**
     * 固定车车主联系地址
     */
    private String ownerAddress;

    /**
     * 固定车车主联系电话
     */
    private String ownerPhone;

    /**
     * 流动车位数
     */
    private Integer flowPlaceNumber;

    /**
     * 有效期开始时间
     */
    private LocalDate startTime;

    /**
     * 有效期结束时间
     */
    private LocalDate endTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否可续费 false：不可续费 true：可续费
     */
    private boolean renewFlag;
}
