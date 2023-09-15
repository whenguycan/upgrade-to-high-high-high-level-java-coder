package com.czdx.parkingcharge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingcharge.common.constants.BUStr;
import com.czdx.parkingcharge.domain.ParkChargeRule;
import com.czdx.parkingcharge.domain.custom.ParkChargeRuleCustom;
import com.czdx.parkingcharge.service.DroolsService;
import com.czdx.parkingcharge.service.ParkChargeRuleService;
import com.czdx.parkingcharge.mapper.ParkChargeRuleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

/**
* @author mingchenxu
* @description 针对表【b_park_charge_rule(车场收费规则)】的数据库操作Service实现
* @createDate 2023-03-14 09:45:34
*/
@Slf4j
@RequiredArgsConstructor
@Service
public class ParkChargeRuleServiceImpl extends ServiceImpl<ParkChargeRuleMapper, ParkChargeRule>
    implements ParkChargeRuleService{

    private final ParkChargeRuleMapper parkChargeRuleMapper;

    private final DroolsService droolsService;

    /**
     *
     * description: 初始化时，重新加载计费规则
     * @author mingchenxu
     * @date 2023/3/21 10:48
     */
    @PostConstruct
    public void initChargeRule() {
        log.info("准备加载【车场计费规则】 >>>>>>>>>");
        StopWatch sw = new StopWatch(UUID.randomUUID().toString());
        // 加载计费规则
        sw.start("加载【车场计费规则】");
        int sumNum = loadChargeRule();
        sw.stop();
        log.info("成功加载【车场计费规则】：{}条", sumNum);
        log.info(sw.prettyPrint());
    }

    /**
     *
     * description: 加载算费规则
     * @author mingchenxu
     * @date 2023/3/14 09:49
     * @return boolean
     */
    @Override
    public int loadChargeRule() {
        int sumNum = 0;
        // 查询计费规则
        List<ParkChargeRuleCustom> ruleCustomList = parkChargeRuleMapper.queryParkChargeRuleCustomList(null, null);
        if (CollectionUtils.isNotEmpty(ruleCustomList)) {
            sumNum = compileChargeRule(ruleCustomList);
        }
        return sumNum;
    }

    /**
     *
     * description: 编译计费规则
     * @author mingchenxu
     * @date 2023/3/21 09:25
     * @param ruleCustomList 计费规则列表
     * @return int
     */
    private int compileChargeRule(List<ParkChargeRuleCustom> ruleCustomList) {
        // 循环编译
        int sucNum = 0;
        for(ParkChargeRuleCustom item : ruleCustomList) {
            try {
                droolsService.compileChargeRule(item);
                sucNum++;
            } catch (Exception e) {
                log.error("加载算费规则异常，规则名称：[{}]，所属车场：[{}]", item.getRuleName(), item.getParkNo(), e);
            }
        }
        return sucNum;
    }

    /**
     *
     * description: 刷新计费规则
     * @author mingchenxu
     * @date 2023/3/21 08:59
     * @param parkNo 车场编号
     * @param ruleId 规则ID
     * @return int
     */
    @Override
    public int refreshChargeRule(String parkNo, Integer ruleId) {
        if (StringUtils.isNotEmpty(parkNo)) {
            List<ParkChargeRuleCustom> ruleCustomList = parkChargeRuleMapper.queryParkChargeRuleCustomList(parkNo, ruleId);
            if (CollectionUtils.isNotEmpty(ruleCustomList)) {
                return compileChargeRule(ruleCustomList);
            }
        }
        return 0;
    }
}




