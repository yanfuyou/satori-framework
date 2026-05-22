package com.satori.pay.config;

import lombok.Data;

/**
 * @author yanfuyou
 * @date 2025/08/04 14:29:21
 * @description
 */
@Data
public class WxProperties {
    /**
     * appId
     */
    private String appId;
    /**
     * 商户号
     */
    private String mchId;
    /**
     * 商户密钥
     */
    private String mchKey;
    /**
     * v3密钥
     */
    private String v3Key;
    /**
     * 证书序列号
     */
    private String serialNo;
    /**
     * 私钥路径
     */
    private String privateKeyPath;
    /**
     * 证书路径
     */
    private String privateCertPath;
    /**
     * 微信支付公钥路径
     */
    private String wxPublicKeyPath;
    /**
     * 微信支付公钥id
     */
    private String wxPublicKeyId;
    /**
     * 默认支付通知api
     */
    private String payNotifyUrl;
    /**
     * 默认退款通知api
     */
    private String refundNotifyUrl;
    /**
     * 默认转账账单通知api
     */
    private String transferBillNotifyUrl;
}
