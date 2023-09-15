package com.ruoyi.project.parking.enums;

public class BParkChargeSchemeEnums {

    private BParkChargeSchemeEnums() {}

    //舍入方式（1=全入；2=全舍；3=四舍五入）
    public enum ROUNDWAY {
        ALL_IN("1", "全入"), ALL_ABANDON("2", "全舍"),
        ROUNDING("3", "四舍五入");

        private String value;

        private String desc;

        ROUNDWAY(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static ROUNDWAY getByValue(String value) {
            for (ROUNDWAY grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return null;
        }
    }

    //时间优惠劵包含免费时段（N=不包含；Y=包含）
    public enum TCFREETIMEFLAG {
        NOT_INCLUDE("N", "不包含"), INCLUDE("Y", "包含");

        private String value;

        private String desc;

        TCFREETIMEFLAG(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static TCFREETIMEFLAG getByValue(String value) {
            for (TCFREETIMEFLAG grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return null;
        }
    }

    //时间优惠券是使用方式（1=入场时间前移；2=出场时间后移；3=减掉优惠时间）
    public enum TCUSEWAY {
        FORWARD("1", "入场时间前移"), BACKWARD("2", "出场时间后移"),
        REDUCE("3", "减掉优惠时间");

        private String value;

        private String desc;

        TCUSEWAY(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static TCUSEWAY getByValue(String value) {
            for (TCUSEWAY grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return null;
        }
    }

    //秒进位方式（1=截秒；2=秒进位）
    public enum SECONDCARRY {
        CUTOFF("1", "截秒"), CARRY("2", "秒进位");

        private String value;

        private String desc;

        SECONDCARRY(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static SECONDCARRY getByValue(String value) {
            for (SECONDCARRY grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return null;
        }
    }
}
