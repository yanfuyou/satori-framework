package com.satori.pay.model.event;

import com.satori.pay.model.PaymentResult;
import com.satori.pay.model.enums.TradeChannelEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author yanfuyou
 * @date 2025/08/07 14:30:35
 * @description
 */
@Data
@Accessors(chain = true)
public class PayEvent implements Serializable {
    /**
     * 交易通道
     */
    private TradeChannelEnum channel;
    /**
     * 交易号
     */
    private String tradeNo;
    /**
     * 交易结果
     */
    private PaymentResult result;
}
