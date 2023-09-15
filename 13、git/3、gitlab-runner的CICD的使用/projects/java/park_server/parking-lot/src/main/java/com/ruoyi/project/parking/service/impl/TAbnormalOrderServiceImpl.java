package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.parking.domain.TExitRecords;
import com.ruoyi.project.parking.domain.bo.AbnormalOrderStatisticsBO;
import com.ruoyi.project.parking.domain.param.TAbnormalOrderParam;
import com.ruoyi.project.parking.domain.vo.StatisticsSheetVO;
import com.ruoyi.project.parking.domain.vo.TAbnormalOrderVO;
import com.ruoyi.project.parking.domain.vo.UnusualOrderVO;
import com.ruoyi.project.parking.entity.TAbnormalOrder;
import com.ruoyi.project.parking.mapper.TAbnormalOrderMapper;
import com.ruoyi.project.parking.service.ITAbnormalOrderService;
import com.ruoyi.project.parking.service.ITExitRecordsService;
import com.ruoyi.project.parking.service.ParkingOrderGrpcServiceImpl;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 异常订单记录 服务实现类
 * </p>
 *
 * @author yinwen
 * @since 2023-03-10
 */
@Service
public class TAbnormalOrderServiceImpl extends ServiceImpl<TAbnormalOrderMapper, TAbnormalOrder> implements ITAbnormalOrderService {

    @Autowired
    ITExitRecordsService exitRecordsService;

    @Autowired
    ParkingOrderGrpcServiceImpl parkingOrderGrpcService;

    @Override
    public List<TAbnormalOrder> listCondition(TAbnormalOrder tAbnormalOrder) {
        LambdaQueryWrapper<TAbnormalOrder> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(tAbnormalOrder.getParkNo()), TAbnormalOrder::getParkNo, tAbnormalOrder.getParkNo())
                .eq(tAbnormalOrder.getOrderType() != null, TAbnormalOrder::getOrderType, tAbnormalOrder.getOrderType())
                .eq(StringUtils.isNotEmpty(tAbnormalOrder.getOrderNo()), TAbnormalOrder::getOrderNo, tAbnormalOrder.getOrderNo())
                .eq(tAbnormalOrder.getAbnormalType() != null, TAbnormalOrder::getAbnormalType, tAbnormalOrder.getAbnormalType())
                .eq(StringUtils.isNotEmpty(tAbnormalOrder.getRemark()), TAbnormalOrder::getRemark, tAbnormalOrder.getRemark());
        return list(qw);
    }

    @Override
    public Pair<List<UnusualOrderVO>, Long> queryUnusualOrderVOCondition(TAbnormalOrder tAbnormalOrder) {
        QueryWrapper<TAbnormalOrder> qw = new QueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(tAbnormalOrder.getParkNo()), "t1.park_no", tAbnormalOrder.getParkNo())
                .eq(tAbnormalOrder.getOrderType() != null, "t1.order_type", tAbnormalOrder.getOrderType())
                .eq(StringUtils.isNotEmpty(tAbnormalOrder.getOrderNo()), "t1.order_no", tAbnormalOrder.getOrderNo())
                .eq(tAbnormalOrder.getAbnormalType() != null, "t1.abnormal_type", tAbnormalOrder.getAbnormalType())
                .eq(StringUtils.isNotEmpty(tAbnormalOrder.getRemark()), "t1.remark", tAbnormalOrder.getRemark())
                .like(StringUtils.isNotEmpty(tAbnormalOrder.getCarNumber()), "t1.car_number", tAbnormalOrder.getCarNumber());
        List<UnusualOrderVO> list = baseMapper.queryUnusualOrderVOCondition(qw);
        return new ImmutablePair<>(list, new PageInfo(list).getTotal());
    }

    @Override
    public boolean add(TAbnormalOrderVO tAbnormalOrderVO) {
        TAbnormalOrder tAbnormalOrder = new TAbnormalOrder();
        BeanUtils.copyBeanProp(tAbnormalOrder, tAbnormalOrderVO);
        tAbnormalOrder.setCreateBy(SecurityUtils.getUsername());
        tAbnormalOrder.setCreateTime(LocalDateTime.now());
        return save(tAbnormalOrder);
    }

    @Override
    public boolean editById(TAbnormalOrderParam tAbnormalOrderParam) {
        TAbnormalOrder tAbnormalOrder = new TAbnormalOrder();
        BeanUtils.copyBeanProp(tAbnormalOrder, tAbnormalOrderParam);
        tAbnormalOrder.setCreateBy(SecurityUtils.getUsername());
        tAbnormalOrder.setCreateTime(LocalDateTime.now());
        return updateById(tAbnormalOrder);
    }

    @Override
    public AbnormalOrderStatisticsBO statistics(StatisticsSheetVO statisticsSheetVO) {
        AbnormalOrderStatisticsBO abnormalOrderStatisticsBO = null;
        abnormalOrderStatisticsBO = parkingOrderGrpcService.abnormalStatistics(statisticsSheetVO);
        //获取离场记录数
        long exitNum = exitRecordsService.count(new LambdaQueryWrapper<TExitRecords>()
                .eq(TExitRecords::getParkNo, statisticsSheetVO.getParkNo())
                .ge(TExitRecords::getCreateTime, statisticsSheetVO.getStartTime())
                .le(TExitRecords::getCreateTime, statisticsSheetVO.getEndTime()));
        abnormalOrderStatisticsBO.setExitNum((int) exitNum);
        return abnormalOrderStatisticsBO;
    }
}
