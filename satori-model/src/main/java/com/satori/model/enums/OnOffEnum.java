package com.satori.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

/**
 * @author YanFuYou
 * @date 2024/09/30 23:10
 * @description
 */

@AllArgsConstructor
public enum OnOffEnum {
    ON(1, "开"),
    OFF(2, "关"),
    ;

    @JsonValue
    @EnumValue
    public final Integer value;

    public final String desc;


}
