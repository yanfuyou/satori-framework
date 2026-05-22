package com.satori.pay.service;

import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.request.WxPayRefundQueryV3Request;
import com.satori.pay.model.*;
import com.satori.pay.model.enums.TradeChannelEnum;
import com.satori.pay.model.event.PayEvent;
import com.satori.pay.model.event.RefundEvent;

/**
 * @author yanfuyou
 * @date 2025/08/04 11:41:31
 * @description
 */
public interface PayService {

    /**
     * 获取当前支付渠道
     * @return 当前支付渠道
     */
    default TradeChannelEnum getChl() {
        throw new UnsupportedOperationException("通道不明");
    }

    /**
     * jsapi 下单
     *
     * @param request 下单数据
     * @return 支付数据
     */
    JsapiPayResult jsapiPay(CreatePayRequest request);

    /**
     * 退款申请
     *
     * @param request 退款数据
     * @return 支付数据
     */
    RefundApplyResult refundApply(RefundApplyRequest request);

    /**
     * 支付结果查询
     *
     * @param tradeNo 我方交易号
     * @return 支付结果
     */
    PayEvent payQuery(String tradeNo);

    /**
     * 退款结果查询
     *
     * @param refundNo 我方退款单号
     * @return 退款结果
     */
    RefundEvent refundQuery(String refundNo);

    /**
     * 关闭订单
     *
     * @param tradeNo 我方交易单号
     */
    void payClose(String tradeNo);

    /**
     * 转账申请
     * @param request 申请数据
     * @return 申请结果
     */
    TransferApplyResult transferBillApply(TransferApplyRequest request);

    /**
     * 转账查询
     * @param billNo 转账单号
     * @param outBillNo 三方转账单号
     * @return 申请结果
     */
    TransferApplyResult transferBillQuery(String billNo, String outBillNo);

    /**
     * 转账撤销
     * @param billNo 转账单号
     * @return 申请结果
     */
    TransferCancelResult transferBillCancel(String billNo);

    /**
     * 回调处理
     *
     * @param notifyData 回调数据
     * @param header     回调头信息
     * @return 处理结果
     */
    String payNotifyProcess(String notifyData, SignatureHeader header);

    /**
     * 回调处理
     *
     * @param notifyData 回调数据
     * @param header     回调头信息
     * @return 处理结果
     */
    String refundNotifyProcess(String notifyData, SignatureHeader header);

    /**
     * 回调处理
     *
     * @param notifyData 回调数据
     * @param header     回调头信息
     * @return 处理结果
     */
    String transferBillNotifyProcess(String notifyData, SignatureHeader header);
}
