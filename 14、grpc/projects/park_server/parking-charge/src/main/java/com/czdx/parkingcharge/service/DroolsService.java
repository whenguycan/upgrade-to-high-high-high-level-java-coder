package com.czdx.parkingcharge.service;

import com.czdx.parkingcharge.domain.custom.ParkChargeRuleCustom;
import com.czdx.parkingcharge.domain.pr.ParkingRecord;

import java.math.BigDecimal;

public interface DroolsService {

    /**
     *
     * description: 计算停车费
     * @author mingchenxu
     * @date 2023/3/10 11:17
     * @param parkingRecord 停车记录
     */
    BigDecimal chargeParkingFee(ParkingRecord parkingRecord);

    /**
     *
     * description: 编译计费规则
     * @author mingchenxu
     * @date 2023/3/14 13:31
     * @param parkChargeRuleCustom 计费规则
     */
    void compileChargeRule(ParkChargeRuleCustom parkChargeRuleCustom);

}
