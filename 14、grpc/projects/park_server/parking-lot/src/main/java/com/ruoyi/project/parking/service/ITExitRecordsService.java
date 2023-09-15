package com.ruoyi.project.parking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.parking.domain.TExitRecords;

import java.util.List;

/**
 * 车辆离场记录Service接口
 *
 * @author ruoyi
 * @date 2023-02-22
 */
public interface ITExitRecordsService extends IService<TExitRecords>
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
    public List<TExitRecords> selectTExitRecordsList(TExitRecords tExitRecords);

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
     * 批量删除车辆离场记录
     *
     * @param ids 需要删除的车辆离场记录主键集合
     * @return 结果
     */
    public int deleteTExitRecordsByIds(Long[] ids);

    /**
     * 删除车辆离场记录信息
     *
     * @param id 车辆离场记录主键
     * @return 结果
     */
    public int deleteTExitRecordsById(Long id);

    public TExitRecords findNewestExitRecord(String parkNo,String carNo);
}
