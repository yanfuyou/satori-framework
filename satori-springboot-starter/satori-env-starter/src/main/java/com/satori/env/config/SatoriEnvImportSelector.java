package com.satori.env.config;

import com.satori.common.util.EnvUtil;
import com.satori.env.advice.SatoriResponseBodyAdvice;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author yanfuyou
 * @date 2025/08/20 10:08:39
 * @description
 */
public class SatoriEnvImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        if (WebApplicationType.SERVLET == EnvUtil.appType()) {
            return new String[]{SatoriEnvServletConfig.class.getName(), SatoriResponseBodyAdvice.class.getName()};
        }
        return new String[0];
    }
}
