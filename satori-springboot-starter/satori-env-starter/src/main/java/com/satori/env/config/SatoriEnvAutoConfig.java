package com.satori.env.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * @author yanfuyou
 * @date 2025/08/20 10:06:14
 * @description
 */

@EnableConfigurationProperties({SatoriEnvProperties.class})
@Import(SatoriEnvImportSelector.class)
public class SatoriEnvAutoConfig {
}
