package com.satori.common.util;

import lombok.experimental.UtilityClass;
import org.springframework.boot.WebApplicationType;
import org.springframework.util.ClassUtils;

/**
 * @author YanFuyou
 * @date 2025-08-20 10:14:53
 * @descraption 环境
 */
@UtilityClass
public class EnvUtil {
    private static final String[] SERVLET_INDICATOR_CLASSES = {"jakarta.servlet.Servlet",
            "org.springframework.web.context.ConfigurableWebApplicationContext"};

    private static final String WEBMVC_INDICATOR_CLASS = "org.springframework.web.servlet.DispatcherServlet";

    private static final String WEBFLUX_INDICATOR_CLASS = "org.springframework.web.reactive.DispatcherHandler";

    private static final String JERSEY_INDICATOR_CLASS = "org.glassfish.jersey.servlet.ServletContainer";


    public static WebApplicationType appType() {
        if (ClassUtils.isPresent(WEBFLUX_INDICATOR_CLASS, null) && !ClassUtils.isPresent(WEBMVC_INDICATOR_CLASS, null)
                && !ClassUtils.isPresent(JERSEY_INDICATOR_CLASS, null)) {
            return WebApplicationType.REACTIVE;
        }
        for (String className : SERVLET_INDICATOR_CLASSES) {
            if (!ClassUtils.isPresent(className, null)) {
                return WebApplicationType.NONE;
            }
        }
        return WebApplicationType.SERVLET;
    }

    public static boolean isWeb() {
        WebApplicationType type = appType();
        return type != WebApplicationType.NONE;
    }
}
