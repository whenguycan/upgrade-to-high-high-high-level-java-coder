package com.czdx.parkingdevice.mqtt.config;


import com.czdx.parkingdevice.mqtt.server.MqttSendClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    @Autowired
    private MqttSendClient mqttSendClient;

    @Conditional(MqttCondition.class)
    @Bean
    public MqttSendClient getMqttSendClient(){
        mqttSendClient.connect();
        return mqttSendClient;
    }
}

