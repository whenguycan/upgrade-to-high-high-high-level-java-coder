package com.czdx.parkingcharge.domain.pr;

import lombok.Data;

/**
 *
 * description: 车场计费规则模型
 * @author mingchenxu
 * @date 2023/3/30 13:22
 */
@Data
public class ParkRuleModel {

    /**
     * 车场编号
     */
    private String parkNo;

    /**
     * 节假日类型(1-假期,2-调休)
     */
    private String holidayType;

    /**
     * 停车场区域标识(ALL-全部(除专门设定的停车场外
     */
    private String parkLotSign;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 车类型标识(LS-临时车,GD-全部固定车(除专门设定的固定车外),其他-固定车类型ID)
     */
    private String vehicleCategory;

    /**
     * 固定车分组ID
     */
    private Integer regularCategoryGroupId;

    /**
     * 固定车类型ID
     */
    private Integer regularCategoryId;

    /**
     * 车型标识(ALL-全部(除专门设定的车型外),其他-车型ID)
     */
    private String vehicleType;

    /**
     * Drools 规则前缀
     */
    private String rulePrefix;

    /**
     * 收费规则ID
     */
    private Integer chargeRuleId;

    /**
     * 使用免费时长
     */
    private boolean useFreeTime = true;


    public ParkRuleModel() {
    }

    public ParkRuleModel(String parkNo, String carNumber, String vehicleCategory, String vehicleType) {
        this.parkNo = parkNo;
        this.vehicleCategory = vehicleCategory;
        this.vehicleType = vehicleType;
    }
}
