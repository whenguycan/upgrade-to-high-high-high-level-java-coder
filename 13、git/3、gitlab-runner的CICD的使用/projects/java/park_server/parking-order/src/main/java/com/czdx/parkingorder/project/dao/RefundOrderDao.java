package com.czdx.parkingorder.project.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czdx.parkingorder.project.entity.ParkingOrderEntity;
import com.czdx.parkingorder.project.entity.RefundOrderEntity;
import com.czdx.parkingorder.project.vo.*;
import com.google.protobuf.ProtocolStringList;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 退款订单 dao
 * </p>
 *
 * @author 琴声何来
 * @since 2023/4/20 11:13
 */
@Mapper
public interface RefundOrderDao extends BaseMapper<RefundOrderEntity> {

}
