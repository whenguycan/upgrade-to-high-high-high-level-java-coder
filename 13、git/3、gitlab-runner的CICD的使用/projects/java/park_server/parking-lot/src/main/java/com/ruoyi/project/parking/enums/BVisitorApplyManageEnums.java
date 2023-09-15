package com.ruoyi.project.parking.enums;

public class BVisitorApplyManageEnums {

    private BVisitorApplyManageEnums() {}

    // 状态(0-审核中,1-已通过,2-已驳回)
    public enum STATUS {
        NOT_AUDIT("0", "审核中"), APPROVED("1", "已通过"), REJECTED("2", "已驳回");

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
