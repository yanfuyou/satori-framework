package com.satori.pay.model;

import com.satori.pay.model.enums.RefundState;
import com.satori.pay.model.enums.TradeChannelEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yanfuyou
 * @date 2025/08/04 17:48:27
 * @description
 */
@Data
@Accessors(chain = true)
public class RefundApplyResult implements Serializable {
    /**
     * 交易渠道
     */
    private TradeChannelEnum channel;
    /**
     * 我方交易单号
     */
    private String tradeNo;
    /**
     * 外部交易单号
     */
    private String outTradeNo;
    /**
     * 退款单号
     */
    private String refundNo;
    /**
     * 外部退款单号
     */
    private String outRefundNo;

    /**
     * 退款金额
     */
    private BigDecimal refundAmt;

    /**
     * 状态
     * 异常时需要手动确认是否
     */
    private RefundState state;

    /**
     * 退款成功时间
     * 状态成功时有值
     */
    private LocalDateTime refundTime;

}
