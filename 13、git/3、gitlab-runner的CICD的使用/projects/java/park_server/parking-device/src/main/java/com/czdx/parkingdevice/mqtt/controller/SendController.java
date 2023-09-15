package com.czdx.parkingdevice.mqtt.controller;

import com.alibaba.fastjson.JSONObject;
import com.czdx.parkingdevice.Constant.Constant;
import com.czdx.parkingdevice.mqtt.config.MqttProperties;
import com.czdx.parkingdevice.mqtt.server.MqttSendClient;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * description: 发消息控制类
 * date: 2022/6/16 15:58
 *
 * @author: zhouhong
 */
@Slf4j
@RestController
@RequestMapping("/mqtt")
public class SendController {

    @Resource
    private MqttSendClient mqttSendClient;
    @Autowired
    private MqttProperties mqttProperties;

    @GetMapping("/sendmessage")
    public void sendMessage() {

//        JSONObject jsonObject=new JSONObject();
//
//        jsonObject.put("carNumber","马总");
//        jsonObject.put("parkingTime","50");
//        jsonObject.put("masterofCar","马增来");
//        jsonObject.put("userType","月卡用户");
//        jsonObject.put("RemainDay","31");
//        jsonObject.put("passEnable",1);
//        jsonObject.put("stuInTime","2023-03-30");
//        jsonObject.put("stuOutTime","2023-03-31");
//        jsonObject.put("parkCharges","100元");
//        jsonObject.put("remainSpace",88);
//        jsonObject.put("carStatus","PASSING_CAR");
//        jsonObject.put("subUserTypes","");
//        jsonObject.put("remark","测试");
//        jsonObject.put("deviceIp","192.168.71.86");
//        jsonObject.put("port",37777);
//
//        mqttSendClient.publish(false,mqttProperties.getScreenTopic(),jsonObject.toJSONString());


        JSONObject jsonObject=new JSONObject();

        jsonObject.put("deviceIp","192.168.71.86");
        jsonObject.put("channelId","1");
        jsonObject.put("status","0");

        mqttSendClient.publish(false, Constant.open_close_topic,jsonObject.toJSONString());
    }
}
