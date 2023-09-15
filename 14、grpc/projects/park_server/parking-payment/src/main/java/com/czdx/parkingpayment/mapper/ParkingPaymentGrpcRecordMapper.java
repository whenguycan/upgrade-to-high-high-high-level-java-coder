package com.czdx.parkingpayment.mapper;

import com.czdx.parkingpayment.domain.ParkingPaymentGrpcRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.scheduling.annotation.Async;

/**
* @author 琴声何来
* @description 针对表【t_parking_payment_grpc_record(支付系统grpc请求记录)】的数据库操作Mapper
* @since 2023-03-02 15:26:48
* @Entity com.czdx.parkingpayment.domain.ParkingPaymentGrpcRecord
*/
@Mapper
@Async
public interface ParkingPaymentGrpcRecordMapper extends BaseMapper<ParkingPaymentGrpcRecord> {

}




