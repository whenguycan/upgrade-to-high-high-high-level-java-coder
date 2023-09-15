package com.ruoyi.project.parking.enums;

public class BFieldEnums {

    private BFieldEnums() {}

    // 区域状态；'0'-已停用;，'1'-已启用
    public enum FIELD_STATUS {
        DEACTIVATED("0", "已停用"), ACTIVATED("1", "已启用");

        private String value;

        private String desc;

        FIELD_STATUS(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static FIELD_STATUS getByValue(String value) {
            for (FIELD_STATUS grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return null;
        }
    }
}
