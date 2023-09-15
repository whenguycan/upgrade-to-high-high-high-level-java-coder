package com.ruoyi.project.parking.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.parking.domain.TEntryRecords;
import com.ruoyi.project.parking.domain.vo.TEntryRecordsVo;
import com.ruoyi.project.parking.mapper.TEntryRecordsMapper;
import com.ruoyi.project.parking.service.ITEntryRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 车辆进场记录Service业务层处理
 *
 * @author ruoyi
 * @date 2023-02-22
 */
@Service
public class TEntryRecordsServiceImpl implements ITEntryRecordsService
{
    @Autowired
    private TEntryRecordsMapper tEntryRecordsMapper;

    /**
     * 查询车辆进场记录
     *
     * @param id 车辆进场记录主键
     * @return 车辆进场记录
     */
    @Override
    public TEntryRecords selectTEntryRecordsById(Long id)
    {
        return tEntryRecordsMapper.selectTEntryRecordsById(id);
    }

    /**
     * 查询车辆进场记录列表
     *
     * @param tEntryRecordsVo 车辆进场记录
     * @return 车辆进场记录
     */
    @Override
    public List<TEntryRecords> selectTEntryRecordsList(TEntryRecordsVo tEntryRecordsVo)
    {
        return tEntryRecordsMapper.selectTEntryRecordsList(tEntryRecordsVo);
    }

    /**
     * 新增车辆进场记录
     *
     * @param tEntryRecords 车辆进场记录
     * @return 结果
     */
    @Override
    public int insertTEntryRecords(TEntryRecords tEntryRecords)
    {
        tEntryRecords.setCreateTime(DateUtils.getNowDate());
        tEntryRecords.setEntryTime(new Date());
        return tEntryRecordsMapper.insertTEntryRecords(tEntryRecords);
    }

    /**
     * 修改车辆进场记录
     *
     * @param tEntryRecords 车辆进场记录
     * @return 结果
     */
    @Override
    public int updateTEntryRecords(TEntryRecords tEntryRecords)
    {
        tEntryRecords.setUpdateTime(DateUtils.getNowDate());
        return tEntryRecordsMapper.updateTEntryRecords(tEntryRecords);
    }

    /**
     * 批量删除车辆进场记录
     *
     * @param ids 需要删除的车辆进场记录主键
     * @return 结果
     */
    @Override
    public int deleteTEntryRecordsByIds(Long[] ids)
    {
        return tEntryRecordsMapper.deleteTEntryRecordsByIds(ids);
    }

    /**
     * 删除车辆进场记录信息
     *
     * @param id 车辆进场记录主键
     * @return 结果
     */
    @Override
    public int deleteTEntryRecordsById(Long id)
    {
        return tEntryRecordsMapper.deleteTEntryRecordsById(id);
    }

    @Override
    public List<TEntryRecords> selectEntryRecordsByParkLiveId(Integer parkLiveId) {
        TEntryRecordsVo paramEntryRecords = new TEntryRecordsVo();
        paramEntryRecords.setParkLiveId(parkLiveId);
        return selectTEntryRecordsList(paramEntryRecords).stream().
                sorted(Comparator.comparing(TEntryRecords::getCreateTime)).toList();
    }

    @Override
    public String selectEntryRecordsCarImgUrlByParkLiveId(Integer parkLiveId) {
        List<TEntryRecords> list = selectEntryRecordsByParkLiveId(parkLiveId);
        if(!list.isEmpty()){
            return list.get(0).getCarImgUrl();
        }
        return StringUtils.EMPTY;
    }
}
