package com.czdx.parkingorder.enums;

public class PayEnums {
    private PayEnums() {
    }

    public enum RefundStatus {
        REFUNDING("0", "退款中"),
        SUCCESS("1", "退款成功"),
        FAIL("-1", "退款失败");
        private final String value;
        private final String desc;

        RefundStatus(String value, String desc) {
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
}
