package com.ruoyi.project.parking.grpc.client;

import com.czdx.grpc.lib.merchant.CouponTaskServiceGrpc;
import com.google.protobuf.Empty;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("CouponTask")
public class CouponTask {
    @GrpcClient("parking-member-merchant-server")
    private CouponTaskServiceGrpc.CouponTaskServiceBlockingStub couponTaskServiceBlockingStub;

    public void refeshCoupon(){
        couponTaskServiceBlockingStub.refeshCoupon(Empty.newBuilder().build());
    }
}
