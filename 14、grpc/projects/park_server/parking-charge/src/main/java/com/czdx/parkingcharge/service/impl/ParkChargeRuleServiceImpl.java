package com.czdx.parkingcharge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingcharge.domain.ParkChargeRule;
import com.czdx.parkingcharge.domain.custom.ParkChargeRuleCustom;
import com.czdx.parkingcharge.service.DroolsService;
import com.czdx.parkingcharge.service.ParkChargeRuleService;
import com.czdx.parkingcharge.mapper.ParkChargeRuleMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author mingchenxu
* @description 针对表【b_park_charge_rule(车场收费规则)】的数据库操作Service实现
* @createDate 2023-03-14 09:45:34
*/
@Service
public class ParkChargeRuleServiceImpl extends ServiceImpl<ParkChargeRuleMapper, ParkChargeRule>
    implements ParkChargeRuleService{

    @Autowired
    private ParkChargeRuleMapper parkChargeRuleMapper;

    @Autowired
    private DroolsService droolsService;

    /**
     *
     * description: 加载算费规则
     * @author mingchenxu
     * @date 2023/3/14 09:49
     * @return boolean
     */
    @Override
    public boolean loadChargeRule() {
        // 查询计费规则
        List<ParkChargeRuleCustom> ruleCustomList = parkChargeRuleMapper.queryParkChargeRuleCustomList(null, null);
        if (CollectionUtils.isNotEmpty(ruleCustomList)) {
            // 循环编译
            ruleCustomList.forEach(droolsService::compileChargeRule);
        }
        return true;
    }
}




