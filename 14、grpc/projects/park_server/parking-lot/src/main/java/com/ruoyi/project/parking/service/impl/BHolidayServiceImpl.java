package com.ruoyi.project.parking.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.JsonUtil;
import com.ruoyi.framework.web.domain.server.Sys;
import com.ruoyi.project.parking.utils.HolidayUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.parking.mapper.BHolidayMapper;
import com.ruoyi.project.parking.domain.BHoliday;
import com.ruoyi.project.parking.service.IBHolidayService;

/**
 * 节假日设置Service业务层处理
 * 
 * @author fangch
 * @date 2023-02-25
 */
@Service
public class BHolidayServiceImpl implements IBHolidayService 
{
    @Autowired
    private BHolidayMapper bHolidayMapper;

    /**
     * 查询节假日设置
     * 
     * @param id 节假日设置主键
     * @return 节假日设置
     */
    @Override
    public BHoliday selectBHolidayById(Integer id)
    {
        return bHolidayMapper.selectBHolidayById(id);
    }

    /**
     * 查询节假日设置列表
     * 
     * @param bHoliday 节假日设置
     * @return 节假日设置
     */
    @Override
    public List<BHoliday> selectBHolidayList(BHoliday bHoliday)
    {
        return bHolidayMapper.selectBHolidayList(bHoliday);
    }

    /**
     * 新增节假日设置
     * 
     * @param bHoliday 节假日设置
     * @return 结果
     */
    @Override
    public int insertBHoliday(BHoliday bHoliday)
    {
        bHoliday.setCreateTime(LocalDateTime.now());
        return bHolidayMapper.insertBHoliday(bHoliday);
    }

    /**
     * 修改节假日设置
     * 
     * @param bHoliday 节假日设置
     * @return 结果
     */
    @Override
    public int updateBHoliday(BHoliday bHoliday)
    {
        bHoliday.setUpdateTime(LocalDateTime.now());
        return bHolidayMapper.updateBHoliday(bHoliday);
    }

    /**
     * 批量删除节假日设置
     * 
     * @param ids 需要删除的节假日设置主键
     * @return 结果
     */
    @Override
    public int deleteBHolidayByIds(Integer[] ids)
    {
        return bHolidayMapper.deleteBHolidayByIds(ids);
    }

    /**
     * 删除节假日设置信息
     * 
     * @param id 节假日设置主键
     * @return 结果
     */
    @Override
    public int deleteBHolidayById(Integer id)
    {
        return bHolidayMapper.deleteBHolidayById(id);
    }

    /**
     * 获取国家法定节假日
     *
     * @return 节假日设置集合
     */
    @Override
    public List<BHoliday> getThisYearJjr(String userId) {
        String data;
        try {
            data = HolidayUtils.getJjr(LocalDate.now().getYear());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<BHoliday> holidays = JsonUtil.json2CollectObj(data, LinkedList.class, BHoliday.class);
        if (CollectionUtils.isNotEmpty(holidays)) {
            for (BHoliday holiday : holidays) {
                bHolidayMapper.clearBHoliday(holiday);
                holiday.setCreateBy(userId);
                holiday.setCreateTime(LocalDateTime.now());
                bHolidayMapper.insertBHoliday(holiday);
            }
        }
        return bHolidayMapper.selectBHolidayList(new BHoliday());
    }
}
