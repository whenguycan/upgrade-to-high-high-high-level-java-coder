package com.ll.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ll.goods.entity.GoodsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Auther: tangwei
 * @Date: 2023/4/28 8:57 AM
 * @Description: 接口描述信息
 */
@Mapper
public interface GoodsDao extends BaseMapper<GoodsEntity> {
}
