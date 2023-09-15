package com.ruoyi.project.parking.mq;

import com.czdx.common.RabbitmqConstant;
import com.dahuatech.icc.exception.ClientException;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.project.parking.dahua.ipms.device.IpmsDevice;
import com.ruoyi.project.parking.mq.model.PaymentData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * description: 支付消费者
 * @author ps
 * @date 2023/2/28 15:49
 */
@Slf4j
@Component
public class PaymentRabbitmqConsumer {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IpmsDevice ipmsDevice;

//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = RabbitmqConstant.QUEUE_PARKING_PAYMENT, durable = "true"),
//            exchange = @Exchange(value = RabbitmqConstant.EXCHANGE_PARKING_PAYMENT, durable = "true", type = ExchangeTypes.TOPIC),
//            key = RabbitmqConstant.ROUTE_PARKING_PAYMENT
//    ))
    public void consumerPaymentMsg(PaymentData data) throws ClientException {
        log.info("接收到支付信息，{}", data);
        //没有通道id为预支付
        if (StringUtils.isEmpty(data.getPassageNo())){
            //查询redis有没有预出场车牌号
            Collection<String> keys=redisCache.keys(CacheConstants.PARKNO_PASSAGE_KEY+"*");
            boolean tag=true;
            for (String key:keys){
                //如果有车牌号则放行（闸道口预支付）
                if (data.getCarNumber().equals(redisCache.getCacheObject(key))){
                    String passageNo=key.split("_")[2];
                    ipmsDevice.OpenCloseChannel(passageNo,1,null);
                    tag=false;
                    break;
                }
            }
            //正常预支付,放入redis30分钟，车来的时候再开闸
            if (tag){
                redisCache.setCacheObject(CacheConstants.ADVANCE_PAYMENT+data.getCarNumber(),data.getOutTradeNo(),30, TimeUnit.MINUTES);
            }
        }else {
            //有通道id为实时支付,比较车牌号，车牌号一致则放行
            if (data.getCarNumber().equals(redisCache.getCacheObject
                    (CacheConstants.PARKNO_PASSAGE_KEY+data.getParkNo()+"_"+data.getPassageNo()))){
                ipmsDevice.OpenCloseChannel(data.getPassageNo(),1,null);
            }else {
                redisCache.setCacheObject(CacheConstants.ADVANCE_PAYMENT+data.getCarNumber(),data.getOutTradeNo(),30, TimeUnit.MINUTES);
            }
        }
    }


}
