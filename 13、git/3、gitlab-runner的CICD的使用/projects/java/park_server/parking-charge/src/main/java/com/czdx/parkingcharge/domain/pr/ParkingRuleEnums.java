package com.czdx.parkingcharge.domain.pr;

import java.math.RoundingMode;

public class ParkingRuleEnums {

    private ParkingRuleEnums() {}

    /**
     * 计费舍入方式（1=全入；2=全舍；3=四舍五入）
     */
    public enum ChargeRoundWay {
        CEILING("1", "全入", RoundingMode.CEILING),
        DOWN("2", "全舍", RoundingMode.DOWN),
        HALF_UP("3", "四舍五入", RoundingMode.HALF_UP);

        private String value;

        private String desc;

        private RoundingMode way;

        ChargeRoundWay(String value, String desc, RoundingMode way) {
            this.desc = desc;
            this.value = value;
            this.way = way;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public RoundingMode getWay() {
            return way;
        }

        public static ChargeRoundWay getByValue(String value) {
            for (ChargeRoundWay grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return CEILING;
        }
    }

    /**
     * 优惠券包含免费时段
     */
    public enum CouponContainFreeTime {
        YES("1", "包含"), NO("2", "不包含");

        private String value;

        private String desc;

        CouponContainFreeTime(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static CouponContainFreeTime getByValue(String value) {
            for (CouponContainFreeTime sc : values()) {
                if (sc.value.equals(value)) {
                    return sc;
                }
            }
            return YES;
        }
    }

    /**
     *
     * description: 优惠券使用方式
     * @author mingchenxu
     * @date 2023/4/7 14:30
     */
    public enum CouponUseWay {
        ENTER_TIME_MOVE("1", "入场时间后移"), EXIT_TIME_MOVE("2", "出场时间前移");

        private String value;

        private String desc;

        CouponUseWay(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static CouponUseWay getByValue(String value) {
            for (CouponUseWay sc : values()) {
                if (sc.value.equals(value)) {
                    return sc;
                }
            }
            return EXIT_TIME_MOVE;
        }
    }

    /**
     *
     * description: 优惠券类型
     * @author mingchenxu
     * @date 2023/4/7 14:30
     */
    public enum CouponType {
        AMOUNT_COUPON(1, "金额优惠券"), TIME_COUPON(2, "时间优惠券");

        private int value;

        private String desc;

        CouponType(int value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public int getValue() {
            return value;
        }

        public static CouponType getByValue(int value) {
            for (CouponType sc : values()) {
                if (sc.value == value) {
                    return sc;
                }
            }
            return AMOUNT_COUPON;
        }
    }


    /**
     *
     * description: 秒进位方式
     * @author mingchenxu
     * @date 2023/3/9 15:24
     */
    public enum SecondCarry {
        IGNORE("1", "截秒"), UP("2", "秒进位");

        private String value;

        private String desc;

        SecondCarry(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static SecondCarry getByValue(String value) {
            for (SecondCarry sc : values()) {
                if (sc.value.equals(value)) {
                    return sc;
                }
            }
            return IGNORE;
        }
    }

    /**
     * 计时分割方式
     */
    public enum TimeDivisionWay {
        NOT_DIVISION("1", "不分割"), H24_DIVISION("2", "24小时强制分割"),
        ZERO_DIVISION("3", "0点强制分割"), DURATION_DIVISION("4", "期间强制分割");

        private String value;

        private String desc;

        TimeDivisionWay(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static TimeDivisionWay getByValue(String value) {
            for (TimeDivisionWay grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return NOT_DIVISION;
        }
    }


    /**
     * 计时舍入方式（1=全入；2=全舍；3=四舍五入）
     */
    public enum TimeRoundWay {
        CEILING("1", "全入", RoundingMode.CEILING),
        DOWN("2", "全舍", RoundingMode.DOWN),
        HALF_UP("3", "四舍五入", RoundingMode.HALF_UP);

        private String value;

        private String desc;

        private RoundingMode way;

        TimeRoundWay(String value, String desc, RoundingMode way) {
            this.desc = desc;
            this.value = value;
            this.way = way;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public RoundingMode getWay() {
            return way;
        }

        public static TimeRoundWay getByValue(String value) {
            for (TimeRoundWay grade : values()) {
                if (grade.value.equals(value)) {
                    return grade;
                }
            }
            return CEILING;
        }
    }


    /**
     *
     * description: 免费时长次数
     * @author mingchenxu
     * @date 2023/3/9 10:55
     */
    public enum FreeMinuteNumber {
        ONE_CHARGE_ONE_TIME("1", "一次收费一次"),
        EVERY_DIVIDE_ONT_TIME("2", "每次分割一次");

        private String value;

        private String desc;

        FreeMinuteNumber(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static FreeMinuteNumber getByValue(String value) {
            for (FreeMinuteNumber way : values()) {
                if (way.value.equals(value)) {
                    return way;
                }
            }
            return ONE_CHARGE_ONE_TIME;
        }
    }

    /**
     *
     * description: 首时段计费方式
     * @author mingchenxu
     * @date 2023/3/7 15:49
     */
    public enum FirstDurationChargeWay {
        ONE_CHARGE_ONE_TIME("1", "一次收费一次"),
        EVERY_DIVIDE_ONT_TIME("2", "每次分割一次");

        private String value;

        private String desc;

        FirstDurationChargeWay(String value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

        public static FirstDurationChargeWay getByValue(String value) {
            for (FirstDurationChargeWay way : values()) {
                if (way.value.equals(value)) {
                    return way;
                }
            }
            return ONE_CHARGE_ONE_TIME;
        }
    }

    /**
     * 特殊相关节点ID与优先级
     */
    public enum SpecialNode {
        CEILING_PRICE_DIVISION(999,  999, "最高限价分割节点"),
        H24_DIVISION(888,  888, "24小时强制分割节点"),
        ZERO_DIVISION(777,  777, "0点强制分割节点"),
        DURATION_DIVISION(666,  666, "期间强制分割节点"),
        NO_CONTAIN_FREE_MINUTE(555,  555, "算费不包含免费时长节点"),
        HIDE_DURATION_DIVISION(444,  444, "超过期间隐藏的分割节点"),
        HIDE_DURATION_DIVISION_MULTIPLE_PERIOD(443,  443, "超过期间隐藏的分割节点-多时段");

        private int id;

        private int salience;

        private String desc;

        SpecialNode(int id, int salience, String desc) {
            this.desc = desc;
            this.id = id;
            this.salience = salience;
        }

        public String getDesc() {
            return desc;
        }

        public int getId() {
            return id;
        }

        public int getSalience() {
            return salience;
        }

        public static SpecialNode getByValue(int id) {
            for (SpecialNode node : values()) {
                if (node.id == id) {
                    return node;
                }
            }
            return null;
        }
    }

}
