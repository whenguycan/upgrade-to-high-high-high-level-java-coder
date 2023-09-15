package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.PayMethodDayFact;
import com.ruoyi.project.parking.domain.vo.PayStatisticFactVO;

import java.util.List;

/**
 * 首页付费方式分析事实Service接口
 * 
 * @author fangch
 * @date 2023-03-20
 */
public interface IPayMethodDayFactService 
{
    /**
     * 查询首页付费方式分析事实
     * 
     * @param id 首页付费方式分析事实主键
     * @return 首页付费方式分析事实
     */
    public PayMethodDayFact selectPayMethodDayFactById(Integer id);

    /**
     * 查询首页付费方式分析事实列表
     * 
     * @param payMethodDayFact 首页付费方式分析事实
     * @return 首页付费方式分析事实集合
     */
    public List<PayMethodDayFact> selectPayMethodDayFactList(PayMethodDayFact payMethodDayFact);

    /**
     * 新增首页付费方式分析事实
     * 
     * @param payMethodDayFact 首页付费方式分析事实
     * @return 结果
     */
    public int insertPayMethodDayFact(PayMethodDayFact payMethodDayFact);

    /**
     * 修改首页付费方式分析事实
     * 
     * @param payMethodDayFact 首页付费方式分析事实
     * @return 结果
     */
    public int updatePayMethodDayFact(PayMethodDayFact payMethodDayFact);

    /**
     * 批量删除首页付费方式分析事实
     * 
     * @param ids 需要删除的首页付费方式分析事实主键集合
     * @return 结果
     */
    public int deletePayMethodDayFactByIds(Integer[] ids);

    /**
     * 删除首页付费方式分析事实信息
     * 
     * @param id 首页付费方式分析事实主键
     * @return 结果
     */
    public int deletePayMethodDayFactById(Integer id);

    /**
     * 分析首页付费方式事实
     */
    void analysePayMethodDayFact(String userId);

    /**
     * 查询首页付费方式分析事实
     *
     * @param payMethodDayFact 首页付费方式分析事实
     * @return 首页付费方式分析事实集合
     */
    List<PayStatisticFactVO> getPayMethodDayFact(PayMethodDayFact payMethodDayFact);
}
