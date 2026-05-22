package com.satori.env.config;

import com.satori.env.filter.SatoriEnvFilter;
import jakarta.servlet.DispatcherType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yanfuyou
 * @date 2025/08/20 10:22:53
 * @description
 */
@Configuration
public class SatoriEnvServletConfig implements WebMvcConfigurer {

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public FilterRegistrationBean<SatoriEnvFilter> satoriEnvFilter(SatoriEnvProperties envProperties) {
        FilterRegistrationBean<SatoriEnvFilter> registration = new FilterRegistrationBean<>(new SatoriEnvFilter(envProperties));
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

}
