package com.satori.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

/**
 * @author YanFuYou
 * @date 2024/02/05 下午 12:05
 */

@AllArgsConstructor
public enum BaseStateEnum {

    /**
     * 正常
     */
    NORMAL(0, "正常"),
    /**
     * 删除
     */
    DELETE(1, "删除"),
    /**
     * 禁用
     */
    DISABLE(2, "禁用"),
    /**
     * 锁定
     */
    LOCK(3, "锁定"),
    /**
     * 异常
     */
    EXCEPTION(4, "异常");


    @EnumValue
    @JsonValue
    public final Integer code;

    public final String desc;


}
