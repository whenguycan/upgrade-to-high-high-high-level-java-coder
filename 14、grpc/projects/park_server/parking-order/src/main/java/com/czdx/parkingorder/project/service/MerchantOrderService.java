package com.czdx.parkingorder.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czdx.parkingorder.project.entity.MerchantOrderEntity;

/**
 * @Auther: tangwei
 * @Date: 2023/3/8 9:19 AM
 * @Description: 接口描述信息
 */
public interface MerchantOrderService extends IService<MerchantOrderEntity> {
    MerchantOrderEntity createOrder(String parkNo, Integer erchantId, Double discountAmount, Double payAmount);
}
