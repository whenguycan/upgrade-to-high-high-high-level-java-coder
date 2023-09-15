package com.czdx.parkingorder.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.czdx.parkingorder.project.entity.MonthlyOrderEntity;
import com.google.protobuf.ProtocolStringList;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: tangwei
 * @Date: 2023/3/13 2:09 PM
 * @Description: 接口描述信息
 */
public interface MonthlyOrderService extends IService<MonthlyOrderEntity> {
    MonthlyOrderEntity createOrder(String parkNo, Integer orderUserId, Double discountAmount, Double payAmount);

    IPage<MonthlyOrderEntity> searchOrder(Integer orderUserId, String orderStatus, String payStatus, Integer pageNum, Integer pageSize, String orderNo,boolean billable);

    /**
     * @apiNote 查询指定日期指定车场月租车订单总实付金额
     */
    BigDecimal sumOnlineIncome(ProtocolStringList parkNos, String day);

    /**
     * @apiNote 查询指定日期指定车场月租车订单总优惠金额
     */
    BigDecimal sumOnlineDeduction(ProtocolStringList parkNos, String day);

    /**
     * @apiNote 根据订单号查询月租车订单
     */
    List<MonthlyOrderEntity> searchMonthlyOrderByOrderNo(List<String> orderNoList);

    /**
     * @apiNote 更新发票流水号
     */
    void updateToBilled(List<String> orderNoList, String out_trade_no);
}
