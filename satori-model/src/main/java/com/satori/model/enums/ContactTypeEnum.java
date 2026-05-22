package com.satori.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

/**
 * @author YanFuYou
 * @date 2024/02/05 下午 12:14
 * 联系方式
 */

@AllArgsConstructor
public enum ContactTypeEnum {

    /**
     * 微信
     */
    WECHAT(1, "微信"),

    /**
     * QQ
     */
    QQ(2, "QQ"),

    /**
     * 邮箱
     */
    EMAIL(3, "邮箱"),

    /**
     * 电话
     */
    PHONE(4, "电话"),

    /**
     * 其他
     */
    OTHER(5, "其他");

    @EnumValue
    @JsonValue
    public final Integer code;

    public final String desc;

}
