package com.satori.pay.model.ex;

import com.satori.model.code.ICodeService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author yanfuyou
 * @date 2025/08/05 14:28:23
 * @description
 */
@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public enum PayReplyCode implements ICodeService<Object> {
    CREATE_ERROR("11001", "支付订单创建失败", "支付订单创建失败[%s]"),
    PAY_PROCESS_ERROR("11002", "支付回调处理失败", "支付回调处理失败[%S]"),
    REFUND_APPLY_ERROR("11003", "退款申请失败", "退款申请失败[%S]"),
    PAY_CLOSE_ERROR("11004", "取消支付失败", "取消支付失败[%S]"),
    PAY_QUERY_ERROR("11005", "支付结果查询失败", "支付结果查询失败[%S]"),
    REFUND_QUERY_ERROR("11005", "支付结果查询失败", "支付结果查询失败[%S]"),
    TRANSFER_BILL_APPLY_ERROR("11006", "转账申请失败", "转账申请失败[%S]"),
    TRANSFER_BILL_NOTIFY_ERROR("11007", "转账申请回调处理失败", "转账申请回调处理失败[%S]"),
    REFUND_NOTIFY_ERROR("11008", "退款回调处理失败", "退款回调处理失败[%S]"),
    TRANSFER_QUERY_ERROR("11009", "转账查询失败", "转账查询失败[%S]"),
    TRANSFER_CANCEL_ERROR("11010", "转账撤销失败", "转账撤销失败[%S]"),
    PROFIT_RECEIVER_ADD_ERROR("11011", "分账接收方添加失败", "分账接收方添加失败[%S]"),
    ;

    public final String code;

    public final String desc;

    public final String msgFormat;
}
