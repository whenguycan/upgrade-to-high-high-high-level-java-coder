package com.ruoyi.project.parking.enums;

public class BVisitorCodeManageEnums {

    private BVisitorCodeManageEnums() {}

    // 是否永久有效期(0-否,1-是)
    public enum TIME_LIMIT {
        NO("0", "否"), YES("1", "是");

        private String value;

        private String desc;

        TIME_LIMIT(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static TIME_LIMIT getByValue(String value) {
            for (TIME_LIMIT grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return null;
        }
    }

    // 码状态(1-启用 2-停用)
    public enum STATUS {
        ACTIVATED("1", "启用"), DEACTIVATED("2", "停用");

        private String value;

        private String desc;

        STATUS(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static STATUS getByValue(String value) {
            for (STATUS grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return null;
        }
    }
}
