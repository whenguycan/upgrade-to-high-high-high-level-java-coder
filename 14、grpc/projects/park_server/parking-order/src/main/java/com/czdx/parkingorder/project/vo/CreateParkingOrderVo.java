package com.czdx.parkingorder.project.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: tangwei
 * @Date: 2023/2/27 10:43 AM
 * @Description: 类描述信息
 */
@Data
public class CreateParkingOrderVo {

    String parkNo;
    // 岗亭编号
    String passageNo;
    // 车牌号
    String carNumber;
    // 车型编码
    String carTypeCode;
    // 入场时间
    String entryTime;
    // 出场时间
    String exitTime;
    //应付金额
    Double payableAmount;
    //折扣金额
    Double discountAmount;
    //实付金额
    Double payAmount;

    List<OrderItem> itemList;

    List<CouponInfo> couponList;

    String orderNo;

    Boolean payed;

    @Data
    public static class OrderItem{
        private Integer parkFieldId;
        // 入场时间
        private String entryTime;
        // 出场时间
        private String exitTime;

        private Integer parkingTimet;

        private Double payableAmount;
    }

    @Data
    public static class CouponInfo{
        private Integer couponType;

        private Integer couponMold;

        private Integer couponValue;

        private String couponCode;

        private Boolean choosed;
    }

}


