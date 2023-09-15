package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.OrderSituationDayFact;
import com.ruoyi.project.parking.domain.vo.OrderSituationFactVO;

import java.util.List;

/**
 * 首页订单情况事实Service接口
 * 
 * @author fangch
 * @date 2023-03-20
 */
public interface IOrderSituationDayFactService 
{
    /**
     * 查询首页订单情况事实
     * 
     * @param id 首页订单情况事实主键
     * @return 首页订单情况事实
     */
    public OrderSituationDayFact selectOrderSituationDayFactById(Integer id);

    /**
     * 查询首页订单情况事实列表
     * 
     * @param orderSituationDayFact 首页订单情况事实
     * @return 首页订单情况事实集合
     */
    public List<OrderSituationDayFact> selectOrderSituationDayFactList(OrderSituationDayFact orderSituationDayFact);

    /**
     * 新增首页订单情况事实
     * 
     * @param orderSituationDayFact 首页订单情况事实
     * @return 结果
     */
    public int insertOrderSituationDayFact(OrderSituationDayFact orderSituationDayFact);

    /**
     * 修改首页订单情况事实
     * 
     * @param orderSituationDayFact 首页订单情况事实
     * @return 结果
     */
    public int updateOrderSituationDayFact(OrderSituationDayFact orderSituationDayFact);

    /**
     * 批量删除首页订单情况事实
     * 
     * @param ids 需要删除的首页订单情况事实主键集合
     * @return 结果
     */
    public int deleteOrderSituationDayFactByIds(Integer[] ids);

    /**
     * 删除首页订单情况事实信息
     * 
     * @param id 首页订单情况事实主键
     * @return 结果
     */
    public int deleteOrderSituationDayFactById(Integer id);

    /**
     * 分析首页订单情况事实
     */
    void analyseOrderSituationDayFact(String userId);

    /**
     * 查询首页订单情况事实
     *
     * @param orderSituationDayFact 首页订单情况事实
     * @return 首页订单情况事实集合
     */
    List<OrderSituationFactVO> getOrderSituationDayFact(OrderSituationDayFact orderSituationDayFact);
}
