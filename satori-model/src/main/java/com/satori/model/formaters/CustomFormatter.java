package com.satori.model.formaters;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * @author YanFuYou
 * @date 15/10/23 上午 02:26
 */
public class CustomFormatter {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD);
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String TIME = "HH:mm:ss";
    public static final String TIME_HH_MM = "HH:mm";
    public static final String YYYY_MM_DD_TIMESTAMP = "yyyyMMdd";
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME);
    /** 用于数据库统计字段使用 */
    public static final DateTimeFormatter DATETIME_INT_FORMATTER = DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS);
    /** 用于数据库统计字段使用 */
    public static final DateTimeFormatter DATE_INT_FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD_TIMESTAMP);
    public static final DateTimeFormatter HH_MM_SS_HH_MM_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("HH:mm")
            .optionalStart()
            .appendPattern(":ss")
            .optionalEnd()
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    public static final DateTimeFormatter DATE_EXT_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm")
            .optionalStart()
            .appendPattern(":ss")
            .optionalEnd()
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    public static final DateTimeFormatter RFC3339 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");


    public static final SimpleDateFormat dateSimpleFormat = new SimpleDateFormat(YYYY_MM_DD);
    public static final SimpleDateFormat dateTimeSimpleFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
}
