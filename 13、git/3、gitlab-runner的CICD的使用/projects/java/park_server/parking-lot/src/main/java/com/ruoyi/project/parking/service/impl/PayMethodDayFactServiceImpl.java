package com.ruoyi.project.parking.service.impl;

import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import com.ruoyi.project.parking.domain.PayMethodDayFact;
import com.ruoyi.project.parking.domain.vo.PayStatisticFactVO;
import com.ruoyi.project.parking.mapper.HomePageMapper;
import com.ruoyi.project.parking.mapper.PayMethodDayFactMapper;
import com.ruoyi.project.parking.service.IPayMethodDayFactService;
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
 * 首页付费方式分析事实Service业务层处理
 *
 * @author fangch
 * @date 2023-03-20
 */
@Slf4j
@Service
public class PayMethodDayFactServiceImpl implements IPayMethodDayFactService
{
    @Autowired
    private PayMethodDayFactMapper payMethodDayFactMapper;

    @Autowired
    private HomePageMapper homePageMapper;

    @GrpcClient("parking-order-server")
    private ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub parkingOrderServiceBlockingStub;

    /**
     * 查询首页付费方式分析事实
     *
     * @param id 首页付费方式分析事实主键
     * @return 首页付费方式分析事实
     */
    @Override
    public PayMethodDayFact selectPayMethodDayFactById(Integer id)
    {
        return payMethodDayFactMapper.selectPayMethodDayFactById(id);
    }

    /**
     * 查询首页付费方式分析事实列表
     *
     * @param payMethodDayFact 首页付费方式分析事实
     * @return 首页付费方式分析事实
     */
    @Override
    public List<PayMethodDayFact> selectPayMethodDayFactList(PayMethodDayFact payMethodDayFact)
    {
        return payMethodDayFactMapper.selectPayMethodDayFactList(payMethodDayFact);
    }

    /**
     * 新增首页付费方式分析事实
     *
     * @param payMethodDayFact 首页付费方式分析事实
     * @return 结果
     */
    @Override
    public int insertPayMethodDayFact(PayMethodDayFact payMethodDayFact)
    {
        payMethodDayFact.setCreateTime(LocalDateTime.now());
        return payMethodDayFactMapper.insertPayMethodDayFact(payMethodDayFact);
    }

    /**
     * 修改首页付费方式分析事实
     *
     * @param payMethodDayFact 首页付费方式分析事实
     * @return 结果
     */
    @Override
    public int updatePayMethodDayFact(PayMethodDayFact payMethodDayFact)
    {
        payMethodDayFact.setUpdateTime(LocalDateTime.now());
        return payMethodDayFactMapper.updatePayMethodDayFact(payMethodDayFact);
    }

    /**
     * 批量删除首页付费方式分析事实
     *
     * @param ids 需要删除的首页付费方式分析事实主键
     * @return 结果
     */
    @Override
    public int deletePayMethodDayFactByIds(Integer[] ids)
    {
        return payMethodDayFactMapper.deletePayMethodDayFactByIds(ids);
    }

    /**
     * 删除首页付费方式分析事实信息
     *
     * @param id 首页付费方式分析事实主键
     * @return 结果
     */
    @Override
    public int deletePayMethodDayFactById(Integer id)
    {
        return payMethodDayFactMapper.deletePayMethodDayFactById(id);
    }

    /**
     * 分析首页付费方式事实
     */
    @Override
    public void analysePayMethodDayFact(String userId) {
        try {
            // 查询所有车场id和编号
            List<SysDept> parks = homePageMapper.getAllParkIds();
            for (SysDept park : parks) {
                // 查询所有归属子车场编号
                List<String> parkNos = homePageMapper.getChildParkNos(park.getDeptId());
                String day = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                ParkingOrder.AnalysePayMethodDayFactRequest request = ParkingOrder.AnalysePayMethodDayFactRequest.newBuilder()
                        .setDay(day)
                        .addAllParkNos(parkNos)
                        .build();
                ParkingOrder.AnalysePayMethodDayFactResponse response = parkingOrderServiceBlockingStub.analysePayMethodDayFact(request);
                List<ParkingOrder.AnalysePayMethodDayFactProto> methodDayFactProtoList = response.getRowsList();
                methodDayFactProtoList.forEach(item -> {
                    try {
                        // 订单模块分析远程调用返参转pojo
                        PayMethodDayFact payMethodDayFact = ProtoJsonUtil.toPojoBean(PayMethodDayFact.class, item);
                        payMethodDayFact.setParkNo(park.getParkNo());
                        payMethodDayFact.setDay(day);
                        // 检查同维度的分析数据是否已存在，存在更新，不存在新增
                        PayMethodDayFact existFact = payMethodDayFactMapper.selectPayMethodDayFact(payMethodDayFact);
                        if (null == existFact) {
                            payMethodDayFact.setCreateBy(userId);
                            payMethodDayFact.setCreateTime(LocalDateTime.now());
                            payMethodDayFactMapper.insertPayMethodDayFact(payMethodDayFact);
                            // 更新一下昨天的统计，防止丢数据
                            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            ParkingOrder.AnalysePayMethodDayFactResponse yesterdayRes = parkingOrderServiceBlockingStub.analysePayMethodDayFact(ParkingOrder.AnalysePayMethodDayFactRequest.newBuilder()
                                    .setDay(yesterday)
                                    .addAllParkNos(parkNos)
                                    .build());
                            List<ParkingOrder.AnalysePayMethodDayFactProto> yesterdayList = yesterdayRes.getRowsList();
                            yesterdayList.forEach(yesterdayItem -> {
                                try {
                                    // 订单模块分析远程调用返参转pojo
                                    PayMethodDayFact yesterdayFact = ProtoJsonUtil.toPojoBean(PayMethodDayFact.class, yesterdayItem);
                                    yesterdayFact.setParkNo(park.getParkNo());
                                    yesterdayFact.setDay(yesterday);
                                    // 检查同维度的分析数据是否已存在，存在更新，不存在新增
                                    PayMethodDayFact existYesterdayFact = payMethodDayFactMapper.selectPayMethodDayFact(yesterdayFact);
                                    if (null != existYesterdayFact) {
                                        existYesterdayFact.setAmount(yesterdayFact.getAmount());
                                        existYesterdayFact.setRatio(yesterdayFact.getRatio());
                                        existYesterdayFact.setUpdateBy(userId);
                                        existYesterdayFact.setUpdateTime(LocalDateTime.now());
                                        payMethodDayFactMapper.updatePayMethodDayFact(existYesterdayFact);
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        } else {
                            existFact.setAmount(payMethodDayFact.getAmount());
                            existFact.setRatio(payMethodDayFact.getRatio());
                            existFact.setUpdateBy(userId);
                            existFact.setUpdateTime(LocalDateTime.now());
                            payMethodDayFactMapper.updatePayMethodDayFact(existFact);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (StatusRuntimeException e) {
            log.error( "analysePayMethodDayFact 1 FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error( "analysePayMethodDayFact 2 FAILED with " + e.getMessage());
        }
    }

    /**
     * 查询首页付费方式分析事实
     *
     * @param payMethodDayFact 首页付费方式分析事实
     * @return 首页付费方式分析事实集合
     */
    @Override
    public List<PayStatisticFactVO> getPayMethodDayFact(PayMethodDayFact payMethodDayFact) {
        return payMethodDayFactMapper.getPayMethodDayFact(payMethodDayFact);
    }
}
