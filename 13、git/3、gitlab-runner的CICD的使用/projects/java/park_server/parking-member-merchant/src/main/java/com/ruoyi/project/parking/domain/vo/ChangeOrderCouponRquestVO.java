package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 切换优惠券 请求类
 */
@Data
public class ChangeOrderCouponRquestVO {

    private String orderNo;

    private List<ParkingOrderCouponVO> couponList;
}
