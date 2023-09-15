package com.ruoyi.project.parking.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.parking.domain.BSettingLot;
import com.ruoyi.project.parking.mapper.BSettingLotMapper;
import com.ruoyi.project.parking.service.IBSettingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 场库基础配置Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-14
 */
@Service
public class BSettingLotServiceImpl implements IBSettingLotService
{
    @Autowired
    private BSettingLotMapper bSettingLotMapper;

    /**
     * 新增场库基础配置
     *
     * @param bSettingLot 场库基础配置
     * @return 结果
     */
    @Override
    public int insertBSettingLot(BSettingLot bSettingLot)
    {
        bSettingLot.setCreateTime(DateUtils.getNowDate());
        return bSettingLotMapper.insertBSettingLot(bSettingLot);
    }

    /**
     * 修改场库基础配置
     *
     * @param bSettingLot 场库基础配置
     * @return 结果
     */
    @Override
    public int updateBSettingLot(BSettingLot bSettingLot)
    {
        bSettingLot.setUpdateTime(DateUtils.getNowDate());
        return bSettingLotMapper.updateBSettingLot(bSettingLot);
    }

    @Override
    public BSettingLot getBSettingLotByParkNo(BSettingLot bSettingLot) {
        QueryWrapper<BSettingLot> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(BSettingLot::getParkNo, bSettingLot.getParkNo());
        return bSettingLotMapper.selectOne(queryWrapper);
    }
}
