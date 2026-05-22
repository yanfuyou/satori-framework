package com.satori.pay.model.enums;

/**
 * @author yanfuyou
 * @date 2025/08/05 15:43:58
 * @description
 */
public enum RefundState {
    /**
     * 退款成功
     */
    SUCCESS,
    /**
     * 退款处理中
     */
    PROCESSING,
    /**
     * 退款关闭
     */
    CLOSED,
    /**
     * 退款异常
     */
    ABNORMAL,
    ;

    public static RefundState valueOfWx(String wxState) {
        return switch (wxState) {
            case "SUCCESS" -> SUCCESS;
            case "CLOSED" -> CLOSED;
            case "PROCESSING" -> PROCESSING;
            case "ABNORMAL" -> ABNORMAL;
            default -> null;
        };
    }
}
