package com.czdx.parkingorder.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czdx.parkingorder.project.entity.MerchantOrderEntity;
import com.google.protobuf.ProtocolStringList;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: tangwei
 * @Date: 2023/3/8 9:19 AM
 * @Description: 接口描述信息
 */
public interface MerchantOrderService extends IService<MerchantOrderEntity> {
    MerchantOrderEntity createOrder(String parkNo, Integer erchantId, Double discountAmount, Double payAmount);

    /**
     * @apiNote 查询指定日期指定车场商户订单总实付金额
     */
    BigDecimal sumOnlineIncome(ProtocolStringList parkNos, String day);

    /**
     * @apiNote 查询指定日期指定车场商户订单总优惠金额
     */
    BigDecimal sumOnlineDeduction(ProtocolStringList parkNos, String day);

    List<MerchantOrderEntity> searchMerchantOrderByOrderNo(List<String> orderNo);
}
