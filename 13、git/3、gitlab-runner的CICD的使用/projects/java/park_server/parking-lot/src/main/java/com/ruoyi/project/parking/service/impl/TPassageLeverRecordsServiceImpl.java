package com.ruoyi.project.parking.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.parking.domain.vo.TPassageLeverRecordsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.parking.mapper.TPassageLeverRecordsMapper;
import com.ruoyi.project.parking.domain.TPassageLeverRecords;
import com.ruoyi.project.parking.service.ITPassageLeverRecordsService;

/**
 * 起落杆记录Service业务层处理
 *
 * @author ruoyi
 * @date 2023-02-23
 */
@Service
public class TPassageLeverRecordsServiceImpl implements ITPassageLeverRecordsService
{
    @Autowired
    private TPassageLeverRecordsMapper tPassageLeverRecordsMapper;

    /**
     * 查询起落杆记录
     *
     * @param id 起落杆记录主键
     * @return 起落杆记录
     */
    @Override
    public TPassageLeverRecords selectTPassageLeverRecordsById(Long id)
    {
        return tPassageLeverRecordsMapper.selectTPassageLeverRecordsById(id);
    }

    /**
     * 查询起落杆记录列表
     *
     * @param tPassageLeverRecords 起落杆记录
     * @return 起落杆记录
     */
    @Override
    public List<TPassageLeverRecordsVo> selectTPassageLeverRecordsList(TPassageLeverRecords tPassageLeverRecords)
    {
        return tPassageLeverRecordsMapper.selectTPassageLeverRecordsList(tPassageLeverRecords);
    }

    /**
     * 新增起落杆记录
     *
     * @param tPassageLeverRecords 起落杆记录
     * @return 结果
     */
    @Override
    public int insertTPassageLeverRecords(TPassageLeverRecords tPassageLeverRecords)
    {
        tPassageLeverRecords.setCreateTime(DateUtils.getNowDate());
        return tPassageLeverRecordsMapper.insertTPassageLeverRecords(tPassageLeverRecords);
    }

    /**
     * 修改起落杆记录
     *
     * @param tPassageLeverRecords 起落杆记录
     * @return 结果
     */
    @Override
    public int updateTPassageLeverRecords(TPassageLeverRecords tPassageLeverRecords)
    {
        tPassageLeverRecords.setUpdateTime(DateUtils.getNowDate());
        return tPassageLeverRecordsMapper.updateTPassageLeverRecords(tPassageLeverRecords);
    }

    /**
     * 批量删除起落杆记录
     *
     * @param ids 需要删除的起落杆记录主键
     * @return 结果
     */
    @Override
    public int deleteTPassageLeverRecordsByIds(Long[] ids)
    {
        return tPassageLeverRecordsMapper.deleteTPassageLeverRecordsByIds(ids);
    }

    /**
     * 删除起落杆记录信息
     *
     * @param id 起落杆记录主键
     * @return 结果
     */
    @Override
    public int deleteTPassageLeverRecordsById(Long id)
    {
        return tPassageLeverRecordsMapper.deleteTPassageLeverRecordsById(id);
    }
}
