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
public enum ProfitReplyCode implements ICodeService<Object> {
    CREATE_ERROR("11001", "订单分账创建失败", "订单分账创建失败[%s]"),
    
    QUERY_ERROR("11002", "查询分账失败", "查询分账失败[%s]"),
    
    CLOSE_ERROR("11003", "关闭分账失败", "关闭分账失败[%s]"),

    OUT_RETURN_ERROR("11004", "分账回退失败", "分账回退处理失败[%s]"),

    QUERY_RETURN_ERROR("11005", "查询分账回退结果失败", "查询分账回退结果处理失败[%s]"),

    UNFREEZE_ERROR("11006", "解冻剩余资金失败", "解冻剩余资金失败[%s]"),

    UNSPLIT_AMOUNT_ERROR("11007", "查询订单剩余待分金额失败", "查询订单剩余待分金额失败[%s]"),
    ;

    public final String code;

    public final String desc;

    public final String msgFormat;
}
