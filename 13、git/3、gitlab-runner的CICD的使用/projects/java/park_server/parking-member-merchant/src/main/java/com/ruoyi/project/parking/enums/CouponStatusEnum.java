package com.ruoyi.project.parking.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 优惠卷状态
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CouponStatusEnum implements BaseEnum {
    UNALLOCATED("0", "未分配"),
    ALLOCATED("1", "已分配"),
    USED("2", "已使用"),
    INVALIDATION("3", "失效");

    private final String value;
    private final String desc;

    CouponStatusEnum(String value, String desc) {
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
