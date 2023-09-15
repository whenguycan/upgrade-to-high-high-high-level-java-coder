package com.ruoyi.project.parking.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.BField;
import com.ruoyi.project.parking.domain.BParkChargeRule;
import com.ruoyi.project.parking.domain.BSettingRegularCarCategory;
import com.ruoyi.project.parking.domain.bo.BSettingRegularCarCategoryBO;
import com.ruoyi.project.parking.domain.param.BParkChargeRuleParam;
import com.ruoyi.project.parking.domain.param.BParkChargeRuleTestParam;
import com.ruoyi.project.parking.domain.vo.BParkChargeRuleVO;
import com.ruoyi.project.parking.entity.SettingCarType;

import java.util.List;

/**
 * 车场收费规则Service接口
 * 
 * @author fangch
 * @date 2023-02-21
 */
public interface IBParkChargeRuleService 
{
    /**
     * 查询车场收费规则
     * 
     * @param id 车场收费规则主键
     * @return 车场收费规则
     */
    BParkChargeRuleVO selectBParkChargeRuleById(Integer id);

    /**
     * 查询车场收费规则列表
     * 
     * @param bParkChargeRule 车场收费规则
     * @return 车场收费规则集合
     */
    List<BParkChargeRule> selectBParkChargeRuleList(BParkChargeRule bParkChargeRule);

    /**
     * 新增车场收费规则
     * 
     * @param bParkChargeRule 车场收费规则
     * @return 结果
     */
    AjaxResult insertBParkChargeRule(BParkChargeRuleParam bParkChargeRule);

    /**
     * 修改车场收费规则
     * 
     * @param bParkChargeRule 车场收费规则
     * @return 结果
     */
    AjaxResult updateBParkChargeRule(BParkChargeRuleParam bParkChargeRule);

    /**
     * 批量删除车场收费规则
     * 
     * @param ids 需要删除的车场收费规则主键集合
     * @return 结果
     */
    int deleteBParkChargeRuleByIds(Integer[] ids);

    /**
     * 删除车场收费规则信息
     * 
     * @param id 车场收费规则主键
     * @return 结果
     */
    int deleteBParkChargeRuleById(Integer id);

    /**
     * 测试车场收费规则
     *
     * @param bParkChargeRuleTestParam 测试车场收费规则参数
     * @return 停车费
     */
    double testParkRate(BParkChargeRuleTestParam bParkChargeRuleTestParam);

    /**
     * 分页查询
     *
     * @param bField 筛选条件
     * @param
     * @param
     * @return
     */
    List<BField> bFieldList(BField bField);

    /**
     * @apiNote 查询固定车类型列表 通过场地编号、固定车类型识别码、分组类型编号、是否允许线上购买
     */
    List<BSettingRegularCarCategoryBO> regularCarCategoryList(BSettingRegularCarCategory settingRegularCarCategory);

    /**
     * 查询车辆类型列表 通过 场地编号和车辆类型识别码
     * @param settingCarType
     * @return
     */
    List<SettingCarType> carTypeList(SettingCarType settingCarType);

}
