package com.czdx.parkingcharge.domain.dynamic;

import com.czdx.parkingcharge.common.enums.ParkEnums;
import com.czdx.parkingcharge.domain.ParkChargeRelationVehicle;
import com.czdx.parkingcharge.domain.custom.ParkChargeVehicleRelationCustom;
import com.czdx.parkingcharge.domain.pr.RuleNodeFact;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * description: 【计费规则-车型】关系规则文件Drl
 * @author mingchenxu
 * @date 2023/3/31 13:32
 */
@Slf4j
public class ChargeRuleVehicleRelationDrl implements DynamicDrl {

    private ParkChargeVehicleRelationCustom pcvrc;

    public ChargeRuleVehicleRelationDrl(ParkChargeVehicleRelationCustom pcvrc) {
        this.pcvrc = pcvrc;
    }

    @Override
    public DynamicConditionMap getDynamicConditionMap() {
        log.info("准备组装车场[{}]的【计费规则-车型】关联条件Map\n", pcvrc.getParkNo());
        // 组装【计费规则-车型】关联条件规则节点
        List<RuleNodeFact> ruleNodeFacts = assembleRelationNode(pcvrc);
        // 规则节点转填充为模板Map对象
        List<Map<String, Object>> maps = fillConditionMap(pcvrc.getParkNo(), ruleNodeFacts);
        // 规则名称 车场编号_规则ID_rel
        String ruleName = pcvrc.getParkNo() + "_rel";
        return new DynamicConditionMap(ruleName, maps);
    }

    /**
     *
     * description: 组装关系节点
     * @author mingchenxu
     * @date 2023/3/31 15:54
     * @param pcvrc
     * @return java.util.List<com.czdx.parkingcharge.domain.pr.RuleNodeFact>
     */
    private List<RuleNodeFact> assembleRelationNode(ParkChargeVehicleRelationCustom pcvrc) {
        List<RuleNodeFact> rnfs = new ArrayList<>();
        List<ParkChargeRelationVehicle> relations = pcvrc.getRelations();
        int nodeId = 1;
        int salience = 1000;
        for(ParkChargeRelationVehicle rel : relations) {
            RuleNodeFact rn = new RuleNodeFact();
            rn.setSRuleId(rel.getRelId());
            rn.setNodeId(nodeId);
            rn.setSalience(salience - nodeId);
            // LHS 主要有4个：节假日 | 区域 | 车类型 | 车型
            String lhs = "";
            // 如果节假日不是ALL，则增加节假日
            if (!rel.getHolidayType().equals(ParkEnums.HolidayType.ALL.getValue())) {
                lhs = lhs + "holidayType == \"" + rel.getHolidayType() + "\", ";
            }
            // 如果区域不是ALL，则增加区域
            if (!rel.getParkLotSign().equals(ParkEnums.ParkLotSign.ALL.getValue())) {
                lhs = lhs + "parkLotSign == \"" + rel.getParkLotSign() + "\", ";
            }
            // 如果是LS或者GD则直接填充该值，否则是固定车类型ID
            String categorySign = rel.getVehicleCategorySign();
            if (categorySign.equals(ParkEnums.CarVehicleCategory.LS.getValue())
                    || categorySign.equals(ParkEnums.CarVehicleCategory.GD.getValue())) {
                lhs = lhs + "vehicleCategory == \"" + categorySign + "\"";
            } else {
                lhs = lhs + "regularCategoryId == \"" + categorySign + "\"";
            }
            // 车型如果是ALL，无需填写
            String vehicleType = rel.getVehicleTypeSign();
            if (!vehicleType.equals(ParkEnums.VehicleTypeSign.ALL.getValue())) {
                lhs = lhs + ", vehicleType == \"" + rel.getVehicleTypeSign() + "\"";
            }
            String rhs = MessageFormat.format("$prModel.setChargeRuleId({0,number,#});", rel.getRuleId());
            rn.setRuleConditionStr(lhs);
            rn.setRuleResultStr(rhs);
            rnfs.add(rn);
            nodeId++;
        }
        return rnfs;
    }
}
