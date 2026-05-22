package com.satori.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

/**
 * @author yanfuyou
 * @date 2025/5/26 20:34
 * @description
 **/
@AllArgsConstructor
public enum LoginTypeEnum {
    OPEN_ID(1),
    UNION_ID(2),
    NAME_PWD(3),
    PHONE_CODE(4),
    EMAIL_CODE(5),
    PHONE_PPW(6),
    EMAIL_PWD(7),
    ;
    @JsonValue
    @EnumValue
    public final int code;


}
