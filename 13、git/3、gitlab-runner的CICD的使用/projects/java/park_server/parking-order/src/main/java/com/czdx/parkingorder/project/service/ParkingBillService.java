package com.czdx.parkingorder.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.czdx.parkingorder.project.entity.ParkingBillEntity;
import com.czdx.parkingorder.project.entity.ParkingOrderItemEntity;

/**
 * @Auther: tangwei
 * @Date: 2023/4/10 5:23 PM
 * @Description: 接口描述信息
 */
public interface ParkingBillService extends IService<ParkingBillEntity> {

    /**
     * @apiNote 更新发票状态
     */
    void updateByOrderNo(String orderNo,String pdfUrl);

    IPage<ParkingBillEntity> searchByUserId(Integer userId, Integer pageNum, Integer pageSize);

    ParkingBillEntity getOneBillRecordByOutTradeNo(String billOutTradeNo);
}
