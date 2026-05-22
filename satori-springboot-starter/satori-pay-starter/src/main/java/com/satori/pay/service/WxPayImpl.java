package com.satori.pay.service;

import cn.hutool.core.math.Money;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.github.binarywang.wxpay.bean.notify.*;
import com.github.binarywang.wxpay.bean.request.WxPayPartnerOrderQueryV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayPartnerUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.request.*;
import com.github.binarywang.wxpay.bean.result.*;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.bean.transfer.*;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.satori.common.util.AmountUtil;
import com.satori.model.formaters.CustomFormatter;
import com.satori.pay.config.PayProperties;
import com.satori.pay.model.*;
import com.satori.pay.model.enums.PaymentState;
import com.satori.pay.model.enums.RefundState;
import com.satori.pay.model.enums.TradeChannelEnum;
import com.satori.pay.model.enums.TransferState;
import com.satori.pay.model.event.PayEvent;
import com.satori.pay.model.event.RefundEvent;
import com.satori.pay.model.event.TransferBillEvent;
import com.satori.pay.model.ex.PayReplyCode;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;


/**
 * @author yanfuyou
 * @date 2025/08/04 11:41:49
 * @description
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WxPayImpl implements PayService {
    private final WxPayService wxPayService;
    private final ApplicationEventPublisher eventPublisher;
    @Resource
    private TradeNotifyLogInterface tradeNotifyLog;
    private final PayProperties payProperties;

    @Override
    public TradeChannelEnum getChl() {
        return TradeChannelEnum.WX;
    }

    @Override
    public JsapiPayResult jsapiPay(CreatePayRequest request) {
        WxPayUnifiedOrderV3Request wxReq = new WxPayUnifiedOrderV3Request();
        wxReq.setDescription(request.getDescription());
        wxReq.setOutTradeNo(request.getBusinessOrderNo());
        Money money = new Money(request.getAmount());
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount().setTotal((int) money.getCent());
        wxReq.setAmount(amount);
        WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
        payer.setOpenid(request.getPayerOpenId());
        wxReq.setPayer(payer);
        String expireTime = CustomFormatter.RFC3339.format(Optional.ofNullable(request.getExpireTime()).orElse(OffsetDateTime.now().plusMinutes(15)));
        wxReq.setTimeExpire(expireTime);
        WxPayUnifiedOrderV3Result.JsapiResult res;
        try {
            res = wxPayService.createOrderV3(TradeTypeEnum.JSAPI, wxReq);
        } catch (Exception e) {
            log.error("创建支付订单异常,tradeNo:{}", request.getBusinessOrderNo(), e);
            throw PayReplyCode.CREATE_ERROR.buildEx(this.getChl());
        }
        return new JsapiPayResult()
                .setAppId(res.getAppId())
                .setTimeStamp(res.getTimeStamp())
                .setSignType(res.getSignType())
                .setNonceStr(res.getNonceStr())
                .setPackageValue(res.getPackageValue())
                .setPaySign(res.getPaySign());
    }

    @Override
    public RefundApplyResult refundApply(RefundApplyRequest request) {
        WxPayRefundV3Request wxReq = new WxPayRefundV3Request();
        wxReq.setOutTradeNo(request.getTradeNo())
                .setOutRefundNo(request.getRefundNo())
                .setReason(request.getReason())
                .setTransactionId(request.getOutTradeNo());
        WxPayRefundV3Request.Amount amount = new WxPayRefundV3Request.Amount();
        amount.setCurrency("CNY")
                .setRefund((int) new Money(request.getRefundAmt()).getCent())
                .setTotal((int) new Money(request.getOrderAmt()).getCent());
        wxReq.setAmount(amount);
        wxReq.setNotifyUrl(payProperties.getWx().getRefundNotifyUrl());
        WxPayRefundV3Result wxResult;
        try {
            wxResult = wxPayService.refundV3(wxReq);
        } catch (Exception e) {
            log.error("微信退款异常,tradeNo:{}", request.getTradeNo(), e);
            throw PayReplyCode.REFUND_APPLY_ERROR.buildEx(this.getChl());
        }
        LocalDateTime successTime = null;
        if (StrUtil.isNotBlank(wxResult.getSuccessTime())) {
            successTime = LocalDateTime.parse(wxResult.getSuccessTime(), CustomFormatter.RFC3339);
        }
        return new RefundApplyResult()
                .setChannel(this.getChl())
                .setTradeNo(wxResult.getOutTradeNo())
                .setRefundNo(wxResult.getOutRefundNo())
                .setOutTradeNo(wxResult.getTransactionId())
                .setOutRefundNo(wxResult.getRefundId())
                .setRefundAmt(AmountUtil.fenToYuan(wxResult.getAmount().getRefund()))
                .setRefundTime(successTime)
                .setState(RefundState.valueOfWx(wxResult.getStatus()));
    }

    @Override
    public PayEvent payQuery(String tradeNo) {
        WxPayOrderQueryV3Result result;
        try {
            result = wxPayService.queryOrderV3(null, tradeNo);
        } catch (WxPayException e) {
            log.error("微信支付结果查询失败,tradeNo:{}", tradeNo, e);
            throw PayReplyCode.PAY_QUERY_ERROR.buildEx();
        }

        String payerId = Optional.ofNullable(result.getPayer())
                .map(WxPayOrderQueryV3Result.Payer::getOpenid)
                .orElse(null);
        PaymentState state = PaymentState.valueOfWx(result.getTradeState());
        PaymentResult res = new PaymentResult()
                .setChannel(this.getChl())
                .setTradeNo(result.getOutTradeNo())
                .setOutTradeNo(result.getTransactionId())
                .setPayer(payerId)
                .setState(state);
        if (PaymentState.PAID == state) {
            res.setPayAmt(AmountUtil.fenToYuan(result.getAmount().getTotal()))
                    .setPaidTime(LocalDateTime.parse(result.getSuccessTime(), CustomFormatter.RFC3339));
        }
        return new PayEvent()
                .setChannel(this.getChl())
                .setTradeNo(result.getOutTradeNo())
                .setResult(res);
    }

    @Override
    public RefundEvent refundQuery(String refundNo) {
        WxPayRefundQueryV3Result result;
        try {
            result = wxPayService.refundQueryV3(refundNo);
        } catch (WxPayException e) {
            log.error("微信退款结果查询失败，refundNo:{}", refundNo, e);
            throw PayReplyCode.REFUND_QUERY_ERROR.buildEx();
        }
        RefundState state = RefundState.valueOfWx(result.getStatus());
        RefundResult res = new RefundResult()
                .setChannel(this.getChl())
                .setTradeNo(result.getOutTradeNo())
                .setOutTradeNo(result.getTransactionId())
                .setRefundNo(result.getOutRefundNo())
                .setOutRefundNo(result.getRefundId())
                .setOutTradeNo(result.getTransactionId())
                .setRefundAmt(AmountUtil.fenToYuan(result.getAmount().getRefund()))
                .setState(state);
        if (state == RefundState.SUCCESS) {
            res.setRefundTime(LocalDateTime.parse(result.getSuccessTime(), CustomFormatter.RFC3339));
        }
        return new RefundEvent()
                .setChannel(this.getChl())
                .setTradeNo(result.getOutTradeNo())
                .setRefundNo(result.getOutRefundNo())
                .setResult(res);
    }

    @Override
    public void payClose(String tradeNo) {
        try {
            wxPayService.closeOrderV3(tradeNo);
        } catch (WxPayException e) {
            log.error("微信订单关闭失败", e);
            throw PayReplyCode.PAY_CLOSE_ERROR.buildEx();
        }
    }

    @Override
    public TransferApplyResult transferBillApply(TransferApplyRequest request) {
        TransferBillsRequest req = new TransferBillsRequest();
        req.setAppid(request.getAppid());
        req.setOutBillNo(request.getBillNo());
        req.setTransferSceneId(request.getTransferSceneId().sceneId);
        req.setOpenid(request.getOpenid());
        req.setUserName(request.getUserName());
        req.setTransferAmount((int) AmountUtil.yuanToFen(request.getTransferAmt()));
        req.setTransferRemark(request.getRemark());
        req.setNotifyUrl(request.getNotifyUrl());
        req.setUserRecvPerception(request.getPerceptionReason());
        List<TransferBillsRequest.TransferSceneReportInfo> infos = request.getTransferSceneReportInfos().stream().map(item -> {
            TransferBillsRequest.TransferSceneReportInfo report = new TransferBillsRequest.TransferSceneReportInfo();
            report.setInfoType(item.getInfoType().infoType);
            report.setInfoContent(item.getInfoContent());
            return report;
        }).toList();
        req.setTransferSceneReportInfos(infos);
        TransferBillsResult wxRes;
        try {
            wxRes = wxPayService.getTransferService().transferBills(req);
        } catch (Exception e) {
            log.error(PayReplyCode.TRANSFER_BILL_APPLY_ERROR.desc, e);
            throw PayReplyCode.TRANSFER_BILL_APPLY_ERROR.buildEx();
        }
        TransferApplyResult result = new TransferApplyResult();
        result.setBillNo(request.getBillNo())
                .setOutBillNo(wxRes.getTransferBillNo())
                .setState(TransferState.valueOfWx(wxRes.getState()))
                .setFailReason(wxRes.getFailReason())
                .setPackageInfo(wxRes.getPackageInfo());
        return result;
    }

    @Override
    public TransferApplyResult transferBillQuery(String billNo, String outBillNo) {
        TransferBillsGetResult wxRes;
        try {
            wxRes = StrUtil.isNotBlank(billNo) ? wxPayService.getTransferService().getBillsByOutBillNo(billNo)
                    : wxPayService.getTransferService().getBillsByTransferBillNo(billNo);
        } catch (Exception e) {
            log.error("微信转账结果查询失败，billNo:{}", billNo, e);
            throw PayReplyCode.TRANSFER_QUERY_ERROR.buildEx(billNo);
        }
        TransferApplyResult result = new TransferApplyResult();
        return result.setBillNo(wxRes.getOutBillNo())
                .setOutBillNo(wxRes.getTransferBillNo())
                .setState(TransferState.valueOfWx(wxRes.getState()))
                .setFailReason(wxRes.getFailReason());
    }

    @Override
    public TransferCancelResult transferBillCancel(String billNo) {
        try {
            TransferBillsCancelResult wxRes = wxPayService.getTransferService().transformBillsCancel(billNo);
            TransferCancelResult result = new TransferCancelResult();
            result.setBillNo(billNo);
            result.setOutBillNo(wxRes.getTransferBillNo());
            result.setState(TransferState.valueOfWx(wxRes.getState()));
            return result;
        } catch (Exception e) {
            log.error("微信转账撤销失败，billNo:{}", billNo, e);
            throw PayReplyCode.TRANSFER_QUERY_ERROR.buildEx(billNo);
        }
    }

    @Override
    public String payNotifyProcess(String notifyData, SignatureHeader header) {
        WxPayNotifyV3Result v3Result;
        log.info("微信支付回调消息：{}", notifyData);
        try {
            v3Result = wxPayService.parseOrderNotifyV3Result(notifyData, header);
            log.info("微信支付回调消息处理结果：{}", JSON.toJSONString(v3Result));
        } catch (WxPayException e) {
            log.error("微信支付回调消息解析异常：{}", notifyData, e);
            return WxPayNotifyResponse.fail("fail");
        }
        WxPayNotifyV3Result.DecryptNotifyResult result = v3Result.getResult();
        PaymentState state = PaymentState.valueOfWx(result.getTradeState());
        if (state == null) {
            log.info("微信支付回调消息非终态,tradeNo:{}", result.getOutTradeNo());
            return WxPayNotifyResponse.success("success");
        }
        String payerId = Optional.ofNullable(result.getPayer())
                .map(WxPayNotifyV3Result.Payer::getOpenid)
                .orElse(null);
        PaymentResult res = new PaymentResult()
                .setChannel(this.getChl())
                .setTradeNo(result.getOutTradeNo())
                .setOutTradeNo(result.getTransactionId())
                .setPayer(payerId)
                .setState(state);
        if (StrUtil.isNotBlank(result.getSuccessTime())) {
            res.setPayAmt(AmountUtil.fenToYuan(result.getAmount().getTotal()))
                    .setPaidTime(LocalDateTime.parse(result.getSuccessTime(), CustomFormatter.RFC3339));
        }
        PayEvent payEvent = new PayEvent()
                .setChannel(this.getChl())
                .setTradeNo(result.getOutTradeNo())
                .setResult(res);
        if (null != tradeNotifyLog) {
            tradeNotifyLog.payLog(payEvent);
        }
        eventPublisher.publishEvent(payEvent);
        return WxPayNotifyResponse.success("success");
    }

    @Override
    public String refundNotifyProcess(String notifyData, SignatureHeader header) {
        WxPayRefundNotifyV3Result v3Result;
        log.info("微信退款回调消息：{}", notifyData);
        try {
            v3Result = wxPayService.parseRefundNotifyV3Result(notifyData, header);
            log.info("微信退款回调消息处理结果：{}", JSON.toJSONString(v3Result));
        } catch (WxPayException e) {
            log.error("微信退款回调消息解析异常：{}", notifyData, e);
            return WxPayNotifyResponse.fail("fail");
        }
        WxPayRefundNotifyV3Result.DecryptNotifyResult result = v3Result.getResult();
        RefundState state = RefundState.valueOfWx(result.getRefundStatus());
        if (state == null || state == RefundState.PROCESSING) {
            log.info("微信退款回调消息非终态,refundNo:{}", result.getOutRefundNo());
            return WxPayNotifyResponse.success("success");
        }

        RefundResult res = new RefundResult()
                .setChannel(this.getChl())
                .setTradeNo(result.getOutTradeNo())
                .setOutTradeNo(result.getTransactionId())
                .setRefundNo(result.getOutRefundNo())
                .setOutRefundNo(result.getRefundId())
                .setOutTradeNo(result.getTransactionId())
                .setRefundAmt(AmountUtil.fenToYuan(result.getAmount().getRefund()))
                .setState(state);
        if (state == RefundState.SUCCESS) {
            res.setRefundTime(LocalDateTime.parse(result.getSuccessTime(), CustomFormatter.RFC3339));
        }
        RefundEvent refundEvent = new RefundEvent()
                .setChannel(this.getChl())
                .setTradeNo(result.getOutTradeNo())
                .setRefundNo(result.getOutRefundNo())
                .setResult(res);
        if (null != tradeNotifyLog) {
            tradeNotifyLog.refundLog(refundEvent);
        }
        eventPublisher.publishEvent(refundEvent);
        return WxPayNotifyResponse.success("success");
    }

    @Override
    public String transferBillNotifyProcess(String notifyData, SignatureHeader header) {
        log.info("微信商家转账回调消息：{}", notifyData);
        TransferBillsNotifyResult result;
        try {
            result = wxPayService.getTransferService().parseTransferBillsNotifyResult(notifyData, header);
            log.info("微信商家转账回调消息处理结果：{}", JSON.toJSONString(result));
        } catch (Exception e) {
            log.error("微信商家转账回调消息解析异常：{}", notifyData, e);
            return WxPayNotifyResponse.fail("fail");
        }
        TransferBillsNotifyResult.DecryptNotifyResult billRes = result.getResult();
        TransferState state = TransferState.valueOfWx(billRes.getState());
        switch (state) {
            case SUCCESS, FAIL, CANCELLED -> {
                TransferBillEvent event = new TransferBillEvent();
                event.setChannel(this.getChl())
                        .setBillNo(billRes.getOutBillNo())
                        .setOutBillNo(billRes.getTransferBillNo())
                        .setState(state)
                        .setFailReason(billRes.getFailReason());
                if (null != tradeNotifyLog) {
                    tradeNotifyLog.BillLog(event);
                }
                eventPublisher.publishEvent(event);
            }
            default -> log.info("当前转账订单通知不是终态;billNo:{},state:{}", billRes.getOutBillNo(), state.name());
        }
        return WxPayNotifyResponse.success("success");
    }
}
