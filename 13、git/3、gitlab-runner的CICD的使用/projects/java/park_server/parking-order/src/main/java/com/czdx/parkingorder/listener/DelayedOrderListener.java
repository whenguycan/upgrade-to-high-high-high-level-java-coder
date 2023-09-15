package com.czdx.parkingorder.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.czdx.common.RabbitmqConstant;
import com.czdx.parkingorder.project.entity.MerchantOrderEntity;
import com.czdx.parkingorder.project.entity.MonthlyOrderEntity;
import com.czdx.parkingorder.project.entity.ParkingOrderEntity;
import com.czdx.parkingorder.project.service.MerchantOrderService;
import com.czdx.parkingorder.project.service.MonthlyOrderService;
import com.czdx.parkingorder.project.service.ParkingOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: tangwei
 * @Date: 2023/3/1 1:28 PM
 * @Description: 类描述信息
 */
@Component
@Slf4j
@RabbitListener(queues = {RabbitmqConstant.ORDER_DELAYED_MESSAGE_QUEUE})
public class DelayedOrderListener {

    @Autowired
    private ParkingOrderService parkingOrderService;

    @Autowired
    private MerchantOrderService merchantOrderService;

    @Autowired
    private MonthlyOrderService monthlyOrderService;

    @RabbitHandler
    public void cnacleOrder(byte[] message){
        String orderNo = new String(message);
        System.out.println("获取到要取消的订单号：" + new String(message));

        if (orderNo.startsWith("ME")){
            MerchantOrderEntity merchantOrderEntity = merchantOrderService.getBaseMapper().selectOne(new QueryWrapper<MerchantOrderEntity>().eq("order_no", orderNo));
            if(merchantOrderEntity != null){
                if(merchantOrderEntity.getOrderStatus().equals("01") || merchantOrderEntity.getOrderStatus().equals("02")){
                    merchantOrderEntity.setOrderStatus("05");
//                    merchantOrderEntity.setPayStatus("5");
                    merchantOrderService.getBaseMapper().updateById(merchantOrderEntity);
                }
            }else{
                log.error("商户订单号：" + orderNo + "在库中没有找到数据");
            }

        }else if(orderNo.startsWith("MO")){
            MonthlyOrderEntity monthlyOrderEntity = monthlyOrderService.getBaseMapper().selectOne(new QueryWrapper<MonthlyOrderEntity>().eq("order_no", orderNo));

            if(monthlyOrderEntity != null){
                if(monthlyOrderEntity.getOrderStatus().equals("01") || monthlyOrderEntity.getOrderStatus().equals("02")){
                    monthlyOrderEntity.setOrderStatus("05");
//                    monthlyOrderEntity.setPayStatus("5");
                    monthlyOrderService.getBaseMapper().updateById(monthlyOrderEntity);
                }
            }else{
                log.error("月租订单号：" + orderNo + "在库中没有找到数据");
            }

        }else {
            ParkingOrderEntity parkingOrderEntity = parkingOrderService.getBaseMapper().selectOne(new QueryWrapper<ParkingOrderEntity>().eq("order_no", orderNo));
            if(parkingOrderEntity != null){
                if(parkingOrderEntity.getOrderStatus().equals("01") || parkingOrderEntity.getOrderStatus().equals("02")){
                    parkingOrderEntity.setOrderStatus("05");
//                    parkingOrderEntity.setPayStatus("5");
                    parkingOrderService.getBaseMapper().updateById(parkingOrderEntity);
                }
            }else{
                log.error("订车订单号：" + orderNo + "在库中没有找到数据");
            }

        }
    }
}
