package com.ruoyi.project.parking.utils.convert;

import com.ruoyi.project.parking.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 类型转化器 工厂类
 */
@Component
public class StringFieldToEnumConvertFactory implements ConverterFactory<String, BaseEnum> {

    private static final Map<Class<?>, Converter> CONVERTERS = new HashMap<>();

    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        Converter<String, T> converter = CONVERTERS.get(targetType);
        if (converter == null) {
            converter = new StringFieldToEnumConvert<>(targetType);
            CONVERTERS.put(targetType, converter);
        }
        return converter;
    }
}
