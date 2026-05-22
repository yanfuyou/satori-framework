package com.satori.dict.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author cat_y
 * @description 字典key注解
 * @date 2023/3/20 15:55
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DictKey {
    @AliasFor("label")
    String value() default "";

    @AliasFor("value")
    String label() default "";
}
