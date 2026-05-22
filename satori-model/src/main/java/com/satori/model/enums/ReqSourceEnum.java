package com.satori.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

/**
 * @author yanfuyou
 * @date 2025/7/26 20:38
 * @description
 **/
@AllArgsConstructor
public enum ReqSourceEnum {
    ADMIN(1, "管理端"),
    CLIENT(2, "用户端"),

    ;
    @EnumValue
    @JsonValue
    public final Integer code;

    public final String desc;
}
