package com.czdx.parkingnotification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingnotification.domain.BField;
import com.czdx.parkingnotification.service.IBFieldService;
import com.czdx.parkingnotification.mapper.BFieldMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 琴声何来
 * @description 针对表【b_field(区域管理表)】的数据库操作Service实现
 * @since 2023-04-04 09:41:14
 */
@Service
public class BFieldServiceImpl extends ServiceImpl<BFieldMapper, BField>
        implements IBFieldService {

    /**
     * 分页查询
     *
     * @param bField 筛选条件
     * @return
     */
    @Override
    public List<BField> findList(BField bField, List<String> parkNos) {
        //1. 构建动态查询条件
        LambdaQueryWrapper<BField> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(bField.getFieldName())) {
            queryWrapper.eq(BField::getFieldName, bField.getFieldName());
        }
        if (StringUtils.isNotBlank(bField.getFieldStatus())) {
            queryWrapper.eq(BField::getFieldStatus, bField.getFieldStatus());
        }
        if (StringUtils.isNotBlank(bField.getRemark())) {
            queryWrapper.eq(BField::getRemark, bField.getRemark());
        }
        if (StringUtils.isNotBlank(bField.getCreateBy())) {
            queryWrapper.eq(BField::getCreateBy, bField.getCreateBy());
        }
        if (StringUtils.isNotBlank(bField.getParkNo())) {
            queryWrapper.eq(BField::getParkNo, bField.getParkNo());
        }
        if (StringUtils.isNotBlank(bField.getUpdateBy())) {
            queryWrapper.eq(BField::getUpdateBy, bField.getUpdateBy());
        }
        if (!parkNos.isEmpty()) {
            queryWrapper.in(BField::getParkNo, parkNos);
        }
        return baseMapper.selectList(queryWrapper);
    }
}




