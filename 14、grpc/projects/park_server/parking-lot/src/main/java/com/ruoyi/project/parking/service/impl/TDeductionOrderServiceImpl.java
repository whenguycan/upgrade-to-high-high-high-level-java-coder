package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.parking.domain.param.TDeductionOrderParam;
import com.ruoyi.project.parking.domain.vo.TDeductionOrderVO;
import com.ruoyi.project.parking.domain.vo.UnusualOrderVO;
import com.ruoyi.project.parking.entity.TDeductionOrder;
import com.ruoyi.project.parking.mapper.TDeductionOrderMapper;
import com.ruoyi.project.parking.service.ITDeductionOrderService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 抵扣订单记录 服务实现类
 * </p>
 *
 * @author yinwen
 * @since 2023-03-10
 */
@Service
public class TDeductionOrderServiceImpl extends ServiceImpl<TDeductionOrderMapper, TDeductionOrder> implements ITDeductionOrderService {

    @Override
    public List<TDeductionOrder> listCondition(TDeductionOrder tDeductionOrder) {
        LambdaQueryWrapper<TDeductionOrder> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(tDeductionOrder.getParkNo()), TDeductionOrder::getParkNo, tDeductionOrder.getParkNo())
                .eq(tDeductionOrder.getOrderType() != null, TDeductionOrder::getOrderType, tDeductionOrder.getOrderType())
                .eq(StringUtils.isNotEmpty(tDeductionOrder.getOrderNo()), TDeductionOrder::getOrderNo, tDeductionOrder.getOrderNo())
                .eq(tDeductionOrder.getDeductionType() != null, TDeductionOrder::getDeductionType, tDeductionOrder.getDeductionType())
                .eq(StringUtils.isNotEmpty(tDeductionOrder.getRemark()), TDeductionOrder::getRemark, tDeductionOrder.getRemark());
        return list(qw);
    }

    @Override
    public Pair<List<UnusualOrderVO>, Long> queryUnusualOrderVOCondition(TDeductionOrder tDeductionOrder) {
        QueryWrapper<TDeductionOrder> qw = new QueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(tDeductionOrder.getParkNo()), "t1.park_no", tDeductionOrder.getParkNo())
                .eq(tDeductionOrder.getOrderType() != null, "t1.order_type", tDeductionOrder.getOrderType())
                .eq(StringUtils.isNotEmpty(tDeductionOrder.getOrderNo()), "t1.order_no", tDeductionOrder.getOrderNo())
                .eq(tDeductionOrder.getDeductionType() != null, "t1.deduction_type", tDeductionOrder.getDeductionType())
                .eq(StringUtils.isNotEmpty(tDeductionOrder.getRemark()), "t1.remark", tDeductionOrder.getRemark());
        List<UnusualOrderVO> list = baseMapper.queryUnusualOrderVOCondition(qw);
        return new ImmutablePair<>(list, new PageInfo(list).getTotal());
    }

    @Override
    public boolean add(TDeductionOrderVO tDeductionOrderVO) {
        TDeductionOrder tDeductionOrder = new TDeductionOrder();
        BeanUtils.copyBeanProp(tDeductionOrder, tDeductionOrderVO);
        tDeductionOrder.setCreateBy(SecurityUtils.getUsername());
        tDeductionOrder.setCreateTime(LocalDateTime.now());
        return save(tDeductionOrder);
    }

    @Override
    public boolean editById(TDeductionOrderParam tDeductionOrderParam) {
        TDeductionOrder tDeductionOrder = new TDeductionOrder();
        BeanUtils.copyBeanProp(tDeductionOrder, tDeductionOrderParam);
        tDeductionOrder.setCreateBy(SecurityUtils.getUsername());
        tDeductionOrder.setCreateTime(LocalDateTime.now());
        return updateById(tDeductionOrder);
    }
}
