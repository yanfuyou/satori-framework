package com.satori.pay.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * @author yanfuyou
 * @date 2025/08/04 15:47:13
 * @description
 */
@Data
@Accessors(chain = true)
public class CreatePayRequest implements Serializable {
    /**
     * 商品描述
     */
    private String description;
    /**
     * 商户订单号
     */
    private String businessOrderNo;
    /**
     * 金额
     * 单位：元
     */
    private BigDecimal amount;

    /**
     * 过期时间
     */
    private OffsetDateTime expireTime;

    /**
     * 支付人openId
     */
    private String payerOpenId;
}
