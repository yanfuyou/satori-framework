package com.satori.pay.model.enums;

import lombok.AllArgsConstructor;

/**
 * @author yanfuyou
 * @date 2025/09/23 16:56:14
 * @description
 */

@AllArgsConstructor
public enum SceneInfoType {
    POSITION("岗位类型"),
    REMUNERATION_DESC("报酬说明"),
    PRODUCT_NAME("采购商品名称");

    public final String infoType;
}
