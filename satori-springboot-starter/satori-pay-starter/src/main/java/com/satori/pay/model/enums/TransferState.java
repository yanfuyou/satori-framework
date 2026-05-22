package com.satori.pay.model.enums;

import lombok.AllArgsConstructor;

/**
 * @author yanfuyou
 * @date 2025/09/23 10:42:03
 * @description
 */
@AllArgsConstructor
public enum TransferState {
    ACCEPTED(0, "转账已受理"),
    PROCESSING(1, "转账处理中"),
    WAIT_USER_CONFIRM(2, "待收款用户确认"),
    TRANSFERING(3, "转账中"),
    SUCCESS(4, "转账成功,终态"),
    FAIL(5, "转账失败,终态"),
    CANCELING(6, "转账撤销中"),
    CANCELLED(7, "转账撤销完成,终态"),
    ;

    public final int value;

    public final String desc;

    public static TransferState valueOfWx(String wxState) {
        for (TransferState transferState : TransferState.values()) {
            if (transferState.name().equals(wxState)) {
                return transferState;
            }
        }
        throw new RuntimeException("没有对应的状态");
    }
}