package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.parking.domain.param.SettingCarTypeParam;
import com.ruoyi.project.parking.domain.vo.SettingCarTypeVO;
import com.ruoyi.project.parking.entity.SettingCarType;
import com.ruoyi.project.parking.mapper.SettingCarTypeMapper;
import com.ruoyi.project.parking.service.ISettingCarTypeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 车辆类型表 服务实现类
 * </p>
 *
 * @author yinwen
 * @since 2023-02-21
 */
@Service
public class SettingCarTypeServiceImpl extends ServiceImpl<SettingCarTypeMapper, SettingCarType> implements ISettingCarTypeService {

    @Override
    public List<SettingCarType> listCondition(SettingCarType settingCarType) {
        LambdaQueryWrapper<SettingCarType> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(settingCarType.getParkNo()), SettingCarType::getParkNo, settingCarType.getParkNo())
                .like(StringUtils.isNotEmpty(settingCarType.getCode()), SettingCarType::getCode, settingCarType.getCode())
                .like(StringUtils.isNotEmpty(settingCarType.getName()), SettingCarType::getName, settingCarType.getName())
                .eq(StringUtils.isNotEmpty(settingCarType.getStatus()), SettingCarType::getStatus, settingCarType.getStatus())
                .like(StringUtils.isNotEmpty(settingCarType.getRemark()), SettingCarType::getRemark, settingCarType.getRemark());
        return list(qw);
    }

    @Override
    public String queryTypeNameByPartNoAndCode(String parkNo, String code) {
        LambdaQueryWrapper<SettingCarType> qw = new LambdaQueryWrapper<>();
        qw.select(SettingCarType::getName)
                .eq(SettingCarType::getParkNo, parkNo)
                .eq(SettingCarType::getCode, code);
        SettingCarType settingCarType = getOne(qw, false);
        if (settingCarType != null) {
            return settingCarType.getName();
        }
        return null;
    }

    @Override
    public boolean add(SettingCarTypeVO settingCarTypeVO) {
        SettingCarType settingCarType = new SettingCarType();
        BeanUtils.copyBeanProp(settingCarType, settingCarTypeVO);
        settingCarType.setCreateBy(SecurityUtils.getUsername());
        settingCarType.setCreateTime(LocalDateTime.now());
        return save(settingCarType);
    }

    @Override
    public boolean editById(SettingCarTypeParam settingCarTypeParam) {
        SettingCarType settingCarType = new SettingCarType();
        BeanUtils.copyBeanProp(settingCarType, settingCarTypeParam);
        settingCarType.setUpdateBy(SecurityUtils.getUsername());
        settingCarType.setUpdateTime(LocalDateTime.now());
        return updateById(settingCarType);
    }

    @Override
    public boolean removeById(Integer id) {
        SettingCarType settingCarType = getById(id);
        if (settingCarType == null) {
            return true;
        }
        LambdaUpdateWrapper<SettingCarType> uw = new LambdaUpdateWrapper<>();
        uw.set(SettingCarType::getDelFlag, 1)
                .set(SettingCarType::getUpdateBy, SecurityUtils.getUsername())
                .set(SettingCarType::getUpdateTime, LocalDateTime.now())
                .eq(SettingCarType::getId, id);
        return update(uw);
    }

    @Override
    public boolean switchStatusById(Integer id, String status) {
        SettingCarType settingCarType = getById(id);
        if (settingCarType == null) {
            throw new ServiceException("获得车辆类型异常");
        }
        // 状态一致时不进行更新
        if (status.equals(settingCarType.getStatus())) {
            return true;
        } else {
            LambdaUpdateWrapper<SettingCarType> uw = new LambdaUpdateWrapper<>();
            uw.set(SettingCarType::getStatus, status)
                    .set(SettingCarType::getUpdateBy, SecurityUtils.getUsername())
                    .set(SettingCarType::getUpdateTime, LocalDateTime.now())
                    .eq(SettingCarType::getId, id);
            return update(uw);
        }
    }
}
