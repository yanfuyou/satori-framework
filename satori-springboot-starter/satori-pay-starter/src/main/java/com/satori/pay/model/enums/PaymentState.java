package com.satori.pay.model.enums;

import java.io.Serializable;

/**
 * @author yanfuyou
 * @date 2025/08/05 15:43:49
 * @description
 */
public enum PaymentState implements Serializable {
    /**
     * 支付成功
     */
    PAID,
    /**
     * 待支付
     */
    WAIT_PAY,
    /**
     * 支付关闭
     */
    CLOSED,
    /**
     * 已退款
     */
    REFUND,
    ;

    public static PaymentState valueOfWx(String wxState) {
        // 暂未处理付款码支付场景
        return switch (wxState) {
            case "SUCCESS" -> PAID;
            case "NOTPAY" -> WAIT_PAY;
            case "CLOSED" -> CLOSED;
            case "REFUND" -> REFUND;
            default -> null;
        };
    }
}
