package com.ruoyi.project.parking.domain.param;

import com.ruoyi.project.parking.domain.vo.ParkingOrderCouponVO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

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


    /**
     * 优惠券
     */
    List<ParkingOrderCouponVO> couponList;
}
