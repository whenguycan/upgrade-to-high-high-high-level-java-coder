package com.czdx.parkingorder.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.czdx.parkingorder.project.entity.ParkingOrderEntity;
import com.czdx.parkingorder.project.entity.RefundOrderEntity;
import com.czdx.parkingorder.project.vo.*;
import com.google.protobuf.ProtocolStringList;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 退款订单 service类
 * </p>
 *
 * @author 琴声何来
 * @since 2023/4/20 11:12
 */
public interface RefundOrderService extends IService<RefundOrderEntity> {

    RefundOrderEntity getByOrderNoAndRefundNo(String orderNo, String refundNo);

    List<RefundOrderEntity> searchByOrderNos(List<String> orderNos);

    IPage<RefundOrderEntity> searchOrder(String orderNo, String parkNo, Integer pageNum, Integer pageSize, String carNumber);
}