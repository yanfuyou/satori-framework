package com.satori.pay.model.enums;

/**
 * @author yanfuyou
 * @date 2025/09/23 16:33:47
 * @description
 */

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransferScene {
    COMMISSION("1005"),

    BUY_PAYMENT("1009");

    public final String sceneId;
}
