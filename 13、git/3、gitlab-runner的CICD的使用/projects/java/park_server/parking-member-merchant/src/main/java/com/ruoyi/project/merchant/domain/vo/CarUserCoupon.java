package com.ruoyi.project.merchant.domain.vo;

import com.ruoyi.project.merchant.domain.TCouponCarnoRelation;
import com.ruoyi.project.merchant.domain.TCouponDetail;
import lombok.Data;

import java.io.Serializable;

@Data
public class CarUserCoupon implements Serializable {

    private TCouponDetail tCouponDetail;
    private TCouponCarnoRelation couponCarnoRelation;
}
