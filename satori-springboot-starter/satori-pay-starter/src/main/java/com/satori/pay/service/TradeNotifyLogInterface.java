package com.satori.pay.service;

import com.satori.pay.model.PaymentResult;
import com.satori.pay.model.RefundResult;
import com.satori.pay.model.event.PayEvent;
import com.satori.pay.model.event.RefundEvent;
import com.satori.pay.model.event.TransferBillEvent;

/**
 * @author yanfuyou
 * @date 2025/08/07 09:48:39
 * @description 回调日志记录接口
 */
public interface TradeNotifyLogInterface {
    /**
     * 记录支付回调日志
     *
     * @param event 支付事件
     */
    void payLog(PayEvent event);


    /**
     * 记录退款回调日志
     *
     * @param event 退款时间
     */
    void refundLog(RefundEvent event);

    /**
     * 记录转账回调日志
     * @param event 转账事件
     */
    void BillLog(TransferBillEvent event);

    /**
     * 业务单完成后清理支付日志
     *
     * @param tradeNo 我方交易号
     */
    void cleanPayLog(String tradeNo);


    /**
     * 业务单完成后清理退款日志
     *
     * @param refundNo 我方退款单号
     */
    void cleanRefundLog(String refundNo);

    /**
     * 业务单完成后清理转账日志
     * @param billNo 我方转账单号
     */
    void cleanBillLog(String billNo);
}
