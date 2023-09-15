package com.ruoyi.common.enums;

public enum OnlineFlag {
    OFF_LINE(0, "线下"), ALL(1, "线上+线下");
    private final Integer code;
    private final String info;

    OnlineFlag(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
