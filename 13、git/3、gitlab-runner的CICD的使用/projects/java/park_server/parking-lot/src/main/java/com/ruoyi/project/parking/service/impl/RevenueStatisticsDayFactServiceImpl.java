package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.grpc.lib.merchant.CouponProviderServiceGrpc;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import com.ruoyi.project.parking.domain.RevenueStatisticsDayFact;
import com.ruoyi.project.parking.domain.vo.RevenueStatisticsVO;
import com.ruoyi.project.parking.mapper.HomePageMapper;
import com.ruoyi.project.parking.mapper.RevenueStatisticsDayFactMapper;
import com.ruoyi.project.parking.service.IRevenueStatisticsDayFactService;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import com.ruoyi.project.system.domain.SysDept;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 首页收入统计事实Service业务层处理
 *
 * @author fangch
 * @date 2023-03-28
 */
@Slf4j
@Service
public class RevenueStatisticsDayFactServiceImpl extends ServiceImpl<RevenueStatisticsDayFactMapper, RevenueStatisticsDayFact> implements IRevenueStatisticsDayFactService {
    @Autowired
    private RevenueStatisticsDayFactMapper revenueStatisticsDayFactMapper;

    @Autowired
    private HomePageMapper homePageMapper;

    @GrpcClient("parking-order-server")
    private ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub parkingOrderServiceBlockingStub;

    @GrpcClient("parking-member-merchant-server")
    private CouponProviderServiceGrpc.CouponProviderServiceBlockingStub couponProviderServiceBlockingStub;

    /**
     * 查询首页收入统计事实
     *
     * @param id 首页收入统计事实主键
     * @return 首页收入统计事实
     */
    @Override
    public RevenueStatisticsDayFact selectRevenueStatisticsDayFactById(Integer id) {
        return revenueStatisticsDayFactMapper.selectRevenueStatisticsDayFactById(id);
    }

    /**
     * 查询首页收入统计事实列表
     *
     * @param revenueStatisticsDayFact 首页收入统计事实
     * @return 首页收入统计事实
     */
    @Override
    public List<RevenueStatisticsDayFact> selectRevenueStatisticsDayFactList(RevenueStatisticsDayFact revenueStatisticsDayFact) {
        return revenueStatisticsDayFactMapper.selectRevenueStatisticsDayFactList(revenueStatisticsDayFact);
    }

    /**
     * 新增首页收入统计事实
     *
     * @param revenueStatisticsDayFact 首页收入统计事实
     * @return 结果
     */
    @Override
    public int insertRevenueStatisticsDayFact(RevenueStatisticsDayFact revenueStatisticsDayFact) {
        revenueStatisticsDayFact.setCreateTime(LocalDateTime.now());
        return revenueStatisticsDayFactMapper.insertRevenueStatisticsDayFact(revenueStatisticsDayFact);
    }

    /**
     * 修改首页收入统计事实
     *
     * @param revenueStatisticsDayFact 首页收入统计事实
     * @return 结果
     */
    @Override
    public int updateRevenueStatisticsDayFact(RevenueStatisticsDayFact revenueStatisticsDayFact) {
        revenueStatisticsDayFact.setUpdateTime(LocalDateTime.now());
        return revenueStatisticsDayFactMapper.updateRevenueStatisticsDayFact(revenueStatisticsDayFact);
    }

    /**
     * 批量删除首页收入统计事实
     *
     * @param ids 需要删除的首页收入统计事实主键
     * @return 结果
     */
    @Override
    public int deleteRevenueStatisticsDayFactByIds(Integer[] ids) {
        return revenueStatisticsDayFactMapper.deleteRevenueStatisticsDayFactByIds(ids);
    }

    /**
     * 删除首页收入统计事实信息
     *
     * @param id 首页收入统计事实主键
     * @return 结果
     */
    @Override
    public int deleteRevenueStatisticsDayFactById(Integer id) {
        return revenueStatisticsDayFactMapper.deleteRevenueStatisticsDayFactById(id);
    }

    /**
     * 分析首页收入统计事实
     */
    @Override
    public void analyseRevenueStatisticsDayFact(String userId) {
        try {
            // 查询所有车场id和编号
            List<SysDept> parks = homePageMapper.getAllParkIds();
            for (SysDept park : parks) {
                LocalDate now = LocalDate.now();
                // 查询所有归属子车场编号
                List<String> parkNos = homePageMapper.getChildParkNos(park.getDeptId());
                // 获取商家券
                String day = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                // 分析首页收入统计事实
                ParkingOrder.AnalyseRevenueStatisticsRequest request = ParkingOrder.AnalyseRevenueStatisticsRequest.newBuilder()
                        .setDay(day)
                        .addAllParkNos(parkNos)
                        .build();
                ParkingOrder.AnalyseRevenueStatisticsResponse response = parkingOrderServiceBlockingStub.analyseRevenueStatisticsDayFact(request);
                //当日订单数据
                ParkingOrder.AnalyseAmountAnalysisDayFactResponse amountResponse = parkingOrderServiceBlockingStub.analyseAmountAnalysisDayFact(ParkingOrder.AnalyseAmountAnalysisDayFactRequest.newBuilder()
                        .addAllParkNos(parkNos)
                        .setDay(day)
                        .build());
                // grpc请求会员子系统获取优惠券总数
//                CouponProvider.AnalyseMerchantVoucherRequest request1 = CouponProvider.AnalyseMerchantVoucherRequest.newBuilder()
//                        .setDay(day)
//                        .addAllParkNos(parkNos)
//                        .build();
//                CouponProvider.AnalyseMerchantVoucherResponse response1 = couponProviderServiceBlockingStub.analyseMerchantVoucher(request1);
                try {
                    // 订单模块分析远程调用返参转pojo
                    RevenueStatisticsDayFact revenueStatisticsDayFact = ProtoJsonUtil.toPojoBean(RevenueStatisticsDayFact.class, response);
                    revenueStatisticsDayFact.setParkNo(park.getParkNo());
                    revenueStatisticsDayFact.setDay(day);
                    revenueStatisticsDayFact.setMerchantVoucher(0);
                    // 检查同维度的分析数据是否已存在，存在更新，不存在新增
                    RevenueStatisticsDayFact existFact = revenueStatisticsDayFactMapper.selectRevenueStatisticsDayFact(revenueStatisticsDayFact);
                    if (null == existFact) {
                        updateByResponse(revenueStatisticsDayFact, amountResponse);
                        revenueStatisticsDayFact.setCreateBy(userId);
                        revenueStatisticsDayFact.setCreateTime(LocalDateTime.now());
                        revenueStatisticsDayFactMapper.insertRevenueStatisticsDayFact(revenueStatisticsDayFact);
                        //不存在数据则说明是当日第一次分析，需要更新前一天数据，不然前一天数据会有空档未统计分析
                        String yesterdayStr = now.minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
                        RevenueStatisticsDayFact yesterdayResult = revenueStatisticsDayFactMapper.selectOne(new LambdaQueryWrapper<RevenueStatisticsDayFact>()
                                .eq(RevenueStatisticsDayFact::getDay, yesterdayStr)
                                .eq(RevenueStatisticsDayFact::getParkNo, park.getParkNo())
                                .last("limit 1"));
                        ParkingOrder.AnalyseAmountAnalysisDayFactResponse amountYesterdayResponse = parkingOrderServiceBlockingStub.analyseAmountAnalysisDayFact(ParkingOrder.AnalyseAmountAnalysisDayFactRequest.newBuilder()
                                .addAllParkNos(parkNos)
                                .setDay(yesterdayStr)
                                .build());
                        // 更新一下昨天的统计，防止丢数据
                        String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        ParkingOrder.AnalyseRevenueStatisticsResponse yesterdayRes = parkingOrderServiceBlockingStub.analyseRevenueStatisticsDayFact(ParkingOrder.AnalyseRevenueStatisticsRequest.newBuilder()
                                .setDay(yesterday)
                                .addAllParkNos(parkNos)
                                .build());
                        // 订单模块分析远程调用返参转pojo
                        RevenueStatisticsDayFact yesterdayFact = ProtoJsonUtil.toPojoBean(RevenueStatisticsDayFact.class, yesterdayRes);
                        yesterdayFact.setParkNo(park.getParkNo());
                        yesterdayFact.setDay(yesterday);
                        yesterdayFact.setMerchantVoucher(0);
                        if (null != yesterdayFact) {
                            if (null != yesterdayResult) {
                                yesterdayResult.setOnlinePayableAmount(yesterdayFact.getOnlinePayableAmount());
                                yesterdayResult.setOnlinePayAmount(yesterdayFact.getOnlinePayAmount());
                                yesterdayResult.setCashPayableAmount(yesterdayFact.getCashPayableAmount());
                                yesterdayResult.setCashPayAmount(yesterdayFact.getCashPayAmount());
                                yesterdayResult.setFreePayableAmount(yesterdayFact.getFreePayableAmount());
                                yesterdayResult.setFreePayAmount(yesterdayFact.getFreePayAmount());
                                yesterdayResult.setConcession(yesterdayFact.getConcession());
                                yesterdayResult.setMerchantVoucher(yesterdayFact.getMerchantVoucher());
                                yesterdayResult.setTransactionNumber(yesterdayFact.getTransactionNumber());
                                yesterdayResult.setUpdateBy(userId);
                                yesterdayResult.setUpdateTime(LocalDateTime.now());
                                yesterdayResult.setUpdateBy(userId);
                                yesterdayResult.setUpdateTime(LocalDateTime.now());

                                updateByResponse(yesterdayResult, amountYesterdayResponse);
                                updateById(yesterdayResult);
                            }
                        }
                    } else {
                        existFact.setOnlinePayableAmount(revenueStatisticsDayFact.getOnlinePayableAmount());
                        existFact.setOnlinePayAmount(revenueStatisticsDayFact.getOnlinePayAmount());
                        existFact.setCashPayableAmount(revenueStatisticsDayFact.getCashPayableAmount());
                        existFact.setCashPayAmount(revenueStatisticsDayFact.getCashPayAmount());
                        existFact.setFreePayableAmount(revenueStatisticsDayFact.getFreePayableAmount());
                        existFact.setFreePayAmount(revenueStatisticsDayFact.getFreePayAmount());
                        existFact.setConcession(revenueStatisticsDayFact.getConcession());
                        existFact.setMerchantVoucher(revenueStatisticsDayFact.getMerchantVoucher());
                        existFact.setTransactionNumber(revenueStatisticsDayFact.getTransactionNumber());
                        existFact.setUpdateBy(userId);
                        existFact.setUpdateTime(LocalDateTime.now());
                        updateByResponse(existFact, amountResponse);
                        revenueStatisticsDayFactMapper.updateRevenueStatisticsDayFact(existFact);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (StatusRuntimeException e) {
            log.error("analyseRevenueStatisticsDayFact 1 FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("analyseRevenueStatisticsDayFact 2 FAILED with " + e.getMessage());
        }
    }

    /**
     * 查询大屏收入统计
     *
     * @param revenueStatisticsDayFact
     * @return
     */
    @Override
    public Map<String, Object> getRevenueStatistics(RevenueStatisticsDayFact revenueStatisticsDayFact) {
        Map<String, Object> result = new HashMap<>();
        LocalDate now = LocalDate.now();
        String day = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String yesterday = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        RevenueStatisticsVO first = revenueStatisticsDayFactMapper.getRevenueStatisticsFirst(revenueStatisticsDayFact.getParkNo(), now.getYear(), now.getMonthValue(), yesterday, day);
        result.put("first", first);
        result.put("日", revenueStatisticsDayFactMapper.getRevenueStatisticsSecond(revenueStatisticsDayFact.getParkNo(), null, null, null, day));
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        result.put("周", revenueStatisticsDayFactMapper.getRevenueStatisticsSecond(revenueStatisticsDayFact.getParkNo(), now.getYear(), null, now.get(weekFields.weekOfWeekBasedYear()), null));
        result.put("月", revenueStatisticsDayFactMapper.getRevenueStatisticsSecond(revenueStatisticsDayFact.getParkNo(), now.getYear(), now.getMonthValue(), null, null));
        return result;
    }

    @Override
    public RevenueStatisticsDayFact sumDisConsumptionAmountOfDay(LocalDate now, String parkNo) {
        return revenueStatisticsDayFactMapper.sumDisConsumptionAmountOfDay(now, parkNo);
    }


    /**
     * 查询本年收入
     */
    @Override
    public BigDecimal sumAmountOfYear(LocalDate startOfYear, LocalDate endOfYear, List<String> parkNos) {
        return revenueStatisticsDayFactMapper.sumAmountOfYear(startOfYear, endOfYear, parkNos);
    }

    /**
     * 查询当日收入
     */
    @Override
    public BigDecimal sumAmountOfDay(LocalDate now, List<String> parkNos) {
        return revenueStatisticsDayFactMapper.sumAmountOfDay(now, parkNos);
    }

    /**
     * 根据订单系统返回结果，更新首页收入事实
     */
    private void updateByResponse(RevenueStatisticsDayFact amountAnalysisDayFact, ParkingOrder.AnalyseAmountAnalysisDayFactResponse response) {
        //线上收入需要grpc调用订单系统，获取支付时间在本日的已支付订单总实付金额
        amountAnalysisDayFact.setOnlineIncome(BigDecimal.valueOf(response.getOnlineIncome()));
        //todo 线上支出不知道是哪一部分数据，先设为0
        amountAnalysisDayFact.setOnlineOutgo(BigDecimal.ZERO);
        //线上结算为线上收入-线上支出
        amountAnalysisDayFact.setOnlineAmount(amountAnalysisDayFact.getOnlineIncome().subtract(amountAnalysisDayFact.getOnlineOutgo()));
        //todo 现金收入估计要页面上手输存放在一张表中，目前先设为0
        amountAnalysisDayFact.setCashIncome(BigDecimal.ZERO);
        //减免金额需要grpc调用订单系统，获取支付时间在本日的已支付订单总优惠金额
        amountAnalysisDayFact.setDeductionAmount(BigDecimal.valueOf(response.getDeductionAmount()));
        //当日结算为线上结算+现金结算
        amountAnalysisDayFact.setDayAmount(amountAnalysisDayFact.getOnlineAmount().add(amountAnalysisDayFact.getCashIncome()));
    }
}
