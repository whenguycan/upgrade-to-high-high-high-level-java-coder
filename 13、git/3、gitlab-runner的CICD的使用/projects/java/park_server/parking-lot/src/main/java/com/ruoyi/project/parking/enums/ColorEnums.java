package com.ruoyi.project.parking.enums;

public class ColorEnums {


    public enum Color_Kinds {
        WHITE(0, "White"),
        BLACK(1, "Black"),
        RED(2, "Red"),
        YELLOW(3, "Yellow"),
        GRAY(4, "Gray"),
        BLUE(5, "Blue"),
        GREEN(6, "Green"),
        ORANGE(7, "Orange"),
        PURPLE(8, "Purple"),
        CYAN(9, "Cyan"),
        PINK(10, "Pink"),
        BROWN(11, "Brown"),
        UNKNOWN(99, "Unknown"),
        OTHER(100, "Other");

        private int value;

        private String desc;

        Color_Kinds(int value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public int getValue() {
            return value;
        }

        public static ColorEnums.Color_Kinds getByValue(int value) {
            for (ColorEnums.Color_Kinds grade : values()) {
                if (grade.value == value) {
                    return grade;
                }
            }
            return null;
        }
    }
}
