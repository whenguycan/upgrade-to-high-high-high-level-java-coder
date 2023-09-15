package com.czdx.parkingorder.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.czdx.parkingorder.project.entity.ParkingOrderEntity;
import com.czdx.parkingorder.project.vo.CreateParkingOrderVo;
import com.czdx.parkingorder.project.vo.ParkingOrderDetailVo;

import java.util.List;

/**
 * @Auther: tangwei
 * @Date: 2023/2/27 10:32 AM
 * @Description: 接口描述信息
 */
public interface ParkingOrderService extends IService<ParkingOrderEntity> {

    public ParkingOrderEntity createParkingOrder(CreateParkingOrderVo createParkingOrderVo);

    ParkingOrderEntity getParkingOrderByOrderNo(String orderNo);

    void cancleRepeatedOrder(String parkNo, String carNumber, String entryTime);

    IPage<ParkingOrderEntity> searchOrder(String orderNo, Integer payMethod, Integer pageNum, Integer pageSize, String carNumber);
}
