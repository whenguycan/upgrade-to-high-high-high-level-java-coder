package com.czdx.parkingnotification.service.impl;

import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import com.czdx.parkingnotification.domain.czzhtc.response.ChargeResponse;
import com.czdx.parkingnotification.service.ICZZHTCService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CZZHTCServiceImpl implements ICZZHTCService {
    @GrpcClient("parking-order-server")
    ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub parkingOrderServiceBlockingStub;

    /**
     * @apiNote 根据车牌号和计费时间查询订单信息
     * @author 琴声何来
     * @since 2023/4/3 17:02
     * @param carNumber 车牌号
     * @param billTime 计费时间
     * @return com.czdx.parkingnotification.domain.czzhtc.response.ChargeResponse
     */
    @Override
    public ChargeResponse getChargeInfo(String carNumber, LocalDateTime billTime) {
        //todo 根据车牌号获取所在场库，根据在场信息和计费时间计算费用
        return null;
    }
}
