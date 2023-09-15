package com.ruoyi.project.parking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.parking.domain.TExitRecords;
import com.ruoyi.project.parking.domain.vo.TExitRecordsVo;

import java.util.List;

/**
 * 车辆离场记录Mapper接口
 *
 * @author ruoyi
 * @date 2023-02-22
 */
public interface TExitRecordsMapper extends BaseMapper<TExitRecords>
{
    /**
     * 查询车辆离场记录
     *
     * @param id 车辆离场记录主键
     * @return 车辆离场记录
     */
    public TExitRecords selectTExitRecordsById(Long id);

    /**
     * 查询车辆离场记录列表
     *
     * @param tExitRecords 车辆离场记录
     * @return 车辆离场记录集合
     */
    public List<TExitRecords> selectTExitRecordsList(TExitRecordsVo tExitRecords);

    /**
     * 新增车辆离场记录
     *
     * @param tExitRecords 车辆离场记录
     * @return 结果
     */
    public int insertTExitRecords(TExitRecords tExitRecords);

    /**
     * 修改车辆离场记录
     *
     * @param tExitRecords 车辆离场记录
     * @return 结果
     */
    public int updateTExitRecords(TExitRecords tExitRecords);

    /**
     * 删除车辆离场记录
     *
     * @param id 车辆离场记录主键
     * @return 结果
     */
    public int deleteTExitRecordsById(Long id);

    /**
     * 批量删除车辆离场记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTExitRecordsByIds(Long[] ids);
}
