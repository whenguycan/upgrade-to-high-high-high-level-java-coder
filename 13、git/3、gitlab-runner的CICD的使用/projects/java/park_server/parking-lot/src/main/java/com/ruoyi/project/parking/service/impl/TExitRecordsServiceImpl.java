package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.parking.domain.TExitRecords;
import com.ruoyi.project.parking.domain.vo.TExitRecordsVo;
import com.ruoyi.project.parking.entity.ParkLiveRecords;
import com.ruoyi.project.parking.mapper.ParkLiveRecordsMapper;
import com.ruoyi.project.parking.mapper.TExitRecordsMapper;
import com.ruoyi.project.parking.service.ITExitRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 车辆离场记录Service业务层处理
 *
 * @author mzl
 * @date 2023-02-22
 */
@Service
public class TExitRecordsServiceImpl extends ServiceImpl<TExitRecordsMapper, TExitRecords> implements ITExitRecordsService
{
    @Resource
    private TExitRecordsMapper tExitRecordsMapper;

    /**
     * 查询车辆离场记录
     *
     * @param id 车辆离场记录主键
     * @return 车辆离场记录
     */
    @Override
    public TExitRecords selectTExitRecordsById(Long id)
    {
        return tExitRecordsMapper.selectTExitRecordsById(id);
    }

    /**
     * 查询车辆离场记录列表
     *
     * @param tExitRecords 车辆离场记录
     * @return 车辆离场记录
     */
    @Override
    public List<TExitRecords> selectTExitRecordsList(TExitRecordsVo tExitRecords)
    {
        return tExitRecordsMapper.selectTExitRecordsList(tExitRecords);
    }


    /**
     * 新增车辆离场记录
     *
     * @param tExitRecords 车辆离场记录
     * @return 结果
     */
    @Override
    public int insertTExitRecords(TExitRecords tExitRecords)
    {
        return tExitRecordsMapper.insertTExitRecords(tExitRecords);
    }

    /**
     * 修改车辆离场记录
     *
     * @param tExitRecords 车辆离场记录
     * @return 结果
     */
    @Override
    public int updateTExitRecords(TExitRecords tExitRecords)
    {
        tExitRecords.setUpdateTime(DateUtils.getNowDate());
        return tExitRecordsMapper.updateTExitRecords(tExitRecords);
    }

    /**
     * 批量删除车辆离场记录
     *
     * @param ids 需要删除的车辆离场记录主键
     * @return 结果
     */
    @Override
    public int deleteTExitRecordsByIds(Long[] ids)
    {
        return tExitRecordsMapper.deleteTExitRecordsByIds(ids);
    }

    /**
     * 删除车辆离场记录信息
     *
     * @param id 车辆离场记录主键
     * @return 结果
     */
    @Override
    public int deleteTExitRecordsById(Long id)
    {
        return tExitRecordsMapper.deleteTExitRecordsById(id);
    }

    @Override
    public TExitRecords findNewestExitRecord(String parkNo, String carNo) {

        // 存在多条在场记录时 取最新一条记录
        LambdaQueryWrapper<TExitRecords> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TExitRecords::getParkNo, parkNo)
                .eq(TExitRecords::getCarNumber, carNo)
                .orderByDesc(TExitRecords::getCreateTime)
                .last("limit 1 ");
        return getOne(queryWrapper);
    }
}
