package com.satori.pay.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author yanfuyou
 * @date 2025/08/04 17:48:27
 * @description
 */
@Data
@Accessors(chain = true)
public class JsapiPayResult implements Serializable {
    private String appId;
    private String timeStamp;
    private String nonceStr;
    private String packageValue;
    private String signType;
    private String paySign;
}
