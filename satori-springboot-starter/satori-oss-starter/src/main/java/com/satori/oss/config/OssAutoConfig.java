package com.satori.oss.config;

import com.satori.oss.model.OssProperties;
import com.satori.oss.service.OssService;
import com.satori.oss.service.impl.CosServiceImpl;
import com.satori.oss.service.impl.OssServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "satori.file.endpoint")
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfig {
    private final OssProperties ossProperties;

    @Bean
    public OssService ossService() {
        if (CosServiceImpl.SERVER.equals(ossProperties.getServer())) {
            return new CosServiceImpl();
        }
        return new OssServiceImpl(ossProperties);
    }
}
