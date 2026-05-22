package com.satori.config.web;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.satori.config.BaseConfig;
import com.satori.config.convert.CustomConvert;
import com.satori.config.convert.StringToEnumConverterFactory;
import com.satori.config.web.expection.GlobalExceptionHandler;
import com.satori.model.json.SatoriModule;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import static com.satori.model.formaters.CustomFormatter.dateTimeSimpleFormat;


/**
 * @author YanFuYou
 * @date 2024/02/15 下午 11:46
 */

@Configuration
@Import({
        GlobalExceptionHandler.class
})
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class SatoriWebConfig extends
        BaseConfig implements WebMvcConfigurer, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        log.info("添加自定义时间日期格式化器");
        registry.addConverter(CustomConvert.String2LocalDateConverter.INSTANCE);
        registry.addConverter(CustomConvert.String2LocalDateTimeConverter.INSTANCE);
        registry.addConverter(CustomConvert.String2DateConvert.INSTANCE);
        // get请求,枚举转换
        registry.addConverterFactory(new StringToEnumConverterFactory());

        registry.addConverter(CustomConvert.Long2StringConverter.INSTANCE);

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .maxAge(3600L)
                .allowCredentials(false)
                .allowedOriginPatterns(CorsConfiguration.ALL)
                .allowedHeaders(CorsConfiguration.ALL)
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.PUT.name(), HttpMethod.POST.name(), HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name());
    }

    @Override
    public void extendMessageConverters(@NonNull List<HttpMessageConverter<?>> converters) {

    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 替换时间日期序列化格式
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SatoriModule());
        objectMapper.setDateFormat(dateTimeSimpleFormat);
        //反序列化时对未知属性不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }

    @Bean
    @ConditionalOnClass(value = {JacksonAutoConfiguration.class, ObjectMapper.class})
    public Module satoriModule() {
        log.info("注入LocalDate LocalDateTime 序列和与反序列化器 ");
        return new SatoriModule();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }


    @Bean(destroyMethod = "shutdown")
    @Primary
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("satori-task-pool-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        log.info("线程池初始化完成，核心线程: {}, 最大线程: {}, 队列: {}",
                executor.getCorePoolSize(),
                executor.getMaxPoolSize(),
                executor.getThreadPoolExecutor().getQueue().size()
        );
        return executor;
    }
}
