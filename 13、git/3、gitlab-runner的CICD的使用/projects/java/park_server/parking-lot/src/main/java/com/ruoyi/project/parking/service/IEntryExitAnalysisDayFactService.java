package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.EntryExitAnalysisDayFact;
import com.ruoyi.project.parking.domain.bo.CarVolumeSheetBO;
import com.ruoyi.project.parking.domain.vo.CarVolumeSheetVO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 首页出入场分析事实Service接口
 * 
 * @author fangch
 * @date 2023-03-20
 */
public interface IEntryExitAnalysisDayFactService 
{
    /**
     * 查询首页出入场分析事实
     * 
     * @param id 首页出入场分析事实主键
     * @return 首页出入场分析事实
     */
    public EntryExitAnalysisDayFact selectEntryExitAnalysisDayFactById(Integer id);

    /**
     * 查询首页出入场分析事实列表
     * 
     * @param entryExitAnalysisDayFact 首页出入场分析事实
     * @return 首页出入场分析事实集合
     */
    public List<EntryExitAnalysisDayFact> selectEntryExitAnalysisDayFactList(EntryExitAnalysisDayFact entryExitAnalysisDayFact);

    /**
     * 新增首页出入场分析事实
     * 
     * @param entryExitAnalysisDayFact 首页出入场分析事实
     * @return 结果
     */
    public int insertEntryExitAnalysisDayFact(EntryExitAnalysisDayFact entryExitAnalysisDayFact);

    /**
     * 修改首页出入场分析事实
     * 
     * @param entryExitAnalysisDayFact 首页出入场分析事实
     * @return 结果
     */
    public int updateEntryExitAnalysisDayFact(EntryExitAnalysisDayFact entryExitAnalysisDayFact);

    /**
     * 批量删除首页出入场分析事实
     * 
     * @param ids 需要删除的首页出入场分析事实主键集合
     * @return 结果
     */
    public int deleteEntryExitAnalysisDayFactByIds(Integer[] ids);

    /**
     * 删除首页出入场分析事实信息
     * 
     * @param id 首页出入场分析事实主键
     * @return 结果
     */
    public int deleteEntryExitAnalysisDayFactById(Integer id);

    /**
     * 分析首页出入场分析事实
     */
    void analyseEntryExitAnalysisDayFact(String userId);

    /**
     * 查询首页出入场分析事实
     *
     * @param entryExitAnalysisDayFact 首页出入场分析事实
     * @return 首页出入场分析事实集合
     */
    Map<String, Object> getEntryExitAnalysisDayFact(EntryExitAnalysisDayFact entryExitAnalysisDayFact);

    int sumEntryOfDay(LocalDate now, List<String> parkNos);

    int sumExitOfDay(LocalDate now, List<String> parkNos);

    List<CarVolumeSheetBO> getCarVolumeSheetDay(CarVolumeSheetVO carVolumeSheetVO);

    List<CarVolumeSheetBO> getCarVolumeSheetHour(CarVolumeSheetVO carVolumeSheetVO);
}
