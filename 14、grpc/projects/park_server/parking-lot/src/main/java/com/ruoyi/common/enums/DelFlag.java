package com.ruoyi.common.enums;

public enum DelFlag {
    NORMAL(0, "正常"), DELETED(1, "删除");
    private final Integer code;
    private final String info;

    DelFlag(Integer code, String info) {
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
