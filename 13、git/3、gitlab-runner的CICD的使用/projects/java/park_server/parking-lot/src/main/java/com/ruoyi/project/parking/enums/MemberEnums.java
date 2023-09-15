package com.ruoyi.project.parking.enums;

public class MemberEnums {

    private MemberEnums() {}

    // 会员类型；1-商户；2-会员
    public enum MEMBER_TYPE {
        MERCHANT("1", "商户"), MEMBER("2", "会员");

        private String value;

        private String desc;

        MEMBER_TYPE(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static MEMBER_TYPE getByValue(String value) {
            for (MEMBER_TYPE grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return null;
        }
    }
}
