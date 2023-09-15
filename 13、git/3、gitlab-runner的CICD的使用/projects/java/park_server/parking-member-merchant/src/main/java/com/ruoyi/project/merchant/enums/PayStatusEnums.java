package com.ruoyi.project.merchant.enums;

public class PayStatusEnums {

    private PayStatusEnums() {
    }

    // 区域状态；'0'-已停用;，'1'-已启用
    public enum PAY_STATUS {
        UNPAID(0, "未支付"), PAYING(1, "支付中"),
        PAYMENT_SUCCESS(3, "支付成功"), PAYMENT_FAILED(4, "支付失败"),
        REFUNDED(5, "退款成功"), PART_REFUNDED(6, "部分退款");


        private int value;

        private String desc;

        PAY_STATUS(int value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public int getValue() {
            return value;
        }

        public static PAY_STATUS getByValue(Integer value) {
            for (PAY_STATUS grade : values()) {
                if (grade.value == value) {
                    return grade;
                }
            }
            return null;
        }
    }
}
