package com.ruoyi.project.parking.enums;

public class BParkChargeRuleEnums {

    private BParkChargeRuleEnums() {}

    // 期间生成方式（1=按时刻计时；2=按时长计时）
    public enum DURATION_CREATE_WAY {
        TIME_BY_TIME("1", "按时刻计时"), RECKON_BY_TIME("2", "按时长计时");

        private String value;

        private String desc;

        DURATION_CREATE_WAY(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static DURATION_CREATE_WAY getByValue(String value) {
            for (DURATION_CREATE_WAY grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return null;
        }
    }

    // 是否最高限价（N=否；Y=是）
    public enum CEILING_PRICE_FLAG {
        NO("N", "否"), YES("Y", "是");

        private String value;

        private String desc;

        CEILING_PRICE_FLAG(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static CEILING_PRICE_FLAG getByValue(String value) {
            for (CEILING_PRICE_FLAG grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return null;
        }
    }

    // 算费包含免费时间（N=否；Y=是）
    public enum CHARGE_CONTAIN_FREE_TIME {
        NO("N", "否"), YES("Y", "是");

        private String value;

        private String desc;

        CHARGE_CONTAIN_FREE_TIME(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static CHARGE_CONTAIN_FREE_TIME getByValue(String value) {
            for (CHARGE_CONTAIN_FREE_TIME grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return null;
        }
    }

    // 免费时长次数（1=一次收费一次；2=每个分割一次）
    public enum FREE_MINUTE_NUMBER {
        ONE_CHARGE_ONE_TIME("1", "一次收费一次"), ONCE_PER_SPLIT("2", "每个分割一次");

        private String value;

        private String desc;

        FREE_MINUTE_NUMBER(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static FREE_MINUTE_NUMBER getByValue(String value) {
            for (FREE_MINUTE_NUMBER grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return null;
        }
    }

    // 计时分割方式（1=不分割；2=24小时强制分割；3=0点强制分割；4=期间强制分割）
    public enum TIME_DIVISION_WAY {
        UNDIVIDED("1", "不分割"), HOUR_24_FORCED_SPLIT("2", "24小时强制分割"),
        HOUR_0_FORCED_SPLIT("3", "0点强制分割"), PERIOD_FORCED_SPLIT("4", "期间强制分割");

        private String value;

        private String desc;

        TIME_DIVISION_WAY(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static TIME_DIVISION_WAY getByValue(String value) {
            for (TIME_DIVISION_WAY grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return null;
        }
    }

    // 首时段计费方式（1=一次收费只有一次；2=每次分割一次）
    public enum FIRST_DURATION_CHARGE_WAY {
        ONE_CHARGE_ONE_TIME("1", "一次收费只有一次"), ONCE_PER_SPLIT("2", "每次分割一次");

        private String value;

        private String desc;

        FIRST_DURATION_CHARGE_WAY(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static FIRST_DURATION_CHARGE_WAY getByValue(String value) {
            for (FIRST_DURATION_CHARGE_WAY grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return null;
        }
    }

    // 计时舍入方式（1=全入；2=全舍；3=四舍五入）
    public enum TIME_ROUND_WAY {
        ALL_IN("1", "全入"), ALL_ABANDON("2", "全舍"),
        ROUNDING("3", "四舍五入");

        private String value;

        private String desc;

        TIME_ROUND_WAY(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static TIME_ROUND_WAY getByValue(String value) {
            for (TIME_ROUND_WAY grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return null;
        }
    }
}
