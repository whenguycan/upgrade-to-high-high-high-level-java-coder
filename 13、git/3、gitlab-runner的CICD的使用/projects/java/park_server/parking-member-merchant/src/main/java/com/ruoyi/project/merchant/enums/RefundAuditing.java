package com.ruoyi.project.merchant.enums;

public class RefundAuditing {
    private RefundAuditing() {
    }

    // 是否可自主续费
    public enum RefundStatus {
        APPLY("0", "已申请"), YES("1", "审核通过"),
        NO("2", "审核不通过");
        private final String code;
        private final String info;

        RefundStatus(String code, String info) {
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
