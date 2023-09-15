package com.czdx.parkingpayment;

import com.czdx.grpc.lib.alipay.AliPayServiceGrpc;
import com.czdx.grpc.lib.alipay.AlipayTradeWapPayRequestProto;
import com.czdx.grpc.lib.alipay.AlipayTradeWapPayResponseProto;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class ParkingPaymentApplicationTests {

    @Test
    void contextLoads() {
    }

    /**
     * @param
     * @return void
     * @apiNote 支付宝手机网站支付Grpc测试
     * @author 琴声何来
     * @since 2023/3/1 9:31
     */
    @Test
    void alipay() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9100).usePlaintext().build();
        AliPayServiceGrpc.AliPayServiceBlockingStub aliPayServiceBlockingStub = AliPayServiceGrpc.newBlockingStub(channel);
        AlipayTradeWapPayRequestProto proto = AlipayTradeWapPayRequestProto.newBuilder().setOutTradeNo(UUID.randomUUID().toString()).setTotalAmount(100).setSubject("测试商品").build();
        AlipayTradeWapPayResponseProto alipayTradeWapPayResponseProto = aliPayServiceBlockingStub.alipayTradeWapPay(proto);
        System.out.println(alipayTradeWapPayResponseProto);
    }

}
