package com.satori.model.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.satori.model.formaters.CustomFormatter;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author YanFuYou
 * @date 2024/02/15 下午 11:48
 */
public class SatoriModule extends SimpleModule {

    @Serial
    private static final long serialVersionUID = -8370878376342390136L;

    public SatoriModule() {

        this.addSerializer(LocalDate.class, new LocalDateSerializer(CustomFormatter.DATE_FORMATTER));
        this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(CustomFormatter.DATE_TIME_FORMATTER));
        this.addSerializer(LocalTime.class, new LocalTimeSerializer(CustomFormatter.TIME_FORMATTER));

        this.addDeserializer(LocalDate.class, new LocalDateDeserializer(CustomFormatter.DATE_FORMATTER));
        this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(CustomFormatter.DATE_EXT_FORMATTER));
        this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(CustomFormatter.HH_MM_SS_HH_MM_FORMATTER));
        this.addSerializer(Long.class, ToStringSerializer.instance);
    }

}
