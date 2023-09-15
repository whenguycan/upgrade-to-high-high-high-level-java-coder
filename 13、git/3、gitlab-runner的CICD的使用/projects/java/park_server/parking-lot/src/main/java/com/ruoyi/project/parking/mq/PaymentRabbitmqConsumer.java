package com.ruoyi.project.parking.mq;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czdx.common.RabbitmqConstant;
import com.dahuatech.icc.exception.ClientException;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.project.parking.dahua.ipms.device.IpmsDevice;
import com.ruoyi.project.parking.domain.BPassageDevice;
import com.ruoyi.project.parking.domain.TExitRecords;
import com.ruoyi.project.parking.domain.vo.BPassageDeviceVo;
import com.ruoyi.project.parking.entity.TAbnormalOrder;
import com.ruoyi.project.parking.mapper.BPassageDeviceMapper;
import com.ruoyi.project.parking.mq.model.PaymentData;
import com.ruoyi.project.parking.service.ITAbnormalOrderService;
import com.ruoyi.project.parking.service.ITExitRecordsService;
import com.ruoyi.project.parking.service.grpcclient.DeviceGrpcClientService;
import com.ruoyi.project.parking.service.grpcclient.model.OpenCloseChannelRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * description: 支付消费者
 *
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
    @Autowired
    private BPassageDeviceMapper bPassageDeviceMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ITExitRecordsService exitRecordsService;

    @Autowired
    ITAbnormalOrderService abnormalOrderService;
    @Resource
    private DeviceGrpcClientService deviceGrpcClientService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitmqConstant.QUEUE_DEVICE_PARKING_PAYSUCCESS, durable = "true"),
            exchange = @Exchange(value = RabbitmqConstant.EXCHANGE_LOT_PARKING_PAYSUCCESS, durable = "true", type = ExchangeTypes.TOPIC),
            key = RabbitmqConstant.ROUTE_DEVICE_PARKING_PAYSUCCESS
    ))
    public void consumerPaymentMsg(String message) throws ClientException {
        log.info("接收到支付信息，{}", message);
        PaymentData data = JSON.parseObject(message, PaymentData.class);
        //没有通道id为预支付
        try {
            if (StringUtils.isEmpty(data.getPassageNo())) {
                //查询redis有没有预出场车牌号
                Collection<String> keys = redisCache.keys(CacheConstants.PARKNO_PASSAGE_KEY + "*");
                boolean tag = true;
                for (String key : keys) {
                    //如果有车牌号则放行（闸道口预支付）
                    if (data.getCarNumber().equals(redisCache.getCacheObject(key))) {
                        log.info("通道编号为空，准备预支付开闸");
                        String passageNo = key.split("_")[2];
                        BPassageDeviceVo bPassageDeviceVo = bPassageDeviceMapper.selectBPassageDeviceByPassageNo(passageNo);
                        String channelId = bPassageDeviceVo.getDeviceId();
                        log.info("通道号--{}--开闸", channelId);
                        OpenCloseChannelRequest openCloseChannelRequest = new OpenCloseChannelRequest();
                        openCloseChannelRequest.setStatus("1");
                        openCloseChannelRequest.setDeviceIp(bPassageDeviceVo.getServerIp());
                        openCloseChannelRequest.setParkNo(data.getParkNo());
//            openCloseChannelRequest.setEntryOrExitId(entrySiteInfo.getEntryOrExitId());
                        openCloseChannelRequest.setCarNum(data.getCarNumber());
                        deviceGrpcClientService.openCloseChannel(openCloseChannelRequest);

//                        ipmsDevice.OpenCloseChannel(channelId, 1, null);
                        log.info("通道号--{}--开闸成功", channelId);
                        tag = false;
                        break;
                    }
                }

                //正常预支付,放入redis15分钟，车来的时候再开闸
                if (tag) {
                    log.info("预支付存入redis");
                    redisCache.setCacheObject(CacheConstants.ADVANCE_PAYMENT + data.getCarNumber(), setDurationTime(data), 15, TimeUnit.MINUTES);
                }
            } else {
                BPassageDeviceVo bPassageDeviceVo = bPassageDeviceMapper.selectBPassageDeviceByPassageNo(data.getPassageNo());
                String channelId = bPassageDeviceVo.getDeviceId();
                //有通道id为实时支付,比较车牌号，车牌号一致则放行
                log.info(JSONObject.toJSONString("-----bPassageDeviceVo:" + bPassageDeviceVo));
                if (data.getCarNumber().equals(redisCache.getCacheObject
                        (CacheConstants.PARKNO_PASSAGE_KEY + bPassageDeviceVo.getParkNo() + "_" + data.getPassageNo()))) {
                    log.info("实时支付，通道号--{}--开闸", channelId);
                    OpenCloseChannelRequest openCloseChannelRequest = new OpenCloseChannelRequest();
                    openCloseChannelRequest.setStatus("1");
                    openCloseChannelRequest.setDeviceIp(bPassageDeviceVo.getServerIp());
                    openCloseChannelRequest.setParkNo(data.getParkNo());
//            openCloseChannelRequest.setEntryOrExitId(entrySiteInfo.getEntryOrExitId());
                    openCloseChannelRequest.setCarNum(data.getCarNumber());
                    deviceGrpcClientService.openCloseChannel(openCloseChannelRequest);

//                    ipmsDevice.OpenCloseChannel(channelId, 1, null);
                    log.info("通道号--{}--开闸成功", channelId);
                    //实时开闸，发送延时消息队列，延时验证出场信息
                    AsyncManager.me().execute(new TimerTask() {
                        @Override
                        public void run() {
                            MessageProperties messageProperties = new MessageProperties();
                            //1分钟延时
                            messageProperties.setDelay(60000); //设置消息延迟时间，单位是毫秒
                            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);//设置消息持久化存储到磁盘上
                            //传递车场、通道、车牌、创建时间，用于延时消息判断
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("parkNo", bPassageDeviceVo.getParkNo());
                            jsonObject.put("passageId", bPassageDeviceVo.getPassageId());
                            jsonObject.put("carNumber", data.getCarNumber());
                            jsonObject.put("exitTime", DateUtils.format(LocalDateTime.now()));
                            rabbitTemplate.convertAndSend(RabbitmqConstant.EXCHANGE_LOT_PARKING_PAYSUCCESS_EXIT_DELAY,
                                    RabbitmqConstant.ROUTE_LOT_PARKING_PAYSUCCESS_EXIT_DELAY,
                                    new Message(JSON.toJSONString(jsonObject).getBytes(StandardCharsets.UTF_8), messageProperties),
                                    new CorrelationData()
                            ); //只有消息用Message对象包裹才能实现持久化
                        }
                    });
                } else {
                    redisCache.setCacheObject(CacheConstants.ADVANCE_PAYMENT + data.getCarNumber(), setDurationTime(data), 15, TimeUnit.MINUTES);
                }
            }
        } catch (Exception e) {
            log.error("消费支付信息异常 - {}", e.getMessage(), e);
        }
    }

    public PaymentData setDurationTime(PaymentData data) throws ParseException {
        data.setExitTime(DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS));
        int minutes = DateUtils.differentMinutesByMillisecond(DateUtils.parseDate(data.getEntryTime(), DateUtils.YYYY_MM_DD_HH_MM_SS),
                DateUtils.parseDate(data.getEntryTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
        data.setDurationTime(String.valueOf(minutes));
        return data;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitmqConstant.QUEUE_LOT_PARKING_PAYSUCCESS_EXIT_DELAY, durable = "true"),
            exchange = @Exchange(value = RabbitmqConstant.EXCHANGE_LOT_PARKING_PAYSUCCESS_EXIT_DELAY, delayed = "true"),
            key = RabbitmqConstant.ROUTE_LOT_PARKING_PAYSUCCESS_EXIT_DELAY
    ))
    public void consumerDelayMsg(Message message) {
        try {
            log.info("接收到验证正常离场延时消息，body：{}", message.getBody());
            String data = new String(message.getBody());
            JSONObject jsonObject = JSON.parseObject(data);
            String parkNo = jsonObject.getString("parkNo");
            String passageId = jsonObject.getString("passageId");
            String carNumber = jsonObject.getString("carNumber");
            LocalDateTime exitTime = DateUtils.toLocalDateTime((String) jsonObject.get("exitTime"));
            //查询离场信息表
            TExitRecords exitRecords = exitRecordsService.getOne(new LambdaQueryWrapper<TExitRecords>()
                    .eq(TExitRecords::getParkNo, parkNo)
                    .eq(TExitRecords::getPassageId, passageId)
                    .eq(TExitRecords::getCarNumber, carNumber)
                    .ge(TExitRecords::getExitTime, exitTime)
                    .last("limit 1"));
            if (exitRecords == null) {
                //无离场记录，记录异常信息
                log.error("找不到离场信息，记录异常日志，parkNo：{}，passageId：{}，carNumber：{}，exitTime：{}", parkNo, passageId, carNumber, exitTime);
                TAbnormalOrder tAbnormalOrder = new TAbnormalOrder();
                tAbnormalOrder.setCarNumber(carNumber);
                tAbnormalOrder.setParkNo(parkNo);
                tAbnormalOrder.setOrderType(1);
                tAbnormalOrder.setAbnormalType(Integer.parseInt(DictUtils.getDictValue("abnormal_type", "用户支付未抬杆")));
                tAbnormalOrder.setAbnormalReason("用户支付未抬杆");
                tAbnormalOrder.setCreateTime(exitTime);
                abnormalOrderService.save(tAbnormalOrder);
            }
        } catch (Exception e) {
            log.error("消费验证正常离场延时消息异常，message：{}", message, e);
        }
    }

}
