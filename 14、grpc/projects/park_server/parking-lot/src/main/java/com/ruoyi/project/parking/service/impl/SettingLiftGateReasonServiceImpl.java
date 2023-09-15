package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.parking.domain.param.SettingLiftGateReasonParam;
import com.ruoyi.project.parking.domain.vo.SettingLiftGateReasonVO;
import com.ruoyi.project.parking.entity.SettingCarType;
import com.ruoyi.project.parking.entity.SettingLiftGateReason;
import com.ruoyi.project.parking.mapper.SettingLiftGateReasonMapper;
import com.ruoyi.project.parking.service.ISettingLiftGateReasonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 闸道抬杆原因表 服务实现类
 * </p>
 *
 * @author yinwen
 * @since 2023-02-21
 */
@Service
public class SettingLiftGateReasonServiceImpl extends ServiceImpl<SettingLiftGateReasonMapper, SettingLiftGateReason> implements ISettingLiftGateReasonService {

    @Override
    public List<SettingLiftGateReason> listByPartNo(SettingLiftGateReason settingLiftGateReason) {
        LambdaQueryWrapper<SettingLiftGateReason> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(settingLiftGateReason.getParkNo()), SettingLiftGateReason::getParkNo, settingLiftGateReason.getParkNo());
        return list(qw);
    }

    @Override
    public boolean add(SettingLiftGateReasonVO settingLiftGateReasonVO) {
        SettingLiftGateReason settingLiftGateReason = new SettingLiftGateReason();
        BeanUtils.copyBeanProp(settingLiftGateReason, settingLiftGateReasonVO);
        settingLiftGateReason.setCreateBy(SecurityUtils.getUsername());
        settingLiftGateReason.setCreateTime(LocalDateTime.now());
        return save(settingLiftGateReason);
    }

    @Override
    public boolean editById(SettingLiftGateReasonParam settingLiftGateReasonParam) {
        SettingLiftGateReason settingLiftGateReason = new SettingLiftGateReason();
        BeanUtils.copyBeanProp(settingLiftGateReason, settingLiftGateReasonParam);
        settingLiftGateReason.setUpdateBy(SecurityUtils.getUsername());
        settingLiftGateReason.setUpdateTime(LocalDateTime.now());
        return updateById(settingLiftGateReason);
    }

    @Override
    public boolean removeById(Integer id) {
        SettingLiftGateReason settingLiftGateReason = getById(id);
        if (settingLiftGateReason == null) {
            return true;
        }
        LambdaUpdateWrapper<SettingLiftGateReason> uw = new LambdaUpdateWrapper<>();
        uw.set(SettingLiftGateReason::getDelFlag, 1)
                .set(SettingLiftGateReason::getUpdateBy, SecurityUtils.getUsername())
                .set(SettingLiftGateReason::getUpdateTime, LocalDateTime.now())
                .eq(SettingLiftGateReason::getId, id);
        return update(uw);
    }
}
