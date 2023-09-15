package com.ruoyi.project.parking.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.parking.domain.BParkChargeRelationVehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.parking.mapper.BParkChargeRelationHolidayMapper;
import com.ruoyi.project.parking.domain.BParkChargeRelationHoliday;
import com.ruoyi.project.parking.service.IBParkChargeRelationHolidayService;

/**
 * 节假日-区域-车类型-车型-收费规则关联Service业务层处理
 * 
 * @author fangch
 * @date 2023-02-23
 */
@Service
public class BParkChargeRelationHolidayServiceImpl implements IBParkChargeRelationHolidayService 
{
    @Autowired
    private BParkChargeRelationHolidayMapper bParkChargeRelationHolidayMapper;

    /**
     * 查询节假日-区域-车类型-车型-收费规则关联
     * 
     * @param id 节假日-区域-车类型-车型-收费规则关联主键
     * @return 节假日-区域-车类型-车型-收费规则关联
     */
    @Override
    public BParkChargeRelationHoliday selectBParkChargeRelationHolidayById(Integer id)
    {
        return bParkChargeRelationHolidayMapper.selectBParkChargeRelationHolidayById(id);
    }

    /**
     * 查询节假日-区域-车类型-车型-收费规则关联列表
     * 
     * @param bParkChargeRelationHoliday 节假日-区域-车类型-车型-收费规则关联
     * @return 节假日-区域-车类型-车型-收费规则关联
     */
    @Override
    public List<BParkChargeRelationHoliday> selectBParkChargeRelationHolidayList(BParkChargeRelationHoliday bParkChargeRelationHoliday)
    {
        return bParkChargeRelationHolidayMapper.selectBParkChargeRelationHolidayList(bParkChargeRelationHoliday);
    }

    /**
     * 设置关联关系
     * 
     * @param bParkChargeRelationHolidays 节假日-区域-车类型-车型-收费规则关联集合
     * @return 结果
     */
    @Override
    public int setRelation(List<BParkChargeRelationHoliday> bParkChargeRelationHolidays, String createBy, String parkNo) {
        int count = 0;
        for (BParkChargeRelationHoliday item : bParkChargeRelationHolidays) {
            if (bParkChargeRelationHolidayMapper.checkRelationExist(item) == 0) {
                item.setParkNo(parkNo);
                item.setCreateBy(createBy);
                item.setCreateTime(LocalDateTime.now());
                count += bParkChargeRelationHolidayMapper.insertBParkChargeRelationHoliday(item);
            }
        }
        return count;
    }

    /**
     * 批量删除节假日-区域-车类型-车型-收费规则关联
     * 
     * @param ids 需要删除的节假日-区域-车类型-车型-收费规则关联主键
     * @return 结果
     */
    @Override
    public int deleteBParkChargeRelationHolidayByIds(Integer[] ids)
    {
        return bParkChargeRelationHolidayMapper.deleteBParkChargeRelationHolidayByIds(ids);
    }

    /**
     * 删除节假日-区域-车类型-车型-收费规则关联信息
     * 
     * @param id 节假日-区域-车类型-车型-收费规则关联主键
     * @return 结果
     */
    @Override
    public int deleteBParkChargeRelationHolidayById(Integer id)
    {
        return bParkChargeRelationHolidayMapper.deleteBParkChargeRelationHolidayById(id);
    }
}
