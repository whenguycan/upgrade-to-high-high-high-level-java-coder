package com.ruoyi.common.enums;

public enum TimeLimit {
    UNLIMITED("0", "不限制"), LIMITED("1", "限制");
    private final String code;
    private final String info;

    TimeLimit(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
