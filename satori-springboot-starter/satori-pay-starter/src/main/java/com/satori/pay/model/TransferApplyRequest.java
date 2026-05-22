package com.satori.pay.model;

import com.satori.pay.model.enums.TransferScene;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yanfuyou
 * @date 2025/09/23 16:13:34
 * @description 转账申请参数
 */
@Data
public class TransferApplyRequest {
    /**
     * appId
     */
    private String appid;

    /**
     * 商户转账单号
     */
    private String billNo;

    /**
     * 转账场景Id
     */
    private TransferScene transferSceneId;

    /**
     * 转账用户openid
     */
    private String openid;

    /**
     * 金额>=2000必传
     */
    private String userName;

    /**
     * 转账金额
     */
    private BigDecimal transferAmt;

    /**
     * 转账原因
     */
    private String perceptionReason;

    /**
     * 转账备注
     */
    private String remark;

    /**
     * 转账结果通知地址
     */
    private String notifyUrl;

    /**
     * 转账场景
     */
    private List<TransferSceneReport> transferSceneReportInfos;

}
