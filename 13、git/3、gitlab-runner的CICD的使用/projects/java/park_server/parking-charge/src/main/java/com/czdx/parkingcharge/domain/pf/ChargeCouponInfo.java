package com.czdx.parkingcharge.domain.pf;

import com.czdx.grpc.lib.charge.CouponInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 *
 * description: 计费优惠券信息
 * @author mingchenxu
 * @date 2023/3/16 15:20
 */
@Data
public class ChargeCouponInfo {

    /**
     * 优惠券种类
     * 1-面值型；2-小时型
     */
    private Integer couponType;

    /**
     * 优惠券类型
     * 1-平台券；2-商户券
     */
    private Integer couponMold;

    /**
     * 优惠券值（面值型则为金额，小时型则为小时）
     */
    private Integer couponValue;

    /**
     * 券码
     */
    private String couponCode;

    /**
     * 能否使用
     */
    private Boolean canUse;

    /**
     * 是否已选
     */
    private Boolean selected;

    public ChargeCouponInfo() {
    }

    public ChargeCouponInfo(CouponInfo couponInfo) {
        BeanUtils.copyProperties(couponInfo, this);
    }
}
