package com.czdx.parkingcharge.mapper;

import com.czdx.parkingcharge.domain.ParkChargeRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czdx.parkingcharge.domain.custom.ParkChargeRuleCustom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author mingchenxu
* @description 针对表【b_park_charge_rule(车场收费规则)】的数据库操作Mapper
* @createDate 2023-03-14 09:45:34
* @Entity com.czdx.parkingcharge.domain.ParkChargeRule
*/
@Mapper
public interface ParkChargeRuleMapper extends BaseMapper<ParkChargeRule> {

    /**
     *
     * description: 查询计费规则扩展列表
     * @author mingchenxu
     * @date 2023/3/14 11:23
     * @param parkNo 车场编号
     * @param ruleId 规则ID
     * @return java.util.List<com.czdx.parkingcharge.domain.custom.ParkChargeRuleCustom>
     */
    List<ParkChargeRuleCustom> queryParkChargeRuleCustomList(@Param("parkNo") String parkNo, @Param("ruleId") Integer ruleId);
}




