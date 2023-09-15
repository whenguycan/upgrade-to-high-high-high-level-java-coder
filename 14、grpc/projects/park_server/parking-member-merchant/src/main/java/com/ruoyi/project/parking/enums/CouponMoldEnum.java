package com.ruoyi.project.parking.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 优惠卷模板
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CouponMoldEnum implements BaseEnum {
    PLATFORM("1", "平台券"),
    MERCHANT("2", "商户券");

    private final String value;
    private final String desc;

    CouponMoldEnum(String value, String desc) {
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
