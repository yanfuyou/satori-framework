package com.satori.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

/**
 * @author YanFuyou
 * @date 2024-12-07 21:02:00
 * @descraption
 */
@AllArgsConstructor
public enum YesOrNoEnum {
    YES(1, "是"),
    NO(0, "否");

    @JsonValue
    @EnumValue
    public final Integer value;

    public final String desc;
}
