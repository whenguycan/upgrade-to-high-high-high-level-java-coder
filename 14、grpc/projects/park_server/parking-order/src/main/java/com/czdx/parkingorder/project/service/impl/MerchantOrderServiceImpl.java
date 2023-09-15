package com.czdx.parkingorder.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingorder.project.dao.MerchantOrderDao;
import com.czdx.parkingorder.project.entity.MerchantOrderEntity;
import com.czdx.parkingorder.project.service.MerchantOrderService;
import com.czdx.parkingorder.utils.SnowflakeIdWorker;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: tangwei
 * @Date: 2023/3/8 9:20 AM
 * @Description: 类描述信息
 */
@Service("MerchantOrderService")
public class MerchantOrderServiceImpl extends ServiceImpl<MerchantOrderDao, MerchantOrderEntity> implements MerchantOrderService {

    @Override
    public MerchantOrderEntity createOrder(String parkNo, Integer erchantId, Double discountAmount, Double payAmount) {
        MerchantOrderEntity merchantOrderEntity = new MerchantOrderEntity();

        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1, 2);
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
}
