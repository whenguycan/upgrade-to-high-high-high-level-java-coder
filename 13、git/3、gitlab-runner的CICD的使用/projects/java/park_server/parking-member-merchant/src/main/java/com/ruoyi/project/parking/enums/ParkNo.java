package com.ruoyi.project.parking.enums;

public enum ParkNo {
    WEST_TAI_LAKE_GROUP("P20230000000001", "西太湖集团");

    private String value;

    private String desc;

    ParkNo(String value, String desc) {
        this.desc = desc;
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public String getValue() {
        return value;
    }
}
