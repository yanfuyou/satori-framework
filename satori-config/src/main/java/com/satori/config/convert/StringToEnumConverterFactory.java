package com.satori.config.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.Assert;
import reactor.util.annotation.NonNull;

/**
 * @author yfy
 * 覆盖Spring默认的 org.springframework.core.convert.support.StringToEnumConverterFactory
 */
public class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {

    @Override
    public <T extends Enum> Converter<String, T> getConverter(@NonNull Class<T> targetType) {
        return new CustomConvert.stringToEnumConverter<>(getEnumType(targetType));
    }

    public static Class getEnumType(Class targetType) {
        Class<?> enumType = targetType;
        while (enumType != null && !enumType.isEnum()) {
            enumType = enumType.getSuperclass();
        }
        Assert.notNull(enumType, () -> {
            assert targetType != null;
            return "The target type " + targetType.getName() + " does not refer to an enum";
        });
        return enumType;
    }
}
