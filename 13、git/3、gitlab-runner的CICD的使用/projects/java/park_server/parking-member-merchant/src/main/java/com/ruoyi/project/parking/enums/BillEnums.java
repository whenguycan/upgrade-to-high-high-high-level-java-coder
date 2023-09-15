package com.ruoyi.project.parking.enums;

public class BillEnums {
    private BillEnums() {}

    public enum TYPE {
        PARKING("1", "停车缴费"), MONTHLY("2", "月卡充值");

        private final String value;

        private final String desc;

        TYPE(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

    }
}
