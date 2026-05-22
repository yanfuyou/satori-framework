package com.satori.dict;

import com.satori.dict.annotation.DictKey;
import com.satori.dict.annotation.DictValue;
import com.satori.dict.annotation.Dictionary;
import com.satori.dict.model.DictionaryModel;
import com.satori.dict.properties.DictConfigProperties;
import com.satori.dict.service.DictService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.redisson.api.RBatch;
import org.redisson.api.RMapAsync;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfigurationV2;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * @author cat_y
 * @description 扫描器
 * @date 2024/08/04 00:16
 */

@ConditionalOnClass(RedissonAutoConfigurationV2.class)
@EnableConfigurationProperties(DictConfigProperties.class)
public class DictInitScanner {
    private static final Log logger = LogFactory.getLog(AutoConfigurationPackages.class);
    private final DictConfigProperties dictConfigProperties;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private Executor executor;

    @PostConstruct
    public void dictInit() {
        executor.execute(() -> {
            try {
                start();
            } catch (ClassNotFoundException | IllegalAccessException e) {
                logger.warn("枚举字典扫描失败");
                throw new RuntimeException(e);
            }
        });
    }

    private void start() throws ClassNotFoundException, IllegalAccessException {
        List<String> paths = dictConfigProperties.getPaths();
        if (CollectionUtils.isEmpty(paths)) {
            logger.warn("dict scan paths is empty! will scan all packages!");
            // return;
            // 扫描所有的包
            paths = new ArrayList<>();
            paths.add("*");
        }
        ClassPathScanningCandidateComponentProvider pathScanner = new ClassPathScanningCandidateComponentProvider(false);
        // 扫描包含指定注解的类
        pathScanner.addIncludeFilter(new AnnotationTypeFilter(Dictionary.class));
        Class<?> clazz;
        Field[] fields;
        Set<DictionaryModel> dicts = new HashSet<>();
        for (String path : paths) {
            Set<BeanDefinition> beans = pathScanner.findCandidateComponents(path);
            for (BeanDefinition bean : beans) {
                clazz = Class.forName(bean.getBeanClassName());
                fields = clazz.getDeclaredFields();
                DictionaryModel model = new DictionaryModel();
                model.setName("".equals(clazz.getAnnotation(Dictionary.class).name()) ? clazz.getSimpleName() : clazz.getAnnotation(Dictionary.class).name());
                // 拿到声明的所有枚举常量
                Object[] enumConstants = clazz.getEnumConstants();
                for (Object constant : enumConstants) {
                    Object key = null;
                    Object val = null;
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(DictKey.class)) {
                            field.setAccessible(true);
                            // 枚举key
                            key = field.get(constant);
                        }
                        if (field.isAnnotationPresent(DictValue.class)) {
                            field.setAccessible(true);
                            // 枚举值
                            val = field.get(constant);
                        }
                    }
                    model.addDict(key, val);
                }
                dicts.add(model);
                // DictService.DICT_CACHE.add(model);
            }
        }
        if (CollectionUtils.isEmpty(dicts)) {
            return;
        }
        RBatch batch = redissonClient.createBatch();
        String key = Optional.ofNullable(dictConfigProperties.getScope())
                .map(scope -> "satori-dict:" + scope)
                .orElse("satori-dict");
        RMapAsync<String, Map<Object, Object>> dictMap = batch.getMap(key);
        dicts.forEach(dict -> {
            dictMap.putAsync(dict.getName(), dict.getPairs());
        });
        batch.execute();
        logger.info("枚举字典扫描完成");
    }

    public DictInitScanner(DictConfigProperties dictConfigProperties) {
        this.dictConfigProperties = dictConfigProperties;
    }

    @Bean
    public DictService dictService() {
        return new DictService();
    }
}
