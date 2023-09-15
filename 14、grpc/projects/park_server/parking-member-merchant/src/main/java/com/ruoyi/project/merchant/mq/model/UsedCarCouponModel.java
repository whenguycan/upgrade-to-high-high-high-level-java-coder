package com.ruoyi.project.merchant.mq.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UsedCarCouponModel implements Serializable {

    private String usedCoupons;
    private String usedTime;
}
