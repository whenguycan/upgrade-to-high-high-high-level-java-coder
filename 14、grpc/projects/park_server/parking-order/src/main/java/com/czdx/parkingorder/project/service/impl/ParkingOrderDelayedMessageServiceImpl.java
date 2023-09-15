package com.czdx.parkingorder.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingorder.project.dao.ParkingOrderDelayedMessageDao;
import com.czdx.parkingorder.project.entity.ParkingOrderDelayedMessageEntity;
import com.czdx.parkingorder.project.service.ParkingOrderDelayedMessageService;
import org.springframework.stereotype.Service;

/**
 * @Auther: tangwei
 * @Date: 2023/3/1 2:50 PM
 * @Description: 类描述信息
 */
@Service("ParkingOrderDelayedMessageService")
public class ParkingOrderDelayedMessageServiceImpl extends ServiceImpl<ParkingOrderDelayedMessageDao, ParkingOrderDelayedMessageEntity> implements ParkingOrderDelayedMessageService {
}
