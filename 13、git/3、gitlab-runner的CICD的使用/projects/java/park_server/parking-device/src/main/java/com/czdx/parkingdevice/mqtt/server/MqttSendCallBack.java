package com.czdx.parkingdevice.mqtt.server;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.czdx.grpc.lib.lot.*;
import com.czdx.parkingdevice.Constant.Constant;
import com.czdx.parkingdevice.mqtt.config.MqttProperties;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

@Slf4j
@Component
public class MqttSendCallBack implements MqttCallbackExtended {

    @Resource
    private MqttSendClient mqttSendClient;
    @Autowired
    private MqttProperties mqttProperties;
    @GrpcClient("parking-lot-server")
    DeviceLotParkingServiceGrpc.DeviceLotParkingServiceBlockingStub deviceLotParkingServiceBlockingStub;


    /**
     * 链接EMQ服务器后触发
     * @param reconnect
     * @param serverURI
     */
    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        log.info("————————————————-ClientID:{}——————————————"+"链接成功",serverURI);
        mqttSendClient.subscribe(Constant.subscribe_topic, mqttProperties.getQos());
        mqttSendClient.subscribe(Constant.channel_status_topic, mqttProperties.getQos());
    }

    /**
     * 客户端连接断开后触发
     * 这里可以做重新链接操作
     */
    @Override
    public void connectionLost(Throwable cause) {
        log.error("【MQTT-发送端】链接断开！");
        try{
            log.error("【MQTT-发送端】链接断开！尝试重连");
            if (MqttSendClient.getClient() == null || !MqttSendClient.getClient().isConnected()) {
                log.info("【MQTT-消费端】emqx重新连接....................................................");
                mqttSendClient.connect();
            }
        }catch (Exception e){
            log.error("【MQTT-发送端】链接断开！重连失败");
            log.error(e.getCause().toString());
            log.error(e.getMessage());

        }


    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("【MQTT-发送端】接收消息主题 : " + topic);
        log.info("【MQTT-发送端】接收消息Qos : " + message.getQos());
        log.info("【MQTT-发送端】接收消息内容 : " + new String(message.getPayload()));
        JSONObject jsonObject = JSON.parseObject(new String(message.getPayload()));
        if (Constant.channel_status_topic.equals(topic)){
            callBackChannelMessage(jsonObject);
        }else if (Constant.subscribe_topic.equals(topic)){
            sendSubscribeMessage(jsonObject);
        }

    }

    /**
     * 发送消息回调
     * @param token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

//        String[] topics = token.getTopics();
//
//        if (topics!=null && topics.length>0){
//            for (String topic : topics) {
//

                log.info("【MQTT-发送端】 发送消息成功！");
//            }
//        }

//        try {
//
//
//            MqttMessage message = token.getMessage();
//            byte[] payload = message.getPayload();
//            String s = new String(payload, "UTF-8");
//            log.info("【MQTT-发送端】消息的内容是：" + s);
//        } catch (MqttException e) {
//
//
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//
//
//            e.printStackTrace();
//        }
    }

    /**
     * 推送进出场消息
     * @param jsonObject
     */
    public void sendSubscribeMessage(JSONObject jsonObject){
        try {
            CaptureDeviceInfoRequest.Builder builder= CaptureDeviceInfoRequest.newBuilder();
            builder.setCarNum(jsonObject.getString("m_PlateNumber"));
            builder.setParkNo(jsonObject.getString("parkNo"));
            builder.setDeviceLocalIp(jsonObject.getString("ip"));
            builder.setCarImgUrl(jsonObject.getString("bigPicture"));
            builder.setCarNumPic(jsonObject.getString("platePicture"));
            builder.setCarColor(jsonObject.getString("m_VehicleColor"));
            builder.setCarNumColor(jsonObject.getString("m_PlateColor"));
            builder.setParkingCarType(jsonObject.getString("m_VehicleType"));
            builder.setCapTime(jsonObject.getString("arriveTime"));
            CaptureDeviceInfoResponse response=deviceLotParkingServiceBlockingStub.capture(builder.build());
        } catch (Exception e) {
            log.error("sendSubscribeMessage FAILED with " + e.getMessage());
        }


    }

    /**
     * 开闸回调
     * @param jsonObject
     */
    public void callBackChannelMessage(JSONObject jsonObject){
        try {
            log.info("------jsonObject:"+jsonObject);
            CaptureDeviceInfoRequest.Builder builder= CaptureDeviceInfoRequest.newBuilder();
            builder.setCarNum(jsonObject.getString("m_PlateNumber"));
            builder.setParkNo(jsonObject.getString("parkNo"));
            builder.setDeviceLocalIp(jsonObject.getString("ip"));
            builder.setCarImgUrl(jsonObject.getString("bigPicture"));
            builder.setCarNumPic(jsonObject.getString("platePicture"));
            builder.setCarColor(jsonObject.getString("m_VehicleColor"));
            builder.setCarNumColor(jsonObject.getString("m_PlateColor"));
            builder.setParkingCarType(jsonObject.getString("m_VehicleType"));
            builder.setCapTime(jsonObject.getString("arriveTime"));
            CaptureDeviceInfoResponse response=deviceLotParkingServiceBlockingStub.updateCarRecord(builder.build());
        } catch (StatusRuntimeException e) {
            log.error("开闸callBack-1-FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("开闸callBack-2-FAILED with " + e.getMessage());
        }
    }

}
