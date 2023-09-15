package com.ruoyi.project.parking.domain.vo.parkingorder;

import lombok.Data;

import java.util.List;

/**
 *  订单系统 生成订单接口 输入参数对象
 */
@Data
public class VehicleParkCreateOrderRequestVO {

    /**
     * 场库编号
     */
    private String parkNo;

    /**
     * 通道编号
     * 移动端 null
     */
    private String passageNo;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 车型编码
     */
    private String carTypeCode;

    /**
     * 入场时间
     */
    private String entryTime;

    /**
     * 出场时间
     */
    private String exitTime;

    /**
     * 详情
     */
    private List<VehicleParkOrderItemVO> itemList;

    /**
     * 订单编号
     * 有订单编号为 切换优惠券重计算
     * 无订单 创建新订单
     */
    private String orderNo;

    /**
     * 可用优惠券
     */
    private List<ParkingOrderCouponVO> couponList;

}
