package com.ruoyi.project.merchant.mq;

import com.czdx.common.RabbitmqConstant;
import com.czdx.grpc.lib.lot.RegularCarServiceGrpc;
import com.czdx.grpc.lib.lot.UpdateRegularCarAfterPayRequestProto;
import com.czdx.grpc.lib.lot.UpdateRegularCarAfterPayResponseProto;
import com.ruoyi.project.merchant.domain.MonthlyCarRentalOrder;
import com.ruoyi.project.merchant.service.IMonthlyCarRentalOrderService;
import com.ruoyi.project.parking.domain.vo.MemberUserVO;
import com.ruoyi.project.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MonthlyCarRentalOrderRabbitmqConsumer {

    @Autowired
    IMonthlyCarRentalOrderService monthlyCarRentalOrderService;

    @Autowired
    ISysUserService sysUserService;

    @GrpcClient("parking-lot-server")
    RegularCarServiceGrpc.RegularCarServiceBlockingStub regularCarServiceBlockingStub;

    /**
     * @apiNote 收到支付成功消息，根据订单号查询到对应的续租车辆，再grpc调用场库系统修改固定车时间或新增固定车记录
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitmqConstant.QUEUE_MEMBER_MONTHLY_ORDER_PAYSUCCESS, durable = "true"),
            exchange = @Exchange(value = RabbitmqConstant.EXCHANGE_ORDER_PAYSUCCESS, type = ExchangeTypes.TOPIC),
            key = RabbitmqConstant.ROUTE_MEMBER_MONTHLY_ORDER_PAYSUCCESS
    ))
    public void updateRegularCarAfterPay(Message message) {
        String orderNo = new String(message.getBody());
        //获取月租车订单信息
        MonthlyCarRentalOrder monthlyCarRentalOrder = monthlyCarRentalOrderService.getMonthlyCarRentalOrderByOrderNo(orderNo);
        //获取用户信息
        MemberUserVO memberUserVO = sysUserService.selectMemberById(monthlyCarRentalOrder.getOrderUserId().longValue());
        //根据categoryId和carNumber确认唯一的固定车记录，并根据rentalDays进行新增或延期
        UpdateRegularCarAfterPayRequestProto requestProto = UpdateRegularCarAfterPayRequestProto.newBuilder()
                .setParkNo(monthlyCarRentalOrder.getParkNo())
                .setUserName(memberUserVO.getPhonenumber())
                .setPhoneNumber(memberUserVO.getPhonenumber())
                .setRegularCarCategoryId(monthlyCarRentalOrder.getRegularCarCategoryId())
                .setRentalDays(monthlyCarRentalOrder.getRentalDays())
                .setCarNumber(monthlyCarRentalOrder.getCarNumber())
                .build();
        UpdateRegularCarAfterPayResponseProto responseProto = regularCarServiceBlockingStub.updateRegularCarAfterPay(requestProto);
        if (responseProto.getStatus()) {
            log.info("车牌号：{} 的固定车记录更新成功，天数：{}，价格：{}", monthlyCarRentalOrder.getCarNumber(), monthlyCarRentalOrder.getRentalDays(), monthlyCarRentalOrder.getRentalPrice());
        } else {
            log.error("车牌号：{} 的固定车记录更新失败，天数：{}，价格：{}", monthlyCarRentalOrder.getCarNumber(), monthlyCarRentalOrder.getRentalDays(), monthlyCarRentalOrder.getRentalPrice());
        }
    }
}
