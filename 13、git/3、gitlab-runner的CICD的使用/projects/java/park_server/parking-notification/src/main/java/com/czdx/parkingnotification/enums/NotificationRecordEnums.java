package com.czdx.parkingnotification.enums;

public class NotificationRecordEnums {
    private NotificationRecordEnums() {
    }

    public enum SendTime {
        ENTRY("0", "入场通知"), EXIT("1", "离场通知"), PAY("2", "支付通知");

        private final String value;

        private final String desc;

        SendTime(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static SendTime getByValue(String value) {
            for (SendTime sendTime : values()) {
                if (sendTime.value.equals(value)) {
                    return sendTime;
                }
            }
            return null;
        }
    }

    public enum Status {
        WAIT("0", "未响应"), SUCCESS("1", "成功"), FAIL_USER_BLOCK("-1", "用户拒绝接收"), FAIL_SYSTEM_FAILED("-2", "系统错误");

        private final String value;

        private final String desc;

        Status(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static Status getByValue(String value) {
            for (Status status : values()) {
                if (status.value.equals(value)) {
                    return status;
                }
            }
            return null;
        }
    }
}
