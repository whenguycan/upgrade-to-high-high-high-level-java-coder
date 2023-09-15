package com.ruoyi.project.parking.mapper;

import com.ruoyi.project.parking.domain.OrderSituationDayFact;
import com.ruoyi.project.parking.domain.vo.OrderSituationFactVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 首页订单情况事实Mapper接口
 * 
 * @author fangch
 * @date 2023-03-20
 */
@Mapper
public interface OrderSituationDayFactMapper 
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
     * 删除首页订单情况事实
     * 
     * @param id 首页订单情况事实主键
     * @return 结果
     */
    public int deleteOrderSituationDayFactById(Integer id);

    /**
     * 批量删除首页订单情况事实
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrderSituationDayFactByIds(Integer[] ids);

    int deleteOrderSituationDayFact(OrderSituationDayFact orderSituationDayFact);

    /**
     * 查询首页订单情况事实
     *
     * @param orderSituationDayFact 首页订单情况事实
     * @return 首页订单情况事实集合
     */
    List<OrderSituationFactVO> getOrderSituationDayFact(OrderSituationDayFact orderSituationDayFact);
}
