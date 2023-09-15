package com.ruoyi.project.parking.enums;

public class BMyCarEnums {

    private BMyCarEnums() {}

    // 是否默认(0-否,1-是)
    public enum DEFAULT_FLAG {
        NO("0", "否"), YES("1", "是");

        private String value;

        private String desc;

        DEFAULT_FLAG(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static DEFAULT_FLAG getByValue(String value) {
            for (DEFAULT_FLAG grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return null;
        }
    }
}
