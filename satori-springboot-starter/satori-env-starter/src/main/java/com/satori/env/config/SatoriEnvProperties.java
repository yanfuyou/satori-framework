package com.satori.env.config;

import com.satori.model.enums.ReqSourceEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author yanfuyou
 * @date 2025/08/20 10:06:34
 * @description
 */
@ConfigurationProperties(prefix = "satori.env")
@Data
public class SatoriEnvProperties implements Serializable {
    private ReqSourceEnum reqSource;
}
