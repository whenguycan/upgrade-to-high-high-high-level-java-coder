package com.ruoyi.project.parking.service.grpcclient;

import com.alibaba.fastjson2.JSONObject;
import com.czdx.grpc.lib.device.*;
import com.ruoyi.project.parking.service.grpcclient.model.OpenCloseChannelRequest;
import com.ruoyi.project.parking.service.grpcclient.model.SendLedScreenMessageRequest;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

/**
 * 硬件设备相关接口调用
 */
@Slf4j
@Service("deviceGrpcClientService")
public class DeviceGrpcClientService {
    @GrpcClient("parking-device-server")
    DeviceControlServiceGrpc.DeviceControlServiceBlockingStub deviceLedScreenMessageServiceBlockingStub;

    public void sendLedScreenMessage(SendLedScreenMessageRequest requestVo) {

        try {
            SendLedScreenMessageRequestProto.Builder ledShowBuilder = SendLedScreenMessageRequestProto.newBuilder();
            ProtoJsonUtil.toProtoBean(ledShowBuilder, requestVo);
            SendLedScreenMessageResponseProto sendLedScreenMessageResponseProto = deviceLedScreenMessageServiceBlockingStub.sendLedScreenMessage(ledShowBuilder.build());
            if (sendLedScreenMessageResponseProto.getFlag()) {
                return;
            }
        } catch (StatusRuntimeException e) {
            log.error("Screen-1-FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("Screen-2-FAILED with " + e.getMessage());
        }
    }

    public void openCloseChannel(OpenCloseChannelRequest requestVo) {

        try {
            log.info("------requestVo:"+ JSONObject.toJSONString(requestVo));
            OpenCloseChannelRequestProto.Builder openRequestBuilder = OpenCloseChannelRequestProto.newBuilder();
            ProtoJsonUtil.toProtoBean(openRequestBuilder, requestVo);
            OpenCloseChannelResponseProto openCloseChannelResponseProto = deviceLedScreenMessageServiceBlockingStub.openCloseChannel(openRequestBuilder.build());
            if (openCloseChannelResponseProto.getFlag()) {
                return;
            }
        } catch (StatusRuntimeException e) {
            log.error("openClose-1-FAILED with " + e.getStatus().getCode().name(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            log.error("openClose-2-FAILED with " + e.getMessage());
        }


    }
}
