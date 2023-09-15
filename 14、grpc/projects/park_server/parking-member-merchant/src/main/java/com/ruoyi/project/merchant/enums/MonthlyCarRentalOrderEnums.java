package com.ruoyi.project.merchant.enums;

public class MonthlyCarRentalOrderEnums {
    private MonthlyCarRentalOrderEnums() {
    }

    public enum OrderStatus {
        UNCONFIRMED("01", "未确认"),
        CONFIRMED("02", "已确认"),
        PAID("03", "已支付"),
        COMPLETED("04", "已完成"),
        CANCELED("05", "已取消"),
        REFUNDING("06", "退款中");
        private final String value;
        private final String desc;

        OrderStatus(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }

        public static OrderStatus getByValue(String value) {
            for (OrderStatus orderStatus : values()) {
                if (orderStatus.value.equals(value)) {
                    return orderStatus;
                }
            }
            return null;
        }
    }

    public enum OrderPayStatus {
        UNPAID("01", "未支付"),
        PAYING("02", "支付中"),
        PAY_SUCCESS("03", "支付成功"),
        PAY_FAIL("04", "支付失败"),
        REFUNDED("05", "已退款"),
        PART_REFUND("06", "部分退款");
        private final String value;
        private final String desc;

        OrderPayStatus(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }

        public static OrderPayStatus getByValue(String value) {
            for (OrderPayStatus sendTime : values()) {
                if (sendTime.value.equals(value)) {
                    return sendTime;
                }
            }
            return null;
        }
    }

    public enum PayMethod {
        UNKNOWN("0", "未知"),
        ALI_PAY("1", "支付宝"),
        WECHAT_PAY("2", "微信"),
        UNION_PAY("3", "银联");
        private final String value;
        private final String desc;

        PayMethod(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }

        public static PayMethod getByValue(String value) {
            for (PayMethod payMethod : values()) {
                if (payMethod.value.equals(value)) {
                    return payMethod;
                }
            }
            return UNKNOWN;
        }
    }
}
