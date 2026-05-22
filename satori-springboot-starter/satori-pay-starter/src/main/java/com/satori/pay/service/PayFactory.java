package com.satori.pay.service;

import com.satori.pay.model.enums.TradeChannelEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yanfuyou
 * @date 2025/08/04 11:41:16
 * @description
 */
@Slf4j
@Component
public class PayFactory implements InitializingBean {
    @Resource
    private ObjectProvider<PayService> payServiceProvider;

    private static Map<TradeChannelEnum, PayService> payServiceMap;


    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            payServiceMap = payServiceProvider.stream().collect(Collectors.toMap(PayService::getChl, Function.identity()));
        } catch (Exception e) {
            log.error("初始化支付服务失败", e);
            throw new RuntimeException("初始化支付服务失败");
        }
    }

    public static PayService getPayService(TradeChannelEnum payChannel) {
        PayService payService = payServiceMap.get(payChannel);
        return Optional.ofNullable(payService).orElseThrow(() -> new UnsupportedOperationException(payChannel.name() + "支付服务不存在"));
    }
}
