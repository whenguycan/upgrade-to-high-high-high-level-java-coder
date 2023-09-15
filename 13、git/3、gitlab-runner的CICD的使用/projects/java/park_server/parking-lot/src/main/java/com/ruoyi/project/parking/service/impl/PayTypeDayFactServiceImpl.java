package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import com.ruoyi.project.parking.domain.PayTypeDayFact;
import com.ruoyi.project.parking.domain.vo.PayStatisticFactVO;
import com.ruoyi.project.parking.domain.vo.StatisticsSheetVO;
import com.ruoyi.project.parking.mapper.HomePageMapper;
import com.ruoyi.project.parking.mapper.PayTypeDayFactMapper;
import com.ruoyi.project.parking.service.IPayTypeDayFactService;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import com.ruoyi.project.system.domain.SysDept;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author 琴声何来
 * @description 针对表【pay_type_day_fact(首页付费类型分析事实表)】的数据库操作Service实现
 * @since 2023-04-18 11:03:11
 */
@Slf4j
@Service
public class PayTypeDayFactServiceImpl extends ServiceImpl<PayTypeDayFactMapper, PayTypeDayFact>
        implements IPayTypeDayFactService {

    @Autowired
    private HomePageMapper homePageMapper;

    @GrpcClient("parking-order-server")
    private ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub parkingOrderServiceBlockingStub;

    /**
     * 查询首页付费类型分析事实
     *
     * @param id 首页付费类型分析事实主键
     * @return 首页付费类型分析事实
     */
    @Override
    public PayTypeDayFact selectPayTypeDayFactById(Integer id) {
        return baseMapper.selectPayTypeDayFactById(id);
    }

    /**
     * 查询首页付费类型分析事实列表
     *
     * @param payTypeDayFact 首页付费类型分析事实
     * @return 首页付费类型分析事实
     */
    @Override
    public List<PayTypeDayFact> selectPayTypeDayFactList(PayTypeDayFact payTypeDayFact) {
        return baseMapper.selectPayTypeDayFactList(payTypeDayFact);
    }

    /**
     * 新增首页付费类型分析事实
     *
     * @param payTypeDayFact 首页付费类型分析事实
     * @return 结果
     */
    @Override
    public int insertPayTypeDayFact(PayTypeDayFact payTypeDayFact) {
        payTypeDayFact.setCreateTime(LocalDateTime.now());
        return baseMapper.insertPayTypeDayFact(payTypeDayFact);
    }

    /**
     * 修改首页付费类型分析事实
     *
     * @param payTypeDayFact 首页付费类型分析事实
     * @return 结果
     */
    @Override
    public int updatePayTypeDayFact(PayTypeDayFact payTypeDayFact) {
        payTypeDayFact.setUpdateTime(LocalDateTime.now());
        return baseMapper.updatePayTypeDayFact(payTypeDayFact);
    }

    /**
     * 批量删除首页付费类型分析事实
     *
     * @param ids 需要删除的首页付费类型分析事实主键
     * @return 结果
     */
    @Override
    public int deletePayTypeDayFactByIds(Integer[] ids) {
        return baseMapper.deletePayTypeDayFactByIds(ids);
    }

    /**
     * 删除首页付费类型分析事实信息
     *
     * @param id 首页付费类型分析事实主键
     * @return 结果
     */
    @Override
    public int deletePayTypeDayFactById(Integer id) {
        return baseMapper.deletePayTypeDayFactById(id);
    }

    /**
     * 分析首页付费类型事实
     */
    @Override
    public void analysePayTypeDayFact(String userId) {
        try {
            // 查询所有车场id和编号
            List<SysDept> parks = homePageMapper.getAllParkIds();
            for (SysDept park : parks) {
                // 查询所有归属子车场编号
                List<String> parkNos = homePageMapper.getChildParkNos(park.getDeptId());
                // 分析首页付费类型事实
                String day = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                ParkingOrder.AnalysePayTypeDayFactRequest request = ParkingOrder.AnalysePayTypeDayFactRequest.newBuilder()
                        .setDay(day)
                        .addAllParkNos(parkNos)
                        .build();
                ParkingOrder.AnalysePayTypeDayFactResponse response = parkingOrderServiceBlockingStub.analysePayTypeDayFact(request);
                List<ParkingOrder.AnalysePayTypeDayFactProto> typeDayFactProtoList = response.getRowsList();
                typeDayFactProtoList.forEach(item -> {
                    try {
                        // 订单模块分析远程调用返参转pojo
                        PayTypeDayFact payTypeDayFact = ProtoJsonUtil.toPojoBean(PayTypeDayFact.class, item);
                        payTypeDayFact.setParkNo(park.getParkNo());
                        payTypeDayFact.setDay(day);
                        // 检查同维度的分析数据是否已存在，存在更新，不存在新增
                        PayTypeDayFact existFact = baseMapper.selectPayTypeDayFact(payTypeDayFact);
                        if (null == existFact) {
                            payTypeDayFact.setCreateBy(userId);
                            payTypeDayFact.setCreateTime(LocalDateTime.now());
                            baseMapper.insertPayTypeDayFact(payTypeDayFact);
                            // 更新一下昨天的统计，防止丢数据
                            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            ParkingOrder.AnalysePayTypeDayFactResponse yesterdayRes = parkingOrderServiceBlockingStub.analysePayTypeDayFact(ParkingOrder.AnalysePayTypeDayFactRequest.newBuilder()
                                    .setDay(yesterday)
                                    .addAllParkNos(parkNos)
                                    .build());
                            List<ParkingOrder.AnalysePayTypeDayFactProto> yesterdayList = yesterdayRes.getRowsList();
                            yesterdayList.forEach(yesterdayItem -> {
                                try {
                                    // 订单模块分析远程调用返参转pojo
                                    PayTypeDayFact yesterdayFact = ProtoJsonUtil.toPojoBean(PayTypeDayFact.class, yesterdayItem);
                                    yesterdayFact.setParkNo(park.getParkNo());
                                    yesterdayFact.setDay(yesterday);
                                    // 检查同维度的分析数据是否已存在，存在更新，不存在新增
                                    PayTypeDayFact existYesterdayFact = baseMapper.selectPayTypeDayFact(yesterdayFact);
                                    if (null != existYesterdayFact) {
                                        existYesterdayFact.setAmount(yesterdayFact.getAmount());
                                        existYesterdayFact.setRatio(yesterdayFact.getRatio());
                                        existYesterdayFact.setUpdateBy(userId);
                                        existYesterdayFact.setUpdateTime(LocalDateTime.now());
                                        baseMapper.updatePayTypeDayFact(existYesterdayFact);
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        } else {
                            existFact.setAmount(payTypeDayFact.getAmount());
                            existFact.setRatio(payTypeDayFact.getRatio());
                            existFact.setUpdateBy(userId);
                            existFact.setUpdateTime(LocalDateTime.now());
                            baseMapper.updatePayTypeDayFact(existFact);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (StatusRuntimeException e) {
            log.error("analysePayTypeDayFact 1 FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("analysePayTypeDayFact 2 FAILED with " + e.getMessage());
        }
    }

    /**
     * 查询首页付费类型分析事实
     *
     * @param payTypeDayFact 首页付费类型分析事实
     * @return 首页付费类型分析事实集合
     */
    @Override
    public List<PayStatisticFactVO> getPayTypeDayFact(PayTypeDayFact payTypeDayFact) {
        return baseMapper.getPayTypeDayFact(payTypeDayFact);
    }

    /**
     * @param statisticsSheetVO 查询条件
     * @return java.util.List<com.ruoyi.project.parking.domain.bo.PayTypeStatisticsSheetBO>
     * @apiNote 报表 预支付和岗亭支付统计
     * @author 琴声何来
     * @since 2023/4/18 10:53
     */
    @Override
    public List<PayTypeDayFact> getPayTypeStatisticsSheet(StatisticsSheetVO statisticsSheetVO) {
        return baseMapper.getPayTypeStatisticsSheet(statisticsSheetVO);
    }
}
