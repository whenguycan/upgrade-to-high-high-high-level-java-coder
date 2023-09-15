package com.czdx.parkingpayment.service;

import com.czdx.parkingpayment.domain.ParkingPaymentGrpcRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 琴声何来
* @description 针对表【t_parking_payment_grpc_record(支付系统grpc请求记录)】的数据库操作Service
* @since  2023-03-02 14:19:54
*/
public interface IParkingPaymentGrpcRecordService extends IService<ParkingPaymentGrpcRecord> {

    ParkingPaymentGrpcRecord getAlipayRecordByOutTradeNo(String outTradeNo);
}
