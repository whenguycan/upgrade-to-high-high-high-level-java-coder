package com.czdx.parkingorder.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingorder.common.utils.StringUtils;
import com.czdx.parkingorder.project.dao.ParkingOrderDao;
import com.czdx.parkingorder.project.dao.RefundOrderDao;
import com.czdx.parkingorder.project.entity.ParkingOrderEntity;
import com.czdx.parkingorder.project.entity.ParkingOrderItemEntity;
import com.czdx.parkingorder.project.entity.RefundOrderEntity;
import com.czdx.parkingorder.project.service.ParkingOrderItemService;
import com.czdx.parkingorder.project.service.ParkingOrderService;
import com.czdx.parkingorder.project.service.RefundOrderService;
import com.czdx.parkingorder.project.vo.*;
import com.czdx.parkingorder.utils.SnowflakeIdWorker;
import com.google.protobuf.ProtocolStringList;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 退款订单 service实现类
 * </p>
 *
 * @author 琴声何来
 * @since 2023/4/20 11:13
 */
@Service("RefundOrderService")
public class RefundOrderServiceImpl extends ServiceImpl<RefundOrderDao, RefundOrderEntity> implements RefundOrderService {


    @Override
    public RefundOrderEntity getByOrderNoAndRefundNo(String orderNo, String refundNo) {
        return getOne(new LambdaQueryWrapper<RefundOrderEntity>()
                .eq(RefundOrderEntity::getOrderNo, orderNo)
                .eq(RefundOrderEntity::getRefundNo, refundNo)
                .last("limit 1"));
    }

    @Override
    public List<RefundOrderEntity> searchByOrderNos(List<String> orderNos) {
        return list(new LambdaQueryWrapper<RefundOrderEntity>()
                .in(RefundOrderEntity::getOrderNo, orderNos));
    }

    @Override
    public IPage<RefundOrderEntity> searchOrder(String orderNo, String parkNo, Integer pageNum, Integer pageSize, String carNumber) {
        IPage<RefundOrderEntity> page = Page.of(pageNum, pageSize);
        this.baseMapper.selectPage(page, new QueryWrapper<RefundOrderEntity>()
                .like(StringUtils.isNotEmpty(orderNo), "order_no", orderNo)
                .eq(StringUtils.isNotEmpty(parkNo), "park_no", parkNo)
                .like(StringUtils.isNotEmpty(carNumber), "car_number", carNumber));
        return page;
    }
}
