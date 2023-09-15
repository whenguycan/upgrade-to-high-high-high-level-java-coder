package com.czdx.parkingdevice.grpcService;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.czdx.grpc.lib.device.*;
import com.czdx.parkingdevice.Constant.Constant;
import com.czdx.parkingdevice.mqtt.config.MqttProperties;
import com.czdx.parkingdevice.mqtt.server.MqttSendClient;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

@Slf4j
@GrpcService
public class DeviceLedScreenMessageServiceImpl extends DeviceControlServiceGrpc.DeviceControlServiceImplBase {

    @Resource
    private MqttSendClient mqttSendClient;
    @Autowired
    private MqttProperties mqttProperties;

    @Override
    public void sendLedScreenMessage(SendLedScreenMessageRequestProto request, StreamObserver<SendLedScreenMessageResponseProto> responseObserver) {
        JSONObject jsonObject=new JSONObject();

        jsonObject.put("carNumber",request.getCarNumber());
        jsonObject.put("parkingTime",request.getParkingTime());
        jsonObject.put("masterofCar",request.getMasterofCar());
        jsonObject.put("userType",request.getUserType());
        jsonObject.put("RemainDay",request.getRemainDay());
        jsonObject.put("passEnable",request.getPassEnable());
        jsonObject.put("stuInTime",request.getStuInTime());
        jsonObject.put("stuOutTime",request.getStuOutTime());
        jsonObject.put("parkCharges",request.getParkCharges());
        jsonObject.put("remainSpace",request.getRemainSpace());
        jsonObject.put("carStatus",request.getCarStatus());
        jsonObject.put("subUserTypes",request.getSubUserTypes());
        jsonObject.put("remark",request.getRemark());
        jsonObject.put("deviceIp",request.getDeviceIp());
        jsonObject.put("port",request.getPort());
        jsonObject.put("parkNo",request.getParkNo());

        mqttSendClient.publish(false, Constant.screen_topic,jsonObject.toJSONString());
        SendLedScreenMessageResponseProto.Builder response=SendLedScreenMessageResponseProto.newBuilder();
        response.setFlag(true);
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void openCloseChannel(OpenCloseChannelRequestProto request, StreamObserver<OpenCloseChannelResponseProto> responseObserver) {
        JSONObject jsonObject=new JSONObject();

        jsonObject.put("deviceIp",request.getDeviceIp());
        jsonObject.put("status",request.getStatus());
        jsonObject.put("parkNo",request.getParkNo());
        jsonObject.put("carNum",request.getCarNum());

        mqttSendClient.publish(false,Constant.open_close_topic,jsonObject.toJSONString());
        OpenCloseChannelResponseProto.Builder response = OpenCloseChannelResponseProto.newBuilder();
        response.setFlag(true);
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
