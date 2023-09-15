package com.czdx.parkingorder.project.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czdx.parkingorder.project.entity.ParkingOrderEntity;
import com.czdx.parkingorder.project.vo.*;
import com.google.protobuf.ProtocolStringList;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: tangwei
 * @Date: 2023/2/27 10:29 AM
 * @Description: 接口描述信息
 */
@Mapper
public interface ParkingOrderDao extends BaseMapper<ParkingOrderEntity> {

    StatisticIncomeVo statisticIncome(String day, List<String> parkNos);

    List<PayTypeDayFactVO> analysePayTypeDayFact(String day, List<String> parkNos);

    List<PayMethodDayFactVO> analysePayMethodDayFact(String day, List<String> parkNos);

    List<OrderSituationDayFactVO> analyseOrderSituationDayFact(String day, List<String> parkNos);

    /**
     * @apiNote 查询指定日期指定车场商户订单总实付金额
     */
    BigDecimal sumOnlineIncome(ProtocolStringList parkNos, String day);

    /**
     * @apiNote 查询指定日期指定车场商户订单总优惠金额
     */
    BigDecimal sumOnlineDeduction(ProtocolStringList parkNos, String day);

    RevenueStatisticsDayFactVO analyseRevenueStatisticsParking(String day, List<String> parkNos);

    RevenueStatisticsDayFactVO analyseRevenueStatisticsMerchant(String day, List<String> parkNos);

    RevenueStatisticsDayFactVO analyseRevenueStatisticsMonthly(String day, List<String> parkNos);

    BigDecimal sumPayAmount(String parkNo, String startTime, String endTime);

    BigDecimal sumDiscountAmount(String parkNo, String startTime, String endTime);
}
