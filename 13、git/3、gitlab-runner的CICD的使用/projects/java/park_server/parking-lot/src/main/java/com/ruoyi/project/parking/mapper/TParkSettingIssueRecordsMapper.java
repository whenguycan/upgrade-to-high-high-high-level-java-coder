package com.ruoyi.project.parking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.parking.domain.TParkSettingIssueRecords;

import java.util.List;

/**
 * 车场配置下发记录Mapper接口
 * 
 * @author ruoyi
 * @date 2023-03-27
 */
public interface TParkSettingIssueRecordsMapper extends BaseMapper<TParkSettingIssueRecords>
{
    /**
     * 查询车场配置下发记录
     * 
     * @param id 车场配置下发记录主键
     * @return 车场配置下发记录
     */
    public TParkSettingIssueRecords selectTParkSettingIssueRecordsById(Long id);

    /**
     * 查询车场配置下发记录列表
     * 
     * @param tParkSettingIssueRecords 车场配置下发记录
     * @return 车场配置下发记录集合
     */
    public List<TParkSettingIssueRecords> selectTParkSettingIssueRecordsList(TParkSettingIssueRecords tParkSettingIssueRecords);

    /**
     * 新增车场配置下发记录
     * 
     * @param tParkSettingIssueRecords 车场配置下发记录
     * @return 结果
     */
    public int insertTParkSettingIssueRecords(TParkSettingIssueRecords tParkSettingIssueRecords);

    /**
     * 修改车场配置下发记录
     * 
     * @param tParkSettingIssueRecords 车场配置下发记录
     * @return 结果
     */
    public int updateTParkSettingIssueRecords(TParkSettingIssueRecords tParkSettingIssueRecords);

    /**
     * 删除车场配置下发记录
     * 
     * @param id 车场配置下发记录主键
     * @return 结果
     */
    public int deleteTParkSettingIssueRecordsById(Long id);

    /**
     * 批量删除车场配置下发记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTParkSettingIssueRecordsByIds(Long[] ids);
}
