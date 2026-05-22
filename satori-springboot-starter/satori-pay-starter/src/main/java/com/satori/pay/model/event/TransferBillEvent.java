package com.satori.pay.model.event;

import com.satori.pay.model.enums.TradeChannelEnum;
import com.satori.pay.model.enums.TransferState;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author yanfuyou
 * @date 2025/9/23 21:11
 * @description
 **/
@Data
@Accessors(chain = true)
public class TransferBillEvent implements Serializable {
    private TradeChannelEnum channel;

    /**
     * 转账单号
     */
    private String billNo;

    /**
     * 外部转账单号
     */
    private String outBillNo;

    /**
     * 转账状态
     */
    private TransferState state;

    /**
     * 转账失败原因
     */
    private String failReason;
}
