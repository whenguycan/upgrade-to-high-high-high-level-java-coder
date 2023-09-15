package com.czdx.parkingorder.project.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czdx.parkingorder.project.entity.MonthlyOrderEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Auther: tangwei
 * @Date: 2023/3/13 2:07 PM
 * @Description: 接口描述信息
 */
@Mapper
public interface MonthlyOrderDao extends BaseMapper<MonthlyOrderEntity> {
}
