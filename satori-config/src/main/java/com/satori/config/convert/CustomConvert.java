package com.satori.config.convert;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.satori.model.formaters.CustomFormatter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author YanFuYou
 * @date 2024/02/15 下午 11:17
 */

@Slf4j
public class CustomConvert {

    public enum String2LocalDateConverter implements Converter<String, LocalDate> {
        INSTANCE;


        @Override
        public LocalDate convert(@NonNull String source) {
            if (StrUtil.isBlank(source)){
                return null;
            }
            return LocalDate.parse(source, CustomFormatter.DATE_FORMATTER);
        }
    }


    public enum String2LocalDateTimeConverter implements Converter<String, LocalDateTime> {
        INSTANCE;


        @Override
        public LocalDateTime convert(@NonNull String source) {
            if (StrUtil.isBlank(source)){
                return null;
            }
            return LocalDateTime.parse(source, CustomFormatter.DATE_EXT_FORMATTER);
        }
    }

    public enum String2LocalTimeConverter implements Converter<String, LocalTime> {
        INSTANCE;


        @Override
        public LocalTime convert(@NonNull String source) {
            if (StrUtil.isBlank(source)){
                return null;
            }
            return LocalTime.parse(source, CustomFormatter.HH_MM_SS_HH_MM_FORMATTER);
        }
    }


    public enum String2DateConvert implements Converter<String, Date> {
        INSTANCE;

        @Override
        public Date convert(@NonNull String source) {
            if (StrUtil.isBlank(source)){
                return null;
            }
            try {
                if (10 >= source.length()) {
                    return CustomFormatter.dateTimeSimpleFormat.parse(source);
                } else {
                    return CustomFormatter.dateSimpleFormat.parse(source);
                }
            } catch (ParseException e) {
                log.error("日期格式转换失败", e);
            }
            return null;
        }
    }

    /**
     * 枚举
     *
     * @param <T>
     */
    public static class stringToEnumConverter<T extends Enum> implements Converter<String, T> {

        private final Class<T> enumType;
        private final boolean isMpEnums;
        private MybatisEnumConverter<?> mybatisEnumConverter;

        stringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
            this.isMpEnums = MybatisEnumConverter.isMpEnums(enumType);
            if (this.isMpEnums) {
                mybatisEnumConverter = new MybatisEnumConverter<>(enumType);
            }
        }

        @Override
        @Nullable
        public T convert(String source) {
            if (source.isEmpty()) {
                // It's an empty enum identifier: reset the enum value to null.
                return null;
            }
            if (isMpEnums) {
                return (T) mybatisEnumConverter.valueOf(source);
            }
            return (T) Enum.valueOf(this.enumType, source.trim());
        }
    }

    /**
     * 还可以补充localDate等与Date类型互转的convert
     */

    public enum Long2StringConverter implements Converter<Long, String> {
        INSTANCE;

        @Override
        public String convert(@NonNull Long source) {
            return String.valueOf(source);
        }
    }
}
