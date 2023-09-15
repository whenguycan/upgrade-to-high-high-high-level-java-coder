package com.czdx.parkingcharge.service;

import com.czdx.parkingcharge.domain.ParkChargeRelationVehicle;
import com.czdx.parkingcharge.domain.custom.ParkChargeRuleCustom;
import com.czdx.parkingcharge.domain.custom.ParkChargeVehicleRelationCustom;
import com.czdx.parkingcharge.domain.pr.ParkRuleModel;
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
    BigDecimal chargeParkingFeeEntrance(ParkingRecord parkingRecord);

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

    /**
     *
     * description: 编译【计费规则-车型】
     * @author mingchenxu
     * @date 2023/3/31 13:29
     * @param pcrv
     */
    void compileChargeRuleVehicleRelation(ParkChargeVehicleRelationCustom pcrv);

    /**
     *
     * description: 获取计费规则
     * @author mingchenxu
     * @date 2023/3/30 17:22
     * @param prm
     * @return java.lang.Integer
     */
    Integer getChargeRule(ParkRuleModel prm);

}
