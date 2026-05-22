package com.satori.pay.model;

import com.satori.pay.model.enums.SceneInfoType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yanfuyou
 * @date 2025/09/23 16:38:03
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferSceneReport {

    private SceneInfoType infoType;

    private String infoContent;
}
