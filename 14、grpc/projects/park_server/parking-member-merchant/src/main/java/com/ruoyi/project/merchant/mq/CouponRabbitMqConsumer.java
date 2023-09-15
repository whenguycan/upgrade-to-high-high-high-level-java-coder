package com.ruoyi.project.merchant.mq;

import com.alibaba.fastjson2.JSONObject;
import com.dahuatech.icc.exception.ClientException;
import com.ruoyi.common.utils.JsonUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sign.Base64;
import com.ruoyi.project.merchant.mq.model.UsedCarCouponModel;
import com.ruoyi.project.merchant.service.ITCouponCarnoRelationService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CouponRabbitMqConsumer {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    ITCouponCarnoRelationService assignedCouponService;

    //    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = RabbitmqConstant.QUEUE_PARKING_PAYMENT, durable = "true"),
//            exchange = @Exchange(value = RabbitmqConstant.EXCHANGE_PARKING_PAYMENT, durable = "true", type = ExchangeTypes.TOPIC),
//            key = RabbitmqConstant.ROUTE_PARKING_PAYMENT
//    ))
    public void consumerOrderUseCouponMsg(byte[] data) throws ClientException {
        String encode = Base64.encode(data);
        UsedCarCouponModel usedCarCouponModel = JSONObject.parseObject(encode, UsedCarCouponModel.class);
        String usedCoupons = usedCarCouponModel.getUsedCoupons();
        if (StringUtils.isEmpty(usedCoupons)) {
            return;
        }
        List<String> couponList=new ArrayList<>();
        String[] split = usedCoupons.split(",");
        for (String s : split) {
            
        }
        logger.info("couponUsed info :" + data.toString());
        //int result = assignedCouponService.updateCouponStatus(data.getCouponDetailId(), data.getUsedTime());
        logger.info("couponUsed info end:" + data.toString());

    }
}
