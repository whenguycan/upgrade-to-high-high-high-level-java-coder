package com.czdx.parkingnotification.service.grpc;

import com.czdx.grpc.lib.WechatMessageTemplateSend.*;
import com.czdx.parkingnotification.config.MyWechatConfig;
import com.czdx.parkingnotification.utils.WechatAccessTokenUtil;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@GrpcService
public class WechatGrpcServiceImpl extends WechatMessageTemplateSendServiceGrpc.WechatMessageTemplateSendServiceImplBase {

    private Environment environment;

    private RestTemplate restTemplate;

    private WechatAccessTokenUtil wechatAccessTokenUtil;

    @Override
    public void wechatGetOpenId(WechatGetOpenIdRequestProto request, StreamObserver<WechatGetOpenIdResponseProto> responseObserver) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", MyWechatConfig.APP_ID);
        params.put("secret", MyWechatConfig.SECRET);
        params.put("code", request.getCode());
        String pageAccessTokenUrl = MyWechatConfig.PAGE_ACCESS_TOKEN_URL;
        ResponseEntity<WechatGetOpenIdResponseProto> response = restTemplate.getForEntity(pageAccessTokenUrl, WechatGetOpenIdResponseProto.class, params);
        responseObserver.onNext(response.getBody());
    }

    @Override
    public void wechatMessageTemplateSend(WechatMessageTemplateSendRequestProto request, StreamObserver<WechatMessageTemplateSendResponseProto> responseObserver) {
        String messageTemplateSendUrl = MyWechatConfig.MESSAGE_TEMPLATE_SEND_URL.replace("{accessToken}", wechatAccessTokenUtil.getToken());
        ResponseEntity<WechatMessageTemplateSendResponseProto> responseEntity = restTemplate.postForEntity(messageTemplateSendUrl, request, WechatMessageTemplateSendResponseProto.class);
        responseObserver.onNext(responseEntity.getBody());
    }
}
