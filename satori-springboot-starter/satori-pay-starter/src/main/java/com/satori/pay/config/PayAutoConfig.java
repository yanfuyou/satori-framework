package com.satori.pay.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author yanfuyou
 * @date 2025/08/04 11:31:29
 * @description
 */
@EnableConfigurationProperties({PayProperties.class})
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.satori.pay.service")
public class PayAutoConfig {
    private final PayProperties payProperties;

    @ConditionalOnMissingBean
    @Bean
    public WxPayService wxPayService() {
        WxProperties wx = payProperties.getWx();
        WxPayConfig conf = new WxPayConfig();
        conf.setAppId(wx.getAppId());
        conf.setMchId(wx.getMchId());
        conf.setMchKey(wx.getMchKey());
        conf.setCertSerialNo(wx.getSerialNo());
        conf.setPrivateCertPath(wx.getPrivateCertPath());
        conf.setPrivateKeyPath(wx.getPrivateKeyPath());
        conf.setNotifyUrl(wx.getPayNotifyUrl());
        conf.setApiV3Key(wx.getV3Key());
        conf.setPublicKeyId(wx.getWxPublicKeyId());
        conf.setPublicKeyPath(wx.getWxPublicKeyPath());
        WxPayServiceImpl wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(conf);
        return wxPayService;
    }
}
