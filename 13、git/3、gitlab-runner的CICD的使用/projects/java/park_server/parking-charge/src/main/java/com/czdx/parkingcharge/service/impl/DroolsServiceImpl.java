package com.czdx.parkingcharge.service.impl;

import com.czdx.parkingcharge.common.constants.BUStr;
import com.czdx.parkingcharge.domain.ParkChargeRelationVehicle;
import com.czdx.parkingcharge.domain.ParkChargeScheme;
import com.czdx.parkingcharge.domain.custom.ParkChargeRuleCustom;
import com.czdx.parkingcharge.domain.custom.ParkChargeVehicleRelationCustom;
import com.czdx.parkingcharge.domain.dynamic.DynamicConditionMap;
import com.czdx.parkingcharge.domain.dynamic.DynamicDrl;
import com.czdx.parkingcharge.domain.dynamic.DynamicDrlFactory;
import com.czdx.parkingcharge.domain.pr.ParkRuleModel;
import com.czdx.parkingcharge.domain.pr.ParkingRecord;
import com.czdx.parkingcharge.domain.pr.ParkingRuleEnums;
import com.czdx.parkingcharge.service.DroolsService;
import com.czdx.parkingcharge.service.ParkChargeSchemeService;
import com.czdx.parkingcharge.system.drools.KieUtil;
import com.czdx.parkingcharge.utils.ParkingFeeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service("droolsService")
public class DroolsServiceImpl implements DroolsService {

    private final ParkChargeSchemeService parkChargeSchemeService;

    /**
     *
     * description: 计算停车费入口 - 接口调用
     * @author mingchenxu
     * @date 2023/3/21 18:30
     * @param
     * @return java.math.BigDecimal
     */
    @Override
    public BigDecimal chargeParkingFeeEntrance(ParkingRecord pr) {
        // 记录一下原始数据时间
        if (pr.getOriginalEntryTime() == null) {
            pr.setOriginalEntryTime(pr.getEntryTime());
            pr.setOriginalExitTime(pr.getExitTime());
        }
        // 获取车场配置
        Optional<ParkChargeScheme> scheme = parkChargeSchemeService.getChargeScheme(pr.getParkNo());
        String sc = ParkingRuleEnums.SecondCarry.IGNORE.getValue();
        ParkingRuleEnums.ChargeRoundWay crw = ParkingRuleEnums.ChargeRoundWay.DOWN;
        int scale = 1;
        BigDecimal ceilingPrice = null;
        if (scheme.isPresent()) {
            sc = scheme.get().getSecondCarry();
            crw = ParkingRuleEnums.ChargeRoundWay.getByValue(scheme.get().getRoundWay());
            scale = scheme.get().getMinimumChargeAccurary().scale();
            ceilingPrice = scheme.get().getMaximumCharge();
        }
        // 秒进位设置
        handleSecondCarry(pr, sc);
        // 计费
        BigDecimal parkingFee = chargeParkingFee(pr);
        // 不能超过最高价格
        if (ceilingPrice != null && parkingFee.compareTo(ceilingPrice) > 0) {
            return ceilingPrice;
        }
        // 如果有小数位处理最小计费精度与计费舍入方式
        if (parkingFee.scale() != 0) {
            parkingFee = parkingFee.setScale(scale, crw.getWay());
        }
        return parkingFee;
    }

    /**
     *
     * description: 处理秒设置
     * @author mingchenxu
     * @date 2023/3/21 18:34
     * @param pr 停车记录
     */
    private void handleSecondCarry(ParkingRecord pr, String sc) {
        // 处理秒设置，不为空才需要计算
        // 停车开始时间默认进位
        LocalDateTime enTime = ParkingFeeUtil.handleSecondCarry(pr.getEntryTime(), ParkingRuleEnums.SecondCarry.UP.getValue());
        // 结束时间根据车场配置进位or截秒
        LocalDateTime exTime = ParkingFeeUtil.handleSecondCarry(pr.getExitTime(), sc);
        pr.setEntryTime(enTime);
        pr.setExitTime(exTime);
    }

    /**
     * description: 计算停车费
     *
     * @param parkingRecord 停车记录
     * @author mingchenxu
     * @date 2023/3/10 11:17
     */
    @Override
    public BigDecimal chargeParkingFee(ParkingRecord parkingRecord) {
        // 计费预处理
        handlePreChargeFeeRecord(parkingRecord);
        if (parkingRecord.getParkingDuration() > 0) {
            KieSession kieSession = KieUtil.getKieBase().newKieSession();
            kieSession.insert(parkingRecord);
            kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter(parkingRecord.getRulePrefix()));
            kieSession.dispose();
            // 走完了规则变成了垃圾对象，也无法被垃圾回收器回收，需要手动清除
//            kieSession.delete(factHandle);
        } else {
            parkingRecord.setParkingFee(BigDecimal.ZERO);
        }
        return parkingRecord.getParkingFee();
    }

    /**
     * description: 编译计费规则
     *
     * @param parkingRule 计费规则
     * @author mingchenxu
     * @date 2023/3/14 13:31
     */
    @Override
    public void compileChargeRule(ParkChargeRuleCustom parkingRule) {
        DynamicDrl dynamicDrl = DynamicDrlFactory.newDynamicDrl(parkingRule);
        DynamicConditionMap dcm = dynamicDrl.getDynamicConditionMap();
        // 编译停车规则
        compileParkingFeeRule(BUStr.D_CHARGE_RULE_TEMPLATE_NAME, dcm.getRuleName(), dcm.getDynamicConditionMap());
    }

    /**
     *
     * description: 编译【计费规则-车型】
     * @author mingchenxu
     * @date 2023/3/31 13:29
     * @param pcrv
     */
    @Override
    public void compileChargeRuleVehicleRelation(ParkChargeVehicleRelationCustom pcrv) {
        DynamicDrl dynamicDrl = DynamicDrlFactory.newDynamicDrl(pcrv);
        DynamicConditionMap dcm = dynamicDrl.getDynamicConditionMap();
        // 编译停车规则
        compileParkingFeeRule(BUStr.D_CHARGE_RULE_VEHICLE_RELATION_TEMPLATE_NAME, dcm.getRuleName(), dcm.getDynamicConditionMap());
    }

    /**
     *
     * description: 获取计费规则
     * @author mingchenxu
     * @date 2023/3/30 17:22
     * @param prm
     * @return java.lang.Integer
     */
    @Override
    public Integer getChargeRule(ParkRuleModel prm) {
        KieSession kieSession = KieUtil.getKieBase().newKieSession();
        kieSession.insert(prm);
        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter(prm.getRulePrefix()));
        kieSession.dispose();
        return prm.getChargeRuleId();
    }

    /**
     * description: 根据车场配置，对预计费停车记录进行处理
     *
     * @param pr 停车记录
     * @author mingchenxu
     * @date 2023/3/9 15:20
     */
    private void handlePreChargeFeeRecord(ParkingRecord pr) {
        LocalDateTime enTime = pr.getEntryTime();
        LocalDateTime exTime = pr.getExitTime();
        // 计算停车时长
        long pd = Duration.between(enTime, exTime).toMinutes();
        pr.setParkingDuration(pd);
        // 是否跨天判断，间隔为1天，同时是零点，不算跨天
        boolean noNeedJudgeInterDay = Math.abs(enTime.toLocalDate().toEpochDay() - exTime.toLocalDate().toEpochDay()) == 1
                && exTime.toLocalTime() == LocalTime.of(0, 0, 0);
        if (noNeedJudgeInterDay) {
            pr.setHasInterDay(false);
        } else {
            pr.setHasInterDay(!enTime.toLocalDate().isEqual(exTime.toLocalDate()));
        }
    }

    /**
     * description: 编译停车计费规则
     *
     * @param conditionMapList 条件Map
     * @author mingchenxu
     * @date 2023/3/6 18:09
     */
    private void compileParkingFeeRule(String tempName, String ruleName, List<Map<String, Object>> conditionMapList) {
        ObjectDataCompiler converter = new ObjectDataCompiler();
        String drlContent = "";
        try (InputStream dis = ResourceFactory.
                newClassPathResource("rules/"+ tempName + ".drt", StandardCharsets.UTF_8.name())
                .getInputStream()) {
            // 填充模板内容
            drlContent = converter.compile(conditionMapList, dis);
            log.info("生成的规则内容:{}", drlContent);
        } catch (IOException e) {
            log.error("获取规则模板文件出错:{}", e.getMessage());
        }
        // 检查错误，无错误再进行编译
        if (checkDrlError(drlContent)) {
            // 删除原来的规则
            KieUtil.delete("rules/parking/" + ruleName, ResourceType.DRL);
            // 增加新的规则
            KieUtil.addRule(drlContent, "rules/parking/" + ruleName);
            // 更新KieBase
            KieUtil.updateKieBase();
        }
    }

    /**
     * 检查DRL文件异常
     * @param drl 规则文件字符串
     * @return
     */
    public static boolean checkDrlError(String drl) {
        KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kb.add(ResourceFactory.newByteArrayResource(drl.getBytes(StandardCharsets.UTF_8)), ResourceType.DRL);
        if (kb.hasErrors()) {
            String errorMessage = kb.getErrors().toString();
            log.error("规则语法异常---{}\n", errorMessage);
            return false;
        }
        return true;
    }
}
