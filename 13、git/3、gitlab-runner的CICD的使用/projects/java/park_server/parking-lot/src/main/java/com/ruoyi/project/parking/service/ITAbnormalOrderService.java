package com.ruoyi.project.parking.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.parking.domain.bo.AbnormalOrderStatisticsBO;
import com.ruoyi.project.parking.domain.param.TAbnormalOrderParam;
import com.ruoyi.project.parking.domain.vo.StatisticsSheetVO;
import com.ruoyi.project.parking.domain.vo.TAbnormalOrderVO;
import com.ruoyi.project.parking.domain.vo.UnusualOrderVO;
import com.ruoyi.project.parking.entity.TAbnormalOrder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * <p>
 * 异常订单记录 服务类
 * </p>
 *
 * @author yinwen
 * @since 2023-03-10
 */
public interface ITAbnormalOrderService extends IService<TAbnormalOrder> {
    /**
     * 查询 异常订单
     *
     * @param tAbnormalOrder 查询条件
     */
    List<TAbnormalOrder> listCondition(TAbnormalOrder tAbnormalOrder);

    /**
     * 查询 不寻常订单
     *
     * @param tAbnormalOrder 查询条件
     */
    Pair<List<UnusualOrderVO>, Long> queryUnusualOrderVOCondition(TAbnormalOrder tAbnormalOrder);

    /**
     * 新增 异常订单
     *
     * @param tAbnormalOrderVO 新增对象
     */
    boolean add(TAbnormalOrderVO tAbnormalOrderVO);

    /**
     * 保存 异常订单
     *
     * @param tAbnormalOrderParam 更新对象
     */
    boolean editById(TAbnormalOrderParam tAbnormalOrderParam);

    AbnormalOrderStatisticsBO statistics(StatisticsSheetVO statisticsSheetVO);
}
