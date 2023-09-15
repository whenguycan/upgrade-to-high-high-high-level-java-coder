package com.czdx.parkingcharge.common.utils;

import java.util.*;

/**
 * 罗马数字工具
 *
 */
public class RomeUtil {

    private RomeUtil() {
    }

    private static final Map<Integer, String> code = new LinkedHashMap<>();

    static {
        code.put(1000, "M");
        code.put(900, "CM");
        code.put(500, "D");
        code.put(400, "CD");
        code.put(100, "C");
        code.put(90, "XC");
        code.put(50, "L");
        code.put(40, "XL");
        code.put(10, "X");
        code.put(9, "IX");
        code.put(5, "V");
        code.put(4, "IV");
        code.put(1, "I");
    }

    public static String intToRoman(int num) {
        String res = "";
        Set<Map.Entry<Integer, String>> entries = code.entrySet();
        for (Map.Entry<Integer, String> item : entries) {
            if (num <= 0) {
                break;
            }
            int k = num / item.getKey();
            num %= item.getKey();
            for (int j = 0; j < k; ++j) {
                res += item.getValue();
            }
        }
        return res;
    }

    public static void main(String[] args) {
        String str1 = intToRoman(1);
        System.out.println(str1);
    }
}
