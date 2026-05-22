package com.satori.pay.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: ly
 * @date: 2026-03-12 12:30
 * @description 查询剩余待分金额结果
 */
@Data
public class QueryOrderAmountResult {

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 订单剩余待分金额，单位：元
     */
    private BigDecimal unsplitAmount;
}