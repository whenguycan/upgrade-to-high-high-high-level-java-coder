package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.ParkUsageDayFact;

import java.util.List;

/**
 * 首页今日泊车使用情况事实Service接口
 * 
 * @author fangch
 * @date 2023-03-28
 */
public interface IParkUsageDayFactService 
{
    /**
     * 查询首页今日泊车使用情况事实
     * 
     * @param id 首页今日泊车使用情况事实主键
     * @return 首页今日泊车使用情况事实
     */
    public ParkUsageDayFact selectParkUsageDayFactById(Integer id);

    /**
     * 查询首页今日泊车使用情况事实列表
     * 
     * @param parkUsageDayFact 首页今日泊车使用情况事实
     * @return 首页今日泊车使用情况事实集合
     */
    public List<ParkUsageDayFact> selectParkUsageDayFactList(ParkUsageDayFact parkUsageDayFact);

    /**
     * 新增首页今日泊车使用情况事实
     * 
     * @param parkUsageDayFact 首页今日泊车使用情况事实
     * @return 结果
     */
    public int insertParkUsageDayFact(ParkUsageDayFact parkUsageDayFact);

    /**
     * 修改首页今日泊车使用情况事实
     * 
     * @param parkUsageDayFact 首页今日泊车使用情况事实
     * @return 结果
     */
    public int updateParkUsageDayFact(ParkUsageDayFact parkUsageDayFact);

    /**
     * 批量删除首页今日泊车使用情况事实
     * 
     * @param ids 需要删除的首页今日泊车使用情况事实主键集合
     * @return 结果
     */
    public int deleteParkUsageDayFactByIds(Integer[] ids);

    /**
     * 删除首页今日泊车使用情况事实信息
     * 
     * @param id 首页今日泊车使用情况事实主键
     * @return 结果
     */
    public int deleteParkUsageDayFactById(Integer id);

    /**
     * 分析首页今日泊车使用情况事实
     *
     */
    void analyseParkUsageDayFact(String userId);

    /**
     * 查询大屏今日泊车使用情况事实
     *
     * @param parkUsageDayFact
     * @return
     */
    ParkUsageDayFact selectParkUsageDayFact(ParkUsageDayFact parkUsageDayFact);
}
