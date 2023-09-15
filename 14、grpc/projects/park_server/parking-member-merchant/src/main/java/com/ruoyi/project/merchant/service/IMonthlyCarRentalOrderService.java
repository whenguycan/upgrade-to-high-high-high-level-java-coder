package com.ruoyi.project.merchant.service;

import com.ruoyi.project.merchant.domain.MonthlyCarRentalOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.merchant.domain.bo.MonthlyCarRentalOrderBO;
import com.ruoyi.project.merchant.domain.param.MonthlyCarRentalOrderParam;

import java.util.List;

/**
* @author 琴声何来
* @description 针对表【t_monthly_car_rental_order(H5我的车辆管理表)】的数据库操作Service
* @since 2023-03-06 15:25:43
*/
public interface IMonthlyCarRentalOrderService extends IService<MonthlyCarRentalOrder> {

    /**
     * @apiNote 获取我的订单列表
     */
    List<MonthlyCarRentalOrderBO> listOrder(MonthlyCarRentalOrder monthlyCarRentalOrder);

    /**
     * @apiNote 验证下单资格
     */
    boolean verifyOrderQualification(MonthlyCarRentalOrder monthlyCarRentalOrder);

    /**
     * @apiNote 调用订单系统生成订单，返回订单号
     */
    Integer createOrder(MonthlyCarRentalOrder monthlyCarRentalOrder);

    /**
     * @apiNote 确认支付，调用订单系统拉起支付
     */
    String confirmPay(MonthlyCarRentalOrderParam monthlyCarRentalOrderParam);

    /**
     * @apiNote 获取单个订单详情
     */
    MonthlyCarRentalOrderBO getMonthlyCarRentalOrderById(Integer id);
}
