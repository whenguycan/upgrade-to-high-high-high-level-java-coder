package com.ruoyi.project.parking.enums;

/**
 * 结算类型
 */
public enum SettleTypeEnum {
    MANUAL_PAY(1, "线下支付"),
    PREPAY_CODE_PAY(2, "预支付码支付"),
    PAVILION_CODE_PAY(3, "岗亭码支付");

    private final int value;
    private final String desc;


    SettleTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static SettleTypeEnum valueOf(int value) {
        for (SettleTypeEnum settleTypeEnum : values()) {
            if (settleTypeEnum.value == value) {
                return settleTypeEnum;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

}
