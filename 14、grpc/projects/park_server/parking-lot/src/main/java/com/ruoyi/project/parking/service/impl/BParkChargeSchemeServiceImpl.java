package com.ruoyi.project.parking.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.parking.mapper.BParkChargeSchemeMapper;
import com.ruoyi.project.parking.domain.BParkChargeScheme;
import com.ruoyi.project.parking.service.IBParkChargeSchemeService;

/**
 * 停车场收费方案Service业务层处理
 * 
 * @author fangch
 * @date 2023-02-21
 */
@Service
public class BParkChargeSchemeServiceImpl implements IBParkChargeSchemeService 
{
    @Autowired
    private BParkChargeSchemeMapper bParkChargeSchemeMapper;

    /**
     * 查询停车场收费方案
     * 
     * @param parkNo 车场编号
     * @return 停车场收费方案
     */
    @Override
    public BParkChargeScheme selectBParkChargeSchemeById(String parkNo)
    {
        return bParkChargeSchemeMapper.selectBParkChargeSchemeById(parkNo);
    }

    /**
     * 查询停车场收费方案列表
     * 
     * @param bParkChargeScheme 停车场收费方案
     * @return 停车场收费方案
     */
    @Override
    public List<BParkChargeScheme> selectBParkChargeSchemeList(BParkChargeScheme bParkChargeScheme)
    {
        return bParkChargeSchemeMapper.selectBParkChargeSchemeList(bParkChargeScheme);
    }

    /**
     * 新增停车场收费方案
     * 
     * @param bParkChargeScheme 停车场收费方案
     * @return 结果
     */
    @Override
    public int insertBParkChargeScheme(BParkChargeScheme bParkChargeScheme)
    {
        bParkChargeScheme.setCreateTime(LocalDateTime.now());
        return bParkChargeSchemeMapper.insertBParkChargeScheme(bParkChargeScheme);
    }

    /**
     * 修改停车场收费方案
     * 
     * @param bParkChargeScheme 停车场收费方案
     * @return 结果
     */
    @Override
    public int updateBParkChargeScheme(BParkChargeScheme bParkChargeScheme)
    {
        bParkChargeScheme.setUpdateTime(LocalDateTime.now());
        return bParkChargeSchemeMapper.updateBParkChargeScheme(bParkChargeScheme);
    }

    /**
     * 批量删除停车场收费方案
     * 
     * @param ids 需要删除的停车场收费方案主键
     * @return 结果
     */
    @Override
    public int deleteBParkChargeSchemeByIds(Integer[] ids)
    {
        return bParkChargeSchemeMapper.deleteBParkChargeSchemeByIds(ids);
    }

    /**
     * 删除停车场收费方案信息
     * 
     * @param id 停车场收费方案主键
     * @return 结果
     */
    @Override
    public int deleteBParkChargeSchemeById(Integer id)
    {
        return bParkChargeSchemeMapper.deleteBParkChargeSchemeById(id);
    }
}
