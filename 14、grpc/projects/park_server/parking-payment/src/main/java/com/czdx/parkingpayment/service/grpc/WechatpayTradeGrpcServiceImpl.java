package com.czdx.parkingpayment.service.grpc;

import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
//@GrpcService
public class WechatpayTradeGrpcServiceImpl {

//    @Autowired
//    private JsapiService jsapiService;

//    public void wechatPayTradePay(){
//        PrepayRequest prepayRequest=new PrepayRequest();
//        prepayRequest.setDescription("测试商品");
//        PrepayResponse response = jsapiService.prepay(prepayRequest);
//        log.info("response：{}",response);
//    }
}
