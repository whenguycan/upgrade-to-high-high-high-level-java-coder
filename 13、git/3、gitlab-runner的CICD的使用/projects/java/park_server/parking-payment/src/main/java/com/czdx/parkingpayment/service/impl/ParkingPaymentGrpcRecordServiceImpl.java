package com.czdx.parkingpayment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingpayment.common.constant.PayMethodConstants;
import com.czdx.parkingpayment.domain.ParkingPaymentGrpcRecord;
import com.czdx.parkingpayment.mapper.ParkingPaymentGrpcRecordMapper;
import com.czdx.parkingpayment.service.IParkingPaymentGrpcRecordService;
import org.springframework.stereotype.Service;

/**
 * @author 琴声何来
 * @description 针对表【t_parking_payment_grpc_record(支付系统grpc请求记录)】的数据库操作Service实现
 * @since 2023-03-02 15:26:48
 */
@Service
public class ParkingPaymentGrpcRecordServiceImpl extends ServiceImpl<ParkingPaymentGrpcRecordMapper, ParkingPaymentGrpcRecord>
        implements IParkingPaymentGrpcRecordService {

    @Override
    public ParkingPaymentGrpcRecord getAlipayRecordByOutTradeNo(String outTradeNo) {
        LambdaQueryWrapper<ParkingPaymentGrpcRecord> qw = new LambdaQueryWrapper<>();
        qw.eq(ParkingPaymentGrpcRecord::getOutTradeNo, outTradeNo)
                .eq(ParkingPaymentGrpcRecord::getType, PayMethodConstants.ALI_PAY)
                .isNotNull(ParkingPaymentGrpcRecord::getTotalAmount)
                .last(" limit 1");
        return getOne(qw);
    }
}




