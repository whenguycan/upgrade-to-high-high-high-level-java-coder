package com.ruoyi.project.parking.domain.param;

import com.ruoyi.project.parking.domain.vo.ParkingOrderCouponVO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

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

    /**
     * 优惠券
     */
    List<ParkingOrderCouponVO> couponList;
}
