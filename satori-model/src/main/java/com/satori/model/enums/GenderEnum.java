package com.satori.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

/**
 * @author YanFuYou
 * @date 2024/02/05 下午 12:16
 */

@AllArgsConstructor
public enum GenderEnum {
    MAN(0, "男"),
    WOMAN(1, "女"),
    ;

    @EnumValue
    @JsonValue
    public final Integer code;

    public final String desc;
}
