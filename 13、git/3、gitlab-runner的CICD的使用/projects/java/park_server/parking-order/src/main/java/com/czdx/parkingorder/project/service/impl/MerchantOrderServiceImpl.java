package com.czdx.parkingorder.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingorder.project.dao.MerchantOrderDao;
import com.czdx.parkingorder.project.entity.MerchantOrderEntity;
import com.czdx.parkingorder.project.service.MerchantOrderService;
import com.czdx.parkingorder.utils.SnowflakeIdWorker;
import com.google.protobuf.ProtocolStringList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Auther: tangwei
 * @Date: 2023/3/8 9:20 AM
 * @Description: 类描述信息
 */
@Service("MerchantOrderService")
public class MerchantOrderServiceImpl extends ServiceImpl<MerchantOrderDao, MerchantOrderEntity> implements MerchantOrderService {

    @Autowired
    SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public MerchantOrderEntity createOrder(String parkNo, Integer erchantId, Double discountAmount, Double payAmount) {
        MerchantOrderEntity merchantOrderEntity = new MerchantOrderEntity();

        String orderNum = String.valueOf(snowflakeIdWorker.nextId());

        merchantOrderEntity.setOrderNo("ME" + orderNum);
        merchantOrderEntity.setOrderType("1");
        merchantOrderEntity.setOrderStatus("01");
        merchantOrderEntity.setOrderParam("");
        merchantOrderEntity.setParkNo(parkNo);
        merchantOrderEntity.setErchantId(erchantId);
        merchantOrderEntity.setPayableAmount(BigDecimal.valueOf(payAmount));
        merchantOrderEntity.setDiscountAmount(BigDecimal.valueOf(discountAmount));
        merchantOrderEntity.setPaidAmount(BigDecimal.valueOf(0L));
        merchantOrderEntity.setPayAmount(BigDecimal.valueOf(payAmount));
        merchantOrderEntity.setPayStatus("01");
        merchantOrderEntity.setPayNumber("");
        merchantOrderEntity.setExpireTime(new Date());
        merchantOrderEntity.setRemark("");
        merchantOrderEntity.setCreateTime(new Date());
        merchantOrderEntity.setUpdateTime(new Date());
        this.baseMapper.insert(merchantOrderEntity);

        return merchantOrderEntity;
    }

    /**
     * @apiNote 查询指定日期指定车场商户订单总实付金额
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
    public List<MerchantOrderEntity> searchMerchantOrderByOrderNo(List<String> orderNo) {
        return this.baseMapper.selectList(new QueryWrapper<MerchantOrderEntity>().eq("order_status", "03").in("order_no", orderNo));
    }
}
