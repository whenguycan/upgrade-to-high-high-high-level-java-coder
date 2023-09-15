package com.czdx.parkingcharge.service;

import com.czdx.parkingcharge.domain.ParkChargeRule;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author mingchenxu
* @description 针对表【b_park_charge_rule(车场收费规则)】的数据库操作Service
* @createDate 2023-03-14 09:45:34
*/
public interface ParkChargeRuleService extends IService<ParkChargeRule> {


    /**
     *
     * description: 加载算费规则
     * @author mingchenxu
     * @date 2023/3/14 09:49
     * @return boolean
     */
    boolean loadChargeRule();

}
