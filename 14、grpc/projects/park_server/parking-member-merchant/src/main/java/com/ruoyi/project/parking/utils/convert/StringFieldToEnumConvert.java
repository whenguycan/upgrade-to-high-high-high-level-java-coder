package com.ruoyi.project.parking.utils.convert;

import com.ruoyi.project.parking.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 类型转化器
 * String 类型 转 枚举类型
 * @param <T>
 */
@Component
public class StringFieldToEnumConvert<T extends BaseEnum> implements Converter<String, T> {
    private Map<String, T> enumMap = new HashMap<>();

    public StringFieldToEnumConvert() {
    }

    public StringFieldToEnumConvert(Class<T> enumType) {
        T[] enums = enumType.getEnumConstants();
        for (T e : enums) {
            enumMap.put(e.getValue(), e);
        }
    }

    @Override
    public T convert(String source) {
        T t = enumMap.get(source);
        if (t == null) {
            throw new RuntimeException("无法匹配对应的枚举类型");
        }
        return t;
    }
}
