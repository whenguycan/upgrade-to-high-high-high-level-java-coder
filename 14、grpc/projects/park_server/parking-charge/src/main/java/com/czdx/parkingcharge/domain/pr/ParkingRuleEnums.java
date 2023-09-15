package com.czdx.parkingcharge.domain.pr;

import java.math.RoundingMode;

public class ParkingRuleEnums {

    private ParkingRuleEnums() {}

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
     * 分割相关节点ID与优先级
     */
    public enum DivisionNode {
        CEILING_PRICE_DIVISION(999,  999, "最高限价分割节点"),
        H24_DIVISION(888,  888, "24小时强制分割节点"),
        ZERO_DIVISION(777,  777, "0点强制分割节点"),
        DURATION_DIVISION(666,  666, "期间强制分割节点"),
        HIDE_DURATION_DIVISION(555,  555, "超过期间隐藏的分割节点");

        private int id;

        private int salience;

        private String desc;

        DivisionNode(int id, int salience, String desc) {
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

        public static DivisionNode getByValue(int id) {
            for (DivisionNode node : values()) {
                if (node.id == id) {
                    return node;
                }
            }
            return null;
        }
    }

}
