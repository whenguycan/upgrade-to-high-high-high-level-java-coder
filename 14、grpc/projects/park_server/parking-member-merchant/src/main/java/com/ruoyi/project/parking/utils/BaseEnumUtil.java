package com.ruoyi.project.parking.utils;

import com.ruoyi.project.parking.enums.BaseEnum;

/**
 * 枚举类 工具
 */
public class BaseEnumUtil {
    /**
     * 转换为对应枚举对象
     * @param enumClass 枚举类型
     * @param value 待转换数据
     */
    public static <E extends Enum<?> & BaseEnum> E valueOf(Class<E> enumClass, String value) {
        E[] enumConstants = enumClass.getEnumConstants();
        for (E e : enumConstants) {
            if (e.getValue().equals(value))
                return e;
        }
        return null;
    }
}
