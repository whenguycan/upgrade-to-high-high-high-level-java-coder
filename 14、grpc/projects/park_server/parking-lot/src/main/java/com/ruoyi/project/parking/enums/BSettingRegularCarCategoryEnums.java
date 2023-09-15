package com.ruoyi.project.parking.enums;

public class BSettingRegularCarCategoryEnums {

    private BSettingRegularCarCategoryEnums() {}

    // 状态  '0'-停用 '1'-启用
    public enum STATUS {
        DEACTIVATED("0", "停用"), ACTIVATED("1", "启用");

        private final String value;

        private final String desc;

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

    // 删除标记 0正常 1删除
    public enum DEL_FLAG {
        NORMAL(0, "正常"), DELETED(1, "删除");

        private Integer value;

        private String desc;

        DEL_FLAG(Integer value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public Integer getValue() {
            return value;
        }

        public static DEL_FLAG getByValue(Integer value) {
            for (DEL_FLAG grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return null;
        }
    }
}
