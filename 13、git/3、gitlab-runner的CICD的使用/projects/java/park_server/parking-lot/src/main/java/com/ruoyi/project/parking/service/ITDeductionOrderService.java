package com.ruoyi.project.parking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.parking.domain.param.TDeductionOrderParam;
import com.ruoyi.project.parking.domain.vo.TDeductionOrderVO;
import com.ruoyi.project.parking.domain.vo.UnusualOrderVO;
import com.ruoyi.project.parking.entity.TDeductionOrder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * <p>
 * 抵扣订单记录 服务类
 * </p>
 *
 * @author yinwen
 * @since 2023-03-10
 */
public interface ITDeductionOrderService extends IService<TDeductionOrder> {
    /**
     * 查询 抵扣订单
     *
     * @param tDeductionOrder 查询条件
     */
    List<TDeductionOrder> listCondition(TDeductionOrder tDeductionOrder);

    /**
     * 查询 不寻常订单
     *
     * @param tDeductionOrder 查询条件
     */
    Pair<List<UnusualOrderVO>, Long> queryUnusualOrderVOCondition(TDeductionOrder tDeductionOrder);

    /**
     * 新增 抵扣订单
     *
     * @param tDeductionOrderVO 新增对象
     */
    boolean add(TDeductionOrderVO tDeductionOrderVO);

    /**
     * 保存 抵扣订单
     *
     * @param tDeductionOrderParam 更新对象
     */
    boolean editById(TDeductionOrderParam tDeductionOrderParam);
}
