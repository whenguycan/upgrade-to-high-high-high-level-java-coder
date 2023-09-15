package com.ruoyi.project.merchant.enums;

public class BSelfPaySchemeEnums {
    private BSelfPaySchemeEnums(){}

    // 是否可自主续费
    public enum RenewStatus{
        NO("0", "不可自主续费"), YES("1", "可自主续费");
        private final String code;
        private final String info;

        RenewStatus(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }
}
