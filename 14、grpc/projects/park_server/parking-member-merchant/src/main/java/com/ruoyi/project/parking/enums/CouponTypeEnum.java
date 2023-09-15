package com.ruoyi.project.parking.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 优惠卷类型
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CouponTypeEnum implements BaseEnum {
    AMOUNT_RMB("1", "面值型"),
    TIME_HOUR("2", "小时型");

    private final String value;
    private final String desc;

    CouponTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
