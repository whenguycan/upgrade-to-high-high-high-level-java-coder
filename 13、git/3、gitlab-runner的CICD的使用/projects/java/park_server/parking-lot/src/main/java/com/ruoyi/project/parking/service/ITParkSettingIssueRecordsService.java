package com.ruoyi.project.parking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.parking.domain.TParkSettingIssueRecords;

import java.util.List;

/**
 * 车场配置下发记录Service接口
 * 
 * @author ruoyi
 * @date 2023-03-27
 */
public interface ITParkSettingIssueRecordsService extends IService<TParkSettingIssueRecords>
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
     * 批量删除车场配置下发记录
     * 
     * @param ids 需要删除的车场配置下发记录主键集合
     * @return 结果
     */
    public int deleteTParkSettingIssueRecordsByIds(Long[] ids);

    /**
     * 删除车场配置下发记录信息
     * 
     * @param id 车场配置下发记录主键
     * @return 结果
     */
    public int deleteTParkSettingIssueRecordsById(Long id);

    /**
     * 导入配置
     * @param tParkSettingIssueRecordsList
     * @param updateSupport
     * @return
     */
    String importTParkSettingIssueRecords(List<TParkSettingIssueRecords> tParkSettingIssueRecordsList,boolean updateSupport);
}
