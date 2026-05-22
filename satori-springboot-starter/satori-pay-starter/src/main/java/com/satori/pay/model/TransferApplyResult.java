package com.satori.pay.model;

import com.satori.pay.model.enums.TransferState;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yanfuyou
 * @date 2025/09/22 15:50:28
 * @description
 */
@Data
@Accessors(chain = true)
public class TransferApplyResult {
    /**
     * 交易单号
     */
    private String billNo;

    /**
     * 微信转账单号
     */
    private String outBillNo;

    /**
     * 转账状态
     */
    private TransferState state;

    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 转账信息
     */
    private String packageInfo;
}
