package com.czdx.parkingcharge.common.enums;

import com.czdx.parkingcharge.domain.pr.ParkingRuleEnums;

public enum YesOrNo {

    YES("Y", "是"),
    NO("N", "否");

    private String value;

    private String desc;

    YesOrNo(String value, String desc) {
        this.desc = desc;
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public String getValue() {
        return value;
    }

    public static YesOrNo getByValue(String value) {
        for (YesOrNo way : values()) {
            if (way.value.equals(value)) {
                return way;
            }
        }
        return NO;
    }
}
