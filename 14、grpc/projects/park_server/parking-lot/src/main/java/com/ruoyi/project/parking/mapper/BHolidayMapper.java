package com.ruoyi.project.parking.mapper;

import java.util.List;
import com.ruoyi.project.parking.domain.BHoliday;
import org.apache.ibatis.annotations.Mapper;

/**
 * 节假日设置Mapper接口
 * 
 * @author fangch
 * @date 2023-02-25
 */
@Mapper
public interface BHolidayMapper 
{
    /**
     * 查询节假日设置
     * 
     * @param id 节假日设置主键
     * @return 节假日设置
     */
    BHoliday selectBHolidayById(Integer id);

    /**
     * 查询节假日设置列表
     * 
     * @param bHoliday 节假日设置
     * @return 节假日设置集合
     */
    List<BHoliday> selectBHolidayList(BHoliday bHoliday);

    /**
     * 新增节假日设置
     * 
     * @param bHoliday 节假日设置
     * @return 结果
     */
    int insertBHoliday(BHoliday bHoliday);

    /**
     * 修改节假日设置
     * 
     * @param bHoliday 节假日设置
     * @return 结果
     */
    int updateBHoliday(BHoliday bHoliday);

    /**
     * 删除节假日设置
     * 
     * @param id 节假日设置主键
     * @return 结果
     */
    int deleteBHolidayById(Integer id);

    /**
     * 批量删除节假日设置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBHolidayByIds(Integer[] ids);

    /**
     * 清空节假日
     *
     * @param holiday 需要删除的数据
     * @return 结果
     */
    boolean clearBHoliday(BHoliday holiday);
}
