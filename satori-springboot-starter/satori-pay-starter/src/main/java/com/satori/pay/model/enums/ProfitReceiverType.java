package com.satori.pay.model.enums;

/**
 * @author yanfuyou
 * @date 2026/03/09 14:44:20
 * @description 分账接收方类型
 */
public enum ProfitReceiverType {
    /**
     * 商户号
     */
    MERCHANT_ID,
    /**
     * 个人openid,此时账号openId为商户appId对应的openId
     */
    PERSONAL_OPENID,
    /**
     * 个人openid,此时账号openId为子商户appId对应的openId
     */
    PERSONAL_SUB_OPENID,
}
