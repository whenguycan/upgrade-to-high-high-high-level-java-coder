package com.czdx.parkingcharge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingcharge.domain.ParkChargeRelationVehicle;
import com.czdx.parkingcharge.domain.custom.ParkChargeRuleCustom;
import com.czdx.parkingcharge.domain.custom.ParkChargeVehicleRelationCustom;
import com.czdx.parkingcharge.service.DroolsService;
import com.czdx.parkingcharge.service.ParkChargeRelationVehicleService;
import com.czdx.parkingcharge.mapper.ParkChargeRelationVehicleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
* @author mingchenxu
* @description 针对表【b_park_charge_relation_vehicle(区域-车类型-车型-收费规则关联表)】的数据库操作Service实现
* @createDate 2023-03-14 09:46:13
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class ParkChargeRelationVehicleServiceImpl extends ServiceImpl<ParkChargeRelationVehicleMapper, ParkChargeRelationVehicle>
    implements ParkChargeRelationVehicleService{

    private final DroolsService droolsService;

    /**
     *
     * description: 初始化加载【计费规则-车型】
     * @author mingchenxu
     * @date 2023/3/31 13:26
     */
    @PostConstruct
    public void initChargeRuleVehicleRelations() {
        log.info("准备加载【计费规则-车型】关联关系 >>>>>>>>>");
        StopWatch sw = new StopWatch(UUID.randomUUID().toString());
        // 加载计费规则
        sw.start("加载【计费规则-车型】关联关系");
        int sumNum = loadParkChargeRuleVehicleRelations();
        sw.stop();
        log.info("成功加载【计费规则-车型】关联关系：{}条", sumNum);
        log.info(sw.prettyPrint());
    }

    /**
     *
     * description: 加载计费规则与车型关联
     * @author mingchenxu
     * @date 2023/3/31 13:18
     * @return int
     */
    @Override
    public int loadParkChargeRuleVehicleRelations() {
        int sumNum = 0;
        List<ParkChargeVehicleRelationCustom> relations = getParkChargeRelationVehicles("");
        if (CollectionUtils.isNotEmpty(relations)) {
            sumNum = compileChargeVehicleRelation(relations);
        }
        return sumNum;
    }

    /**
     *
     * description: 刷新车场计费关系
     * @author mingchenxu
     * @date 2023/3/31 16:35
     * @param parkNo 车场编号
     * @return int
     */
    @Override
    public int refreshParkLotChargeRelation(String parkNo) {
        if (StringUtils.isNotEmpty(parkNo)) {
            List<ParkChargeVehicleRelationCustom> relationCustoms = getParkChargeRelationVehicles(parkNo);
            return compileChargeVehicleRelation(relationCustoms);
        }
        return 0;
    }

    /**
     *
     * description: 获取车场计费车型关联
     * @author mingchenxu
     * @date 2023/3/30 18:26
     * @param parkNo 车场编号
     * @return java.util.List<com.czdx.parkingcharge.domain.ParkChargeRelationVehicle>
     */
    @Override
    public List<ParkChargeVehicleRelationCustom> getParkChargeRelationVehicles(String parkNo) {
        return baseMapper.selectParkChargeVehicleRelationCustomList(parkNo);
    }

    /**
     *
     * description: 编译计费-车场关联规则
     * @author mingchenxu
     * @date 2023/3/31 13:22
     * @param pcrvs
     * @return int
     */
    private int compileChargeVehicleRelation(List<ParkChargeVehicleRelationCustom> pcrvs) {
        // 循环编译
        int sucNum = 0;
        for(ParkChargeVehicleRelationCustom item : pcrvs) {
            try {
                droolsService.compileChargeRuleVehicleRelation(item);
                sucNum++;
            } catch (Exception e) {
                log.error("加载【计费规则-车型】异常，所属车场编号：[{}]", item.getParkNo(), item.getParkNo(), e);
            }
        }
        return sucNum;
    }
}




