package com.satori.pay.model;

import com.satori.pay.model.enums.TradeChannelEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yanfuyou
 * @date 2025/08/07 09:50:21
 * @description
 */
@Data
public class TradeResult<T extends TradeResult<T>> implements Serializable {
    /**
     * 交易通道
     */
    private TradeChannelEnum channel;
    /**
     * 我方交易号
     */
    private String tradeNo;


    public T setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
        return (T) this;
    }

    public T setChannel(TradeChannelEnum channel) {
        this.channel = channel;
        return (T) this;
    }
}
