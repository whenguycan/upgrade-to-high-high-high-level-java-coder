package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.param.VehiclePaymentInfoPavilionCodeParam;
import com.ruoyi.project.parking.domain.param.VehiclePaymentInfoPrepayCodeParam;
import com.ruoyi.project.parking.domain.vo.parkingorder.VehicleParkOrderVO;

/**
 * 移动端 服务类
 */
public interface IParkMobileService {

    VehicleParkOrderVO queryVehiclePrepayCodePaymentInfo(VehiclePaymentInfoPrepayCodeParam vehiclePaymentInfoPrepayCodeParam);

    VehicleParkOrderVO queryVehiclePavilionCodePaymentInfo(VehiclePaymentInfoPavilionCodeParam vehiclePaymentInfoPavilionCodeParam);

}
