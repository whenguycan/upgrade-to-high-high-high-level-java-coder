package com.ruoyi.project.parking.domain.rgpcmodel;

import lombok.Data;

import java.io.Serializable;

@Data
public class AssignedCoupon implements Serializable {

    private Integer id;
    // 车牌号
    private String carNumber;
    //场地编号
    private String parkNo ;

    //优惠券费用
    private String couponValue ;
    //优惠券种类;1-面值型；2-小时型
    private String couponType ;
    //优惠券类型；1-平台券；2-商户券
    private String couponMold ;
    //优惠券编号
    private String couponCode ;
    //优惠券状态
    private String couponStatus ;
    //领取时间
    private String allocatedTime ;
    // 有效结束时间
    private String validEndTime ;
}
