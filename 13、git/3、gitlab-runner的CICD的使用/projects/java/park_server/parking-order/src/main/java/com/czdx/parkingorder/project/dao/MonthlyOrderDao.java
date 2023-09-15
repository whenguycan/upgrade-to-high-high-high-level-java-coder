package com.czdx.parkingorder.project.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czdx.parkingorder.project.entity.MonthlyOrderEntity;
import com.google.protobuf.ProtocolStringList;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

/**
 * @Auther: tangwei
 * @Date: 2023/3/13 2:07 PM
 * @Description: 接口描述信息
 */
@Mapper
public interface MonthlyOrderDao extends BaseMapper<MonthlyOrderEntity> {
    /**
     * @apiNote 查询指定日期指定车场商户订单总实付金额
     */
    BigDecimal sumOnlineIncome(ProtocolStringList parkNos, String day);

    /**
     * @apiNote 查询指定日期指定车场商户订单总优惠金额
     */
    BigDecimal sumOnlineDeduction(ProtocolStringList parkNos, String day);
}
