package com.example.demolog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demolog.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Auther: tangwei
 * @Date: 2023/7/19 3:54 PM
 * @Description: 接口描述信息
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

}
