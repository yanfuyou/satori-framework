package com.satori.common.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.satori.model.constant.EnvConstant;
import com.satori.model.enums.ReqSourceEnum;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YanFuyou
 * @date 2025-08-20 09:27:53
 * @descraption 线程本地变量
 */
@UtilityClass
@SuppressWarnings("unchecked")
public class SatoriThreadLocalUtil {
    /**
     * 统一管理ThreadLocal
     * 可实现父子线程ThreadLocal变量传递
     */
    private static final TransmittableThreadLocal<Map<String, Object>> CONTEXT = new TransmittableThreadLocal<>() {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<>(16);
        }
    };

    public static <T> T get(String key) {
        return (T) CONTEXT.get().get(key);
    }

    public static <T> void put(String key, @NonNull T value) {
        CONTEXT.get().put(key, value);
    }

    public static void putReqSource(ReqSourceEnum source) {
        CONTEXT.get().put(EnvConstant.REQ_SOURCE_KEY, source);
    }

    public static ReqSourceEnum getReqSource() {
        return (ReqSourceEnum) CONTEXT.get().get(EnvConstant.REQ_SOURCE_KEY);
    }

    public static <T> T remove(String key) {
        return (T) CONTEXT.get().remove(key);
    }

    public static void clear() {
        CONTEXT.get().clear();
    }
}
