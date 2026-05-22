package com.satori.sso.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.satori.sso.mp.SatoriMetaObjectHandler;
import com.satori.sso.service.StpInterfaceImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@ConfigurationProperties(prefix = "satori")
@PropertySource("classpath:sa.properties")
@Import(SaExceptionHandle.class)
@Data
@Configuration
public class SsoConfig implements WebMvcConfigurer {
    private Set<String> noLoginPaths = new HashSet<>();
    private List<String> whiteUri = new ArrayList<>();

//    @Bean
//    public SaServletFilter getSaServletFilter() {
//        return new SaServletFilter()
//
//                // 指定 [拦截路由] 与 [放行路由]
//                .addInclude("/**").addExclude("/favicon.ico")
//
//                // 认证函数: 每次请求执行
//                .setAuth(obj -> {
//                    SaManager.getLog().debug("----- 请求path={}  提交token={}", SaHolder.getRequest().getRequestPath(), StpUtil.getTokenValue());
//                    // ...
//                })
//
//                // 异常处理函数：每次认证函数发生异常时执行此函数
//                .setError(e -> {
//                    return SaResult.error(e.getMessage());
//                })
//
//                // 前置函数：在每次认证函数之前执行
//                .setBeforeAuth(obj -> {
//                    SaHolder.getResponse()
//
//                            // ---------- 设置跨域响应头 ----------
//                            // 允许指定域访问跨域资源
//                            .setHeader("Access-Control-Allow-Origin", "*")
//                            // 允许所有请求方式
//                            .setHeader("Access-Control-Allow-Methods", "*")
//                            // 允许的header参数
//                            .setHeader("Access-Control-Allow-Headers", "*")
//                            // 有效时间
//                            .setHeader("Access-Control-Max-Age", "3600")
//                    ;
//
//                    // 如果是预检请求，则立即返回到前端
//                    SaRouter.match(SaHttpMethod.OPTIONS)
//                            .back();
//                });
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("sa-token拦截器注入");
        this.whiteUri.addAll(this.noLoginPaths);
        registry.addInterceptor(new SaInterceptor(handler -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns(this.whiteUri)
                .order(-10);
    }

    @Bean
    public StpInterface stpInterface(){
        return new StpInterfaceImpl();
    }

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new SatoriMetaObjectHandler();
    }
}
