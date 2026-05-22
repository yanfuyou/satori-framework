package com.satori.pay.model;

import com.satori.pay.model.enums.RefundState;
import com.satori.pay.model.enums.TradeChannelEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yanfuyou
 * @date 2025/08/05 15:38:04
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RefundResult extends TradeResult<RefundResult> {
    /**
     * 交易通道
     */
    private TradeChannelEnum channel;
    /**
     * 外部交易号
     */
    private String outTradeNo;
    /**
     * 我方退款号
     */
    private String refundNo;
    /**
     * 外部退款号
     */
    private String outRefundNo;
    /**
     * 退款金额
     */
    private BigDecimal refundAmt;
    /**
     * 退款状态
     */
    private RefundState state;
    /**
     * 退款成功时间
     */
    private LocalDateTime refundTime;
}
