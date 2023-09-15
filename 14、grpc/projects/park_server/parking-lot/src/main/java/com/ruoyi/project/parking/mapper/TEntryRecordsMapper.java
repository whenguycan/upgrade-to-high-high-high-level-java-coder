package com.ruoyi.project.parking.mapper;

import com.ruoyi.project.parking.domain.TEntryRecords;
import com.ruoyi.project.parking.domain.vo.TEntryRecordsVo;

import java.util.List;

/**
 * 车辆进场记录Mapper接口
 *
 * @author ruoyi
 * @date 2023-02-22
 */
public interface TEntryRecordsMapper
{
    /**
     * 查询车辆进场记录
     *
     * @param id 车辆进场记录主键
     * @return 车辆进场记录
     */
    public TEntryRecords selectTEntryRecordsById(Long id);

    /**
     * 查询车辆进场记录列表
     *
     * @param tEntryRecordsVo 车辆进场记录
     * @return 车辆进场记录集合
     */
    public List<TEntryRecords> selectTEntryRecordsList(TEntryRecordsVo tEntryRecordsVo);

    /**
     * 新增车辆进场记录
     *
     * @param tEntryRecords 车辆进场记录
     * @return 结果
     */
    public int insertTEntryRecords(TEntryRecords tEntryRecords);

    /**
     * 修改车辆进场记录
     *
     * @param tEntryRecords 车辆进场记录
     * @return 结果
     */
    public int updateTEntryRecords(TEntryRecords tEntryRecords);

    /**
     * 删除车辆进场记录
     *
     * @param id 车辆进场记录主键
     * @return 结果
     */
    public int deleteTEntryRecordsById(Long id);

    /**
     * 批量删除车辆进场记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTEntryRecordsByIds(Long[] ids);
}
