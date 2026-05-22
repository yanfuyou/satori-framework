package com.satori.oss.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "satori.file")
public class OssProperties {
    /**
     * 服务商
     */
    private String server;

    /**
     * 密钥ID
     */
    private String secretId;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 节点
     */
    private String endpoint;

    /**
     * 主机
     */
    private String host;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 指纹
     */
    private String fingerprint;

    /**
     * 区域
     */
    private String region;

    /**
     * 命名空间
     */
    private String nameSpace;

    /**
     * bucket
     */
    private String bucket;

    /**
     * 预先验证的请求路径
     */
    private String authUrl;
}
