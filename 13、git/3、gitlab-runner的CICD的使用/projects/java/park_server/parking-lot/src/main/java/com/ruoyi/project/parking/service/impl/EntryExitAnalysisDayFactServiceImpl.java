package com.ruoyi.project.parking.service.impl;

import com.ruoyi.project.parking.domain.EntryExitAnalysisDayFact;
import com.ruoyi.project.parking.domain.bo.CarVolumeSheetBO;
import com.ruoyi.project.parking.domain.vo.CarVolumeSheetVO;
import com.ruoyi.project.parking.mapper.EntryExitAnalysisDayFactMapper;
import com.ruoyi.project.parking.mapper.HomePageMapper;
import com.ruoyi.project.parking.service.IEntryExitAnalysisDayFactService;
import com.ruoyi.project.system.domain.SysDept;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页出入场分析事实Service业务层处理
 *
 * @author fangch
 * @date 2023-03-20
 */
@Service
public class EntryExitAnalysisDayFactServiceImpl implements IEntryExitAnalysisDayFactService {
    @Autowired
    private EntryExitAnalysisDayFactMapper entryExitAnalysisDayFactMapper;

    @Autowired
    private HomePageMapper homePageMapper;

    /**
     * 查询首页出入场分析事实
     *
     * @param id 首页出入场分析事实主键
     * @return 首页出入场分析事实
     */
    @Override
    public EntryExitAnalysisDayFact selectEntryExitAnalysisDayFactById(Integer id) {
        return entryExitAnalysisDayFactMapper.selectEntryExitAnalysisDayFactById(id);
    }

    /**
     * 查询首页出入场分析事实列表
     *
     * @param entryExitAnalysisDayFact 首页出入场分析事实
     * @return 首页出入场分析事实
     */
    @Override
    public List<EntryExitAnalysisDayFact> selectEntryExitAnalysisDayFactList(EntryExitAnalysisDayFact entryExitAnalysisDayFact) {
        return entryExitAnalysisDayFactMapper.selectEntryExitAnalysisDayFactList(entryExitAnalysisDayFact);
    }

    /**
     * 新增首页出入场分析事实
     *
     * @param entryExitAnalysisDayFact 首页出入场分析事实
     * @return 结果
     */
    @Override
    public int insertEntryExitAnalysisDayFact(EntryExitAnalysisDayFact entryExitAnalysisDayFact) {
        entryExitAnalysisDayFact.setCreateTime(LocalDateTime.now());
        return entryExitAnalysisDayFactMapper.insertEntryExitAnalysisDayFact(entryExitAnalysisDayFact);
    }

    /**
     * 修改首页出入场分析事实
     *
     * @param entryExitAnalysisDayFact 首页出入场分析事实
     * @return 结果
     */
    @Override
    public int updateEntryExitAnalysisDayFact(EntryExitAnalysisDayFact entryExitAnalysisDayFact) {
        entryExitAnalysisDayFact.setUpdateTime(LocalDateTime.now());
        return entryExitAnalysisDayFactMapper.updateEntryExitAnalysisDayFact(entryExitAnalysisDayFact);
    }

    /**
     * 批量删除首页出入场分析事实
     *
     * @param ids 需要删除的首页出入场分析事实主键
     * @return 结果
     */
    @Override
    public int deleteEntryExitAnalysisDayFactByIds(Integer[] ids) {
        return entryExitAnalysisDayFactMapper.deleteEntryExitAnalysisDayFactByIds(ids);
    }

    /**
     * 删除首页出入场分析事实信息
     *
     * @param id 首页出入场分析事实主键
     * @return 结果
     */
    @Override
    public int deleteEntryExitAnalysisDayFactById(Integer id) {
        return entryExitAnalysisDayFactMapper.deleteEntryExitAnalysisDayFactById(id);
    }

    /**
     * 分析首页出入场分析事实
     */
    @Override
    public void analyseEntryExitAnalysisDayFact(String userId) {
        // 查询所有车场id和编号
        List<SysDept> parks = homePageMapper.getAllParkIds();
        for (SysDept park : parks) {
            // 查询所有归属子车场编号
            List<String> parkNos = homePageMapper.getChildParkNos(park.getDeptId());
            // 分析首页付费类型事实
            String day = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            List<EntryExitAnalysisDayFact> entryExitAnalysisDayFactList = entryExitAnalysisDayFactMapper.analyseEntryExitAnalysisDayFact(parkNos, day);
            if (CollectionUtils.isNotEmpty(entryExitAnalysisDayFactList)) {
                entryExitAnalysisDayFactList.forEach(item -> {
                    // 设置车场编号、日期
                    item.setParkNo(park.getParkNo());
                    item.setDay(day);
                    // 检查同维度的分析数据是否已存在，存在更新，不存在新增
                    EntryExitAnalysisDayFact existFact = entryExitAnalysisDayFactMapper.selectEntryExitAnalysisDayFact(item);
                    if (null == existFact) {
                        item.setCreateBy(userId);
                        item.setCreateTime(LocalDateTime.now());
                        entryExitAnalysisDayFactMapper.insertEntryExitAnalysisDayFact(item);
                    } else {
                        existFact.setEntryCount(item.getEntryCount());
                        existFact.setExitCount(item.getExitCount());
                        existFact.setUpdateBy(userId);
                        existFact.setUpdateTime(LocalDateTime.now());
                        entryExitAnalysisDayFactMapper.updateEntryExitAnalysisDayFact(existFact);
                    }
                });
            }
        }
    }

    /**
     * 查询首页出入场分析事实
     *
     * @param entryExitAnalysisDayFact 首页出入场分析事实
     * @return 首页出入场分析事实集合
     */
    @Override
    public Map<String, Object> getEntryExitAnalysisDayFact(EntryExitAnalysisDayFact entryExitAnalysisDayFact) {
        Map<String, Object> result = new HashMap<>();
        result.put("入场车辆", entryExitAnalysisDayFactMapper.getEntryAnalysisDayFact(entryExitAnalysisDayFact));
        result.put("出场车辆", entryExitAnalysisDayFactMapper.getExitAnalysisDayFact(entryExitAnalysisDayFact));
        return result;
    }

    @Override
    public int sumEntryOfDay(LocalDate now, List<String> parkNos) {
        return entryExitAnalysisDayFactMapper.sumEntryOfDay(now, parkNos);
    }

    @Override
    public int sumExitOfDay(LocalDate now, List<String> parkNos) {
        return entryExitAnalysisDayFactMapper.sumExitOfDay(now, parkNos);
    }

    @Override
    public List<CarVolumeSheetBO> getCarVolumeSheetDay(CarVolumeSheetVO carVolumeSheetVO) {
        return entryExitAnalysisDayFactMapper.getCarVolumeSheetDay(carVolumeSheetVO);
    }

    @Override
    public List<CarVolumeSheetBO> getCarVolumeSheetHour(CarVolumeSheetVO carVolumeSheetVO) {
        return entryExitAnalysisDayFactMapper.getCarVolumeSheetHour(carVolumeSheetVO);
    }
}
