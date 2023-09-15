package com.czdx.parkingorder.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.czdx.parkingorder.project.entity.MonthlyOrderEntity;

/**
 * @Auther: tangwei
 * @Date: 2023/3/13 2:09 PM
 * @Description: 接口描述信息
 */
public interface MonthlyOrderService extends IService<MonthlyOrderEntity> {
    MonthlyOrderEntity createOrder(String parkNo, Integer orderUserId, Double discountAmount, Double payAmount);

    IPage<MonthlyOrderEntity> searchOrder(Integer orderUserId, String orderStatus, String payStatus, Integer pageNum, Integer pageSize, String orderNo);
}
