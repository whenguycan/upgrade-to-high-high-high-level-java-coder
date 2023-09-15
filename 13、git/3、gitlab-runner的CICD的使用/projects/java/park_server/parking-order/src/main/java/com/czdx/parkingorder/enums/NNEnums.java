package com.czdx.parkingorder.enums;

/**
 * <p>
 * 诺诺发票相关数据枚举类
 * </p>
 *
 * @author 琴声何来
 * @since 2023/4/10 8:56
 */
public class NNEnums {
    private NNEnums() {
    }

    public enum PushMode {
        NON("-1", "不推送"),MAIL("0", "邮箱"), PHONE("1", "手机"),
        MAIL_AND_PHONE("2", "邮箱、手机");
        private final String value;
        private final String desc;

        PushMode(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum BillStatus {
        SUCCESS("2", "成功"),
        INVOICING("20", "开票中"), SINGING("21", "开票成功签章中"),
        INVOICE_ERROR("22", "开票失败"), SIGN_ERROR("24", "开票成功签章失败"),
        REPEALED("3", "发票已作废"), REPEALING("31", "发票作废中");
        private final String value;
        private final String desc;

        BillStatus(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum ReturnCode {
        SUCCESS("E0000", "成功"), FREE_NUM_LIMIT("070201", "免费API调用次数限制"),
        CHARGE_NUM_LIMIT("070204", "收费API调用次数限制"), PARAM_ERROR("0201", "参数异常"),
        SIGN_ERROR("070601", "签名异常");
        private final String value;
        private final String desc;

        ReturnCode(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }
}
