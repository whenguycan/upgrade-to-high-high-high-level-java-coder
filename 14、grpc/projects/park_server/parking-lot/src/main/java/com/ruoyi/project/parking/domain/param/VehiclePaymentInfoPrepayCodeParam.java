package com.ruoyi.project.parking.domain.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 预支付请求参数
 * 预支付码 需要车牌号
 */
@Data
public class VehiclePaymentInfoPrepayCodeParam {
    /**
     * 停车场编号
     */
    @NotBlank
    private String parkNo;

    /**
     * 车牌号
     */
    @NotBlank
    private String carNumber;
}
