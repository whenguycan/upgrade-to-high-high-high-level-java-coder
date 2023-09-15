package com.ruoyi.project.parking.service;

import java.util.List;
import com.ruoyi.project.parking.domain.BHoliday;

/**
 * 节假日设置Service接口
 * 
 * @author fangch
 * @date 2023-02-25
 */
public interface IBHolidayService 
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
     * 批量删除节假日设置
     * 
     * @param ids 需要删除的节假日设置主键集合
     * @return 结果
     */
    int deleteBHolidayByIds(Integer[] ids);

    /**
     * 删除节假日设置信息
     * 
     * @param id 节假日设置主键
     * @return 结果
     */
    int deleteBHolidayById(Integer id);

    /**
     * 获取国家法定节假日
     *
     * @return 节假日设置集合
     */
    List<BHoliday> getThisYearJjr(String userId);
}
