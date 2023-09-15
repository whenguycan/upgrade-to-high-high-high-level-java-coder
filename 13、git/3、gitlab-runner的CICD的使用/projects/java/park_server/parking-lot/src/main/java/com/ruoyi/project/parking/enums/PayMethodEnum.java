package com.ruoyi.project.parking.enums;

public enum PayMethodEnum {
    ALIPAY("1", "支付宝支付"),
    WECHAT("2", "微信支付"),
    UNION_PAY("3", "银联"),
    CASH_PAY("4", "现金支付");
    private final String value;
    private final String desc;

    PayMethodEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
