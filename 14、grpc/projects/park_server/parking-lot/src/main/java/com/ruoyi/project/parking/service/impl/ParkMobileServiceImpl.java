package com.ruoyi.project.parking.service.impl;

import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.project.parking.domain.param.VehiclePaymentInfoPavilionCodeParam;
import com.ruoyi.project.parking.domain.param.VehiclePaymentInfoPrepayCodeParam;
import com.ruoyi.project.parking.domain.vo.parkingorder.VehicleParkOrderVO;
import com.ruoyi.project.parking.service.IParkLiveRecordsService;
import com.ruoyi.project.parking.service.IParkMobileService;
import com.ruoyi.project.parking.service.ParkingOrderGrpcServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 移动端 服务实现类
 */
@Service
public class ParkMobileServiceImpl implements IParkMobileService {

    @Autowired
    private IParkLiveRecordsService parkLiveRecordsService;

    @Autowired
    private ParkingOrderGrpcServiceImpl parkingOrderGrpcService;

    @Autowired
    private RedisCache redisCache;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public VehicleParkOrderVO queryVehiclePrepayCodePaymentInfo(VehiclePaymentInfoPrepayCodeParam vehiclePaymentInfoPrepayCodeParam) {
        // 预支付订单
        return parkLiveRecordsService.queryOrCreatePrepayOrderByParkNoCarNumber(vehiclePaymentInfoPrepayCodeParam.getParkNo(), vehiclePaymentInfoPrepayCodeParam.getCarNumber());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public VehicleParkOrderVO queryVehiclePavilionCodePaymentInfo(VehiclePaymentInfoPavilionCodeParam vehiclePaymentInfoPavilionCodeParam) {
        // 岗亭订单
        return parkLiveRecordsService.queryOrCreatePavilionOrderByParkNoPassageNo(vehiclePaymentInfoPavilionCodeParam.getParkNo(),vehiclePaymentInfoPavilionCodeParam.getPassageNo());
    }

}
