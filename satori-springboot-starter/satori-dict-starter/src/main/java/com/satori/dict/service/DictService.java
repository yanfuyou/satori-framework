package com.satori.dict.service;

import com.satori.dict.model.DictionaryModel;
import com.satori.dict.properties.DictConfigProperties;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author cat_y
 * @description
 * @date 2024/08/24 21:54
 */
public class DictService {
    private static final Log log = LogFactory.getLog(DictService.class);
    //  本地缓存
    public static final Set<DictionaryModel> DICT_CACHE = new HashSet<>();
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private DictConfigProperties dictConfigProperties;

    public static void display() {
        DICT_CACHE.forEach(dict -> {
            log.info(dict.getName() + "->" + dict.getPairs());
        });
    }

    public Map<Object, Object> getDict(String name) {
        RMap<String, Map<Object, Object>> map = redissonClient.getMap("satori-dict");
        return map.getOrDefault(name, Map.of());
    }

    public void refresh(String code, Map<Object, Object> dictData) {
        String key = Optional.ofNullable(dictConfigProperties.getScope())
                .map(scope -> "satori-dict:" + scope)
                .orElse("satori-dict");
        RMap<Object, Object> map = redissonClient.getMap(key);
        map.put(code, dictData);
    }
}
