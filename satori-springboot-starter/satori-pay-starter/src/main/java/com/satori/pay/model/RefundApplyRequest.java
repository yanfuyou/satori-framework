package com.satori.pay.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yanfuyou
 * @date 2025/08/04 15:47:13
 * @description
 */
@Data
@Accessors(chain = true)
public class RefundApplyRequest implements Serializable {
    /**
     * 我方交易单号
     */
    private String tradeNo;

    /**
     * 外部交易单号
     */
    private String outTradeNo;

    /**
     * 我方退款单号
     */
    private String refundNo;

    /**
     * 退款金额
     */
    private BigDecimal refundAmt;

    /**
     * 订单支付金额
     */
    private BigDecimal orderAmt;

    /**
     * 退款理由
     */
    private String reason;
}
