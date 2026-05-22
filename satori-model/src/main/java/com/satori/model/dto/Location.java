package com.satori.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yanfuyou
 * @date 2025/8/2 13:48
 * @description 经纬度
 **/
public class Location implements Serializable {
    @Schema(description ="纬度")
    private BigDecimal latitude;
    @Schema(description ="经度")
    private BigDecimal longitude;
}
