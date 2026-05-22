package com.satori.pay.model;

import com.satori.pay.model.enums.PaymentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yanfuyou
 * @date 2025/08/05 15:37:56
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PaymentResult extends TradeResult<PaymentResult> {
    /**
     * 外部交易号
     */
    private String outTradeNo;
    /**
     * 支付人
     * 微信为openId
     */
    private String payer;
    /**
     * 支付金额
     * 订单总金额
     */
    private BigDecimal payAmt;
    /**
     * 支付时间
     */
    private LocalDateTime paidTime;
    /**
     * 支付状态
     */
    private PaymentState state;
}
