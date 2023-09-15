package com.ruoyi.project.merchant.mq;

import com.alibaba.fastjson2.JSONObject;
import com.czdx.common.RabbitmqConstant;
import com.dahuatech.icc.exception.ClientException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.common.CouponEnums;
import com.ruoyi.project.merchant.domain.TOperRecords;
import com.ruoyi.project.merchant.domain.vo.TCouponDetailVo;
import com.ruoyi.project.merchant.domain.vo.TOperRecordsVo;
import com.ruoyi.project.merchant.enums.PayStatusEnums;
import com.ruoyi.project.merchant.mq.model.UsedCarCouponModel;
import com.ruoyi.project.merchant.service.ITCouponDetailService;
import com.ruoyi.project.merchant.service.ITOperRecordsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

@Slf4j
@Component
public class CouponRabbitMqConsumer {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    ITCouponDetailService itCouponDetailService;

    @Resource
    ITOperRecordsService operRecordsService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitmqConstant.QUEUE_MEMBER_MERCHANT_ORDER_PAYSUCCESS, durable = "true"),
            exchange = @Exchange(value = RabbitmqConstant.EXCHANGE_ORDER_PAYSUCCESS, durable = "true", type = ExchangeTypes.TOPIC),
            key = RabbitmqConstant.ROUTE_MEMBER_MERCHANT_ORDER_PAYSUCCESS
    ))
    public void consumerOrderUseCouponMsg(Message message) throws ClientException {
        String messageInfo = new String(message.getBody());
        logger.info("achieve orderInfo :" + messageInfo);
        UsedCarCouponModel usedCarCouponModel = JSONObject.parseObject(messageInfo, UsedCarCouponModel.class);
        String usedCoupons = usedCarCouponModel.getUsedCoupons();
        if (StringUtils.isNotEmpty(usedCoupons)) {
            logger.info("coupon status change start");
            String[] split = usedCoupons.split(",");
            for (String coupon : split) {
                String[] s = coupon.split("_");
                if (split.length > 2) {
                    String couponCode = s[1];
                    TCouponDetailVo tCouponDetail = new TCouponDetailVo();
                    tCouponDetail.setCouponCode(couponCode);
                    tCouponDetail.setCouponStatus(CouponEnums.COUPON_STATUS.USED.getValue());
                    tCouponDetail.setOrderNo(s[0]);
                    try {
                        tCouponDetail.setUsedTime(DateUtils.parseDate(usedCarCouponModel.getUsedTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
                    } catch (ParseException e) {
                        logger.info(e.getMessage());
                    }
                    itCouponDetailService.updateCouponUsedByCode(tCouponDetail);
                }
            }
            logger.info("coupon status change end");
            return;
        }
        if (StringUtils.isNotEmpty(usedCarCouponModel.getOrderNo())) {
            logger.info("recharge start");
            TOperRecordsVo tOperRecords = new TOperRecordsVo();
            tOperRecords.setOrderNo(usedCarCouponModel.getOrderNo());
            List<TOperRecords> tOperRecordsList = operRecordsService.selectTOperRecordsList(tOperRecords);
            if (CollectionUtils.isEmpty(tOperRecordsList)) {
                logger.info("订单流水线没有生成");
                return;
            }
            TOperRecords recordResult = tOperRecordsList.get(0);
            TOperRecords updateRecord = new TOperRecords();
            updateRecord.setId(recordResult.getId());
            updateRecord.setStatus(PayStatusEnums.PAY_STATUS.PAYMENT_SUCCESS.getValue());
            updateRecord.setAmount(recordResult.getAmount());
            updateRecord.setOperId(recordResult.getOperId());
            //更新流水账单状态
            operRecordsService.updateTOperRecords(updateRecord);
            logger.info("recharge end");
            return;
        }
        logger.info("handle orderInfo end");
    }
}

