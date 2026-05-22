package com.satori.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

/**
 * @author yanfuyou
 * @date 2025/5/26 22:33
 * @description 客户端枚举
 **/
@AllArgsConstructor
public enum ClientEnum {
    WEB(1),
    WECHAT(2),
    ALIPAY(3),
    ;
    @JsonValue
    @EnumValue
    public final int code;
}
