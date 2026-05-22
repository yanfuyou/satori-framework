package com.satori.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yanfuyou
 * @date 2025/08/04 11:30:05
 * @description properties
 */

@ConfigurationProperties(prefix = "satori.pay")
@Data
public class PayProperties {
   private WxProperties wx;

}
