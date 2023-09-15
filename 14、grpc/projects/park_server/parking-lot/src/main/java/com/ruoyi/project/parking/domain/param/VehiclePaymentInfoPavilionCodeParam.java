package com.ruoyi.project.parking.domain.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 岗亭支付请求参数
 * 岗亭支付码 需要岗亭信息
 */
@Data
public class VehiclePaymentInfoPavilionCodeParam {
    /**
     * 停车场编号
     */
    @NotBlank
    private String parkNo;

    /**
     * 通道编号
     */
    @NotBlank
    private String passageNo;
}
