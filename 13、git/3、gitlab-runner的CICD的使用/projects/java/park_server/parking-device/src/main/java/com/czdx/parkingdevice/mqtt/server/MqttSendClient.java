package com.czdx.parkingdevice.mqtt.server;

import com.czdx.parkingdevice.mqtt.config.MqttProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MqttSendClient {

        
    @Autowired
    private MqttSendCallBack mqttSendCallBack;

    @Autowired
    private MqttProperties mqttProperties;

    private static MqttClient mqttClient;

    public static MqttClient getClient() {


        return mqttClient;
    }

    private static void setClient(MqttClient client) {


        MqttSendClient.mqttClient = client;
    }

    /**
     * 客户端连接
     * @return
     */
    public void connect(){
        MqttClient client = null;

        try {


            //String uuid = UUID.randomUUID().toString().replaceAll("-",""); //设置每一个客户端的id
            client = new MqttClient(mqttProperties.getHostUrl(),mqttProperties.getClientId() , new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(mqttProperties.getUsername());
            options.setPassword(mqttProperties.getPassword().toCharArray());
            options.setConnectionTimeout(mqttProperties.getTimeout());
            options.setKeepAliveInterval(mqttProperties.getKeepAlive());
            options.setCleanSession(mqttProperties.getCleanSession());
            options.setAutomaticReconnect(mqttProperties.getReconnect());

            MqttSendClient.setClient(client);
            try {


                // 设置回调
                client.setCallback(mqttSendCallBack);
                client.connect(options);
            } catch (Exception e) {


                e.printStackTrace();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

        
        
        
    /**
     * 发布，默认qos为0，非持久化
     *
     * @param topic 主题名
     * @param pushMessage 消息
     */
    public void publish( boolean retained,String topic, String pushMessage) {
        publish(mqttProperties.getQos(), false, topic, pushMessage);
    }

    /**
     * 发布
     *
     * @param qos
     * @param retained
     * @param topic
     * @param pushMessage
     */
    public void publish(int qos, boolean retained, String topic, String pushMessage) {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        MqttTopic mTopic = MqttSendClient.getClient().getTopic(topic);
        if (null == mTopic) {
            log.error("主题不存在:{}",mTopic);
        }
        try {
            mTopic.publish(message);
            log.info("消息发送成功");
        } catch (Exception e) {
            log.error("mqtt发送消息异常:",e);
        }
    }

    public void disConnect() throws MqttException {
        getClient().disconnect();
    }


    /**
     * 订阅某个主题
     *
     * @param topic 主题
     * @param qos   连接方式
     */
    public void subscribe(String topic, int qos) {


        log.info("==============开始订阅主题==============" + topic);
        try {


            mqttClient.subscribe(topic, qos);
        } catch (MqttException e) {


            e.printStackTrace();
        }
    }
}

