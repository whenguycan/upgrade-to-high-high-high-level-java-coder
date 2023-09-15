package com.czdx.parkingorder.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.czdx.parkingorder.project.entity.ParkingOrderEntity;
import com.czdx.parkingorder.project.vo.*;
import com.google.protobuf.ProtocolStringList;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: tangwei
 * @Date: 2023/2/27 10:32 AM
 * @Description: 接口描述信息
 */
public interface ParkingOrderService extends IService<ParkingOrderEntity> {

    public ParkingOrderEntity createParkingOrder(CreateParkingOrderVo createParkingOrderVo);

    ParkingOrderEntity getParkingOrderByOrderNo(String orderNo);

    void cancleRepeatedOrder(String parkNo, String carNumber, String entryTime);

    IPage<ParkingOrderEntity> searchOrder(String orderNo,String parkNo, Integer payMethod, Integer pageNum, Integer pageSize, String carNumber,String startDate,String endDate,String orderStatus,String orderType);

    StatisticIncomeVo statisticIncome(List<String> parkNos);

    List<PayTypeDayFactVO> analysePayTypeDayFact(String day, List<String> parkNos);

    List<PayMethodDayFactVO> analysePayMethodDayFact(String day, List<String> parkNos);

    List<OrderSituationDayFactVO> analyseOrderSituationDayFact(String day, List<String> parkNos);

    /**
     * @apiNote 查询指定日期指定车场停车订单总实付金额
     */
    BigDecimal sumOnlineIncome(ProtocolStringList parkNos, String day);

    /**
     * @apiNote 查询指定日期指定车场停车订单总优惠金额
     */
    BigDecimal sumOnlineDeduction(ProtocolStringList parkNos, String day);

    RevenueStatisticsDayFactVO analyseRevenueStatisticsDayFact(String day, List<String> parkNos);

    IPage<ParkingOrderEntity> searchParkingOrder(List<String> carNums, Integer pageNum, Integer pageSize,boolean billable);

    List<ParkingOrderEntity> searchParkingOrderByOrderNo(List<String> orderNo);

    void updateToBilled(List<String> orderNoList, String out_trade_no);

    BigDecimal sumPayAmount(String parkNo, String startTime, String endTime);

    BigDecimal sumDiscountAmount(String parkNo, String startTime, String endTime);
}
