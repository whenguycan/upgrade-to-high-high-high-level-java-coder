package com.ruoyi.project.parking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.parking.domain.param.TExemptOrderParam;
import com.ruoyi.project.parking.domain.vo.TExemptOrderVO;
import com.ruoyi.project.parking.domain.vo.UnusualOrderVO;
import com.ruoyi.project.parking.entity.TExemptOrder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * <p>
 * 减免订单记录 服务类
 * </p>
 *
 * @author yinwen
 * @since 2023-03-10
 */
public interface ITExemptOrderService extends IService<TExemptOrder> {
    /**
     * 查询 减免订单
     *
     * @param tExemptOrder 查询条件
     */
    List<TExemptOrder> listCondition(TExemptOrder tExemptOrder);

    /**
     * 查询 不寻常订单
     *
     * @param tExemptOrder 查询条件
     */
    Pair<List<UnusualOrderVO>, Long> queryUnusualOrderVOCondition(TExemptOrder tExemptOrder);

    /**
     * 新增 减免订单
     *
     * @param tExemptOrderVO 新增对象
     */
    boolean add(TExemptOrderVO tExemptOrderVO);

    /**
     * 保存 减免订单
     *
     * @param tExemptOrderParam 更新对象
     */
    boolean editById(TExemptOrderParam tExemptOrderParam);
}
