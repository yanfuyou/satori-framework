package com.satori.msg.config;

import com.satori.msg.service.MsgService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class MsgConfig {


    @Bean
    @ConditionalOnProperty(name = "satori.msg.enable", havingValue = "true")
    public MsgService msgService() {
        return new MsgService();
    }
}
