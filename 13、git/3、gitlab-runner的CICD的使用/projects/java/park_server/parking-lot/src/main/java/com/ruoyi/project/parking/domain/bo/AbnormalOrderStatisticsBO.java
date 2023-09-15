package com.ruoyi.project.parking.domain.bo;

import lombok.Data;


@Data
public class AbnormalOrderStatisticsBO {
    //停车订单未支付数
    private Integer unpaidNum;
    //停车订单支付成功数
    private Integer payNum;
    //停车订单支付总金额
    private double payAmount;
    //停车订单优惠总金额
    private double discountAmount;
    //停车订单出场纪录数
    private Integer exitNum;
}
