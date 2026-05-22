package com.satori.pay.model.event;

import com.satori.pay.model.RefundResult;
import com.satori.pay.model.enums.TradeChannelEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author yanfuyou
 * @date 2025/08/07 14:30:24
 * @description
 */
@Data
@Accessors(chain = true)
public class RefundEvent implements Serializable {
    /**
     * 渠道通道
     */
    private TradeChannelEnum channel;
    /**
     * 交易号
     */
    private String tradeNo;
    /**
     * 退款单号
     */
    private String refundNo;
    /**
     * 退款结果
     */
    private RefundResult result;
}
