package com.ruoyi.project.parking.mapper;

import com.ruoyi.project.parking.domain.EntryExitAnalysisDayFact;
import com.ruoyi.project.parking.domain.bo.CarVolumeSheetBO;
import com.ruoyi.project.parking.domain.vo.CarVolumeSheetVO;
import com.ruoyi.project.parking.domain.vo.EntryExitAnalysisFactVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 首页出入场分析事实Mapper接口
 * 
 * @author fangch
 * @date 2023-03-20
 */
@Mapper
public interface EntryExitAnalysisDayFactMapper 
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
     * 删除首页出入场分析事实
     * 
     * @param id 首页出入场分析事实主键
     * @return 结果
     */
    public int deleteEntryExitAnalysisDayFactById(Integer id);

    /**
     * 批量删除首页出入场分析事实
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEntryExitAnalysisDayFactByIds(Integer[] ids);

    /**
     * 分析首页出入场分析事实
     *
     * @return 首页出入场分析事实
     */
    List<EntryExitAnalysisDayFact> analyseEntryExitAnalysisDayFact(@Param("parkNos") List<String> parkNos, @Param("day") String day);

    /**
     * 查询同维度的首页出入场分析事实
     *
     * @param entryExitAnalysisDayFact
     * @return
     */
    EntryExitAnalysisDayFact selectEntryExitAnalysisDayFact(EntryExitAnalysisDayFact entryExitAnalysisDayFact);

    /**
     * 查询首页入场分析事实
     *
     * @param entryExitAnalysisDayFact 首页出入场分析事实
     * @return 首页入场分析事实集合
     */
    List<EntryExitAnalysisFactVO> getEntryAnalysisDayFact(EntryExitAnalysisDayFact entryExitAnalysisDayFact);

    /**
     * 查询首页出场分析事实
     *
     * @param entryExitAnalysisDayFact 首页出入场分析事实
     * @return 首页出场分析事实集合
     */
    List<EntryExitAnalysisFactVO> getExitAnalysisDayFact(EntryExitAnalysisDayFact entryExitAnalysisDayFact);

    int sumEntryOfDay(LocalDate now, List<String> parkNos);

    int sumExitOfDay(LocalDate now, List<String> parkNos);

    List<CarVolumeSheetBO> getCarVolumeSheetDay(CarVolumeSheetVO carVolumeSheetVO);

    List<CarVolumeSheetBO> getCarVolumeSheetHour(CarVolumeSheetVO carVolumeSheetVO);
}
