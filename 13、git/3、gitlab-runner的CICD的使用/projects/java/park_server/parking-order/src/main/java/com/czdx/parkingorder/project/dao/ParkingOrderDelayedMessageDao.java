package com.czdx.parkingorder.project.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czdx.parkingorder.project.entity.ParkingOrderDelayedMessageEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Auther: tangwei
 * @Date: 2023/3/1 2:48 PM
 * @Description: 接口描述信息
 */
@Mapper
public interface ParkingOrderDelayedMessageDao extends BaseMapper<ParkingOrderDelayedMessageEntity> {
}
