package com.satori.config.convert;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.satori.model.code.ReplyCode;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.invoker.Invoker;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YanFuYou
 * {@link com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler}
 */

public class MybatisEnumConverter<E extends Enum<E>> {
    private static final Map<String, String> TABLE_METHOD_OF_ENUM_TYPES = new ConcurrentHashMap<>();
    private static final ReflectorFactory REFLECTOR_FACTORY = new DefaultReflectorFactory();
    private final Class<E> enumClassType;
    private final Invoker getInvoker;

    public MybatisEnumConverter(Class<E> enumClassType) {
        if (enumClassType == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.enumClassType = enumClassType;
        MetaClass metaClass = MetaClass.forClass(enumClassType, REFLECTOR_FACTORY);
        String name = "value";
        if (!IEnum.class.isAssignableFrom(enumClassType)) {
            name = findEnumValueFieldName(this.enumClassType).orElseThrow(() -> new IllegalArgumentException(String.format("Could not find @EnumValue in Class: %s.", this.enumClassType.getName())));
        }
        // this.propertyType = ReflectionKit.resolvePrimitiveIfNecessary(metaClass.getGetterType(name));
        this.getInvoker = metaClass.getGetInvoker(name);
    }

    /**
     * 查找标记标记EnumValue字段
     *
     * @param clazz class
     * @return EnumValue字段
     * @since 3.3.1
     */
    public static Optional<String> findEnumValueFieldName(Class<?> clazz) {
        if (clazz != null && clazz.isEnum()) {
            String className = clazz.getName();
            return Optional.ofNullable(CollectionUtils.computeIfAbsent(TABLE_METHOD_OF_ENUM_TYPES, className, key -> {
                Optional<Field> fieldOptional = findEnumValueAnnotationField(clazz);
                return fieldOptional.map(Field::getName).orElse(null);
            }));
        }
        return Optional.empty();
    }

    public static Optional<Field> findEnumValueAnnotationField(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(EnumValue.class)).findFirst();
    }

    /**
     * 判断是否为MP枚举处理
     *
     * @param clazz class
     * @return 是否为MP枚举处理
     * @since 3.3.1
     */
    public static boolean isMpEnums(Class<?> clazz) {
        return clazz != null && clazz.isEnum() && (IEnum.class.isAssignableFrom(clazz) || findEnumValueFieldName(clazz).isPresent());
    }

    public E valueOf(Object value) {
        E[] es = this.enumClassType.getEnumConstants();
        return Arrays.stream(es)
                .filter((e) -> equalsValue(value, getValue(e)))
                .findAny()
                .orElseThrow(() -> ReplyCode.PARAMETER_FAIL.buildEx(value));
    }

    /**
     * 值比较
     *
     * @param sourceValue 字段值
     * @param targetValue 当前枚举属性值
     * @return 是否匹配
     * @since 3.3.0
     */
    protected boolean equalsValue(Object sourceValue, Object targetValue) {
        String sValue = StringUtils.toStringTrim(sourceValue);
        String tValue = StringUtils.toStringTrim(targetValue);
        if (sourceValue instanceof Number && targetValue instanceof Number
                && new BigDecimal(sValue).compareTo(new BigDecimal(tValue)) == 0) {
            return true;
        }
        return Objects.equals(sValue, tValue);
    }

    private Object getValue(Object object) {
        try {
            return this.getInvoker.invoke(object, new Object[0]);
        } catch (ReflectiveOperationException e) {
            throw ExceptionUtils.mpe(e);
        }
    }
}
