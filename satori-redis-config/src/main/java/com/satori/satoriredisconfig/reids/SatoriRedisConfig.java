package com.satori.satoriredisconfig.reids;


import com.satori.satoriredisconfig.reids.codec.CustomSnappyCodecV2;
import lombok.Data;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.TransportMode;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.redisson.spring.starter.RedissonAutoConfigurationV2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;

/**
 * @author YanFuYou
 * @date 2024/02/24 下午 10:57
 */
@Data
@Configuration
@ConditionalOnClass(RedissonAutoConfigurationV2.class)
@ConfigurationProperties(prefix = "spring.data.redisson")
@PropertySource("classpath:satori-redis.properties")
public class SatoriRedisConfig implements RedissonAutoConfigurationCustomizer {
    private SingleServerConfig singleServerConfig;

    private Boolean useCodeV2;

    @Override
    public void customize(Config config) {
        config.useSingleServer()
                .setAddress(singleServerConfig.getAddress())
                .setConnectionMinimumIdleSize(singleServerConfig.getConnectionMinimumIdleSize())
                .setConnectionPoolSize(singleServerConfig.getConnectionPoolSize())
                .setSubscriptionConnectionPoolSize(singleServerConfig.getSubscriptionConnectionPoolSize())
                .setSubscriptionConnectionMinimumIdleSize(singleServerConfig.getSubscriptionConnectionMinimumIdleSize());
        if (StringUtils.hasLength(singleServerConfig.getPassword())) {
            config.useSingleServer().setPassword(singleServerConfig.getPassword());
        }
        if (StringUtils.hasLength(singleServerConfig.getUsername())) {
            config.useSingleServer().setUsername(singleServerConfig.getUsername());
        }
        config.setTransportMode(TransportMode.NIO);
        config.setCodec(new CustomSnappyCodecV2());
    }
}
