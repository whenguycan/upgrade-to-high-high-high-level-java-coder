package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.parking.domain.param.TExemptOrderParam;
import com.ruoyi.project.parking.domain.vo.TExemptOrderVO;
import com.ruoyi.project.parking.domain.vo.UnusualOrderVO;
import com.ruoyi.project.parking.entity.TExemptOrder;
import com.ruoyi.project.parking.mapper.TExemptOrderMapper;
import com.ruoyi.project.parking.service.ITExemptOrderService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 减免订单记录 服务实现类
 * </p>
 *
 * @author yinwen
 * @since 2023-03-10
 */
@Service
public class TExemptOrderServiceImpl extends ServiceImpl<TExemptOrderMapper, TExemptOrder> implements ITExemptOrderService {

    @Override
    public List<TExemptOrder> listCondition(TExemptOrder tExemptOrder) {
        LambdaQueryWrapper<TExemptOrder> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(tExemptOrder.getParkNo()), TExemptOrder::getParkNo, tExemptOrder.getParkNo())
                .eq(tExemptOrder.getOrderType() != null, TExemptOrder::getOrderType, tExemptOrder.getOrderType())
                .eq(StringUtils.isNotEmpty(tExemptOrder.getOrderNo()), TExemptOrder::getOrderNo, tExemptOrder.getOrderNo())
                .eq(tExemptOrder.getExemptType() != null, TExemptOrder::getExemptType, tExemptOrder.getExemptType())
                .eq(StringUtils.isNotEmpty(tExemptOrder.getRemark()), TExemptOrder::getRemark, tExemptOrder.getRemark());
        return list(qw);
    }

    @Override
    public Pair<List<UnusualOrderVO>, Long> queryUnusualOrderVOCondition(TExemptOrder tExemptOrder) {
        QueryWrapper<TExemptOrder> qw = new QueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(tExemptOrder.getParkNo()), "t1.park_no", tExemptOrder.getParkNo())
                .eq(tExemptOrder.getOrderType() != null, "t1.order_type", tExemptOrder.getOrderType())
                .eq(StringUtils.isNotEmpty(tExemptOrder.getOrderNo()), "t1.order_no", tExemptOrder.getOrderNo())
                .eq(tExemptOrder.getExemptType() != null, "t1.exempt_type", tExemptOrder.getExemptType())
                .eq(StringUtils.isNotEmpty(tExemptOrder.getRemark()), "t1.remark", tExemptOrder.getRemark());
        List<UnusualOrderVO> list = baseMapper.queryUnusualOrderVOCondition(qw);
        return new ImmutablePair<>(list, new PageInfo(list).getTotal());
    }

    @Override
    public boolean add(TExemptOrderVO tExemptOrderVO) {
        TExemptOrder tExemptOrder = new TExemptOrder();
        BeanUtils.copyBeanProp(tExemptOrder, tExemptOrderVO);
        tExemptOrder.setCreateBy(SecurityUtils.getUsername());
        tExemptOrder.setCreateTime(LocalDateTime.now());
        return save(tExemptOrder);
    }

    @Override
    public boolean editById(TExemptOrderParam tExemptOrderParam) {
        TExemptOrder tExemptOrder = new TExemptOrder();
        BeanUtils.copyBeanProp(tExemptOrder, tExemptOrderParam);
        tExemptOrder.setCreateBy(SecurityUtils.getUsername());
        tExemptOrder.setCreateTime(LocalDateTime.now());
        return updateById(tExemptOrder);
    }
}
