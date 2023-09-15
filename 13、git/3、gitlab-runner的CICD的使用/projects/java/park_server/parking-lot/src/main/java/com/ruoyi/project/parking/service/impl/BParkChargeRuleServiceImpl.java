package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.common.constant.DictConstants;
import com.ruoyi.project.parking.domain.*;
import com.ruoyi.project.parking.domain.bo.BSettingRegularCarCategoryBO;
import com.ruoyi.project.parking.domain.param.BParkChargeDurationParam;
import com.ruoyi.project.parking.domain.param.BParkChargeRuleParam;
import com.ruoyi.project.parking.domain.param.BParkChargeRuleTestParam;
import com.ruoyi.project.parking.domain.vo.BParkChargeDurationVO;
import com.ruoyi.project.parking.domain.vo.BParkChargeRuleVO;
import com.ruoyi.project.parking.entity.SettingCarType;
import com.ruoyi.project.parking.enums.BParkChargeRuleEnums;
import com.ruoyi.project.parking.mapper.*;
import com.ruoyi.project.parking.service.IBParkChargeRuleService;
import com.ruoyi.project.parking.service.grpcclient.ParkingChargeGrpcClientServiceImpl;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 车场收费规则Service业务层处理
 *
 * @author fangch
 * @date 2023-02-21
 */
@Slf4j
@Service("IBParkChargeRuleService")
public class BParkChargeRuleServiceImpl implements IBParkChargeRuleService
{
    @Autowired
    private ParkingChargeGrpcClientServiceImpl pCGrpcClientService;

    @Autowired
    private BParkChargeRuleMapper bParkChargeRuleMapper;

    @Autowired
    private BParkChargeDurationMapper bParkChargeDurationMapper;

    @Autowired
    private BParkChargeDurationPeriodMapper bParkChargeDurationPeriodMapper;

    @Autowired
    private BFieldMapper bFieldMapper;

    @Resource
    private BSettingRegularCarCategoryMapper bSettingRegularCarCategoryMapper;

    @Resource
    private SettingCarTypeMapper settingCarTypeMapper;

    /**
     * 查询车场收费规则
     *
     * @param id 车场收费规则主键
     * @return 车场收费规则
     */
    @Override
    public BParkChargeRuleVO selectBParkChargeRuleById(Integer id)
    {
        BParkChargeRule rule = bParkChargeRuleMapper.selectBParkChargeRuleById(id);
        BParkChargeRuleVO vo = new BParkChargeRuleVO();
        BeanUtils.copyProperties(rule, vo);
        this.buildVO(vo);
        return vo;
    }

    /**
     * 查询车场收费规则列表
     *
     * @param bParkChargeRule 车场收费规则
     * @return 车场收费规则
     */
    @Override
    public List<BParkChargeRule> selectBParkChargeRuleList(BParkChargeRule bParkChargeRule)
    {
        return bParkChargeRuleMapper.selectBParkChargeRuleList(bParkChargeRule);
    }

    private void buildVO(BParkChargeRuleVO vo) {
        BParkChargeDuration duration = new BParkChargeDuration();
        duration.setRuleId(vo.getId());
        List<BParkChargeDuration> durationList = bParkChargeDurationMapper.selectBParkChargeDurationList(duration);
        if (CollectionUtils.isNotEmpty(durationList)) {
            List<BParkChargeDurationVO> durationVOList = new ArrayList<>();
            BParkChargeDurationVO durationVO;
            for (BParkChargeDuration durationItem : durationList) {
                durationVO = new BParkChargeDurationVO();
                BeanUtils.copyProperties(durationItem, durationVO);
                BParkChargeDurationPeriod period = new BParkChargeDurationPeriod();
                period.setDurationId(durationItem.getId());
                List<BParkChargeDurationPeriod> periodList = bParkChargeDurationPeriodMapper.selectBParkChargeDurationPeriodList(period);
                durationVO.setPeriodList(periodList);
                durationVOList.add(durationVO);
            }
            vo.setDurationList(durationVOList);
        }
    }

    /**
     * 新增车场收费规则
     *
     * @param bParkChargeRule 车场收费规则
     * @return 结果
     */
    @Override
    public AjaxResult insertBParkChargeRule(BParkChargeRuleParam bParkChargeRule) {
        String msg = checkParam(bParkChargeRule);
        if (StringUtils.isNotEmpty(msg)) {
            return AjaxResult.error(msg);
        }
        bParkChargeRule.setCreateTime(LocalDateTime.now());
        int result = bParkChargeRuleMapper.insertBParkChargeRule(bParkChargeRule);
        this.updateChildren(bParkChargeRule, bParkChargeRule.getCreateBy());
        if (result > 0) {
            // 刷新计费规则
            pCGrpcClientService.refreshChargeRule(bParkChargeRule.getParkNo(), bParkChargeRule.getId());
        }
        return result > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    private String checkParam(BParkChargeRuleParam bParkChargeRule) {
        String msg = "";
        // 按时长计时校验
        if (bParkChargeRule.getDurationCreateWay().equals(BParkChargeRuleEnums.DURATION_CREATE_WAY.RECKON_BY_TIME.getValue())) {
            if (CollectionUtils.isNotEmpty(bParkChargeRule.getDurationList()) && bParkChargeRule.getDurationList().size() > 1) {
                msg = "按时长计时，期间个数只能1个";
            } else if (CollectionUtils.isNotEmpty(bParkChargeRule.getDurationList()) && bParkChargeRule.getDurationList().get(0).getLengthOfTime().equals(0)) {
                msg = "期间时长不能为0";
            } else {
                List<BParkChargeDurationPeriod> periodList = bParkChargeRule.getDurationList().get(0).getPeriodList();
                if (CollectionUtils.isNotEmpty(periodList)) {
                    int size = periodList.size();
                    // 校验每个时段时长是否为0(除末时段)
                    List<BParkChargeDurationPeriod> periodList1 = periodList.stream().filter(x -> x.getLengthOfTime().equals(0) && !x.getSort().equals(size)).collect(Collectors.toList());
                    Collections.sort(periodList1);
                    if (CollectionUtils.isNotEmpty(periodList1)) {
                        msg = "期间1-" + (periodList1.get(0).getSort().equals(1) ? "首" : ("第" + periodList1.get(0).getSort())) + "时段 时长不能为0";
                    } else {
                        // 校验时段时长总和不能超过期间时长(除末时段)
                        List<BParkChargeDurationPeriod> periodList2 = periodList.stream().filter(x -> !x.getSort().equals(size)).collect(Collectors.toList());
                        Collections.sort(periodList2);
                        if (CollectionUtils.isNotEmpty(periodList2)) {
                            int sumTime = bParkChargeRule.getDurationList().get(0).getLengthOfTime();
                            int remainTime = sumTime;
                            for (BParkChargeDurationPeriod period : periodList2) {
                                if (period.getLengthOfTime() > remainTime) {
                                    msg = "期间1-" + (period.getSort().equals(1) ? "首" : ("第" + period.getSort())) + "时段，时长不能超过" + remainTime + "分钟";
                                    break;
                                }
                                remainTime -= period.getLengthOfTime();
                            }
                        }
                    }
                }
            }
        } else {
            // todo 按时刻计时校验
        }
        return msg;
    }

    /**
     * 修改车场收费规则
     *
     * @param bParkChargeRule 车场收费规则
     * @return 结果
     */
    @Override
    public AjaxResult updateBParkChargeRule(BParkChargeRuleParam bParkChargeRule) {
        String msg = checkParam(bParkChargeRule);
        if (StringUtils.isNotEmpty(msg)) {
            return AjaxResult.error(msg);
        }
        bParkChargeRule.setUpdateTime(LocalDateTime.now());
        int result = bParkChargeRuleMapper.updateBParkChargeRule(bParkChargeRule);
        this.updateChildren(bParkChargeRule, bParkChargeRule.getUpdateBy());
        if (result > 0) {
            // 刷新计费规则
            pCGrpcClientService.refreshChargeRule(bParkChargeRule.getParkNo(), bParkChargeRule.getId());
        }
        return result > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    private void updateChildren(BParkChargeRuleParam bParkChargeRule, String createBy) {
        bParkChargeDurationPeriodMapper.deleteByRuleId(bParkChargeRule.getId());
        bParkChargeDurationMapper.deleteByRuleId(bParkChargeRule.getId());
        List<BParkChargeDurationParam> durationParams = bParkChargeRule.getDurationList();
        if (CollectionUtils.isNotEmpty(durationParams)) {
            for (BParkChargeDurationParam durationParam : durationParams) {
                durationParam.setRuleId(bParkChargeRule.getId());
                durationParam.setCreateBy(createBy);
                bParkChargeDurationMapper.insertBParkChargeDuration(durationParam);
                List<BParkChargeDurationPeriod> periodList = durationParam.getPeriodList();
                if (CollectionUtils.isNotEmpty(periodList)) {
                    for (BParkChargeDurationPeriod period : periodList) {
                        period.setDurationId(durationParam.getId());
                        period.setCreateBy(createBy);
                        bParkChargeDurationPeriodMapper.insertBParkChargeDurationPeriod(period);
                    }
                }
            }
        }
    }

    /**
     * 批量删除车场收费规则
     *
     * @param ids 需要删除的车场收费规则主键
     * @return 结果
     */
    @Override
    public int deleteBParkChargeRuleByIds(Integer[] ids)
    {
        bParkChargeDurationPeriodMapper.deleteByRuleIds(ids);
        bParkChargeDurationMapper.deleteByRuleIds(ids);
        int result = bParkChargeRuleMapper.deleteBParkChargeRuleByIds(ids);
        if (result > 0) {
            // 刷新计费规则
            pCGrpcClientService.refreshChargeRule(SecurityUtils.getParkNo(), null);
        }
        return result;
    }

    /**
     * 删除车场收费规则信息
     *
     * @param id 车场收费规则主键
     * @return 结果
     */
    @Override
    public int deleteBParkChargeRuleById(Integer id)
    {
        bParkChargeDurationPeriodMapper.deleteByRuleId(id);
        bParkChargeDurationMapper.deleteByRuleId(id);
        int result = bParkChargeRuleMapper.deleteBParkChargeRuleById(id);
        if (result > 0) {
            // 刷新计费规则
            pCGrpcClientService.refreshChargeRule(SecurityUtils.getParkNo(), null);
        }
        return result;
    }

    /**
     * 测试车场收费规则
     *
     * @param bParkChargeRuleTestParam 测试车场收费规则参数
     * @return 停车费
     */
    @Override
    public double testParkRate(BParkChargeRuleTestParam bParkChargeRuleTestParam) {
        double result = 0.0;
        try {
            result = pCGrpcClientService.testChargeParkingFee(bParkChargeRuleTestParam);
        } catch (StatusRuntimeException e) {
            log.error( "testParkRate 1 FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error( "testParkRate 2 FAILED with " + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询
     *
     * @param bField 筛选条件
     * @return
     */
    public List<BField> bFieldList(BField bField){
        //1. 构建动态查询条件
        LambdaQueryWrapper<BField> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(bField.getFieldName()), BField::getFieldName, bField.getFieldName())
                    .eq(StringUtils.isNotBlank(bField.getFieldStatus()), BField::getFieldStatus, bField.getFieldStatus())
                    .eq(StringUtils.isNotBlank(bField.getRemark()), BField::getRemark, bField.getRemark())
                    .eq(StringUtils.isNotBlank(bField.getCreateBy()), BField::getCreateBy, bField.getCreateBy())
                    .eq(StringUtils.isNotBlank(bField.getParkNo()), BField::getParkNo, bField.getParkNo())
                    .eq(StringUtils.isNotBlank(bField.getUpdateBy()), BField::getUpdateBy, bField.getUpdateBy());
        return bFieldMapper.selectList(queryWrapper);
    }

    /**
     * @apiNote 查询固定车类型列表 通过场地编号和固定车类型识别码和分组类型编号
     */
    @Override
    public List<BSettingRegularCarCategoryBO> regularCarCategoryList(BSettingRegularCarCategory settingRegularCarCategory) {
        LambdaQueryWrapper<BSettingRegularCarCategory> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(SecurityUtils.getParkNo()), BSettingRegularCarCategory::getParkNo, SecurityUtils.getParkNo())
                .eq(StringUtils.isNotEmpty(settingRegularCarCategory.getCode()), BSettingRegularCarCategory::getCode, settingRegularCarCategory.getCode())
                .eq(StringUtils.isNotEmpty(settingRegularCarCategory.getGroupId()), BSettingRegularCarCategory::getGroupId, settingRegularCarCategory.getGroupId())
                .eq(null != settingRegularCarCategory.getOnlineFlag(), BSettingRegularCarCategory::getOnlineFlag, settingRegularCarCategory.getOnlineFlag())
                .eq(StringUtils.isNotEmpty(settingRegularCarCategory.getStatus()), BSettingRegularCarCategory::getStatus, settingRegularCarCategory.getStatus())
                .eq(null != settingRegularCarCategory.getDelFlag(), BSettingRegularCarCategory::getDelFlag, settingRegularCarCategory.getDelFlag());
        List<BSettingRegularCarCategory> list = bSettingRegularCarCategoryMapper.selectList(qw);
        // 增加分组类型字典项描述
        List<BSettingRegularCarCategoryBO> result = new ArrayList<>();
        list.forEach(item -> {
            BSettingRegularCarCategoryBO bSettingRegularCarCategoryBO = new BSettingRegularCarCategoryBO();
            com.ruoyi.common.utils.bean.BeanUtils.copyBeanProp(bSettingRegularCarCategoryBO, item);
            bSettingRegularCarCategoryBO.setGroupName(DictUtils.getDictLabel(DictConstants.MOD_CAR_OWNER_GROUP_TYPE_KEY, item.getGroupId()));
            result.add(bSettingRegularCarCategoryBO);
        });
        return result;
    }

    @Override
    public List<SettingCarType> carTypeList(SettingCarType settingCarType) {
        LambdaQueryWrapper<SettingCarType> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(settingCarType.getParkNo()), SettingCarType::getParkNo, settingCarType.getParkNo())
                .eq(StringUtils.isNotEmpty(settingCarType.getCode()), SettingCarType::getCode, settingCarType.getCode())
                .eq(StringUtils.isNotEmpty(settingCarType.getStatus()), SettingCarType::getStatus, settingCarType.getStatus())
                .eq(null != settingCarType.getDelFlag(), SettingCarType::getDelFlag, settingCarType.getDelFlag());
        return settingCarTypeMapper.selectList(qw);
    }

}
