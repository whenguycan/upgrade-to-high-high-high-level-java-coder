package com.ruoyi.project.common;

import com.ruoyi.project.parking.enums.UserEnums;

public class CouponEnums {

    private CouponEnums() {
    }

    // 优惠券状态；0-未分配;1-已分配;2-已使用;3-失效
    public enum COUPON_STATUS {
        CREATED("0", "未分配"), ALLOCATED("1", "已分配"),
        USED("2", "已使用"), EXPIRED("3", "失效"),
        CANCEL("4", "作废");

        private String value;

        private String desc;

        COUPON_STATUS(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static COUPON_STATUS getByValue(String value) {
            for (COUPON_STATUS grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return null;
        }
    }
}
