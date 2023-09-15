package com.ruoyi.project.parking.mapper;

import java.util.List;
import com.ruoyi.project.parking.domain.BParkChargeRelationHoliday;
import org.apache.ibatis.annotations.Mapper;

/**
 * 节假日-区域-车类型-车型-收费规则关联Mapper接口
 * 
 * @author fangch
 * @date 2023-02-23
 */
@Mapper
public interface BParkChargeRelationHolidayMapper 
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
     * 新增节假日-区域-车类型-车型-收费规则关联
     * 
     * @param bParkChargeRelationHoliday 节假日-区域-车类型-车型-收费规则关联
     * @return 结果
     */
    int insertBParkChargeRelationHoliday(BParkChargeRelationHoliday bParkChargeRelationHoliday);

    /**
     * 删除节假日-区域-车类型-车型-收费规则关联
     * 
     * @param id 节假日-区域-车类型-车型-收费规则关联主键
     * @return 结果
     */
    int deleteBParkChargeRelationHolidayById(Integer id);

    /**
     * 批量删除节假日-区域-车类型-车型-收费规则关联
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBParkChargeRelationHolidayByIds(Integer[] ids);

    /**
     * 检查同关联设置存在否
     *
     * @param item 节假日-区域-车类型-车型-收费规则关联
     * @return
     */
    int checkRelationExist(BParkChargeRelationHoliday item);
}
