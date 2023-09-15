package com.czdx.parkingorder.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingorder.common.utils.StringUtils;
import com.czdx.parkingorder.project.dao.ParkingOrderDao;
import com.czdx.parkingorder.project.entity.ParkingOrderEntity;
import com.czdx.parkingorder.project.entity.ParkingOrderItemEntity;
import com.czdx.parkingorder.project.service.ParkingOrderItemService;
import com.czdx.parkingorder.project.service.ParkingOrderService;
import com.czdx.parkingorder.project.vo.*;
import com.czdx.parkingorder.utils.SnowflakeIdWorker;
import com.google.protobuf.ProtocolStringList;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Auther: tangwei
 * @Date: 2023/2/27 10:33 AM
 * @Description: 类描述信息
 */
@Service("ParkingOrderService")
public class ParkingOrderServiceImpl extends ServiceImpl<ParkingOrderDao, ParkingOrderEntity> implements ParkingOrderService {

    @Autowired
    private ParkingOrderItemService parkingOrderItemService;

    @Autowired
    SnowflakeIdWorker snowflakeIdWorker;

    @Override
    @Transactional
    public ParkingOrderEntity createParkingOrder(CreateParkingOrderVo createParkingOrderVo) {
        ParkingOrderEntity parkingOrderEntity = new ParkingOrderEntity();
        String orderNum = String.valueOf(snowflakeIdWorker.nextId());

        if (createParkingOrderVo.getPassageNo().equals("")) {//如果传入的岗亭编号为空，证明预支付订单
            parkingOrderEntity.setOrderType("2");
            orderNum = "PP" + orderNum;
//            parkingOrderEntity.setOrderNo("PP" + orderNum);
        } else {
            parkingOrderEntity.setOrderType("1");
            orderNum = "PL" + orderNum;
//            parkingOrderEntity.setOrderNo("PL" + orderNum);
        }

        parkingOrderEntity.setOrderNo(orderNum);

        parkingOrderEntity.setOrderStatus(createParkingOrderVo.getPayed() ? "03" : "01");
        parkingOrderEntity.setOrderParam("");
        parkingOrderEntity.setParkNo(createParkingOrderVo.getParkNo());
        parkingOrderEntity.setPassageNo(createParkingOrderVo.getPassageNo());
        parkingOrderEntity.setCarNumber(createParkingOrderVo.getCarNumber());
        parkingOrderEntity.setCarTypeCode(createParkingOrderVo.getCarTypeCode());
        String time = createParkingOrderVo.getEntryTime();
        parkingOrderEntity.setEntryTime(new Date(Long.valueOf(time)));
        parkingOrderEntity.setPayableAmount(BigDecimal.valueOf(createParkingOrderVo.getPayableAmount()));
        parkingOrderEntity.setDiscountAmount(BigDecimal.valueOf(createParkingOrderVo.getDiscountAmount()));
        parkingOrderEntity.setPaidAmount(BigDecimal.valueOf(createParkingOrderVo.getPaidAmount()));
        parkingOrderEntity.setPayAmount(BigDecimal.valueOf(createParkingOrderVo.getPayAmount()));
        parkingOrderEntity.setPayMethod(createParkingOrderVo.getPayMethod());
        parkingOrderEntity.setPayTime(createParkingOrderVo.getPayTime());
        parkingOrderEntity.setPayStatus(createParkingOrderVo.getPayed() ? "3" : "1");
        parkingOrderEntity.setPayNumber("");
        parkingOrderEntity.setExpireTime(new Date(new Date().getTime() + 15 * 60 * 1000));
        parkingOrderEntity.setRemark("");
        parkingOrderEntity.setCreateTime(new Date());
        parkingOrderEntity.setUpdateTime(new Date());

        AtomicReference<String> coupons = new AtomicReference<>("");
        if (createParkingOrderVo.getCouponList() != null) {
            createParkingOrderVo.getCouponList().stream().forEach(everyCoupon -> {
                coupons.compareAndExchange(coupons.get(), coupons.get() + everyCoupon.getCouponMold() + "_" + everyCoupon.getCouponCode() + ",");
            });
        }
        parkingOrderEntity.setCoupons(coupons.get());

        parkingOrderEntity.setDiscountReason(createParkingOrderVo.getDiscountReason());
        this.baseMapper.insert(parkingOrderEntity);


        //TODO 这儿为了偷懒就不写sql语句一次性插入数据了，而是写个循环，后面如果有人看到，希望你来改，我就不改了，写这玩意儿太浪费时间
        if (createParkingOrderVo.getItemList() != null) {
            String finalOrderNum = orderNum;
            createParkingOrderVo.getItemList().stream().forEach(item -> {
                ParkingOrderItemEntity parkingOrderItemEntity = new ParkingOrderItemEntity();
                parkingOrderItemEntity.setOrderNo(finalOrderNum);
                parkingOrderItemEntity.setParkFieldId(item.getParkFieldId());
                parkingOrderItemEntity.setEntryTime(new Date(Long.valueOf(item.getEntryTime())));
                parkingOrderItemEntity.setExitTime(new Date(Long.valueOf(item.getExitTime())));
                parkingOrderItemEntity.setParkingTime(item.getParkingTimet().intValue());
                parkingOrderItemEntity.setPayableAmount(BigDecimal.valueOf(item.getPayableAmount()));
                parkingOrderItemEntity.setCreateTime(new Date());
                parkingOrderItemEntity.setUpdateTime(new Date());
                parkingOrderItemService.save(parkingOrderItemEntity);
            });
        }


        return parkingOrderEntity;
    }

    @Override
    public ParkingOrderEntity getParkingOrderByOrderNo(String orderNo) {
        return this.baseMapper.selectOne(new QueryWrapper<ParkingOrderEntity>().eq("order_no", orderNo));
    }

    @Override
    public void cancleRepeatedOrder(String parkNo, String carNumber, String entryTime) {
        List<ParkingOrderEntity> orderInfoList = this.baseMapper.selectList(new QueryWrapper<ParkingOrderEntity>()
                .and(
                        wp -> wp.eq("park_no", parkNo)
                                .eq("car_number", carNumber)
                                .eq("entry_time", new Date(Long.valueOf(entryTime)))
                )
                .or(
                        wp -> wp.eq("order_status", "01").eq("order_status", "02")
                )
        );

        orderInfoList.stream().forEach(everyOrder -> {
            everyOrder.setOrderStatus("05");
            this.baseMapper.updateById(everyOrder);//更新订单状态为已取消！
        });

    }

    @Override
    public IPage<ParkingOrderEntity> searchOrder(String orderNo, String parkNo, Integer payMethod, Integer pageNum, Integer pageSize, String carNumber, String startDate, String endDate, String orderStatus, String orderType) {
        IPage<ParkingOrderEntity> page = Page.of(pageNum, pageSize);
        this.baseMapper.selectPage(page, new QueryWrapper<ParkingOrderEntity>()
                .like(StringUtils.isNotEmpty(orderNo), "order_no", orderNo)
                .eq(StringUtils.isNotEmpty(parkNo), "park_no", parkNo)
                .eq(payMethod != 0, "pay_method", payMethod)
                .like(StringUtils.isNotEmpty(carNumber), "car_number", carNumber)
                .ge(StringUtils.isNotEmpty(startDate), "pay_time", startDate)
                .le(StringUtils.isNotEmpty(endDate), "pay_time", endDate)
                .eq(StringUtils.isNotEmpty(orderStatus), "order_status", orderStatus)
                .eq(StringUtils.isNotEmpty(orderType), "order_type", orderType)
                .like(StringUtils.isNotEmpty(carNumber), "car_number", carNumber));

        return page;
    }

    @Override
    public StatisticIncomeVo statisticIncome(List<String> parkNos) {
        return baseMapper.statisticIncome(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), parkNos);
    }

    @Override
    public List<PayTypeDayFactVO> analysePayTypeDayFact(String day, List<String> parkNos) {
        List<PayTypeDayFactVO> result = baseMapper.analysePayTypeDayFact(day, parkNos);
        if (CollectionUtils.isNotEmpty(result)) {
            BigDecimal sum = BigDecimal.ZERO;
            for (PayTypeDayFactVO item : result) {
                sum = sum.add(item.getAmount());
            }
            for (PayTypeDayFactVO payTypeDayFactVO : result) {
                payTypeDayFactVO.setRatio(sum.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : payTypeDayFactVO.getAmount().multiply(new BigDecimal(100)).divide(sum, 2, RoundingMode.HALF_UP));
            }
        }
        return result;
    }

    @Override
    public List<PayMethodDayFactVO> analysePayMethodDayFact(String day, List<String> parkNos) {
        List<PayMethodDayFactVO> result = baseMapper.analysePayMethodDayFact(day, parkNos);
        if (CollectionUtils.isNotEmpty(result)) {
            BigDecimal sum = BigDecimal.ZERO;
            for (PayMethodDayFactVO item : result) {
                sum = sum.add(item.getAmount());
            }
            for (PayMethodDayFactVO payMethodDayFactVO : result) {
                payMethodDayFactVO.setRatio(sum.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : payMethodDayFactVO.getAmount().multiply(new BigDecimal(100)).divide(sum, 2, RoundingMode.HALF_UP));
            }
        }
        return result;
    }

    @Override
    public List<OrderSituationDayFactVO> analyseOrderSituationDayFact(String day, List<String> parkNos) {
        return baseMapper.analyseOrderSituationDayFact(day, parkNos);
    }

    /**
     * @apiNote 查询指定日期指定车场停车订单总实付金额
     */
    @Override
    public BigDecimal sumOnlineIncome(ProtocolStringList parkNos, String day) {
        return baseMapper.sumOnlineIncome(parkNos, day);
    }

    /**
     * @apiNote 查询指定日期指定车场商户订单总优惠金额
     */
    @Override
    public BigDecimal sumOnlineDeduction(ProtocolStringList parkNos, String day) {
        return baseMapper.sumOnlineDeduction(parkNos, day);
    }

    @Override
    public RevenueStatisticsDayFactVO analyseRevenueStatisticsDayFact(String day, List<String> parkNos) {
        RevenueStatisticsDayFactVO factParkingVO = baseMapper.analyseRevenueStatisticsParking(day, parkNos);
        RevenueStatisticsDayFactVO factMerchantVO = baseMapper.analyseRevenueStatisticsMerchant(day, parkNos);
        RevenueStatisticsDayFactVO factMonthlyVO = baseMapper.analyseRevenueStatisticsMonthly(day, parkNos);
        factParkingVO.setOnlinePayableAmount(factParkingVO.getOnlinePayableAmount().add(factMerchantVO.getOnlinePayableAmount()).add(factMonthlyVO.getOnlinePayableAmount()));
        factParkingVO.setOnlinePayAmount(factParkingVO.getOnlinePayAmount().add(factMerchantVO.getOnlinePayAmount()).add(factMonthlyVO.getOnlinePayAmount()));
        factParkingVO.setCashPayableAmount(factParkingVO.getCashPayableAmount().add(factMerchantVO.getCashPayableAmount()).add(factMonthlyVO.getCashPayableAmount()));
        factParkingVO.setCashPayAmount(factParkingVO.getCashPayAmount().add(factMerchantVO.getCashPayAmount()).add(factMonthlyVO.getCashPayAmount()));
        factParkingVO.setConcession(factParkingVO.getConcession() + factMerchantVO.getConcession() + factMonthlyVO.getConcession());
        factParkingVO.setTransactionNumber(factParkingVO.getTransactionNumber() + factMerchantVO.getTransactionNumber() + factMonthlyVO.getTransactionNumber());
        return factParkingVO;
    }

    @Override
    public IPage<ParkingOrderEntity> searchParkingOrder(List<String> carNums, Integer pageNum, Integer pageSize, boolean billable) {
        IPage<ParkingOrderEntity> page = Page.of(pageNum, pageSize);
        this.baseMapper.selectPage(page, new QueryWrapper<ParkingOrderEntity>()
                .eq("order_status", "03")
                .in("car_number", carNums)
                .eq(billable, "bill_out_tarde_no", "")
                .ne(billable, "pay_amount", 0.00));
        return page;
    }

    @Override
    public List<ParkingOrderEntity> searchParkingOrderByOrderNo(List<String> orderNo) {
        return this.baseMapper.selectList(new QueryWrapper<ParkingOrderEntity>().eq("order_status", "03").in("order_no", orderNo));
    }

    @Override
    public void updateToBilled(List<String> orderNoList, String out_trade_no) {
        this.update(new UpdateWrapper<ParkingOrderEntity>().in("order_no", orderNoList).set("bill_out_tarde_no", out_trade_no));
    }

    @Override
    public BigDecimal sumPayAmount(String parkNo, String startTime, String endTime) {
        return baseMapper.sumPayAmount(parkNo, startTime, endTime);
    }

    @Override
    public BigDecimal sumDiscountAmount(String parkNo, String startTime, String endTime) {
        return baseMapper.sumDiscountAmount(parkNo, startTime, endTime);
    }

}
