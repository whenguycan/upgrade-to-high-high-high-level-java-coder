package com.example.demolog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demolog.dao.OrderDao;
import com.example.demolog.entity.OrderEntity;
import com.example.demolog.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * @Auther: tangwei
 * @Date: 2023/7/20 10:26 AM
 * @Description: 类描述信息
 */
@Service("OrderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

}
