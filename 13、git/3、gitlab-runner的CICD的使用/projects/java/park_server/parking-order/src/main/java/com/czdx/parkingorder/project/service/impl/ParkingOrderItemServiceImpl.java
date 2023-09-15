package com.czdx.parkingorder.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingorder.project.dao.ParkingOrderItemDao;
import com.czdx.parkingorder.project.entity.ParkingOrderItemEntity;
import com.czdx.parkingorder.project.service.ParkingOrderItemService;
import org.springframework.stereotype.Service;

/**
 * @Auther: tangwei
 * @Date: 2023/2/27 5:17 PM
 * @Description: 类描述信息
 */
@Service("ParkingOrderItemService")
public class ParkingOrderItemServiceImpl extends ServiceImpl<ParkingOrderItemDao, ParkingOrderItemEntity> implements ParkingOrderItemService {
}
