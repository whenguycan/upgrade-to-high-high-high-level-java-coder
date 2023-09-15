package com.czdx.parkingorder.project.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czdx.parkingorder.project.entity.MonthlyOrderEntity;
import com.czdx.parkingorder.project.entity.ParkingBillEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Auther: tangwei
 * @Date: 2023/4/10 5:22 PM
 * @Description: 接口描述信息
 */
@Mapper
public interface ParkingBillDao  extends BaseMapper<ParkingBillEntity> {
}
