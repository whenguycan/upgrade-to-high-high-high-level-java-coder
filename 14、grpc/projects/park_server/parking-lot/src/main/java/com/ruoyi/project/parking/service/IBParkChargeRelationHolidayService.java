package com.ruoyi.project.parking.service;

import java.util.List;
import com.ruoyi.project.parking.domain.BParkChargeRelationHoliday;

/**
 * 节假日-区域-车类型-车型-收费规则关联Service接口
 * 
 * @author fangch
 * @date 2023-02-23
 */
public interface IBParkChargeRelationHolidayService 
{
    /**
     * 查询节假日-区域-车类型-车型-收费规则关联
     * 
     * @param id 节假日-区域-车类型-车型-收费规则关联主键
     * @return 节假日-区域-车类型-车型-收费规则关联
     */
    BParkChargeRelationHoliday selectBParkChargeRelationHolidayById(Integer id);

    /**
     * 查询节假日-区域-车类型-车型-收费规则关联列表
     * 
     * @param bParkChargeRelationHoliday 节假日-区域-车类型-车型-收费规则关联
     * @return 节假日-区域-车类型-车型-收费规则关联集合
     */
    List<BParkChargeRelationHoliday> selectBParkChargeRelationHolidayList(BParkChargeRelationHoliday bParkChargeRelationHoliday);

    /**
     * 设置关联关系
     * 
     * @param bParkChargeRelationHolidays 节假日-区域-车类型-车型-收费规则关联集合
     * @return 结果
     */
    int setRelation(List<BParkChargeRelationHoliday> bParkChargeRelationHolidays, String createBy, String parkNo);

    /**
     * 批量删除节假日-区域-车类型-车型-收费规则关联
     * 
     * @param ids 需要删除的节假日-区域-车类型-车型-收费规则关联主键集合
     * @return 结果
     */
    int deleteBParkChargeRelationHolidayByIds(Integer[] ids);

    /**
     * 删除节假日-区域-车类型-车型-收费规则关联信息
     * 
     * @param id 节假日-区域-车类型-车型-收费规则关联主键
     * @return 结果
     */
    int deleteBParkChargeRelationHolidayById(Integer id);
}
