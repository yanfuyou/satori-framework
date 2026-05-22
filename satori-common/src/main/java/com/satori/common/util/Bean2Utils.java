package com.satori.common.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.TypeReference;
import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author YanFuYou
 */
@UtilityClass
public class Bean2Utils extends BeanUtils {

    public static <T, R> R clone(final T source, Class<R> destClass) {
        if (source == null) {
            return null;
        }
        return JSONB.parseObject(JSONB.toBytes(source), destClass);
    }

    public static <T, R> R copy(final T source, Class<R> destClass) {
        if (source == null) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONBytes(source), destClass);
    }

    public static <T, R> R clone(final T source,
                                 Class<R> destClass,
                                 @Nullable BiConsumer<T, R> biConsumer) {
        if (source == null) {
            return null;
        }
        R r = clone(source, destClass);
        if (biConsumer != null) {
            biConsumer.accept(source, r);
        }
        return r;
    }

    public static <T, R> R clone(final T source, TypeReference<R> typeReference) {
        if (source == null) {
            return null;
        }
        return JSONB.parseObject(JSONB.toBytes(source), typeReference);
    }

    public static <T, R> List<R> cloneList(final List<T> sourceList, Class<R> destClass) {
        if (null == sourceList || sourceList.isEmpty()) {
            return new ArrayList<>();
        }
        return JSONB.parseArray(JSONB.toBytes(sourceList), destClass);
    }

    public static <T, R> List<R> cloneList(List<T> source, Class<R> clazz, BiConsumer<? super T, ? super R> callback) {
        List<R> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(source)) {
            return list;
        }
        source.forEach(item -> {
            R r = clone(item, clazz);
            callback.accept(item, r);
            list.add(r);
        });
        return list;
    }

    /**
     * 拷贝对象，忽略空值
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    /**
     * 拷贝对象，忽略空值
     *
     * @param source   源对象
     * @param target   目标对象
     * @param consumer 后续操作
     */
    public static <S, T> void copyPropertiesIgnoreNull(S source, T target, BiConsumer<S, T> consumer) {
        if (Objects.isNull(source)) {
            return;
        }
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        if (Objects.nonNull(consumer)) {
            consumer.accept(source, target);
        }
    }

    /**
     * 两个对象的赋值,拷贝完成后可继续处理
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static <S, T> void copyProperties(S source, T target, BiConsumer<S, T> consumer) {
        BeanUtils.copyProperties(source, target);
        if (Objects.nonNull(consumer)) {
            consumer.accept(source, target);
        }
    }

    /**
     * @param source   源对象
     * @param clazz    目标对象
     * @param consumer 后续操作
     * @return 转换结果
     */
    public static <S, T> T convertWithNonNull(S source, Class<T> clazz, BiConsumer<S, T> consumer) {
        if (Objects.isNull(source)) {
            return null;
        }
        T target;
        try {
            target = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("目标类型实例化失败", e);
        }
        copyPropertiesIgnoreNull(source, target, consumer);
        return target;
    }

    /**
     * 获取空参数名称
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Method readMethod = pd.getReadMethod();
            if (Objects.isNull(readMethod)) {
                continue;
            }
            Object srcValue = src.getPropertyValue(pd.getName());
            String propertyValue = null;
            if (null != srcValue) {
                propertyValue = srcValue.toString();
            }
            if (null == srcValue || "null".equals(propertyValue)) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
