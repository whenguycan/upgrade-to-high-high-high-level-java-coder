package com.czdx.parkingorder.project.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czdx.parkingorder.project.entity.MerchantOrderEntity;
import com.google.protobuf.ProtocolStringList;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

/**
 * @Auther: tangwei
 * @Date: 2023/3/8 9:16 AM
 * @Description: 接口描述信息
 */
@Mapper
public interface MerchantOrderDao extends BaseMapper<MerchantOrderEntity> {

    /**
     * @apiNote 查询指定日期指定车场商户订单总实付金额
     */
    BigDecimal sumOnlineIncome(ProtocolStringList parkNos, String day);

    /**
     * @apiNote 查询指定日期指定车场商户订单总优惠金额
     */
    BigDecimal sumOnlineDeduction(ProtocolStringList parkNos, String day);
}
