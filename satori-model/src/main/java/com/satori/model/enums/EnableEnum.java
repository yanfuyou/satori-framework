package com.satori.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

/**
 * @author yanfuyou
 * @date 2025/05/27 17:42:27
 * @description
 */
@SuppressWarnings("all")
@AllArgsConstructor
public enum EnableEnum {
    DISABLE(1, "启用"),
    ENABLE(0, "禁用");

    @JsonValue
    @EnumValue
    public final Integer code;
    public final String desc;
}
